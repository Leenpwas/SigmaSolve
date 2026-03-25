package com.sigmasolve.app.ui.formula;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.sigmasolve.app.R;
import com.sigmasolve.app.databinding.FragmentFormulaLibraryBinding;
import com.sigmasolve.app.ui.adapter.FormulaAdapter;
import com.sigmasolve.app.viewmodel.FormulaViewModel;

/**
 * FormulaLibraryFragment — searchable, filterable formula library.
 * Supports subject filter chips and real-time search.
 */
public class FormulaLibraryFragment extends Fragment {

    private FragmentFormulaLibraryBinding binding;
    private FormulaViewModel viewModel;
    private FormulaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFormulaLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FormulaViewModel.class);

        setupRecyclerView();
        setupSearch();
        setupSubjectChips();
    }

    private void setupRecyclerView() {
        adapter = new FormulaAdapter(formula -> {
            viewModel.markViewed(formula.id);
            Bundle args = new Bundle();
            args.putInt("formula_id", formula.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_formula_to_detail, args);
        });

        binding.recyclerFormulas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFormulas.setAdapter(adapter);

        viewModel.formulas.observe(getViewLifecycleOwner(), formulas -> {
            adapter.submitList(formulas);
            binding.tvFormulaCount.setText(
                    formulas != null ? formulas.size() + " formulas" : "0 formulas");
        });
    }

    private void setupSearch() {
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSubjectChips() {
        // Add "All" chip first
        addSubjectChip("All", true);

        viewModel.subjects.observe(getViewLifecycleOwner(), subjects -> {
            // Remove old subject chips (keep "All")
            if (binding.chipGroupSubjects.getChildCount() > 1) {
                binding.chipGroupSubjects.removeViews(1, binding.chipGroupSubjects.getChildCount() - 1);
            }
            if (subjects != null) {
                for (String subject : subjects) {
                    addSubjectChip(subject, false);
                }
            }
        });
    }

    private void addSubjectChip(String subject, boolean checked) {
        Chip chip = new Chip(requireContext());
        chip.setText(subject);
        chip.setCheckable(true);
        chip.setChecked(checked);
        chip.setOnClickListener(v -> {
            if (subject.equals("All")) {
                viewModel.setSubjectFilter("");
            } else {
                viewModel.setSubjectFilter(subject);
            }
        });
        binding.chipGroupSubjects.addView(chip);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
