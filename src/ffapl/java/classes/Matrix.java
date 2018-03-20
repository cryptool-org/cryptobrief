package ffapl.java.classes;

import ffapl.exception.FFaplException;
import ffapl.java.exception.FFaplAlgebraicException;
import ffapl.java.interfaces.IAlgebraicOperations;
import ffapl.java.interfaces.IJavaType;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

/**
 * Sparse matrix implementation using a double-nested TreeMap in row-major order.
 *
 * @param <V> type of values in the matrix
 * @see TreeMap
 * @see MatrixIterator
 */
public class Matrix<V extends IAlgebraicOperations<V>> implements IJavaType<Matrix<V>>, Iterable<Matrix.MatrixEntry<V>> {

    // TODO method: importMatrix which copies a matrix (or parts of a matrix) to some place in this matrix
    // TODO method: transpose
    // TODO method: determinant

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

    // TODO implement decomposition
    private Matrix<V>[] LuDecomposition = null;

    /**
     * A double nested TreeMap that stores the elements of the matrix.
     * The outer TreeMap is indexed by row number and stores TreeMaps
     * containing the elements of the respective row. The inner TreeMap
     * is indexed by column number. If an element is not present (no
     * entry in the inner TreeMap or the whole row is missing) it is
     * assumed to have the default value.
     */
    private TreeMap<Long, TreeMap<Long, V>> matrix = new TreeMap<>();

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
            this.set(entry.i, entry.j, entry.value);
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

        V value;
        for (int i = 0; i < m; i++) {
            if (array[i].length != n)
                throw new FFaplException();

            for (int j = 0; j < n; j++) {
                value = array[i][j];
                this.set(i + 1, j + 1, value);
            }
        }
    }

    /**
     * Creates a sparse matrix from a two-dimensional {@link Array}.
     *
     * @param array        matrix stored as {@link Array}
     * @param defaultValue default value of the sparse matrix
     * @throws FFaplException when array is null, not two-dimensional or not rectangular
     * @see Array
     */
    public Matrix(Array array, V defaultValue) throws FFaplException {
        if (array == null || array.dim() != 2)
            throw new FFaplException();

        this.m = array.length();
        this.n = ((Array) array.getValue(0)).length();
        this.defaultValue = defaultValue.clone();

        V value;
        Vector<Integer> pos = new Vector<>();
        pos.add(0);
        pos.add(0);

        for (int i = 0; i < m; i++) {
            pos.set(0, i);
            if (((Array) array.getValue(pos)).length() != n)
                throw new FFaplException();

            for (int j = 0; j < n; j++) {
                pos.set(1, j);
                value = (V) array.getValue(pos);
                this.set(i + 1, j + 1, value);
            }
        }
    }

    /**
     * Creates a sparse matrix from a (coordinates -> value) map.
     * The map shall be indexed by Vector containing two Long values,
     * a row and a column number, in that order.
     *
     * @param map          map of elements
     * @param m            number of rows
     * @param n            number of columns
     * @param defaultValue default value of the matrix
     * @throws FFaplException when coordinates are not two-dimensional
     */
    public Matrix(Map<Vector<Long>, V> map, long m, long n, V defaultValue) throws FFaplException {
        this.m = m;
        this.n = n;
        this.defaultValue = defaultValue.clone();

        if (map != null) {
            Vector<Long> pos;
            for (Map.Entry<Vector<Long>, V> entry : map.entrySet()) {
                pos = entry.getKey();

                if (pos.size() != 2)
                    throw new FFaplException();

                this.set(pos.get(0), pos.get(1), entry.getValue());
            }
        }
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

    // matrix entry accessor methods

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
     * Set the value in row {@code i} and column {@code j} to {@code value}.
     * Will remove entry if value is null or equal to the default value of the matrix.
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
     * Get the value in row {@code i} and column {@code j} or the
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
     * Checks whether element is non zero
     * (i.e. there is an entry for the given coordinates).
     *
     * @param i row
     * @param j column
     * @return true if value at coordinates is non zero
     */
    public boolean isValuePresent(long i, long j) {
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
     * Checks whether given coordinates (row {@code i} and column {@code j})
     * are valid for this matrix (i.e. are inside the boundaries).
     * This does not imply that there is a non zero entry at these coordinates.
     *
     * @param i row
     * @param j column
     * @return true if coordinates are valid
     */
    public boolean validCoordinates(long i, long j) {
        return i > 0 && i <= this.getM()
                && j > 0 && j <= this.getN();
    }

    // compatibility checkers

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
     * Checks whether this matrix is compatible to matrix {@code B}
     * with respect to matrix multiplication
     * (i.e. if this matrix has as much columns as the other has rows).
     * <p>
     * Note: matrix multiplication is not commutative. Thus, if {@code A} is compatible
     * to {@code B} this does not imply that {@code B} is compatible to {@code A}
     *
     * @param B other matrix
     * @return true if the matrices are compatible
     */
    public boolean isCompatibleMult(Matrix B) {
        return this.equalType(B) && this.getN() == B.getM();
    }

    // arithmetic methods

    /**
     * Adds another matrix (B mxn) to this matrix (A mxn).
     *
     * @param B summand
     * @throws FFaplAlgebraicException if addition of values fails
     */
    public void add(Matrix<V> B) throws FFaplAlgebraicException {
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
    public void sub(Matrix<V> B) throws FFaplAlgebraicException {
        for (MatrixEntry<V> e : B) {
            this.set(e.i, e.j, this.get(e.i, e.j).subR(e.value));
        }
    }

    /**
     * Multiplies this matrix (A mxn) with another matrix (B nxp).
     *
     * @param B factor
     * @return product (C mxp)
     * @throws FFaplAlgebraicException if addition or multiplication of values fails
     */
    public Matrix<V> multiply(Matrix<V> B) throws FFaplAlgebraicException {
        if (this.isCompatibleMult(B)) {
            // matrices: A mxn, B nxp, C mxp
            Matrix<V> C = new Matrix<>(this.getM(), B.getN(), this.getDefaultValue());

            long i; // row of A and C (0..m)
            long j; // column of B and C (0..p)
            long k; // column of A and row of B (0..n)
            V value;
            TreeMap<Long, V> row;
            // fun begins here
            // normally would have to iterate over rows (m) and columns (p) of C
            // and then do (n) multiplications to find the value
            // but in this sparse matrix, it's trivial too see that when row (i)
            // of matrix A is completely filled with zeroes, the same row in C
            // will be too. so only iterate over non zero rows of A
            for (Map.Entry<Long, TreeMap<Long, V>> rowEntry : matrix.entrySet()) {
                i = rowEntry.getKey();
                row = rowEntry.getValue();

                // because sparse matrix is in row major order (collection of rows
                // which in turn are a collection of elements) have to iterate over
                // all colums of B/C
                for (j = 1; j <= B.getN(); j++) {
                    // for each element of target matrix C:
                    // start with zero
                    value = C.getDefaultValue();
                    // then iterate over the row of A to avoid unnecessary
                    // multiplications with zero
                    for (Map.Entry<Long, V> entry : row.entrySet()) {
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
     * Multiplies this matrix with a scalar value.
     * (i.e. every element in the matrix is multiplied by the value)
     *
     * @param factor scalar factor
     * @throws FFaplAlgebraicException when multiplication of values fails
     * @see BigInteger
     */
    public void scalarMultiply(BigInteger factor) throws FFaplAlgebraicException {
        for (MatrixEntry<V> e : this) {
            this.set(e.i, e.j, e.value.scalarMultR(factor));
        }

        /* more elegant variant using methods of TreeMap and Lambdas:
         * unfeasible because methods defined by IAlgebraicOperations
         * throw exceptions unhandled by TreeMap.forEach and TreeMap.replaceAll */
        //matrix.forEach((Long i, TreeMap<Long, T> row) -> row.replaceAll((Long j, T entry) -> entry.scalarMultR(factor)));

        // alternative:
        /*
        for (Map.Entry<Long, TreeMap<Long, V>> row : matrix.entrySet()) {
            for (Map.Entry<Long, V> entry : row.getValue().entrySet()) {
                entry.setValue(entry.getValue().scalarMultR(factor));
            }
        }
        */
    }

    // methods from IJavaType

    @Override
    public int typeID() {
        return 15;
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

    // utility methods

    /**
     * Checks whether two matrices are equal with respect to their elements.
     * Two matrices (A mxn, B mxn) are considered equal if they are the same size and for
     * each coordinate in the matrices (i,j) the following evaluates to true:
     * <p>
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

            Iterator<MatrixEntry<V>> iterA = this.iterator(), iterB = B.iterator();
            MatrixEntry<V> itemA, itemB;

            while (iterA.hasNext() && iterB.hasNext()) {
                itemA = iterA.next();
                itemB = iterB.next();

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
        // stringbuilder used for better efficiency
        // when concatenating strings in loops
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        for (int i = 1; i <= this.getM(); i++) {
            sb.append('{');
            for (int j = 1; j <= this.getN(); j++) {
                sb.append(this.get(i, j).toString()).append(',');
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

    // iterator stuff

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
    static final class MatrixIterator<U> implements Iterator<MatrixEntry<U>> {

        private TreeMap<Long, U> row;
        private Long i;
        private Long j;
        private TreeMap<Long, TreeMap<Long, U>> matrix;

        private MatrixIterator(TreeMap<Long, TreeMap<Long, U>> matrix) {
            this.matrix = matrix;
            i = matrix.ceilingKey(0L);
            if (i != null) {
                row = matrix.get(i);
                j = row.ceilingKey(0L);
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
                        j = row.ceilingKey(0L);
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
     * @param <T> type of values in the matrix
     * @see Matrix
     * @see MatrixIterator
     */
    public static final class MatrixEntry<T> {
        long i, j;
        T value;

        MatrixEntry(long i, long j, T value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof MatrixEntry) {
                MatrixEntry e = (MatrixEntry) o;
                return this.i == e.i && this.j == e.j &&
                        (this.value == e.value || this.value != null && this.value.equals(e.value));
            } else {
                return false;
            }
        }
    }
}
