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
import com.sigmasolve.app.data.entity.CalculatorHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * HistoryAdapter — shows calculator history entries with reuse on click.
 */
public class HistoryAdapter extends ListAdapter<CalculatorHistory, HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryClickListener {
        void onHistoryClick(CalculatorHistory item);
    }

    private final OnHistoryClickListener listener;
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public HistoryAdapter(OnHistoryClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CalculatorHistory> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CalculatorHistory>() {
                @Override
                public boolean areItemsTheSame(@NonNull CalculatorHistory o, @NonNull CalculatorHistory n) {
                    return o.id == n.id;
                }
                @Override
                public boolean areContentsTheSame(@NonNull CalculatorHistory o, @NonNull CalculatorHistory n) {
                    return o.expression.equals(n.expression) && o.result.equals(n.result);
                }
            };

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvExpression;
        private final TextView tvResult;
        private final TextView tvTime;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpression = itemView.findViewById(R.id.tv_history_expression);
            tvResult = itemView.findViewById(R.id.tv_history_result);
            tvTime = itemView.findViewById(R.id.tv_history_time);
        }

        void bind(CalculatorHistory item, OnHistoryClickListener listener) {
            tvExpression.setText(item.expression);
            tvResult.setText("= " + item.result);
            tvTime.setText(TIME_FORMAT.format(new Date(item.timestamp)));
            itemView.setOnClickListener(v -> listener.onHistoryClick(item));
        }
    }
}
