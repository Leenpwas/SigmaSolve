package com.sigmasolve.app.calculator;

public class Matrix {
    private final double[][] data;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public void set(int r, int c, double val) {
        data[r][c] = val;
    }

    public Matrix add(Matrix other) {
        if (rows != other.rows || cols != other.cols) return null;
        Matrix res = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                res.data[i][j] = data[i][j] + other.data[i][j];
        return res;
    }

    public Matrix multiply(Matrix other) {
        if (cols != other.rows) return null;
        Matrix res = new Matrix(rows, other.cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < other.cols; j++)
                for (int k = 0; k < cols; k++)
                    res.data[i][j] += data[i][k] * other.data[k][j];
        return res;
    }

    public double determinant() {
        if (rows != cols) return Double.NaN;
        return calculateDeterminant(data);
    }

    private double calculateDeterminant(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        double det = 0;
        for (int i = 0; i < n; i++) {
            double[][] sub = new double[n - 1][n - 1];
            for (int j = 1; j < n; j++) {
                int colIdx = 0;
                for (int k = 0; k < n; k++) {
                    if (k == i) continue;
                    sub[j - 1][colIdx++] = matrix[j][k];
                }
            }
            det += Math.pow(-1, i) * matrix[0][i] * calculateDeterminant(sub);
        }
        return det;
    }

    public Matrix transpose() {
        Matrix res = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                res.data[j][i] = data[i][j];
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%.2f", data[i][j]));
                if (j < cols - 1) sb.append(", ");
            }
            sb.append("]");
            if (i < rows - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
