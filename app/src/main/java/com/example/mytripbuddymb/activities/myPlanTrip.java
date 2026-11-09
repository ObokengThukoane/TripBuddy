package com.example.mytripbuddymb.activities;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytripbuddymb.MainActivity;
import com.example.mytripbuddymb.R;
import com.example.mytripbuddymb.database.MemoryDatabaseHelper;
import com.example.mytripbuddymb.utils.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class myPlanTrip extends AppCompatActivity {
//Date UI components
    private MaterialButton btnStartDate, btnEndDate, btnAddExpense;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private Date startDate, endDate;


    //Activity UI compnents
    private RecyclerView rvPredefinedActivities;
    private List<ActivityOption> predefinedActivities;
    private PredefinedActivityAdapter predefinedActivityAdapter;
    private NumberFormat currencyFormatter;

    //ExpenseAddbutton
    private TextInputLayout tilExpenseAmount;
    private TextInputEditText  etExpenseAmount ;
    private RecyclerView  rvCustomExpenses;
    private List<CustomExpense> customExpenses;
    private CustomExpenseAdapter customExpenseAdapter;

    private TextInputLayout tilExpenseName;
    private TextInputEditText  etExpenseName ;









    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_trip);

// Initialize data
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());


        predefinedActivities = getDefaultActivities();
        currencyFormatter = NumberFormat.getCurrencyInstance();

        predefinedActivityAdapter = new PredefinedActivityAdapter(predefinedActivities, this::updateTotalBudget);
            setupActivitiesLists();




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setOnClickListener(v -> showDatePicker(true));

        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(v -> showDatePicker(false));

        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v -> onAddExpenseClicked());

        tilExpenseAmount = findViewById(R.id.tilExpenseAmount);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);

        tilExpenseName = findViewById(R.id.tilExpenseName);
        etExpenseName = findViewById(R.id.etExpenseName);


        Button Back  = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myPlanTrip.this , MainActivity.class);
                startActivity(intent);
            }

        });

            customExpenses = new ArrayList<>();
            customExpenseAdapter = new CustomExpenseAdapter(customExpenses, this::onExpenseRemoved);

            rvCustomExpenses = findViewById(R.id.rvExpenses);
            rvCustomExpenses.setLayoutManager(new LinearLayoutManager(this));
        customExpenseAdapter = new CustomExpenseAdapter(customExpenses, this::onExpenseRemoved);
            rvCustomExpenses.setAdapter(customExpenseAdapter);


        preloadTripIfAny();
        // Save Trip button
        com.google.android.material.button.MaterialButton btnSaveTrip = findViewById(R.id.btnSaveTrip);
        btnSaveTrip.setOnClickListener(v -> saveTrip());



        }
    private void setupActivitiesLists() {
        // Predefined activities

        rvPredefinedActivities = findViewById(R.id.rvPredefinedActivities);
        rvPredefinedActivities.setLayoutManager(new LinearLayoutManager(this));
        rvPredefinedActivities.setAdapter(predefinedActivityAdapter);
    }


    private List<ActivityOption> getDefaultActivities() {
        List<ActivityOption> list = new ArrayList<>();
        list.add(new ActivityOption("Sightseeing", 50.0));
        list.add(new ActivityOption("Hiking", 30.0));
        list.add(new ActivityOption("Dining", 40.0));
        list.add(new ActivityOption("Museum Tour", 25.0));
        list.add(new ActivityOption("Boat Ride", 35.0));
        return list;
    }

    private static class ActivityOption {
        final String name;
        final double cost;
        final AtomicBoolean isSelected = new AtomicBoolean(false);

        ActivityOption(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }
    }

    private class PredefinedActivityAdapter extends RecyclerView.Adapter<PredefinedActivityAdapter.ViewHolder> {
        private final List<ActivityOption> items;
        private final Runnable selectionChangedCallback;

        PredefinedActivityAdapter(List<ActivityOption> items, Runnable selectionChangedCallback) {
            this.items = items;
            this.selectionChangedCallback = selectionChangedCallback;


        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());
            android.view.View view = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ActivityOption option = items.get(position);
            android.widget.CheckedTextView ctv = (android.widget.CheckedTextView) holder.itemView;
            ctv.setText(option.name + " (" + currencyFormatter.format(option.cost) + ")");
            ctv.setChecked(option.isSelected.get());
            ctv.setOnClickListener(v -> {
                option.isSelected.set(!option.isSelected.get());
                ctv.setChecked(option.isSelected.get());
                if (selectionChangedCallback != null) selectionChangedCallback.run();



            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(android.view.View itemView) { super(itemView); }
        }
    }


    private void showDatePicker(boolean isStartDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (isStartDate) {
                        startDate = calendar.getTime();
                        btnStartDate.setText(dateFormat.format(startDate));
                    } else {
                        endDate = calendar.getTime();
                        btnEndDate.setText(dateFormat.format(endDate));
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

        if (!isStartDate && startDate != null) {
            datePickerDialog.getDatePicker().setMinDate(startDate.getTime());
        }
    }


    public static class CustomExpense {
        private final String name;
        private final double amount;


        public CustomExpense( String name ,double amount) {
            this.name = name;
            this.amount = amount;
        }
        public String getName() {
            return name; }

        public double getAmount() {
            return amount;
        }
    }


    private void updateTotalBudget() {
        double activityTotal = 0.0;
        for (ActivityOption option : predefinedActivities) {
            if (option.isSelected.get()) {
                activityTotal += option.cost;
            }
        }
        double customTotal = 0.0;
        for (CustomExpense expense : customExpenses) {
            customTotal += expense.getAmount();
        }
        double subtotal = activityTotal + customTotal;
        double discount = 0.0;
        SharedPreferencesManager spm = new SharedPreferencesManager(this);
        if (spm.isEligibleForLoyaltyDiscount()) {
            discount = subtotal * 0.10; // 10%
        }
        double finalTotal = subtotal - discount;

        // Update the UI
        TextView tvTotal = findViewById(R.id.tvTotalExpenses);
        tvTotal.setText(String.format(Locale.getDefault(),
                "Subtotal: $%.2f\nDiscount: $%.2f\nTotal: $%.2f",
                subtotal, discount, finalTotal));
    }










    private void onExpenseRemoved(int position) {
        if (position >= 0 && position < customExpenses.size()) {
            customExpenses.remove(position);
            customExpenseAdapter.notifyItemRemoved(position);

        }
    }

    private void onAddExpenseClicked() {


        tilExpenseName.setError(null);
        tilExpenseAmount.setError(null);

        String name = etExpenseName.getText() != null ? etExpenseName.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            tilExpenseName.setError("Name is required");
            return;
        }

        tilExpenseAmount.setError(null);
        String amountStr = etExpenseAmount.getText() != null ? etExpenseAmount.getText().toString().trim() : "";

        if (TextUtils.isEmpty(amountStr)) {
            tilExpenseAmount.setError("Amount is required");
            return;
        }


        try {
            double amount = Double.parseDouble(amountStr);
        if (amount < 0) {
            tilExpenseAmount.setError("Please enter a non-negative amount");
            return;
        }


            CustomExpense expense = new CustomExpense(name, amount);
            customExpenses.add(expense);
            customExpenseAdapter.notifyItemInserted(customExpenses.size() - 1);
            etExpenseAmount.setText("");

            etExpenseName.setText("");



            updateTotalBudget();
        } catch (NumberFormatException ex) {
            tilExpenseAmount.setError("Invalid number");
        }
    }

    private void preloadTripIfAny() {
        long tripId = getIntent().getLongExtra("tripId", -1L);
        if (tripId <= 0) {
            updateTotalBudget();
            return;
        }

        com.example.mytripbuddymb.database.TripRepository repo = new com.example.mytripbuddymb.database.TripRepository(this);
        java.util.List<com.example.mytripbuddymb.database.TripRepository.LineItem> items = repo.getLineItemsForTrip(tripId);

        for (com.example.mytripbuddymb.database.TripRepository.LineItem li : items) {
            if ("predefined".equals(li.type)) {
                for (ActivityOption option : predefinedActivities) {
                    if (option.name.equals(li.label)) {
                        option.isSelected.set(true);
                    }
                }
            } else if ("custom".equals(li.type)) {
                customExpenses.add(new CustomExpense(li.label, li.amountCents / 100.0));
            }
        }

        if (predefinedActivityAdapter != null) predefinedActivityAdapter.notifyDataSetChanged();
        if (customExpenseAdapter != null) customExpenseAdapter.notifyDataSetChanged();
        updateTotalBudget();
    }

    private void saveTrip() {
        // Gather trip fields
        com.google.android.material.textfield.TextInputEditText etDestination = findViewById(R.id.etDestination);
        com.google.android.material.textfield.TextInputEditText etNotes = findViewById(R.id.etNotes);
        String destination = etDestination.getText() != null ? etDestination.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        if (TextUtils.isEmpty(destination)) {
            com.google.android.material.textfield.TextInputLayout tilDestination = findViewById(R.id.tilDestination);
            tilDestination.setError("Destination is required");
            return;
        }

        String startDateIso = startDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(startDate) : null;
        String endDateIso = endDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(endDate) : null;

        if (startDate != null && endDate != null && endDate.before(startDate)) {
            android.widget.Toast.makeText(this, "End date must be after start date", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        // Build line items list
        java.util.List<com.example.mytripbuddymb.database.TripRepository.LineItem> items = new java.util.ArrayList<>();

        // Predefined activities selected
        for (ActivityOption option : predefinedActivities) {
            if (option.isSelected.get()) {
                com.example.mytripbuddymb.database.TripRepository.LineItem li = new com.example.mytripbuddymb.database.TripRepository.LineItem();
                li.label = option.name;
                li.amountCents = (int) Math.round(option.cost * 100.0);
                li.type = "predefined";
                items.add(li);
            }
        }

        // Custom expenses
        for (CustomExpense ce : customExpenses) {
            com.example.mytripbuddymb.database.TripRepository.LineItem li = new com.example.mytripbuddymb.database.TripRepository.LineItem();
            li.label = ce.getName();
            li.amountCents = (int) Math.round(ce.getAmount() * 100.0);
            li.type = "custom";
            items.add(li);
        }

        // Persist
        com.example.mytripbuddymb.database.TripRepository repo = new com.example.mytripbuddymb.database.TripRepository(this);
        long tripId = repo.insertTripWithLineItems(destination, startDateIso, endDateIso, notes, items);

        // Increment trip counter
        new SharedPreferencesManager(this).incrementTripCount();

        android.widget.Toast.makeText(this, "Trip saved", android.widget.Toast.LENGTH_SHORT).show();
        // Navigate back or show summary as needed
        finish();
    }



}