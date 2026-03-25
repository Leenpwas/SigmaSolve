package com.sigmasolve.app.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Problem entity — represents a guided engineering problem with step-by-step solving workflow.
 * Each problem links to a formula and contains input variable definitions.
 */
@Entity(tableName = "problems")
public class Problem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;               // e.g., "Find Current (Ohm's Law)"

    @ColumnInfo(name = "description")
    public String description;         // Problem statement

    @ColumnInfo(name = "subject")
    public String subject;             // Subject area

    @ColumnInfo(name = "formula_id")
    public int formulaId;              // FK to Formula

    @ColumnInfo(name = "input_variables")
    public String inputVariables;      // JSON: [{"symbol":"V","name":"Voltage","unit":"V"}, ...]

    @ColumnInfo(name = "solve_variable")
    public String solveVariable;       // The variable being solved, e.g., "I"

    @ColumnInfo(name = "steps_template")
    public String stepsTemplate;       // JSON array of step descriptions

    @ColumnInfo(name = "difficulty")
    public String difficulty;          // "Easy", "Medium", "Hard"

    // --- Constructor ---
    public Problem(String title, String description, String subject,
                   int formulaId, String inputVariables, String solveVariable,
                   String stepsTemplate, String difficulty) {
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.formulaId = formulaId;
        this.inputVariables = inputVariables;
        this.solveVariable = solveVariable;
        this.stepsTemplate = stepsTemplate;
        this.difficulty = difficulty;
    }
}
