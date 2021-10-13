package com.company;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        double[][] matri1 = {{1,2,3}, {4,5,6}, {7,8,9}};
        double[][] matri2 = {{5,5,5}, {5,5,5}, {5,5,5}};

        Matrix matrix1 = new Matrix(3, 3);
        matrix1.matrix = matri1;

        Matrix matrix2 = new Matrix(3, 3);
        matrix2.matrix = matri2;

        matrix1.sum(matrix2).showMatrix();
        matrix1.multiplyByNumber(5).showMatrix();
        Matrix.multiply(matrix1, matrix2).showMatrix();

        //Задание:
        //Сделать подкласс Прямоугольная матрица
        //в конструктор передаётся размер. Заполнение рандомом
        //метод - определитель

        //Определитель можно посчитать ТОЛЬКО у квадратной матрицы, но в задании метод находиться в классе ПРЯМОУГОЛЬНОЙ матрицы, пришлось для теста работоспособности делать статические методы.
        System.out.println(RectangleMatrix.determinantOfMatrix(matrix1.matrix, 3, 3));
    }
}

class Matrix {
    public double[][] matrix;
    protected int m, n;

    public static Matrix multiply(Matrix firstMatrix, Matrix secondMatrix) {
        if (firstMatrix.m != secondMatrix.n) throw new IllegalArgumentException("Чтобы умножить матрицу нужно, чтобы число столбцов матрицы равнялось числу строк матрицы.");

        Matrix output = new Matrix(firstMatrix.m, secondMatrix.n);

        for (int i = 0; i < firstMatrix.m; i++) {
            for (int j = 0; j < secondMatrix.n; j++) {
                for (int k = 0; k < secondMatrix.m; k++) {
                    output.matrix[i][j] += firstMatrix.matrix[i][k] * secondMatrix.matrix[k][j];
                }
            }
        }

        return output;
    }

    Matrix (int m, int n) {
        this.matrix = new double[m][n];
        this.m = m;
        this.n = n;
    }

    public Matrix sum(Matrix secondMatrix) {
        if (!isSizeSame(secondMatrix)) throw new ArithmeticException("Складывать можно только матрицы одинакового размера.");

        Matrix output = new Matrix(m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                output.matrix[i][j] = matrix[i][j] + secondMatrix.matrix[i][j];
            }
        }

        return output;
    }

    public Matrix multiplyByNumber(int number) {
        Matrix output = new Matrix(m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                output.matrix[i][j] = matrix[i][j] * number;
            }
        }

        return output;
    }

    public void fillWithRandom() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (int) (Math.random() * 42);
            }
        }
    }

    public void showMatrix() {
        for (int i = 0; i < this.m; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }

        System.out.println();
    }

    protected boolean isSizeSame(Matrix secondMatrix) {
        return m == secondMatrix.m && n == secondMatrix.n;
    }
}

class RectangleMatrix extends Matrix {
    RectangleMatrix(int m, int n) {
        super(m, n);

        if (m == n) throw new ArithmeticException("Прямоугольная матрица — это матрица, у которой число строк не равно числу столбцов.");
    }

    //Взял готовое open source решение :) https://www.geeksforgeeks.org/determinant-of-a-matrix/
    public static double determinantOfMatrix(double matrix[][], int n, int N) {
        int D = 0;

        if (n == 1)
            return matrix[0][0];

        double temp[][] = new double[N][N];

        int sign = 1;

        for (int f = 0; f < n; f++) {
            getCofactor(matrix, temp, 0, f, n);
            D += sign * matrix[0][f] * determinantOfMatrix(temp, n - 1, N);

            sign = -sign;
        }

        return D;
    }

    public static void getCofactor(double matrix[][], double temp[][], int p, int q, int n) {
        int i = 0, j = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];

                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }

    }
}