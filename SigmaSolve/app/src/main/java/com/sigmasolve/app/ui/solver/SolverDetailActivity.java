package com.sigmasolve.app.ui.solver;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigmasolve.app.R;
import com.sigmasolve.app.data.entity.Problem;
import com.sigmasolve.app.databinding.ActivitySolverDetailBinding;
import com.sigmasolve.app.solver.SolverResult;
import com.sigmasolve.app.viewmodel.SolverViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SolverDetailActivity — the guided problem-solving interface.
 *
 * Flow:
 * 1. Loads the problem and its formula
 * 2. Dynamically generates input fields for each variable
 * 3. On "Solve", passes values to SolverEngine
 * 4. Displays step-by-step result
 */
public class SolverDetailActivity extends AppCompatActivity {

    private ActivitySolverDetailBinding binding;
    private SolverViewModel viewModel;

    // Dynamically created input fields (symbol → EditText)
    private final Map<String, EditText> inputFields = new HashMap<>();
    private final List<String> variableOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySolverDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int problemId = getIntent().getIntExtra("problem_id", -1);
        viewModel = new ViewModelProvider(this).get(SolverViewModel.class);

        // Load the problem
        viewModel.loadProblem(problemId);

        // Observe loaded problem
        viewModel.getCurrentProblem().observe(this, this::onProblemLoaded);
        viewModel.getCurrentFormula().observe(this, formula -> {
            if (formula != null) {
                binding.tvFormula.setText("Formula: " + formula.equation);
            }
        });

        // Observe solver result
        viewModel.getSolverResult().observe(this, this::displayResult);
        viewModel.getSolverError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                binding.tvError.setText(error);
                binding.tvError.setVisibility(View.VISIBLE);
                binding.cardResult.setVisibility(View.GONE);
            } else {
                binding.tvError.setVisibility(View.GONE);
            }
        });

        // Solve button
        binding.btnSolve.setOnClickListener(v -> solveProblem());
    }

    private void onProblemLoaded(Problem problem) {
        if (problem == null) return;

        setTitle(problem.title);
        binding.tvProblemTitle.setText(problem.title);
        binding.tvProblemDesc.setText(problem.description);
        binding.tvSubject.setText(problem.subject + " • " + problem.difficulty);
        binding.tvSolveFor.setText("Solving for: " + problem.solveVariable);

        // Dynamically build input fields
        buildInputFields(problem);
    }

    private void buildInputFields(Problem problem) {
        binding.layoutInputs.removeAllViews();
        inputFields.clear();
        variableOrder.clear();

        try {
            JSONArray vars = new JSONArray(problem.inputVariables);
            for (int i = 0; i < vars.length(); i++) {
                JSONObject v = vars.getJSONObject(i);
                String symbol = v.getString("symbol");
                String name = v.getString("name");
                String unit = v.optString("unit", "");

                variableOrder.add(symbol);

                // Create a TextInputLayout + EditText for each variable
                TextInputLayout inputLayout = new TextInputLayout(this);
                inputLayout.setHint(name + " (" + symbol + ")"
                        + (unit.isEmpty() ? "" : " [" + unit + "]"));
                inputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 24);
                inputLayout.setLayoutParams(params);

                TextInputEditText editText = new TextInputEditText(inputLayout.getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL |
                        InputType.TYPE_NUMBER_FLAG_SIGNED);
                inputLayout.addView(editText);
                binding.layoutInputs.addView(inputLayout);

                inputFields.put(symbol, editText);
            }
        } catch (Exception e) {
            binding.tvError.setText("Failed to load problem variables.");
            binding.tvError.setVisibility(View.VISIBLE);
        }
    }

    private void solveProblem() {
        // Collect input values
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<String, EditText> entry : inputFields.entrySet()) {
            String val = entry.getValue().getText() != null ?
                    entry.getValue().getText().toString().trim() : "";
            values.put(entry.getKey(), val);
        }

        binding.tvError.setVisibility(View.GONE);
        viewModel.solve(values);
    }

    private void displayResult(SolverResult result) {
        if (result == null) return;

        binding.cardResult.setVisibility(View.VISIBLE);
        binding.tvAnswer.setText(result.getFormattedAnswer());

        // Display each step
        binding.layoutSteps.removeAllViews();
        if (result.getSteps() != null) {
            for (int i = 0; i < result.getSteps().size(); i++) {
                TextView stepView = new TextView(this);
                stepView.setText(result.getSteps().get(i));
                stepView.setTextSize(14f);
                stepView.setPadding(0, 8, 0, 8);
                stepView.setTextColor(getResources().getColor(
                        com.google.android.material.R.color.material_on_background_emphasis_medium,
                        getTheme()));
                binding.layoutSteps.addView(stepView);

                // Divider between steps (except last)
                if (i < result.getSteps().size() - 1) {
                    View divider = new View(this);
                    divider.setBackgroundColor(0xFFE0E0E0);
                    LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    dp.setMargins(0, 4, 0, 4);
                    divider.setLayoutParams(dp);
                    binding.layoutSteps.addView(divider);
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
