package com.sigmasolve.app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sigmasolve.app.data.dao.CalculatorHistoryDAO;
import com.sigmasolve.app.data.database.SigmaSolveDatabase;
import com.sigmasolve.app.data.entity.CalculatorHistory;

import java.util.List;

/**
 * CalculatorRepository — manages calculator history persistence.
 */
public class CalculatorRepository {

    private final CalculatorHistoryDAO historyDAO;

    public CalculatorRepository(Application application) {
        SigmaSolveDatabase db = SigmaSolveDatabase.getInstance(application);
        historyDAO = db.calculatorHistoryDAO();
    }

    public LiveData<List<CalculatorHistory>> getRecentHistory() {
        return historyDAO.getRecentHistory();
    }

    public void saveHistory(String expression, String result, String mode) {
        SigmaSolveDatabase.databaseWriteExecutor.execute(() -> {
            CalculatorHistory history = new CalculatorHistory(
                    expression, result, System.currentTimeMillis(), mode);
            historyDAO.insertHistory(history);
        });
    }

    public void clearHistory() {
        SigmaSolveDatabase.databaseWriteExecutor.execute(historyDAO::clearHistory);
    }
}
