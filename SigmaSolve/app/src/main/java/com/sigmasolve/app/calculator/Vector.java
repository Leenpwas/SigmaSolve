package com.sigmasolve.app.calculator;

public class Vector {
    private final double[] elements;

    public Vector(double[] elements) {
        this.elements = elements;
    }

    public int dimension() {
        return elements.length;
    }

    public double get(int i) {
        return elements[i];
    }

    public Vector add(Vector other) {
        if (this.dimension() != other.dimension()) return null;
        double[] res = new double[dimension()];
        for (int i = 0; i < dimension(); i++) res[i] = elements[i] + other.elements[i];
        return new Vector(res);
    }

    public Vector subtract(Vector other) {
        if (this.dimension() != other.dimension()) return null;
        double[] res = new double[dimension()];
        for (int i = 0; i < dimension(); i++) res[i] = elements[i] - other.elements[i];
        return new Vector(res);
    }

    public double dotProduct(Vector other) {
        if (this.dimension() != other.dimension()) return Double.NaN;
        double res = 0;
        for (int i = 0; i < dimension(); i++) res += elements[i] * other.elements[i];
        return res;
    }

    public Vector crossProduct(Vector other) {
        if (this.dimension() != 3 || other.dimension() != 3) return null;
        return new Vector(new double[]{
            elements[1] * other.elements[2] - elements[2] * other.elements[1],
            elements[2] * other.elements[0] - elements[0] * other.elements[2],
            elements[0] * other.elements[1] - elements[1] * other.elements[0]
        });
    }

    public double magnitude() {
        double sum = 0;
        for (double d : elements) sum += d * d;
        return Math.sqrt(sum);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.length; i++) {
            sb.append(String.format("%.2f", elements[i]));
            if (i < elements.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
