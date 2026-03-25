package com.sigmasolve.app.ui.formula;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.sigmasolve.app.R;
import com.sigmasolve.app.data.entity.Formula;
import com.sigmasolve.app.databinding.ActivityFormulaDetailBinding;
import com.sigmasolve.app.viewmodel.FormulaViewModel;

/**
 * FormulaDetailActivity — displays full details for a selected formula.
 * Shows: equation, variables, explanation, derivation, example.
 * Also includes automatic YouTube search for Neso Academy and Gate Smashers.
 */
public class FormulaDetailActivity extends AppCompatActivity {

    private ActivityFormulaDetailBinding binding;
    private FormulaViewModel viewModel;
    private Formula currentFormula;
    private int formulaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormulaDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        formulaId = getIntent().getIntExtra("formula_id", -1);
        viewModel = new ViewModelProvider(this).get(FormulaViewModel.class);

        observeFormula();
        setupClickListeners();

        // Mark as viewed in history
        if (formulaId != -1) {
            viewModel.markViewed(formulaId);
        }
    }

    private void observeFormula() {
        viewModel.getFormulaById(formulaId).observe(this, formula -> {
            if (formula != null) {
                displayFormula(formula);
            }
        });
    }

    private void displayFormula(Formula formula) {
        this.currentFormula = formula;
        setTitle(formula.name);

        binding.tvFormulaName.setText(formula.name);
        binding.tvSubject.setText(formula.subject + " › " + formula.category);
        binding.tvEquation.setText(formula.equation);
        binding.tvVariables.setText(formula.variables);
        binding.tvExplanation.setText(formula.explanation);

        // Show derivation if available
        if (formula.derivation != null && !formula.derivation.isEmpty()) {
            binding.tvDerivation.setText(formula.derivation);
            binding.cardDerivation.setVisibility(View.VISIBLE);
        } else {
            binding.cardDerivation.setVisibility(View.GONE);
        }

        // Show example if available
        if (formula.example != null && !formula.example.isEmpty()) {
            binding.tvExample.setText(formula.example);
            binding.cardExample.setVisibility(View.VISIBLE);
        } else {
            binding.cardExample.setVisibility(View.GONE);
        }

        // Update bookmark icon in the menu
        invalidateOptionsMenu();
    }

    private void setupClickListeners() {
        // Toggle explanation section
        binding.btnExplainConcept.setOnClickListener(v -> {
            boolean isVisible = binding.cardConceptExplanation.getVisibility() == View.VISIBLE;
            binding.cardConceptExplanation.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            binding.btnExplainConcept.setText(isVisible ? "Explain Concept" : "Hide Explanation");
        });

        // Video Concept Listeners
        binding.btnNesoAcademy.setOnClickListener(v -> {
            if (currentFormula != null) {
                if (currentFormula.videoUrlNeso != null && !currentFormula.videoUrlNeso.isEmpty()) {
                    openVideo(currentFormula.videoUrlNeso);
                } else {
                    // Automatically search Neso Academy for this topic
                    searchYouTube("Neso Academy " + currentFormula.name + " " + currentFormula.subject);
                }
            }
        });

        binding.btnGateSmashers.setOnClickListener(v -> {
            if (currentFormula != null) {
                if (currentFormula.videoUrlGate != null && !currentFormula.videoUrlGate.isEmpty()) {
                    openVideo(currentFormula.videoUrlGate);
                } else {
                    // Automatically search Gate Smashers for this topic
                    searchYouTube("Gate Smashers " + currentFormula.name + " " + currentFormula.subject);
                }
            }
        });
    }

    /**
     * Opens a specific YouTube video URL.
     */
    private void openVideo(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found to open video", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Performs an automatic YouTube search for the given query.
     * Prefers the YouTube app, falls back to the browser.
     */
    private void searchYouTube(String query) {
        try {
            // Attempt to use the YouTube-specific Search Intent
            Intent intent = new Intent(Intent.ACTION_SEARCH);
            intent.setPackage("com.google.android.youtube");
            intent.putExtra("query", query);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Fallback to browser search if YouTube app is missing
            Uri uri = Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode(query));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.formula_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentFormula != null) {
            MenuItem bookmarkItem = menu.findItem(R.id.action_bookmark);
            if (bookmarkItem != null) {
                bookmarkItem.setIcon(currentFormula.isBookmarked ?
                        R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_bookmark && currentFormula != null) {
            viewModel.toggleBookmark(currentFormula);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
