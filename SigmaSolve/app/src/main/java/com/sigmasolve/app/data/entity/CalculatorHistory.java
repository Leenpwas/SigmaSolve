package com.sigmasolve.app.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * CalculatorHistory entity — stores past calculator expressions and results.
 * Allows users to recall and reuse previous calculations.
 */
@Entity(tableName = "calculator_history")
public class CalculatorHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "expression")
    public String expression;          // e.g., "sin(30) + log(100)"

    @ColumnInfo(name = "result")
    public String result;              // e.g., "2.5"

    @ColumnInfo(name = "timestamp")
    public long timestamp;             // System.currentTimeMillis()

    @ColumnInfo(name = "mode")
    public String mode;                // "DEG" or "RAD"

    // --- Constructor ---
    public CalculatorHistory(String expression, String result, long timestamp, String mode) {
        this.expression = expression;
        this.result = result;
        this.timestamp = timestamp;
        this.mode = mode;
    }
}
