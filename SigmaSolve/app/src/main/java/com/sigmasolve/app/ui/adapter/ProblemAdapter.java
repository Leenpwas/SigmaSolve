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
import com.sigmasolve.app.data.entity.Problem;

/**
 * ProblemAdapter — RecyclerView adapter for Problem items.
 */
public class ProblemAdapter extends ListAdapter<Problem, ProblemAdapter.ProblemViewHolder> {

    public interface OnProblemClickListener {
        void onProblemClick(Problem problem);
    }

    private final OnProblemClickListener listener;

    public ProblemAdapter(OnProblemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Problem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Problem>() {
                @Override
                public boolean areItemsTheSame(@NonNull Problem o, @NonNull Problem n) {
                    return o.id == n.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull Problem o, @NonNull Problem n) {
                    return o.title.equals(n.title);
                }
            };

    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_problem, parent, false);
        return new ProblemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ProblemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvSubject;
        private final TextView tvDifficulty;
        private final TextView tvSolveFor;

        ProblemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_problem_title);
            tvSubject = itemView.findViewById(R.id.tv_problem_subject);
            tvDifficulty = itemView.findViewById(R.id.tv_problem_difficulty);
            tvSolveFor = itemView.findViewById(R.id.tv_solve_for);
        }

        void bind(Problem problem, OnProblemClickListener listener) {
            tvTitle.setText(problem.title);
            tvSubject.setText(problem.subject);
            tvDifficulty.setText(problem.difficulty);
            tvSolveFor.setText("Solve for: " + problem.solveVariable);
            itemView.setOnClickListener(v -> listener.onProblemClick(problem));
        }
    }
}
