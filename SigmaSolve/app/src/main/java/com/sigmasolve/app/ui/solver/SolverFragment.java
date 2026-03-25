package com.sigmasolve.app.ui.solver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sigmasolve.app.R;
import com.sigmasolve.app.databinding.FragmentSolverBinding;
import com.sigmasolve.app.ui.adapter.ProblemAdapter;
import com.sigmasolve.app.viewmodel.SolverViewModel;

/**
 * SolverFragment — lists all available guided engineering problems.
 * User selects a problem to launch the step-by-step solver.
 */
public class SolverFragment extends Fragment {

    private FragmentSolverBinding binding;
    private SolverViewModel viewModel;
    private ProblemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSolverBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SolverViewModel.class);

        adapter = new ProblemAdapter(problem -> {
            Bundle args = new Bundle();
            args.putInt("problem_id", problem.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_solver_to_detail, args);
        });

        binding.recyclerProblems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProblems.setAdapter(adapter);

        viewModel.allProblems.observe(getViewLifecycleOwner(), problems -> {
            adapter.submitList(problems);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
