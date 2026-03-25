package com.sigmasolve.app.solver;

import com.sigmasolve.app.calculator.ScientificCalculator;
import com.sigmasolve.app.data.entity.Formula;
import com.sigmasolve.app.data.entity.Problem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SolverEngine — the core logic for guided step-by-step problem solving.
 *
 * Process:
 * 1. Parses problem's input variable definitions (JSON)
 * 2. Validates user inputs
 * 3. Builds and evaluates mathematical expression
 * 4. Renders each step as human-readable explanation
 */
public class SolverEngine {

    private final ScientificCalculator calculator = new ScientificCalculator();

    /**
     * Solve a problem with user-supplied values.
     *
     * @param problem      The Problem entity (contains formula, steps template, solve variable)
     * @param formula      The associated Formula entity
     * @param inputValues  Map of variable symbol → string value from user
     * @return             SolverResult with steps and answer, or error
     */
    public SolverResult solve(Problem problem, Formula formula, Map<String, String> inputValues) {
        try {
            // ── 1. Parse variable definitions ─────────────────────────────
            JSONArray varDefs = new JSONArray(problem.inputVariables);
            List<String> variableSymbols = new ArrayList<>();
            List<String> variableNames = new ArrayList<>();
            List<String> variableUnits = new ArrayList<>();

            for (int i = 0; i < varDefs.length(); i++) {
                JSONObject v = varDefs.getJSONObject(i);
                variableSymbols.add(v.getString("symbol"));
                variableNames.add(v.getString("name"));
                variableUnits.add(v.optString("unit", ""));
            }

            // ── 2. Validate all required inputs are present and numeric ───
            for (int i = 0; i < variableSymbols.size(); i++) {
                String sym = variableSymbols.get(i);
                String val = inputValues.get(sym);
                if (val == null || val.trim().isEmpty()) {
                    return new SolverResult("Missing value for: " + variableNames.get(i));
                }
                try {
                    Double.parseDouble(val.trim());
                } catch (NumberFormatException e) {
                    return new SolverResult("Invalid number for: " + variableNames.get(i) + " → \"" + val + "\"");
                }
            }

            // ── 3. Build computation expression ──────────────────────────
            // Substitute values into the formula equation to evaluate the solve variable
            String computeExpression = buildExpression(
                    problem.solveVariable, formula.equation,
                    variableSymbols, inputValues
            );

            String answerStr = calculator.evaluate(computeExpression, true);
            if (answerStr.startsWith("Error")) {
                return new SolverResult("Calculation failed: " + answerStr);
            }

            // ── 4. Parse step template and render each step ───────────────
            List<String> renderedSteps = renderSteps(
                    problem.stepsTemplate, formula, variableSymbols, variableNames,
                    variableUnits, inputValues, problem.solveVariable, answerStr
            );

            // Determine unit of answer from solve variable
            String answerUnit = getUnitForVariable(problem.solveVariable, formula.variables);

            return new SolverResult(problem.solveVariable, answerStr, answerUnit, renderedSteps);

        } catch (Exception e) {
            return new SolverResult("Solver error: " + e.getMessage());
        }
    }

    // ── EXPRESSION BUILDER ────────────────────────────────────────────────

    /**
     * Rearrange the formula equation to isolate the solve variable,
     * then substitute numeric values.
     *
     * This implementation handles simple linear isolation.
     * For V = I × R, solving for I → I = V / R
     * The formula equation is stored with the solve target already isolated
     * (since each Problem has its own stepsTemplate and solveVariable).
     *
     * We substitute all known variable symbols with their numeric values.
     */
    private String buildExpression(String solveVar, String equation,
                                    List<String> symbols, Map<String, String> values) {
        // Find the RHS of the equation (after '=')
        String rhs = equation;
        if (equation.contains("=")) {
            String[] parts = equation.split("=", 2);
            String lhs = parts[0].trim();
            rhs = parts[1].trim();
            // If LHS is not the solve variable, try the other side
            if (!lhs.equals(solveVar) && parts[0].trim().replace(" ", "").equals(solveVar)) {
                rhs = parts[1].trim();
            }
        }

        // Substitute variable symbols with numeric values in the RHS
        // Sort by length descending to avoid partial replacement issues
        symbols.sort((a, b) -> b.length() - a.length());
        for (String sym : symbols) {
            if (!sym.equals(solveVar) && values.containsKey(sym)) {
                rhs = rhs.replace(sym, "(" + values.get(sym) + ")");
            }
        }

        // Clean up multiplication signs (× → *)
        rhs = rhs.replace("×", "*").replace("÷", "/");

        return rhs;
    }

    // ── STEP RENDERER ─────────────────────────────────────────────────────

    private List<String> renderSteps(String stepsTemplate, Formula formula,
                                      List<String> symbols, List<String> names,
                                      List<String> units, Map<String, String> inputValues,
                                      String solveVar, String answer) throws Exception {

        List<String> steps = new ArrayList<>();

        // Parse template steps
        JSONArray template = new JSONArray(stepsTemplate);

        // Step 0: Show the formula
        steps.add("📐 Formula: " + formula.equation);

        // Add template steps
        for (int i = 0; i < template.length(); i++) {
            steps.add("Step " + (i + 1) + ": " + template.getString(i));
        }

        // Add "Given values" step
        StringBuilder given = new StringBuilder("📋 Given values:\n");
        for (int i = 0; i < symbols.size(); i++) {
            if (!symbols.get(i).equals(solveVar)) {
                given.append("  • ").append(names.get(i)).append(" (").append(symbols.get(i)).append(") = ")
                     .append(inputValues.get(symbols.get(i)))
                     .append(units.get(i).isEmpty() ? "" : " " + units.get(i))
                     .append("\n");
            }
        }
        steps.add(given.toString().trim());

        // Final answer step
        steps.add("✅ Result: " + solveVar + " = " + answer + " " +
                  getUnitForVariable(solveVar, formula.variables));

        return steps;
    }

    // ── UTILITY ───────────────────────────────────────────────────────────

    /** Extract unit for a variable symbol from the variables description string */
    private String getUnitForVariable(String symbol, String variablesDesc) {
        if (variablesDesc == null) return "";
        // Format expected: "V = Voltage (Volts), I = Current (A), R = Resistance (Ω)"
        String[] parts = variablesDesc.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith(symbol + " ") || part.startsWith(symbol + "=")) {
                // Extract content inside parentheses
                int start = part.indexOf('(');
                int end = part.indexOf(')');
                if (start >= 0 && end > start) {
                    String unit = part.substring(start + 1, end);
                    // Return just the unit abbreviation (last word usually)
                    String[] words = unit.split(" ");
                    return words[words.length - 1];
                }
            }
        }
        return "";
    }
}
