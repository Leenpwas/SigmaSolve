package com.sigmasolve.app.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * ScientificCalculator — parses and evaluates engineering math expressions.
 * Enhanced with Equation Solver and support for Advanced Engineering types.
 */
public class ScientificCalculator {

    private static final double PI = Math.PI;
    private static final double E  = Math.E;

    private static final Map<String, Integer> PRECEDENCE = new HashMap<String, Integer>() {{
        put("+", 1); put("-", 1);
        put("*", 2); put("/", 2); put("%", 2);
        put("^", 3);
        put("u-", 4);
    }};

    public String evaluate(String expression, boolean degreeMode) {
        if (expression.contains("=")) {
            return solveEquation(expression, degreeMode);
        }
        try {
            List<String> rpn = parseToRPN(expression);
            double result = evaluateRPN(rpn, new HashMap<>(), degreeMode);
            return formatResult(result);
        } catch (Exception e) {
            return "Error";
        }
    }

    public List<String> parseToRPN(String expression) {
        expression = preprocessExpression(expression);
        List<String> tokens = tokenize(expression);
        return shuntingYard(tokens);
    }

    public double evaluateRPN(List<String> rpn, Map<String, Double> variables, boolean degreeMode) {
        Stack<Double> stack = new Stack<>();
        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (variables.containsKey(token)) {
                stack.push(variables.get(token));
            } else if (isOperator(token)) {
                if (token.equals("u-")) {
                    if (stack.isEmpty()) return Double.NaN;
                    stack.push(-stack.pop());
                } else {
                    if (stack.size() < 2) return Double.NaN;
                    double b = stack.pop();
                    double a = stack.pop();
                    stack.push(applyOperator(token, a, b));
                }
            } else if (token.equals("!")) {
                if (stack.isEmpty()) return Double.NaN;
                double n = stack.pop();
                stack.push((double) factorial((int) n));
            } else if (isFunction(token)) {
                if (isTwoArgFunction(token)) {
                    if (stack.size() < 2) return Double.NaN;
                    double b = stack.pop();
                    double a = stack.pop();
                    stack.push(applyTwoArgFunction(token, a, b));
                } else {
                    if (stack.isEmpty()) return Double.NaN;
                    double arg = stack.pop();
                    stack.push(applyFunction(token, arg, degreeMode));
                }
            } else if (token.equals("π")) {
                stack.push(PI);
            } else if (token.equals("e")) {
                stack.push(E);
            }
        }
        return stack.isEmpty() ? 0 : stack.pop();
    }

    private String solveEquation(String equation, boolean degreeMode) {
        String[] parts = equation.split("=");
        if (parts.length != 2) return "Error: Invalid Equation";
        
        String left = parts[0];
        String right = parts[1];
        String combined = "(" + left + ") - (" + right + ")";
        
        List<String> rpn = parseToRPN(combined);
        
        // Simple Bisection Method to find root between -100 and 100
        double a = -100, b = 100;
        double fa = evaluateAt(rpn, a, degreeMode);
        double fb = evaluateAt(rpn, b, degreeMode);
        
        if (fa * fb > 0) {
            // Try Newton-Raphson from 0 if bisection doesn't find opposite signs
            double x = 0;
            for (int i = 0; i < 20; i++) {
                double fx = evaluateAt(rpn, x, degreeMode);
                if (Math.abs(fx) < 1e-7) return "x ≈ " + formatResult(x);
                double dfx = (evaluateAt(rpn, x + 1e-5, degreeMode) - fx) / 1e-5;
                if (dfx == 0) break;
                x = x - fx / dfx;
            }
            return "No solution found";
        }

        for (int i = 0; i < 100; i++) {
            double c = (a + b) / 2;
            double fc = evaluateAt(rpn, c, degreeMode);
            if (Math.abs(fc) < 1e-7) return "x ≈ " + formatResult(c);
            if (fa * fc < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }
        return "x ≈ " + formatResult((a + b) / 2);
    }

    private double evaluateAt(List<String> rpn, double x, boolean degreeMode) {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", x);
        return evaluateRPN(rpn, vars, degreeMode);
    }

    private String preprocessExpression(String expr) {
        expr = expr.trim().toLowerCase();
        expr = expr.replaceAll("(\\d)\\(", "$1*(");
        expr = expr.replaceAll("\\)(\\d)", ")*$1");
        expr = expr.replaceAll("\\)\\(", ")*(");
        expr = expr.replaceAll("(\\d)([a-zπ])", "$1*$2");
        return expr;
    }

    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i++));
                }
                tokens.add(num.toString());
                continue;
            }
            if (Character.isLetter(c) || c == 'π') {
                StringBuilder name = new StringBuilder();
                while (i < expr.length() && (Character.isLetterOrDigit(expr.charAt(i)) || expr.charAt(i) == 'π')) {
                    name.append(expr.charAt(i++));
                }
                tokens.add(name.toString());
                continue;
            }
            if (c == '!') { tokens.add("!"); i++; continue; }
            if (c == ',') { tokens.add(","); i++; continue; }
            tokens.add(String.valueOf(c));
            i++;
        }
        return tokens;
    }

    private List<String> shuntingYard(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> opStack = new Stack<>();
        String prevToken = null;

        for (String token : tokens) {
            if (isNumber(token) || token.equals("x") || token.equals("y") || token.equals("π") || token.equals("e")) {
                output.add(token);
            } else if (isFunction(token)) {
                opStack.push(token);
            } else if (token.equals(",")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) output.add(opStack.pop());
            } else if (isOperator(token)) {
                String actualToken = token;
                if (token.equals("-") && (prevToken == null || isOperator(prevToken) || prevToken.equals("("))) {
                    actualToken = "u-";
                }
                while (!opStack.isEmpty() && isOperator(opStack.peek()) &&
                       getPrecedence(opStack.peek()) >= getPrecedence(actualToken) &&
                       !opStack.peek().equals("^")) {
                    output.add(opStack.pop());
                }
                opStack.push(actualToken);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) output.add(opStack.pop());
                if (!opStack.isEmpty()) opStack.pop();
                if (!opStack.isEmpty() && isFunction(opStack.peek())) output.add(opStack.pop());
            } else if (token.equals("!")) {
                output.add("!");
            }
            prevToken = token;
        }
        while (!opStack.isEmpty()) output.add(opStack.pop());
        return output;
    }

    private double applyOperator(String op, double a, double b) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b == 0 ? Double.NaN : a / b;
            case "^": return Math.pow(a, b);
            case "%": return a % b;
            default: return 0;
        }
    }

    private double applyFunction(String fn, double arg, boolean degreeMode) {
        double radArg = degreeMode ? Math.toRadians(arg) : arg;
        switch (fn) {
            case "sin": return Math.sin(radArg);
            case "cos": return Math.cos(radArg);
            case "tan": return Math.tan(radArg);
            case "asin": return degreeMode ? Math.toDegrees(Math.asin(arg)) : Math.asin(arg);
            case "acos": return degreeMode ? Math.toDegrees(Math.acos(arg)) : Math.acos(arg);
            case "atan": return degreeMode ? Math.toDegrees(Math.atan(arg)) : Math.atan(arg);
            case "log": return Math.log10(arg);
            case "ln": return Math.log(arg);
            case "sqrt": return Math.sqrt(arg);
            case "abs": return Math.abs(arg);
            case "exp": return Math.exp(arg);
            default: return arg;
        }
    }

    private double applyTwoArgFunction(String fn, double a, double b) {
        switch (fn) {
            case "min": return Math.min(a, b);
            case "max": return Math.max(a, b);
            case "pow": return Math.pow(a, b);
            default: return 0;
        }
    }

    private boolean isNumber(String token) {
        try { Double.parseDouble(token); return true; } catch (Exception e) { return false; }
    }

    private boolean isOperator(String token) { return PRECEDENCE.containsKey(token); }

    private boolean isFunction(String token) {
        return isTwoArgFunction(token) || "sin,cos,tan,asin,acos,atan,log,ln,sqrt,abs,exp".contains(token);
    }

    private boolean isTwoArgFunction(String token) { return "min,max,pow".contains(token); }

    private int getPrecedence(String op) { return PRECEDENCE.getOrDefault(op, 0); }

    private long factorial(int n) {
        if (n < 0 || n > 20) return 0;
        long res = 1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

    private String formatResult(double value) {
        if (Double.isNaN(value)) return "Error";
        if (Double.isInfinite(value)) return "Infinity";
        if (value == (long) value) return String.valueOf((long) value);
        return String.format("%.6f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
