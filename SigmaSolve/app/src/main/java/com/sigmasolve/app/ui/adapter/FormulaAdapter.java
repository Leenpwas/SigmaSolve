package com.sigmasolve.app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sigmasolve.app.R;
import com.sigmasolve.app.data.entity.Formula;

/**
 * FormulaAdapter — RecyclerView adapter for displaying Formula items.
 * Uses ListAdapter with DiffUtil for efficient updates.
 */
public class FormulaAdapter extends ListAdapter<Formula, FormulaAdapter.FormulaViewHolder> {

    public interface OnFormulaClickListener {
        void onFormulaClick(Formula formula);
    }

    private final OnFormulaClickListener listener;

    public FormulaAdapter(OnFormulaClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Formula> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Formula>() {
                @Override
                public boolean areItemsTheSame(@NonNull Formula oldItem, @NonNull Formula newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull Formula oldItem, @NonNull Formula newItem) {
                    return oldItem.name.equals(newItem.name) &&
                           oldItem.isBookmarked == newItem.isBookmarked &&
                           oldItem.subject.equals(newItem.subject) &&
                           oldItem.category.equals(newItem.category);
                }
            };

    @NonNull
    @Override
    public FormulaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_formula, parent, false);
        return new FormulaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormulaViewHolder holder, int position) {
        Formula formula = getItem(position);
        holder.bind(formula, listener);
    }

    // ── ViewHolder ────────────────────────────────────────────────────────

    static class FormulaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvEquation;
        private final TextView tvSubject;
        private final TextView tvBookmark;

        FormulaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_formula_name);
            tvEquation = itemView.findViewById(R.id.tv_formula_equation);
            tvSubject = itemView.findViewById(R.id.tv_formula_subject);
            tvBookmark = itemView.findViewById(R.id.tv_bookmark_indicator);
        }

        void bind(Formula formula, OnFormulaClickListener listener) {
            tvName.setText(formula.name);
            tvEquation.setText(formula.equation);
            // Show hierarchy in the subtitle
            tvSubject.setText(formula.subject + " › " + formula.category);
            tvBookmark.setVisibility(formula.isBookmarked ? View.VISIBLE : View.GONE);
            itemView.setOnClickListener(v -> listener.onFormulaClick(formula));
        }
    }
}
