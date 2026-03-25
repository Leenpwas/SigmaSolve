package com.sigmasolve.app.calculator;

public class ComplexNumber {
    public final double real;
    public final double imag;

    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(real + other.real, imag + other.imag);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(real - other.real, imag - other.imag);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        return new ComplexNumber(
            real * other.real - imag * other.imag,
            real * other.imag + imag * other.real
        );
    }

    public ComplexNumber divide(ComplexNumber other) {
        double denom = other.real * other.real + other.imag * other.imag;
        return new ComplexNumber(
            (real * other.real + imag * other.imag) / denom,
            (imag * other.real - real * other.imag) / denom
        );
    }

    public double magnitude() {
        return Math.sqrt(real * real + imag * imag);
    }

    public double phase() {
        return Math.atan2(imag, real);
    }

    @Override
    public String toString() {
        if (imag == 0) return String.format("%.4f", real).replaceAll("0+$", "").replaceAll("\\.$", "");
        if (real == 0) return String.format("%.4fi", imag).replaceAll("0+$", "").replaceAll("\\.$", "");
        return String.format("%.4f %s %.4fi", real, (imag < 0 ? "-" : "+"), Math.abs(imag))
            .replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
