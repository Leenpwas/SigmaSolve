package com.sigmasolve.app.solver;

import java.util.List;

/**
 * SolverResult — holds the output of a guided problem-solving session.
 * Contains the list of step descriptions and the final computed answer.
 */
public class SolverResult {

    private final boolean success;
    private final String solveVariable;     // e.g., "V"
    private final String answer;            // Numeric result string
    private final String unit;              // Unit of the answer
    private final List<String> steps;       // Rendered step-by-step explanation
    private final String errorMessage;

    // ── Constructor for success ───────────────────────────────────────────
    public SolverResult(String solveVariable, String answer, String unit, List<String> steps) {
        this.success = true;
        this.solveVariable = solveVariable;
        this.answer = answer;
        this.unit = unit;
        this.steps = steps;
        this.errorMessage = "";
    }

    // ── Constructor for error ─────────────────────────────────────────────
    public SolverResult(String errorMessage) {
        this.success = false;
        this.solveVariable = "";
        this.answer = "";
        this.unit = "";
        this.steps = null;
        this.errorMessage = errorMessage;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public boolean isSuccess() { return success; }
    public String getSolveVariable() { return solveVariable; }
    public String getAnswer() { return answer; }
    public String getUnit() { return unit; }
    public List<String> getSteps() { return steps; }
    public String getErrorMessage() { return errorMessage; }

    public String getFormattedAnswer() {
        return solveVariable + " = " + answer + (unit.isEmpty() ? "" : " " + unit);
    }
}
