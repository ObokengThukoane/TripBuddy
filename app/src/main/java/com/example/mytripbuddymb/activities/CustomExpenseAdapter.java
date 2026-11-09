package com.example.mytripbuddymb.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytripbuddymb.R;

import java.util.List;
import java.util.Locale;

public class CustomExpenseAdapter extends RecyclerView.Adapter<CustomExpenseAdapter.ViewHolder> {
    private final List<myPlanTrip.CustomExpense> expenses;
    private final OnExpenseRemoveListener removeListener;

    public interface OnExpenseRemoveListener {
        void onExpenseRemoved(int position);
    }

    public CustomExpenseAdapter(List<myPlanTrip.CustomExpense> expenses, OnExpenseRemoveListener removeListener) {
        this.expenses = expenses;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        myPlanTrip.CustomExpense expense = expenses.get(position);
        holder.tvAmount.setText(String.format(Locale.getDefault(), "$%.2f", expense.getAmount()));

        holder.btnRemove.setOnClickListener(v -> {
            if (removeListener != null) removeListener.onExpenseRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount;
        Button btnRemove;

        ViewHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
