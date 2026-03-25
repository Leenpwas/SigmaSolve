package com.sigmasolve.app.ui.calculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.sigmasolve.app.R;
import com.sigmasolve.app.databinding.CalcBtnDesmosBinding;
import com.sigmasolve.app.databinding.FragmentCalculatorBinding;
import com.sigmasolve.app.viewmodel.CalculatorViewModel;

/**
 * CalculatorFragment — Advanced Engineering Calculator.
 * Supports Graphing, Matrices, Complex Numbers, and Equation Solving.
 */
public class CalculatorFragment extends Fragment {

    private FragmentCalculatorBinding binding;
    private CalculatorViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        // Prevent soft keyboard from showing up
        binding.etExpression.setShowSoftInputOnFocus(false);

        setupExpressionInput();
        setupDisplayObservers();
        setupKeypadTabs();
        setupKeypadButtons();
        
        // Populate dynamic keypads after the view is created
        view.post(() -> {
            if (binding != null) {
                populateFuncKeypad();
                populateMatrixKeypad();
            }
        });
    }

    private void setupExpressionInput() {
        binding.etExpression.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String expr = s.toString();
                viewModel.updateExpressionDirectly(expr);
                if (expr.contains("x")) {
                    binding.graphView.setExpression(expr);
                }
            }
        });

        binding.etExpression.setOnClickListener(v -> v.requestFocus());
    }

    private void setupDisplayObservers() {
        viewModel.getExpression().observe(getViewLifecycleOwner(), expr -> {
            String currentText = binding.etExpression.getText().toString();
            if (!expr.equals(currentText)) {
                int start = binding.etExpression.getSelectionStart();
                binding.etExpression.setText(expr);
                int newPos = Math.min(start + (expr.length() - currentText.length()), expr.length());
                binding.etExpression.setSelection(Math.max(0, newPos));
            }
            if (expr.contains("x")) {
                binding.graphView.setExpression(expr);
            }
        });

        viewModel.getResult().observe(getViewLifecycleOwner(), result ->
                binding.tvResult.setText("= " + result));
    }

    private void setupKeypadTabs() {
        binding.tabKeypad.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.gridMain.setVisibility(View.GONE);
                binding.gridFunc.setVisibility(View.GONE);
                binding.gridMatrix.setVisibility(View.GONE);

                switch (tab.getPosition()) {
                    case 0: binding.gridMain.setVisibility(View.VISIBLE); break;
                    case 1: binding.gridFunc.setVisibility(View.VISIBLE); break;
                    case 2: binding.gridMatrix.setVisibility(View.VISIBLE); break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupKeypadButtons() {
        // Numbers & Operators (Main Grid)
        setupBtn(binding.btn0, "0", "0");
        setupBtn(binding.btn1, "1", "1");
        setupBtn(binding.btn2, "2", "2");
        setupBtn(binding.btn3, "3", "3");
        setupBtn(binding.btn4, "4", "4");
        setupBtn(binding.btn5, "5", "5");
        setupBtn(binding.btn6, "6", "6");
        setupBtn(binding.btn7, "7", "7");
        setupBtn(binding.btn8, "8", "8");
        setupBtn(binding.btn9, "9", "9");
        setupBtn(binding.btnDot, ".", ".");
        setupBtn(binding.btnComma, ",", ",");
        setupBtn(binding.btnAdd, "+", "+");
        setupBtn(binding.btnSub, "-", "−");
        setupBtn(binding.btnMul, "*", "×");
        setupBtn(binding.btnDiv, "/", "÷");
        setupBtn(binding.btnX, "x", "x");
        setupBtn(binding.btnY, "y", "y");
        setupBtn(binding.btnSquare, "^2", "a²");
        setupBtn(binding.btnPower, "^", "aᵇ");
        setupBtn(binding.btnSqrt, "sqrt(", "√");
        setupBtn(binding.btnPi, "π", "π");
        setupBtn(binding.btnAbs, "abs(", "|a|");

        binding.btnEnter.getRoot().setOnClickListener(v -> viewModel.calculate());
        ((MaterialButton) binding.btnEnter.getRoot()).setText("ENTER");
        binding.btnBack.getRoot().setOnClickListener(v -> viewModel.backspace());
        ((MaterialButton) binding.btnBack.getRoot()).setText("⌫");
    }

    private void populateFuncKeypad() {
        binding.gridFunc.removeAllViews();
        binding.gridFunc.setColumnCount(4);
        binding.gridFunc.setRowCount(4);
        String[][] funcs = {
            {"sin(", "sin"}, {"cos(", "cos"}, {"tan(", "tan"}, {"i", "i"},
            {"asin(", "sin⁻¹"}, {"acos(", "cos⁻¹"}, {"atan(", "tan⁻¹"}, {"=", "="},
            {"log(", "log"}, {"ln(", "ln"}, {"sinh(", "sinh"}, {"cosh(", "cosh"},
            {"⌫", "⌫"}, {"(", "("}, {")", ")"}, {" ", " "}
        };
        for (String[] func : funcs) {
            if (func[0].equals("⌫")) {
                addSpecialButton(binding.gridFunc, "⌫", v -> viewModel.backspace());
            } else {
                addDynamicButton(binding.gridFunc, func[0], func[1]);
            }
        }
    }

    private void populateMatrixKeypad() {
        binding.gridMatrix.removeAllViews();
        binding.gridMatrix.setColumnCount(4);
        binding.gridMatrix.setRowCount(3);
        String[][] tools = {
            {"[", "["}, {"]", "]"}, {",", ","}, {"transpose(", "tr"},
            {"det(", "det"}, {"inv(", "inv"}, {"identity(", "id"}, {"rank(", "rank"},
            {"⌫", "⌫"}, {"ENTER", "ENT"}
        };
        for (String[] tool : tools) {
            if (tool[0].equals("⌫")) {
                addSpecialButton(binding.gridMatrix, "⌫", v -> viewModel.backspace());
            } else if (tool[0].equals("ENTER")) {
                addSpecialButton(binding.gridMatrix, "ENT", v -> viewModel.calculate());
            } else {
                addDynamicButton(binding.gridMatrix, tool[0], tool[1]);
            }
        }
    }

    private void addDynamicButton(GridLayout grid, String value, String label) {
        MaterialButton btn = (MaterialButton) getLayoutInflater().inflate(R.layout.calc_btn_desmos, grid, false);
        btn.setText(label);
        btn.setOnClickListener(v -> {
            int cursorPosition = binding.etExpression.getSelectionStart();
            viewModel.insertAtPosition(value, cursorPosition);
            binding.etExpression.requestFocus();
        });
        
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        );
        params.width = 0;
        params.height = 0;
        int margin = (int) (2 * getResources().getDisplayMetrics().density);
        params.setMargins(margin, margin, margin, margin);
        
        grid.addView(btn, params);
    }

    private void addSpecialButton(GridLayout grid, String label, View.OnClickListener listener) {
        MaterialButton btn = (MaterialButton) getLayoutInflater().inflate(R.layout.calc_btn_desmos, grid, false);
        btn.setText(label);
        btn.setOnClickListener(v -> {
            listener.onClick(v);
            binding.etExpression.requestFocus();
        });
        
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        );
        params.width = 0;
        params.height = 0;
        int margin = (int) (2 * getResources().getDisplayMetrics().density);
        params.setMargins(margin, margin, margin, margin);

        grid.addView(btn, params);
    }

    private void setupBtn(CalcBtnDesmosBinding btnBinding, String value, String label) {
        MaterialButton btn = (MaterialButton) btnBinding.getRoot();
        btn.setText(label);
        btn.setOnClickListener(v -> {
            int cursorPosition = binding.etExpression.getSelectionStart();
            viewModel.insertAtPosition(value, cursorPosition);
            binding.etExpression.requestFocus();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
