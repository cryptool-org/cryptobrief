package ffapl.java.classes;

import ffapl.exception.FFaplException;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicOperations;
import ffapl.java.interfaces.IJavaType;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

/**
 * Sparse matrix implementation using a double-nested TreeMap
 * structured in row-major order.
 * <p>
 * This class uses one-based indexing for its entries, meaning the
 * first entry (upper left) of a matrix A mxn (m rows, n columns)
 * can be accessed with {@code A.get(1,1)}
 * and the last entry (lower right) with {@code A.get(m,n)}.
 * Therefore, standard matrix item descriptions like
 * a<subInPlace>{@code ij}</subInPlace> translate directly to
 * {@code A.get(i,j)}.
 *
 * @param <V> type of values in the matrix
 * @see TreeMap
 * @see MatrixIterator
 */
public class Matrix<V extends IAlgebraicOperations<V>>
        implements IJavaType<Matrix<V>>, Iterable<Matrix.MatrixEntry<V>> {

    private final int typeID = 15;

    // TODO method: importMatrix which copies a matrix (or parts of a matrix) to some place in this matrix
    // TODO method: determinant
    // TODO method: inverse

    /**
     * the "zero" value that fills the empty values of the sparse matrix
     * this value is assumed to have properties of zero
     * (i.e. if this value is added to others, they remain unchanged and
     * if this value is multiplied with others it remains unchanged)
     */
    private V defaultValue;

    /**
     * number of rows
     */
    private long m;

    /**
     * number of columns
     */
    private long n;

    /**
     * A double nested TreeMap that stores the elements of the matrix.
     * The outer TreeMap is indexed by row number and stores TreeMaps
     * containing the elements of the respective row. The inner TreeMap
     * is indexed by column number. If an element is not present (no
     * entry in the inner TreeMap or the whole row is missing) it is
     * assumed to have the default value.
     * <p>
     * TreeMap has no default iterator (which makes sense), but can,
     * through use of {@link Map#entrySet}, comfortably be iterated
     * over (providing desirable properties for a sparse implementation):
     * <pre>{@code
     * for (Map.Entry<Long, TreeMap<Long, V>> row : matrix.entrySet()) {
     *     for (Map.Entry<Long, V> entry : row.getValue().entrySet()) {
     *         System.out.println("row: " + row.getKey() + ", col: "
     *              + entry.getKey() + ", value: " + entry.getValue());
     *     }
     * }
     * }</pre>
     */
    private TreeMap<Long, TreeMap<Long, V>> matrix = new TreeMap<>();

    private LUPDecomposition lup;

    /**
     * Create an empty sparse matrix (A mxn) with all values set to the
     * default value.
     *
     * @param m            number of rows
     * @param n            number of columns
     * @param defaultValue default value of the matrix
     */
    public Matrix(long m, long n, V defaultValue) {
        this.m = m;
        this.n = n;
        this.defaultValue = defaultValue.clone();
    }

    /**
     * Creates a matrix as copy of another matrix.
     *
     * @param matrix original
     */
    public Matrix(Matrix<V> matrix) {
        this.m = matrix.getM();
        this.n = matrix.getN();
        this.defaultValue = matrix.getDefaultValue().clone();

        for (MatrixEntry<V> entry : matrix) {
            this.set(entry.i, entry.j, entry.value.clone());
        }
    }

    /**
     * Creates a sparse matrix from a two-dimensional array of values.
     *
     * @param array        matrix stored as array
     * @param defaultValue default value of the matrix
     * @throws FFaplException when array is null or not rectangular
     */
    public Matrix(V[][] array, V defaultValue) throws FFaplException {
        if (array == null)
            throw new FFaplException();

        this.m = array.length;
        this.n = array[0].length;
        this.defaultValue = defaultValue.clone();

        for (int i = 0; i < m; i++) {
            if (array[i].length != n)
                throw new FFaplException();

            for (int j = 0; j < n; j++) {
                V value = array[i][j];
                this.set(i + 1, j + 1, value);
            }
        }
    }

    /**
     * Creates a sparse matrix from a two-dimensional {@link Array}.
     *
     * @param array        matrix stored as {@link Array}
     * @param defaultValue default value of the sparse matrix
     * @throws FFaplException when array is null, not two-dimensional
     *                        or not rectangular
     * @see Array
     */
    public Matrix(Array array, V defaultValue) throws FFaplException, ClassCastException {
        if (array == null || array.dim() != 2)
            throw new FFaplException();

        this.m = array.length();
        this.n = ((Array) array.getValue(0)).length();
        this.defaultValue = defaultValue.clone();

        Vector<Integer> pos = new Vector<>();
        pos.add(0);
        pos.add(0);

        for (int i = 0; i < m; i++) {
            pos.set(0, i);
            if (((Array) array.getValue(pos)).length() != n)
                throw new FFaplException();

            for (int j = 0; j < n; j++) {
                pos.set(1, j);
                // unsafe cast as unavoidable consequence of the nested Object[]
                // data structure used in ffapl.java.classes.Array
                @SuppressWarnings("unchecked")
                V value = (V) array.getValue(pos);
                this.set(i + 1, j + 1, value);
            }
        }
    }

    /**
     * Creates a sparse matrix from a (coordinates -> value) map.
     * The map shall be indexed by vectors (or rather, {@link List}s,
     * for compatibility) containing two Long values,
     * (a row and a column number, in that order).
     *
     * @param map          map of elements
     * @param m            number of rows
     * @param n            number of columns
     * @param defaultValue default value of the matrix
     * @throws FFaplException when coordinates are not two-dimensional
     */
    public Matrix(Map<List<Long>, V> map, long m, long n, V defaultValue) throws FFaplException {
        this.m = m;
        this.n = n;
        this.defaultValue = defaultValue.clone();

        if (map != null) {
            List<Long> pos;
            for (Map.Entry<List<Long>, V> entry : map.entrySet()) {
                pos = entry.getKey();

                if (pos.size() != 2)
                    throw new FFaplException();

                this.set(pos.get(0), pos.get(1), entry.getValue());
            }
        }
    }

    /**
     * Swaps two items in a map.
     *
     * @param map  the map
     * @param key1 key of first item
     * @param key2 key of second item
     * @param <K>  map key type
     * @param <V>  map value type
     */
    public static <K, V> void swapMapItems(Map<K, V> map, K key1, K key2) {
        swapMapItems(map, key1, key2, null, null, false);
    }

    /**
     * Swaps two items in a map.
     *
     * @param map       the map
     * @param key1      key of first item
     * @param key2      key of second item
     * @param default1  value to assume for first item if not set
     * @param default2  value to assume for second item if not set
     * @param writeNull whether to put null values in the map
     * @param <K>       map key type
     * @param <V>       map value type
     */
    public static <K, V> void swapMapItems(Map<K, V> map, K key1, K key2, V default1, V default2, boolean writeNull) {
        // swap map values of key1 and key2 using variables value1 and value2
        // (dear reader, forgive me for using TWO swap vars,
        // but it is necessary to avoid unreadable code. trust me, ive tried)

        if (map != null) {
            // do not swap if keys are equal. keys are considered equal iff:
            // their references match OR a call to their equals method returns true
            if (!Objects.equals(key1, key2)) {

                V value1 = map.getOrDefault(key1, default1);
                V value2 = map.getOrDefault(key2, default2);

                map.remove(key1);
                map.remove(key2);

                if (value2 != null || writeNull)
                    map.put(key1, value2);

                if (value1 != null || writeNull)
                    map.put(key2, value1);
            }
        }
    }

    /**
     * Multiplies two matrices {@code A mxn} and {@code B nxp}.
     * For matrix-vector multiplications ({@code p = 1})
     * consider using {@link #multiplyVectorRight(NavigableMap)} instead.
     *
     * @param A factor
     * @param B factor
     * @return product ({@code C mxp})
     * @throws FFaplAlgebraicException if addition or multiplication
     *                                 of values fails
     * @see #isCompatibleMult(Matrix)
     */
    public static <T extends IAlgebraicOperations<T>> Matrix<T> multiply(Matrix<T> A, Matrix<T> B)
            throws FFaplAlgebraicException {

        if (A.isCompatibleMult(B)) {
            // matrices: A mxn, B nxp, C mxp
            Matrix<T> C = new Matrix<>(A.getM(), B.getN(), A.getDefaultValue());

            long i; // row of A and C (0..m)
            long j; // column of B and C (0..p)
            long k; // column of A and row of B (0..n)

            // fun begins here
            // normally would have to iterate over rows (m) and columns (p)
            // of C and then do (n) multiplications to find the value but in
            // this sparse matrix, it's trivial too see that when row (i)
            // of matrix A is completely filled with zeroes, the same row in C
            // will be too. so only iterate over non zero rows of A
            for (Map.Entry<Long, TreeMap<Long, T>> rowEntry : A.matrix.entrySet()) {
                i = rowEntry.getKey();
                TreeMap<Long, T> row = rowEntry.getValue();

                // because sparse matrix is in row major order (collection
                // of rows which in turn are a collection of elements)
                // have to iterate over all columns of B/C
                for (j = 1; j <= B.getN(); j++) {
                    // for each element of target matrix C:
                    // start with zero
                    T value = C.getDefaultValue();
                    // then iterate over the row of A to avoid unnecessary
                    // multiplications with zero
                    for (Map.Entry<Long, T> entry : row.entrySet()) {
                        k = entry.getKey();
                        // C[i][j] += A[i][k] * B[k][j]
                        value = value.addR(row.get(k).multR(B.get(k, j)));
                    }
                    // and write to C
                    C.set(i, j, value);
                }
            }
            return C;

        } else {
            return null;
        }
    }

    /**
     * Adds two matrices and returns the result.
     * <p>
     * If one of the matrices is no longer needed after this operation,
     * consider using {@link #addInPlace(Matrix)}.
     *
     * @param A   summand
     * @param B   summand
     * @param <T> type of values in the matrices
     * @return C sum
     * @throws FFaplAlgebraicException when operations on underlying objects fail
     * @see #addInPlace(Matrix)
     */
    public static <T extends IAlgebraicOperations<T>> Matrix<T> add(Matrix<T> A, Matrix<T> B)
            throws FFaplAlgebraicException {

        Matrix<T> C = A.clone();
        C.addInPlace(B);
        return C;
    }

    /**
     * Subtracts a matrix from another and returns the result.
     * <p>
     * If one of the matrices is no longer needed after this operation,
     * consider using {@link #subInPlace(Matrix)}.
     *
     * @param A   minuend
     * @param B   subtrahend
     * @param <T> type of values in the matrices
     * @return C difference
     * @throws FFaplAlgebraicException when operations on underlying objects fail
     * @see #subInPlace(Matrix)
     */
    public static <T extends IAlgebraicOperations<T>> Matrix<T> sub(Matrix<T> A, Matrix<T> B)
            throws FFaplAlgebraicException {

        Matrix<T> C = A.clone();
        C.subInPlace(B);
        return C;
    }

    /**
     * Multiplies this matrix (A mxn) with another matrix (B nxp).
     * For matrix-vector multiplications ({@code p = 1})
     * consider using {@link #multiplyVectorRight(NavigableMap)} instead.
     *
     * @param B factor
     * @return product ({@code C mxp})
     * @throws FFaplAlgebraicException if addition or multiplication
     *                                 of values fails
     * @see #isCompatibleMult(Matrix)
     */
    public Matrix<V> multiply(Matrix<V> B)
            throws FFaplAlgebraicException {
        return multiply(this, B);
    }

    /**
     * Get the number of columns in the matrix.
     *
     * @return number of columns
     */
    public long getN() {
        return this.n;
    }

    /**
     * Get the number of rows in the matrix.
     *
     * @return number of rows
     */
    public long getM() {
        return this.m;
    }

    /**
     * Checks whether this matrix is a square matrix,
     * i.e. it has the same number of rows and columns.
     *
     * @return true if this is a square matrix
     */
    public boolean isSquareMatrix() {
        return this.m == this.n;
    }

    /**
     * Get the default value for elements of this sparse matrix,
     * i.e. the value that fills empty entries.
     *
     * @return default element value
     */
    public V getDefaultValue() {
        return this.defaultValue.clone();
    }

    /**
     * Set the default value for elements of this sparse matrix,
     * i.e. the value that fills empty entries.
     *
     * @param defaultValue default element value
     */
    public void setDefaultValue(V defaultValue) {
        this.defaultValue = defaultValue.clone();
    }

    /**
     * Finds the next row that is non-zer
     * (i.e., has at least one non-zero entry),
     * starting at the starting row i and counting up the row number.
     * <p>
     * If orEqual is set to true, this can also return
     * the starting row i itself, if it is non-zero.
     *
     * @param i       the starting row
     * @param orEqual if true, will return i, if row i is non-zero
     * @return the row number of the next non-zero row
     */
    public Long getNextRow(long i, boolean orEqual) {
        return orEqual ? matrix.ceilingKey(i) : matrix.higherKey(i);
    }

    /**
     * Finds the first preceding row that is non-zero
     * (i.e., has at least one non-zero entry),
     * starting at the starting row i and counting down the row number.
     * <p>
     * If orEqual is set to true, this can also return
     * the starting row i itself, if it is non-zero.
     *
     * @param i       the starting row
     * @param orEqual if true, will return i, if row i is non-zero
     * @return the row number of the first preceding non-zero row
     */
    public Long getPrevRow(long i, boolean orEqual) {
        return orEqual ? matrix.floorKey(i) : matrix.lowerKey(i);
    }

    /**
     * Finds the next value in the row i that is non-zero,
     * starting at the starting column j and counting up the column number.
     * <p>
     * If orEqual is set to true, this can also return
     * the starting column j itself, if A[i,j] is non-zero.
     *
     * @param i       the starting row
     * @param j       the starting column
     * @param orEqual if true, will return j, if item A[i,j] is non-zero
     * @return the column number of the next non-zero entry
     */
    public Long getNextValue(long i, long j, boolean orEqual) {
        TreeMap<Long, V> row = matrix.get(i);
        return row == null ? null : (orEqual ? row.ceilingKey(j) : row.higherKey(j));
    }

    /**
     * Finds the first preceding value in the row i that is non-zero,
     * starting at the starting column j and counting down the column number.
     * <p>
     * If orEqual is set to true, this can also return
     * the starting column j itself, if A[i,j] is non-zero.
     *
     * @param i       the starting row
     * @param j       the starting column
     * @param orEqual if true, will return j, if item A[i,j] is non-zero
     * @return the column number of the first preceding non-zero entry
     */
    public Long getPrevValue(long i, long j, boolean orEqual) {
        TreeMap<Long, V> row = matrix.get(i);
        return row == null ? null : (orEqual ? row.floorKey(j) : row.lowerKey(j));
    }

    /**
     * Swaps two rows in this matrix.
     *
     * @param i1 number of first row
     * @param i2 number of second row
     */
    public void swapRows(long i1, long i2) {
        swapMapItems(this.matrix, i1, i2);
    }

    /**
     * Set the value in row {@code i} and column {@code j} to
     * {@code value}. Will remove entry if value is null or equal
     * to the default value of the matrix.
     *
     * @param i     row
     * @param j     column
     * @param value new value
     * @return previous value
     */
    public V set(long i, long j, V value) {
        if (value == null || (defaultValue.equalType(value) && defaultValue.equals(value))) {
            return this.setToDefault(i, j);

        } else {
            if (validCoordinates(i, j)) {
                // get row. if row does not exist (all zero values) insert and get new row.
                TreeMap<Long, V> row = matrix.computeIfAbsent(i, k -> new TreeMap<>());
                return row.put(j, value.clone());
            }
        }

        return null;
    }

    /**
     * Sets the contents of row {@code i} to the values given in the map {@code row}.
     * Note that if there are entries in the given map that would lie outside of this
     * matrix, the changes will be discarded.
     *
     * @param i   the number of the row to replace
     * @param row a map of column numbers to values
     * @return the row that was replaced by this action
     */
    public Map<Long, V> setRow(long i, NavigableMap<Long, V> row) {
        if (validCoordinates(i, 1L) &&
                // entries of row should be within the matrix
                row.lowerKey(1L) == null && row.higherKey(this.n) == null) {

            // clear default values
            row.values().removeAll(Collections.singleton(this.getDefaultValue()));

            // diamond operator "<>" could be used here instead of "<Long, V>"
            // but then this would be an unchecked assignment: 'java.util.TreeMap' to 'java.util.TreeMap<Long,V>'
            return matrix.put(i, new TreeMap<Long, V>(row));
        }

        return null;
    }

    /**
     * Set the value in row {@code i} and column {@code j}
     * to the default value of the matrix.
     * (i.e. remove the entry)
     *
     * @param i row
     * @param j column
     * @return previous value
     */
    public V setToDefault(long i, long j) {
        if (validCoordinates(i, j)) {
            TreeMap<Long, V> row = matrix.get(i);

            if (row != null) {
                V previousValue = row.remove(j);

                // if element was last entry in row, remove row
                if (row.isEmpty())
                    matrix.remove(i);

                return previousValue;
            }
        }

        return null;
    }

    /**
     * Get the value in row {@code i} and column {@code j}, or the
     * default value of the matrix if no entry is present.
     *
     * @param i row
     * @param j column
     * @return value
     */
    public V get(long i, long j) {
        if (validCoordinates(i, j)) {
            TreeMap<Long, V> row = matrix.get(i);

            if (row == null)
                return defaultValue;
            else
                return row.getOrDefault(j, defaultValue).clone();
        } else {
            return null;
        }
    }

    /**
     * Get a whole row {@code i}.
     * Note that zero values are represented by missing entries.
     * Thus, calling this method on a zero row will give an empty map.
     *
     * @param i row
     * @retur n the row
     */
    public TreeMap<Long, V> getRow(long i) {
        if (!validCoordinates(i, 1L))
            return null;

        return matrix.getOrDefault(i, new TreeMap<>());
    }

    /**
     * Checks whether the value in row {@code i} and column
     * {@code j} is non-zero
     * (i.e. there is an entry for the given coordinates).
     *
     * @param i row
     * @param j column
     * @return true iff value at coordinates is non zero
     */
    public boolean hasNonZeroEntryAt(long i, long j) {
        if (validCoordinates(i, j)) {
            TreeMap<Long, V> row = matrix.get(i);

            if (row != null) {
                V value = row.get(j);
                if (value != null && !defaultValue.equals(value))
                    return true;
            }
        }

        return false;
    }

    /**
     * Checks whether row is non zero
     * (i.e. there is at least one non-zero entry in the given row)
     *
     * @param i row
     * @return true iff row has non-zero entries
     */
    public boolean hasNonZeroRowAt(long i) {
        return validCoordinates(i, 1L) && matrix.get(i) != null;
    }

    /**
     * Checks whether given coordinates (row {@code i} and column {@code j})
     * are valid for this matrix (i.e. are inside the boundaries).
     * This does not imply that there is a non zero entry at these coordinates.
     *
     * @param i row
     * @param j column
     * @return true if coordinates are valid
     */
    public boolean validCoordinates(Long i, Long j) {
        return i != null && i > 0 && i <= this.getM() &&
                j != null && j > 0 && j <= this.getN();
    }

    /**
     * Checks whether this matrix is compatible to the matrix {@code B}
     * with respect to matrix addition and subtraction
     * (i.e. if both matrices have the same number of rows and columns).
     *
     * @param B other matrix
     * @return true if the matrices are compatible
     */
    public boolean isCompatibleAdd(Matrix B) {
        return this.equalType(B) && this.getN() == B.getN() && this.getM() == B.getM();
    }

    /**
     * Checks whether this matrix ({@code A}) is compatible to matrix {@code B}
     * with respect to matrix multiplication
     * (i.e. if this matrix has as much columns as the other has rows).
     * <p>
     * Note: matrix multiplication is not commutative. Thus, if {@code A}
     * is compatible to {@code B} this does not imply that {@code B}
     * is compatible to {@code A}.
     *
     * @param B other matrix
     * @return true if the matrices are compatible
     * @see #multiply(Matrix, Matrix)
     */
    public boolean isCompatibleMult(Matrix B) {
        return this.equalType(B) && this.getN() == B.getM();
    }

    /**
     * Adds another matrix (B mxn) to this matrix (A mxn).
     *
     * @param B summand
     * @throws FFaplAlgebraicException if addition of values fails
     */
    public void addInPlace(Matrix<V> B) throws FFaplAlgebraicException {
        for (MatrixEntry<V> e : B) {
            this.set(e.i, e.j, this.get(e.i, e.j).addR(e.value));
        }
    }

    /**
     * Subtracts another matrix (B mxn) from this matrix (A mxn).
     *
     * @param B subtrahend
     * @throws FFaplAlgebraicException if addition of values fails
     */
    public void subInPlace(Matrix<V> B) throws FFaplAlgebraicException {
        for (MatrixEntry<V> e : B) {
            this.set(e.i, e.j, this.get(e.i, e.j).subR(e.value));
        }
    }

    /**
     * Multiplies with a vector on the right (i.e. M = M.v). Assumes column vector.
     * Uses sparsity of matrix and vector and tries to do as few multiplications as possible.
     *
     * @param factor factor (vector)
     * @return product
     * @throws FFaplAlgebraicException if addition or multiplication
     *                                 of values fails
     */
    public TreeMap<Long, V> multiplyVectorRight(NavigableMap<Long, V> factor) throws FFaplAlgebraicException {
        TreeMap<Long, V> product = new TreeMap<>();

        for (Map.Entry<Long, TreeMap<Long, V>> rowEntry : matrix.entrySet()) {

            // iterate over the vector (row or factor) that has less entries,
            // using sparsity. to avoid duplicating code, rename variables
            // into v1 (less entries => iterate over this one) and v2
            NavigableMap<Long, V> v1, v2;
            if (factor.size() <= rowEntry.getValue().size()) {
                v1 = factor;
                v2 = rowEntry.getValue();
            } else {
                v1 = rowEntry.getValue();
                v2 = factor;
            }

            V tmp = this.defaultValue;
            for (Map.Entry<Long, V> entry : v1.entrySet()) {
                V item = v2.get(entry.getKey());

                if (item != null)
                    tmp = tmp.addR(entry.getValue().multR(item));
            }

            product.put(rowEntry.getKey(), tmp);
        }

        return product;
    }

    /**
     * Multiplies with a vector on the right (i.e. M = v.M). Assumes row vector.
     * Uses {@link Matrix#multiply(Matrix)} for calculations.
     *
     * @param factor factor (vector)
     * @return product
     * @throws FFaplAlgebraicException if addition or multiplication
     *                                 of values fails
     */
    public TreeMap<Long, V> multiplyVectorLeft(NavigableMap<Long, V> factor) throws FFaplAlgebraicException {
        Matrix<V> vM = new Matrix<>(1, this.getN(), getDefaultValue());
        vM.setRow(1, factor);
        return vM.multiply(this).getRow(1);
    }

    /**
     * Multiplies this matrix with a scalar value.
     * (i.e. every element in the matrix is multiplied by the value)
     *
     * @param factor scalar factor
     * @throws FFaplAlgebraicException when multiplication of values fails
     * @see BigInteger
     */
    public void scalarMultiplyInPlace(BigInteger factor) throws FFaplAlgebraicException {
        for (MatrixEntry<V> e : this) {
            this.set(e.i, e.j, e.value.scalarMultR(factor));
        }

        /* more elegant variant using methods of TreeMap and Lambdas:
         * unfeasible because methods defined by IAlgebraicOperations
         * throw exceptions unhandled by TreeMap.forEach and TreeMap.replaceAll */
        //matrix.forEach((Long i, TreeMap<Long, V> row) -> row.replaceAll((Long j, V entry) -> entry.scalarMultR(factor)));
    }

    /**
     * Transposes this matrix (A<sup>{@code T}</sup>).
     *
     * @return transposition
     */
    public Matrix<V> transpose() {
        Matrix<V> transposition = new Matrix<>(this.m, this.n, this.defaultValue);

        for (MatrixEntry<V> entry : this) {
            transposition.set(entry.j, entry.i, entry.value);
        }

        return transposition;
    }

    /**
     * Solve equation system given by this matrix and vector {@code b}
     * as {@code Ax = b} for {@code x}.
     * Assume lower triangular matrix.
     *
     * @param b constant term vector
     * @return x - solution
     */
    public TreeMap<Long, V> solveLowerTriangular(Map<Long, V> b) throws FFaplAlgebraicException {
        TreeMap<Long, V> x = new TreeMap<>();

        Long i, j;

        // use iterator, as it's probably more efficient and bug free
        for (Map.Entry<Long, TreeMap<Long, V>> rowEntry : matrix.entrySet()) {
            i = rowEntry.getKey();

            if (this.hasNonZeroRowAt(i)) {
                V tmp = this.defaultValue;

                for (j = getNextValue(i, 1L, true); j != null && j < i; j = getNextValue(i, j, false)) {
                    V a_ij = get(i, j);
                    V x_j = x.get(j);

                    if (a_ij != null && x_j != null) {
                        tmp = tmp.addR(a_ij.multR(x_j));
                    }
                }

                // x_i = ( b_i - sum_j=1..i-1(a_ij * x_j) ) / a_ii
                x.put(i, b.get(i).subR(tmp).divR(get(i, i)));
            }
        }

        return x;
    }

    /**
     * Solve equation system given by this matrix and vector {@code b}
     * as {@code Ax = b} for {@code x}.
     * Assume upper triangular matrix.
     *
     * @param b constant term vector
     * @return x - solution
     */
    public TreeMap<Long, V> solveUpperTriangular(Map<Long, V> b) throws FFaplAlgebraicException {
        TreeMap<Long, V> x = new TreeMap<>();

        Long i, j;

        // iterate over rows, starting at the bottom (no reverse-iterator here)
        for (i = getPrevRow(m, true); i != null; i = getPrevRow(i, false)) {
            if (this.hasNonZeroEntryAt(i, i)) {
                V tmp = getDefaultValue();

                for (j = getNextValue(i, i, false); j != null; j = getNextValue(i, j, false)) {
                    V a_ij = get(i, j);
                    V x_j = x.get(j);

                    if (a_ij != null && x_j != null) {
                        tmp = tmp.addR(a_ij.multR(x_j));
                    }
                }

                V b_i = b.getOrDefault(i, defaultValue);

                // x_i = ( b_i - sum_j=i+1..n(a_ij * x_j) ) / a_ii
                x.put(i, b_i.subR(tmp).divR(get(i, i)));
            }
        }

        return x;
    }

    /**
     * Performs Gaussian Elimination and reduces this matrix to upper triangular form.
     * Returns the nullity, or a lower bound for it,
     * if a non-zero value was given for {@code earlyOut}.
     * Optionally applies the same transformations to a column vector {@code b}
     * or stores the permutations in a map (which rows were swapped in the process).
     * <p>
     * Algorithm is applied in place, it is required to clone this matrix in order to
     * use its normal form afterwards.
     *
     * @param b           vector of constant terms (optional)
     * @param permutation a Map to save the swaps in (optional)
     * @param earlyOut    break and return this value if the matrix is shown
     *                    to have at least this nullity.
     * @return nullity of the matrix, or a lower bound for it,
     * if {@code earlyOut} is set to a non-zero value
     * @throws FFaplAlgebraicException when operations on underlying objects fail
     */
    public long rowReduceInPlace(NavigableMap<Long, V> b, TreeMap<Long, Long> permutation, long earlyOut) throws FFaplAlgebraicException {
        // TODO param: numerically stable (use max pivot, requires compareTo in interface)
        // TODO param: division free (multiply both rows by each others pivots)

        boolean solveForB = b != null;
        boolean trackSwaps = permutation != null;

        long nullity = 0;

        // iterate over rows (h ~ current row, k ~ pivot column)
        for (long h = 1, k = 1; h <= m && k <= n; k++) {

            // find row with non-zero (pivot) element
            Long pivot = this.getNextRow(h, true);
            while (pivot != null && !this.hasNonZeroEntryAt(pivot, k)) {
                pivot = this.getNextRow(pivot, false);
            }

            // if no pivot can be found, no adjusting is necessary; increment nullity
            if (pivot == null || !this.hasNonZeroEntryAt(pivot, k)) {
                if (++nullity == earlyOut)
                    return nullity;
                continue;
            }

            // swap rows/entries in matrix, vector and (if needed) the permutation
            this.swapRows(h, pivot);
            if (solveForB)
                swapMapItems(b, h, pivot);
            if (trackSwaps)
                swapMapItems(permutation, h, pivot, h, pivot, false);

            for (long i = h + 1; i <= m; i++) {
                // only adjust row if pivot entry is non zero
                if (this.hasNonZeroEntryAt(i, k)) {
                    //factor =   A[i, k]  /     A[h, k]
                    V factor = get(i, k).divR(get(h, k));

                    // set pivot value of row to zero
                    this.set(i, k, this.getDefaultValue());

                    // adjust other values in row
                    for (long j = k + 1; j <= n; j++) {
                        //A[i, j] = A[i, j]   -    A[h, j]   *   factor
                        set(i, j, get(i, j).subR(get(h, j).multR(factor)));
                    }

                    // adjust value in vector
                    if (solveForB) {
                        V b_h = b.get(h);
                        if (b_h != null) {
                            V b_i = b.getOrDefault(i, this.getDefaultValue());
                            //  b[i]=b[i] -   b[h]  *   factor
                            b.put(i, b_i.subR(b_h.multR(factor)));
                        }
                    }
                }
            }
            ++h;
        }

        return nullity;
    }

    /**
     * Prepares a matrix (representing a system of linear equations)
     * for being solved for multiple "right hand sides".
     * <p>
     * Currently only supports residual matrices.
     *
     * @see LUPDecomposition
     */
    public static void prepareForSolving(Matrix<ResidueClass> A) {
        if (A == null)
            return;

        if (A.lup != null || !A.getDefaultValue()._modulus.equals(BigInteger.TWO))
            return;

        try {
            A.lup = new LUPDecomposition(A);
        } catch (FFaplAlgebraicException ignored) {
        }
    }

    public static TreeMap<Long, ResidueClass> solve(Matrix<ResidueClass> A, TreeMap<Long, ResidueClass> b, boolean inPlace) throws FFaplAlgebraicException {
        if (A == null || b == null)
            return null;

        if (A.lup != null) {
            try {
                return A.lup.solve(b);
            } catch (FFaplAlgebraicException ignored) {
                // if an error occurs, just solve normally
            }
        }
        return A.solve(b, inPlace);
    }

    /**
     * Solves a system of linear equations {@code Ax = b} given in form of a matrix {@code A} (this matrix)
     * and a vector {@code b} of constant terms, by reducing the Matrix to upper triangular form.
     * <p>
     * Note that if the original matrix (this matrix) or the vector {@code b}
     * are needed after this operation, the inPlace switch should be set to false
     *
     * @param b       the vector b to solve Ax = b for
     * @param inPlace if set to true, all operations on this matrix and vector {@code b}
     *                will be done in place
     * @return the solution vector x
     * @throws FFaplAlgebraicException
     */
    public TreeMap<Long, V> solve(NavigableMap<Long, V> b, boolean inPlace) throws FFaplAlgebraicException {
        Matrix<V> A = inPlace ? this : this.clone();
        b = inPlace ? b : new TreeMap<>(b);

        A.rowReduceInPlace(b, null, 0);
        return A.solveUpperTriangular(b);
    }

    /**
     * Computes the nullity of this matrix,
     * i.e. the number of columns that are not linear independent
     * == the number of columns minus the rank of this matrix.
     * Note that if earlyOut is set to a value greater than zero,
     * the returned value may not be the actual nullity.
     * If this matrix is only ever needed for calculating its nullity,
     * then the inPlace parameter should be set to true,
     * thereby saving space, but potentially changing this matrix.
     * <p>
     * Uses simple Gaussian Elimination.
     *
     * @param inPlace  determines if the calculation should be performed "in place"
     * @param earlyOut break if nullity is shown to be at least this value
     * @return the nullity of this matrix, or the earlyOut value
     * @throws FFaplAlgebraicException
     */
    public long nullity(long earlyOut, boolean inPlace) throws FFaplAlgebraicException {

        long start = System.currentTimeMillis();

        // only perform calculations in place if parameter inPlace is set
        Matrix<V> A = inPlace ? this : this.clone();
        return A.rowReduceInPlace(null, null, earlyOut);
    }

    /**
     * Applies a permutation to this matrix.
     * The permutation is given as a TreeMap,
     * where a mapping of key -> value determines,
     * that the row {@code key} of this matrix should be
     * moved to {@code value}.
     * <p>
     * If {@code inverse} is set to true, the inverse permutation is applied,
     * i.e. the row {@code value} of this matrix is moved to {@code key}.
     *
     * @param permutation a map representing the permutation
     * @param inverse     determines whether the inverse permutation should be applied
     * @return
     */
    public Matrix<V> applyPermutation(TreeMap<Long, Long> permutation, boolean inverse) {
        TreeMap<Long, TreeMap<Long, V>> newMatrix = new TreeMap<>();

        // for each row
        for (Map.Entry<Long, Long> e : permutation.entrySet()) {
            if (!inverse) {
                newMatrix.put(e.getKey(), matrix.get(e.getValue()));
                matrix.remove(e.getValue());
            } else {
                newMatrix.put(e.getValue(), matrix.get(e.getKey()));
                matrix.remove(e.getKey());
            }
        }

        for (Map.Entry<Long, TreeMap<Long, V>> e : matrix.entrySet()) {
            newMatrix.put(e.getKey(), e.getValue());
        }

        this.matrix = newMatrix;
        return this;
    }

    @Override
    public int typeID() {
        return typeID;
    }

    @Override
    public String classInfo() {
        // nxm Matrix of type <type>
        // e.g. 3x4 Matrix of type Integer
        return "" + n + "x" + m + " Matrix of type " + defaultValue.classInfo();
    }

    @Override
    public Matrix<V> clone() {
        return new Matrix<>(this);
    }

    /**
     * Checks whether two matrices are of equal types,
     * meaning they are both matrices, non null and
     * contain elements of equal types.
     *
     * @param type the other matrix to check
     * @return true if the types are equal, false otherwise
     */
    @Override
    public boolean equalType(Object type) {
        // other object is of equal type if other object fulfils the following criteria
        return type instanceof Matrix // is matrix and not null (implied by instanceof)
                && this.defaultValue.equalType(((Matrix) type).getDefaultValue()); // has values of equal types
    }

    /**
     * Checks whether two matrices are equal with respect to their elements.
     * Two matrices (A mxn, B mxn) are considered equal if they are the same
     * size and for each coordinate in the matrices {@code (i,j)}
     * the following evaluates to true:
     * <center>{@code A.get(i, j).equals(B.get(i, j))}</center>
     *
     * @param B other matrix
     * @return true if the matrices are equal
     */
    public boolean equals(Matrix<V> B) {
        // can only be equal if: types are equal, both have same size and default value
        if (this.equalType(B)
                && this.isCompatibleAdd(B)
                && this.getDefaultValue().equals(B.getDefaultValue())) {

            // cant just iterate over one sparse matrix.
            // would be wrong for A = {{1,0},{0,1}} and B = {{1,1},{0,1}}
            // instead implement "parallel foreach" over two iterators

            Iterator<MatrixEntry<V>> iterA = this.iterator();
            Iterator<MatrixEntry<V>> iterB = B.iterator();

            while (iterA.hasNext() && iterB.hasNext()) {
                MatrixEntry<V> itemA = iterA.next();
                MatrixEntry<V> itemB = iterB.next();

                if (itemA == null || !itemA.equals(itemB))
                    return false;
            }

            // if one iterator still has next items
            // (and the other one has not, implied by failed while condition)
            // they cant be equal;
            if (iterA.hasNext() || iterB.hasNext())
                return false;
            else
                return true;

        } else {
            return false;
        }
    }

    /**
     * Returns a string representation of the matrix
     * in Java array-initializer/mathematica matrix notation
     * (curly bracket, row-major order, for instance the identity matrix
     * of size 2 looks like this: {@code {{1,0},{0,1}}}).
     * <p>
     * Note: this method is just for visualization and exports of
     * small matrices. Do not try this on big ones as the string is not
     * sparse but instead includes all zero values.
     *
     * @return string representation of the matrix
     */
    @Override
    public String toString() {
        // StringBuilder used for better efficiency
        // when concatenating strings in loops
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        for (int i = 1; i <= this.getM(); i++) {
            sb.append('{');
            for (int j = 1; j <= this.getN(); j++) {
                V item = this.get(i, j);
                sb.append((item != null ? item : getDefaultValue()).toString()).append(',');
            }

            // insert closing parenthesis before comma
            // so there is one after the parenthesis
            sb.insert(sb.length() - 1, '}');
        }

        // delete last comma (also more readable than replace method)
        sb.deleteCharAt(sb.length() - 1).append('}');
        return sb.toString();
    }

    /**
     * Returns a string representation of the matrix as console "table"
     * with each row in a line separated by tabs.
     * <p>
     * Note: this method is just for visualization and exports of
     * small matrices. Do not try this on big ones as the string is not
     * sparse but instead includes all zero values.
     *
     * @return table representation of the matrix
     */
    public String toStringTable() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= this.getM(); i++) {
            for (int j = 1; j <= this.getN(); j++) {
                sb.append(this.get(i, j).toString()).append('\t');
            }

            sb.deleteCharAt(sb.length() - 1).append(System.lineSeparator());
        }

        return sb.toString();
    }

    /**
     * Applies a function ({@code oldValue -> newValue}) to all non zero entries
     * of this matrix and replaces the entries with the respective results.
     *
     * @param function function
     */
    public void replaceAll(Function<V, V> function) {
        // found no use for this yet (see scalarMultiply), might come in handy though
        matrix.forEach((Long i, TreeMap<Long, V> row) -> row.replaceAll((Long j, V entry) -> function.apply(entry)));
    }

    /**
     * Returns an iterator over the elements in this matrix in row-major order.
     * Elements are given as {@link MatrixEntry} objects that contain
     * row ({@link MatrixEntry#i}) and column number ({@link MatrixEntry#j})
     * as well as the value ({@link MatrixEntry#value}).
     *
     * @return an iterator over the elements in this matrix in row-major order
     * @see MatrixIterator
     * @see MatrixEntry
     */
    @Override
    public Iterator<MatrixEntry<V>> iterator() {
        return new MatrixIterator<>(matrix);
    }

    /**
     * Simple iterator class to enable iterating over the elements of a Matrix.
     * Elements are given as {@link MatrixEntry} objects that contain
     * row ({@link MatrixEntry#i}) and column number ({@link MatrixEntry#j})
     * as well as the value ({@link MatrixEntry#value}).
     *
     * @param <U> type of values of the matrix
     * @see Matrix
     * @see MatrixEntry
     */
    static class MatrixIterator<U> implements Iterator<MatrixEntry<U>> {

        private TreeMap<Long, U> row;
        private Long i;
        private Long j;
        private TreeMap<Long, TreeMap<Long, U>> matrix;

        private MatrixIterator(TreeMap<Long, TreeMap<Long, U>> matrix) {
            this.matrix = matrix;
            i = matrix.ceilingKey(1L);
            if (i != null) {
                row = matrix.get(i);
                j = row.ceilingKey(1L);
            } else {
                j = null;
            }
        }

        @Override
        public boolean hasNext() {
            return i != null && j != null && row != null;
        }

        @Override
        public MatrixEntry<U> next() {
            if (i != null && j != null && row != null) {
                // fetch next value
                MatrixEntry<U> entry = new MatrixEntry<>(i, j, row.get(j));

                // update i and j, then return
                j = row.higherKey(j);
                if (j == null) {
                    i = matrix.higherKey(i);
                    if (i != null) {
                        row = matrix.get(i);
                        j = row.ceilingKey(1L);
                    }
                }
                return entry;

            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Represents one entry/element of a matrix.
     * Contains row number, column number and value
     * of one item in the matrix.
     *
     * @param <U> type of values in the matrix
     * @see Matrix
     * @see MatrixIterator
     */
    public static class MatrixEntry<U> {
        /**
         * row number
         */
        public long i;

        /**
         * column number
         */
        public long j;

        public U value;

        MatrixEntry(long i, long j, U value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MatrixEntry) {
                MatrixEntry e = (MatrixEntry) o;
                return this.i == e.i && this.j == e.j &&
                        Objects.equals(this.value, e.value);
            } else {
                return false;
            }
        }
    }
}
