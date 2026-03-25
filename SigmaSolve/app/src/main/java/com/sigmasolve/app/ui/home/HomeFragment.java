package com.sigmasolve.app.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.sigmasolve.app.R;
import com.sigmasolve.app.databinding.FragmentHomeBinding;
import com.sigmasolve.app.viewmodel.FormulaViewModel;

/**
 * HomeFragment — landing screen featuring YouTube engineering lessons and quick actions.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FormulaViewModel formulaViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formulaViewModel = new ViewModelProvider(requireActivity()).get(FormulaViewModel.class);

        setupClickListeners();
        setupVideoCards();
    }

    private void setupVideoCards() {
        // Link to real engineering educational videos
        binding.cardVideo1.setOnClickListener(v -> 
            openYouTube("https://www.youtube.com/watch?v=fNk_zzaMoBA")); // 3Blue1Brown Linear Algebra
        
        binding.cardVideo2.setOnClickListener(v -> 
            openYouTube("https://www.youtube.com/watch?v=WUvTyaaN26A")); // 3Blue1Brown Calculus
    }

    private void openYouTube(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void setupClickListeners() {
        binding.btnSolveProblem.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_solver));

        binding.btnBrowseFormulas.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_formula));

        binding.btnQuickCalc.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_calculator));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
