public class Matrix {
    private double[][] matrix;

    // Constructors
    Matrix(int rows, int cols) {
        matrix= new double[rows][cols];
        for (int i= 0; i < rows; i++ ) {
            for (int j= 0; j < cols; j++ ) {
                matrix[i][j]= 0;
            }
        }
    }

    Matrix(int rows, int cols, double num) {
        matrix= new double[rows][cols];
        for (int i= 0; i < rows; i++ ) {
            for (int j= 0; j < cols; j++ ) {
                matrix[i][j]= num;
            }
        }
    }

    Matrix(double[][] newArray) {
        matrix= new double[newArray.length][newArray[0].length];
        for (int i= 0; i < newArray.length; i++ ) {
            for (int j= 0; j < newArray[0].length; j++ ) {
                matrix[i][j]= newArray[i][j];
            }
        }
    }

    Matrix(int idenSize) {
        matrix= new double[idenSize][idenSize];
        for (int i= 0; i < idenSize; i++ ) {
            for (int j= 0; j < idenSize; j++ ) {
                if (i == j) {
                    matrix[i][j]= 1;
                } else {
                    matrix[i][j]= 0;
                }
            }
        }
    }

    // Accessors
    public int getRows() {
        return matrix.length;
    }

    public int getCols() {
        return matrix[0].length;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public double[] getRow(int index) {
        return matrix[index];
    }

    public double[] getCol(int index) {
        double[] col= new double[matrix.length];
        for (int i= 0; i < col.length; i++ ) {
            col[i]= matrix[i][index];
        }
        return col;
    }

    public double getElement(int row, int col) {
        return matrix[row][col];
    }

    @Override
    public String toString() {
        String finStr= "";
        for (int i= 0; i < matrix.length; i++ ) {
            for (int j= 0; j < matrix[0].length; j++ ) {
                finStr+= matrix[i][j];
                finStr+= " ";
            }
            finStr+= "\n";
        }
        return finStr;
    }

    // Modifiers
    public void setMatrix(double[][] newMatrix) {
        matrix= new double[newMatrix.length][newMatrix[0].length];
        for (int i= 0; i < newMatrix.length; i++ ) {
            for (int j= 0; j < newMatrix[0].length; j++ ) {
                matrix[i][j]= newMatrix[i][j];
            }
        }
    }

    public void setMatrix(Matrix newMatrix) {
        this.setMatrix(newMatrix.getMatrix());
    }

    public void setRow(int index, double[] newRow) {
        for (int i= 0; i < newRow.length; i++ ) {
            matrix[index][i]= newRow[i];
        }
    }

    public void setCol(int index, double[] newCol) {
        for (int i= 0; i < matrix.length; i++ ) {
            matrix[i][index]= newCol[i];
        }
    }

    public void setElement(int row, int col, double newElem) {
        matrix[row][col]= newElem;
    }

    // Operations
    public void transpose() {
        double[][] newArray= new double[matrix[0].length][matrix.length];
        for (int i= 0; i < matrix[0].length; i++ ) {
            for (int j= 0; j < matrix.length; j++ ) {
                newArray[i][j]= matrix[j][i];
            }
        }
        matrix= newArray;
    }

    public void rotCClws() {
        double[][] rotMatrix= { { 0, -1 }, { 1, 0 } };
        Matrix rot= new Matrix(rotMatrix);
        matrix= multiplyMatrix(this, rot).getMatrix();
    }

    public void rotClws() {
        double[][] rotMatrix= { { 0, 1 }, { -1, 0 } };
        Matrix rot= new Matrix(rotMatrix);
        matrix= multiplyMatrix(this, rot).getMatrix();
    }

    public double determinant() {
        if (matrix.length == matrix[0].length) {
            if (matrix.length == 1) {
                return matrix[0][0];
            } else {
                int sum= 0;
                for (int i= 0; i < matrix.length; i++ ) {
                    double coeff= (-2 * (i % 2) + 1) * matrix[0][i];
                    Matrix minor= new Matrix(generateMinor(matrix, i));
                    sum+= coeff * minor.determinant();
                }
                return sum;
            }
        }
        return 0;
    }

    // Private methods
    private static double[][] generateMinor(double[][] matrix, int index) {
        int newLength= matrix.length - 1;
        double[][] minor= new double[newLength][newLength];
        for (int i= 0; i < newLength; i++ ) {
            for (int j= 0; j < newLength; j++ ) {
                if (j < index) {
                    minor[i][j]= matrix[i + 1][j];
                } else {
                    minor[i][j]= matrix[i + 1][j + 1];
                }
            }
        }
        return minor;
    }

    // Static methods
    public static Matrix addMatrix(Matrix first, Matrix other) {
        int rows= first.getRows();
        int cols= first.getCols();
        int otherRows= other.getRows();
        int otherCols= other.getCols();
        if (rows == otherRows && cols == otherCols) {
            Matrix result= new Matrix(rows, cols);
            for (int i= 0; i < rows; i++ ) {
                for (int j= 0; j < cols; j++ ) {
                    result.setElement(i, j, other.getElement(i, j) +
                        first.getElement(i, j));
                }
            }
            return result;
        }
        return new Matrix(otherRows, otherCols);
    }

    public static Matrix multiplyMatrix(Matrix first, Matrix other) {
        int rows= first.getRows();
        int cols= first.getCols();
        int otherRows= other.getRows();
        int otherCols= other.getCols();
        if (cols == otherRows) {
            Matrix result= new Matrix(rows, otherCols);
            for (int i= 0; i < rows; i++ ) {
                for (int j= 0; j < otherCols; j++ ) {
                    result.setElement(i, j, dotProduct(first.getRow(i),
                        other.getCol(j)));
                }
            }
            return result;
        }
        return new Matrix(otherRows, otherCols);
    }

    public static double dotProduct(double[] vec1, double[] vec2) {
        if (vec1.length == vec2.length) {
            double sum= 0;
            for (int i= 0; i < vec1.length; i++ ) {
                sum+= vec1[i] * vec2[i];
            }
            return sum;
        }
        return 0;
    }

    public static boolean isEqual(Matrix first, Matrix second) {
        int fRows= first.getRows();
        int fCols= first.getCols();
        int sRows= second.getRows();
        int sCols= second.getCols();
        if (fRows == sRows && fCols == sCols) {
            for (int i= 0; i < fRows; i++ ) {
                for (int j= 0; j < fCols; j++ ) {
                    if (first.getElement(i, j) != second.getElement(i, j)) { return false; }
                }
            }
            return true;
        }
        return false;
    }

}
