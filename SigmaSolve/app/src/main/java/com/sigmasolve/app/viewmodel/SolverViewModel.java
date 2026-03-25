package com.sigmasolve.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sigmasolve.app.data.entity.Formula;
import com.sigmasolve.app.data.entity.Problem;
import com.sigmasolve.app.repository.FormulaRepository;
import com.sigmasolve.app.repository.ProblemRepository;
import com.sigmasolve.app.solver.SolverEngine;
import com.sigmasolve.app.solver.SolverResult;
import com.sigmasolve.app.data.database.SigmaSolveDatabase;

import java.util.List;
import java.util.Map;

/**
 * SolverViewModel — manages the guided problem-solving workflow.
 * Exposes current problem, steps, and result to the UI.
 */
public class SolverViewModel extends AndroidViewModel {

    private final ProblemRepository problemRepository;
    private final FormulaRepository formulaRepository;
    private final SolverEngine solverEngine;

    public final LiveData<List<Problem>> allProblems;
    public final LiveData<List<String>> problemSubjects;

    private final MutableLiveData<Problem> currentProblem = new MutableLiveData<>();
    private final MutableLiveData<Formula> currentFormula = new MutableLiveData<>();
    private final MutableLiveData<SolverResult> solverResult = new MutableLiveData<>();
    private final MutableLiveData<String> solverError = new MutableLiveData<>();

    public SolverViewModel(@NonNull Application application) {
        super(application);
        problemRepository = new ProblemRepository(application);
        formulaRepository = new FormulaRepository(application);
        solverEngine = new SolverEngine();

        allProblems = problemRepository.getAllProblems();
        problemSubjects = problemRepository.getProblemSubjects();
    }

    /**
     * Load a problem by ID and its associated formula in background thread.
     */
    public void loadProblem(int problemId) {
        // Load on background thread
        new Thread(() -> {
            Problem problem = problemRepository.getProblemByIdSync(problemId);
            if (problem != null) {
                currentProblem.postValue(problem);
                Formula formula = formulaRepository.getFormulaByIdSync(problem.formulaId);
                currentFormula.postValue(formula);
            }
        }).start();
    }

    /**
     * Solve the current problem with user-provided variable values.
     * @param inputValues Map of variable symbol → user-entered value string
     */
    public void solve(Map<String, String> inputValues) {
        Problem problem = currentProblem.getValue();
        Formula formula = currentFormula.getValue();

        if (problem == null || formula == null) {
            solverError.setValue("No problem loaded.");
            return;
        }

        SolverResult result = solverEngine.solve(problem, formula, inputValues);
        if (result.isSuccess()) {
            solverResult.setValue(result);
            solverError.setValue("");
        } else {
            solverError.setValue(result.getErrorMessage());
        }
    }

    // --- Getters ---
    public LiveData<Problem> getCurrentProblem() { return currentProblem; }
    public LiveData<Formula> getCurrentFormula() { return currentFormula; }
    public LiveData<SolverResult> getSolverResult() { return solverResult; }
    public LiveData<String> getSolverError() { return solverError; }
}
