package com.sigmasolve.app.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Formula entity — represents an engineering topic/concept.
 * Organized by Year -> Branch -> Subject -> Category.
 */
@Entity(tableName = "formulas")
public class Formula {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "year")
    public String year;                // e.g., "Year 1", "Year 2"

    @ColumnInfo(name = "branch")
    public String branch;              // e.g., "Common", "EE", "ECE", "CSE", "ME"

    @ColumnInfo(name = "subject")
    public String subject;             // e.g., "Engineering Mathematics I", "Circuit Theory"

    @ColumnInfo(name = "category")
    public String category;            // e.g., "DC Circuits", "Calculus"

    @ColumnInfo(name = "name")
    public String name;                // Topic Name: e.g., "Ohm's Law"

    @ColumnInfo(name = "equation")
    public String equation;            // Main mathematical expression

    @ColumnInfo(name = "variables")
    public String variables;           // Explanation of symbols

    @ColumnInfo(name = "explanation")
    public String explanation;         // Theoretical concept

    @ColumnInfo(name = "derivation")
    public String derivation;

    @ColumnInfo(name = "example")
    public String example;

    @ColumnInfo(name = "video_url_neso")
    public String videoUrlNeso;        // Link to Neso Academy video

    @ColumnInfo(name = "video_url_gate")
    public String videoUrlGate;        // Link to Gate Smashers video

    @ColumnInfo(name = "is_bookmarked")
    public boolean isBookmarked;

    @ColumnInfo(name = "last_viewed")
    public long lastViewed;            // For "Recently Studied" tracking

    public Formula(String year, String branch, String subject, String category, String name, 
                   String equation, String variables, String explanation, 
                   String derivation, String example, String videoUrlNeso, String videoUrlGate) {
        this.year = year;
        this.branch = branch;
        this.subject = subject;
        this.category = category;
        this.name = name;
        this.equation = equation;
        this.variables = variables;
        this.explanation = explanation;
        this.derivation = derivation;
        this.example = example;
        this.videoUrlNeso = videoUrlNeso;
        this.videoUrlGate = videoUrlGate;
        this.isBookmarked = false;
        this.lastViewed = 0;
    }
}
