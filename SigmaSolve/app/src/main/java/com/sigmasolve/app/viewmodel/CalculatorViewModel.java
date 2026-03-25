package com.sigmasolve.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sigmasolve.app.calculator.ScientificCalculator;
import com.sigmasolve.app.repository.CalculatorRepository;
import com.sigmasolve.app.data.entity.CalculatorHistory;

import java.util.List;

/**
 * CalculatorViewModel — manages the scientific calculator state and history.
 * Enhanced for Desmos-style real-time editing.
 */
public class CalculatorViewModel extends AndroidViewModel {

    private final CalculatorRepository repository;
    private final ScientificCalculator calculator;

    private final MutableLiveData<String> expression = new MutableLiveData<>("");
    private final MutableLiveData<String> result = new MutableLiveData<>("0");
    private final MutableLiveData<Boolean> isDegreeMode = new MutableLiveData<>(true);
    public final LiveData<List<CalculatorHistory>> history;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>("");

    public CalculatorViewModel(@NonNull Application application) {
        super(application);
        repository = new CalculatorRepository(application);
        calculator = new ScientificCalculator();
        history = repository.getRecentHistory();
    }

    public void updateExpressionDirectly(String newExpression) {
        if (!newExpression.equals(expression.getValue())) {
            expression.setValue(newExpression);
            evaluatePreview(newExpression);
        }
    }

    public void insertAtPosition(String value, int position) {
        String current = expression.getValue();
        if (current == null) current = "";
        
        String updated;
        if (position < 0 || position > current.length()) {
            updated = current + value;
        } else {
            updated = current.substring(0, position) + value + current.substring(position);
        }
        
        expression.setValue(updated);
        evaluatePreview(updated);
    }

    public void appendToExpression(String value) {
        String current = expression.getValue();
        if (current == null) current = "";
        expression.setValue(current + value);
        evaluatePreview(current + value);
    }

    public void backspace() {
        String current = expression.getValue();
        if (current != null && !current.isEmpty()) {
            expression.setValue(current.substring(0, current.length() - 1));
            evaluatePreview(expression.getValue());
        }
    }

    public void clearAll() {
        expression.setValue("");
        result.setValue("0");
        errorMessage.setValue("");
    }

    public void calculate() {
        String expr = expression.getValue();
        if (expr == null || expr.isEmpty()) return;

        boolean degMode = isDegreeMode.getValue() != null && isDegreeMode.getValue();
        String calcResult = calculator.evaluate(expr, degMode);

        if (calcResult.startsWith("Error")) {
            errorMessage.setValue(calcResult);
            // Don't update result to "Error" on explicit calculate if we want to keep preview
        } else {
            result.setValue(calcResult);
            errorMessage.setValue("");
            repository.saveHistory(expr, calcResult, degMode ? "DEG" : "RAD");
        }
    }

    private void evaluatePreview(String expr) {
        if (expr.isEmpty()) {
            result.setValue("0");
            errorMessage.setValue("");
            return;
        }
        boolean degMode = isDegreeMode.getValue() != null && isDegreeMode.getValue();
        String preview = calculator.evaluate(expr, degMode);
        if (!preview.startsWith("Error")) {
            result.setValue(preview);
            errorMessage.setValue("");
        } else {
            // Keep previous valid result or show 0?
            // Desmos usually just doesn't show an answer if invalid
        }
    }

    public void toggleAngleMode() {
        Boolean current = isDegreeMode.getValue();
        isDegreeMode.setValue(current == null || !current);
        if (expression.getValue() != null) {
            evaluatePreview(expression.getValue());
        }
    }

    public void clearHistory() {
        repository.clearHistory();
    }

    public void reuseHistoryItem(CalculatorHistory item) {
        expression.setValue(item.expression);
        result.setValue(item.result);
    }

    public LiveData<String> getExpression() { return expression; }
    public LiveData<String> getResult() { return result; }
    public LiveData<Boolean> getIsDegreeMode() { return isDegreeMode; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
}
