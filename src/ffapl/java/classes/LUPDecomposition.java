package ffapl.java.classes;

import com.sun.istack.NotNull;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicError;

import java.math.BigInteger;
import java.util.TreeMap;

import static java.math.BigInteger.ONE;

/**
 * LUP Decomposition implementation.
 * <p>
 * Decomposition of Matrix {@code A} includes three matrices
 * {@code L} (lower triangular), {@code U} (upper triangular)
 * and {@code P} (permutation) such that {@code PA = LU}.
 * Thus, {@code L.multiply(U).applyPermutation(P, true)}
 * gives the original matrix A.
 * <p>
 * see Introduction to Algorithms, 3rd Ed., p813ff
 */
public class LUPDecomposition {

    /** Matrix size **/
    private long n;

    /** modulus of the residue classes **/
    private BigInteger modulus;

    /**
     * Joint representation matrix {@code LU nxn} of lower triangular matrix {@code L}
     * and upper triangular matrix {@code U}.
     * <p>
     * l_ij = i>j ? lu_ij : i==j ? 1 : 0;
     * u_ij = i>j ? 0 : lu_ij;
     */
    private Matrix<ResidueClass> LU;

    /**
     * permutation "matrix" "P"
     * implemented as sparse vector
     */
    private TreeMap<Long, Long> P;

    // ResidueClass elements for later (repeated) use, to save on instantiations
    private ResidueClass zero, one;

    /**
     * Create LUP decomposition from square matrix.
     * <p>
     * see Introduction to Algorithms, 3rd Ed., p824
     *
     * @param A nxn matrix to decompose
     */
    public LUPDecomposition(@NotNull Matrix<ResidueClass> A) throws FFaplAlgebraicException {
        if (!A.isSquareMatrix())
            throw new FFaplAlgebraicException(new Object[0], IAlgebraicError.MATRIX_NOT_SQUARE);

        n = A.getM();
        modulus = A.getDefaultValue()._modulus;

        // instantiate often used values
        zero = A.getDefaultValue();
        one = new ResidueClass(ONE, modulus);

        LU = A.clone();
        P = new TreeMap<>();

        for (long k = 1L; k <= n; k++) {

            // find row for pivoting:
            // row i >= k such that a_ik != 0
            Long kPrime = null;
            for (Long i = LU.getNextRow(k, true); i != null; i = LU.getNextRow(i, false)) {
                if (LU.hasNonZeroEntryAt(i, k)) {
                    kPrime = i;
                    break;
                }
            }

            // throw exception if no pivot was found, as the matrix is singular then
            if (kPrime == null)
                throw new FFaplAlgebraicException(new Object[0], IAlgebraicError.SINGULAR_MATRIX);

            // swap current (k) with pivot (k'); only swap if they are actually different
            if (!kPrime.equals(k)) {
                // exchange pi[k] with pi[k'] (make note in permutation "matrix")
                // swap items in P (use defaults, as P is sparse and
                // does not contain "identity" transformations ( map(i)=i ))
                Matrix.swapMapItems(P, k, kPrime, k, kPrime, false);

                // for i = 1 to n
                //     exchange a_ki with a_k'i
                LU.swapRows(k, kPrime);
            }

            ResidueClass a_kk = LU.get(k, k);

            // for i = k+1 to n
            for (Long i = LU.getNextRow(k, false); i != null; i = LU.getNextRow(i, false)) {
                if (LU.hasNonZeroEntryAt(i, k)) {

                    // a_ik = a_ik/a_kk
                    ResidueClass a_ik = LU.get(i, k);
                    // ResidueClass.equals only compares the moduli
                    if (!a_kk._value.equals(ONE)) {
                        a_ik = a_ik.divR(a_kk);
                        LU.set(i, k, a_ik);
                    }

                    // for j = k+1 to n
                    for (Long j = LU.getNextValue(k, k, false); j != null; j = LU.getNextValue(k, j, false)) {
                        // a_ij   = a_ij         -   a_ik   *   a_kj
                        LU.set(i, j, LU.get(i, j).subR(a_ik.multR(LU.get(k, j))));
                    }
                }
            }
        }
    }

    /**
     * Solve a system of linear equations in the Form Ax = b,
     * where A is the decomposed matrix.
     * <p>
     * see Introduction to Algorithms, 3rd Ed., Ch. 28.1, p817, LUP-Solve
     *
     * @param b the "right hand side" of the equations
     * @return the solution x
     */
    public TreeMap<Long, ResidueClass> solve(TreeMap<Long, ResidueClass> b) throws FFaplAlgebraicException {
        TreeMap<Long, ResidueClass> x = new TreeMap<>();
        TreeMap<Long, ResidueClass> y = new TreeMap<>();

        for (long i = 1; i <= n; i++) {
            // y_i = b_pi[i] - sum(...)
            ResidueClass y_i = b.getOrDefault(p(i), zero);

            for (long j = 1; j < i; j++) {
                // y_i = y_i - l_ij    *     y_j
                y_i = y_i.subR(l(i, j).multR(y.get(j)));
            }

            y.put(i, y_i);
        }

        for (long i = n; i >= 1; i--) {
            // x_i = (y_i - sum(...)) / u_ii
            ResidueClass x_i = y.getOrDefault(i, zero);

            for (long j = i + 1; j <= n; j++) {
                // x_i = x_i - u_ij    *     x_j
                x_i = x_i.subR(u(i, j).multR(x.get(j)));
            }

            x.put(i, x_i.divR(u(i, i)));
        }

        return x;
    }

    /**
     * Get explicit lower triangular matrix L of the decomposition.
     *
     * @return L
     */
    public Matrix<ResidueClass> getL() {
        Matrix<ResidueClass> L = new Matrix<>(n, n, zero);

        for (long i = 1; i <= n; i++) {
            // head-map returns a map with all mappings with keys less than i
            TreeMap row = LU.getRow(i);
            if (row != null)
                L.setRow(i, new TreeMap<>(LU.getRow(i).headMap(i)));
            L.set(i, i, one);
        }

        return L;
    }

    /**
     * Get explicit upper triangular matrix U of the decomposition.
     *
     * @return U
     */
    public Matrix<ResidueClass> getU() {
        Matrix<ResidueClass> U = new Matrix<>(n, n, zero);

        for (long i = 1; i <= n; i++) {
            // tail-map returns a map with all mappings with keys greater than or equal to i
            TreeMap row = LU.getRow(i);
            if (row != null)
                U.setRow(i, new TreeMap<>(LU.getRow(i).tailMap(i)));
        }

        return U;
    }

    /**
     * Get values from the lower triangular matrix L.
     *
     * @param i row
     * @param j column
     * @return value l_ij
     */
    private ResidueClass l(long i, long j) {
        return i > j ? LU.get(i, j) : i == j ? one : zero;
    }

    /**
     * Get values from the lower triangular matrix L.
     *
     * @param i row
     * @param j column
     * @return value l_ij
     */
    private ResidueClass u(long i, long j) {
        return i > j ? zero : LU.get(i, j);
    }

    /**
     * Applies the permutation pi[i].
     *
     * @param i input
     * @return permuted value pi[i]
     */
    private long p(long i) {
        return P.getOrDefault(i, i);
    }
}
