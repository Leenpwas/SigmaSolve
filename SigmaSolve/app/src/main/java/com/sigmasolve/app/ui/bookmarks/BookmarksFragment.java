package com.sigmasolve.app.ui.bookmarks;

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
import com.sigmasolve.app.databinding.FragmentBookmarksBinding;
import com.sigmasolve.app.ui.adapter.FormulaAdapter;
import com.sigmasolve.app.viewmodel.FormulaViewModel;

/**
 * BookmarksFragment — shows all bookmarked formulas.
 */
public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private FormulaViewModel viewModel;
    private FormulaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FormulaViewModel.class);

        adapter = new FormulaAdapter(formula -> {
            viewModel.markViewed(formula.id);
            Bundle args = new Bundle();
            args.putInt("formula_id", formula.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_bookmarks_to_formula_detail, args);
        });

        binding.recyclerBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerBookmarks.setAdapter(adapter);

        viewModel.bookmarkedFormulas.observe(getViewLifecycleOwner(), formulas -> {
            adapter.submitList(formulas);
            binding.tvEmptyBookmarks.setVisibility(
                    formulas == null || formulas.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
