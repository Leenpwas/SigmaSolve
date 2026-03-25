package com.sigmasolve.app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sigmasolve.app.data.dao.FormulaDAO;
import com.sigmasolve.app.data.database.SigmaSolveDatabase;
import com.sigmasolve.app.data.entity.Formula;

import java.util.List;

/**
 * FormulaRepository — abstracts data access for Formula entities.
 * ViewModels use this class instead of calling the DAO directly.
 * All write operations run on a background thread via the DB executor.
 */
public class FormulaRepository {

    private final FormulaDAO formulaDAO;

    public FormulaRepository(Application application) {
        SigmaSolveDatabase db = SigmaSolveDatabase.getInstance(application);
        formulaDAO = db.formulaDAO();
    }

    // --- READ OPERATIONS (LiveData — auto-updated) ---

    public LiveData<List<Formula>> getAllFormulas() {
        return formulaDAO.getAllFormulas();
    }

    public LiveData<List<Formula>> getFormulasBySubject(String subject) {
        return formulaDAO.getFormulasBySubject(subject);
    }

    public LiveData<List<Formula>> searchFormulas(String query) {
        return formulaDAO.searchFormulas(query);
    }

    public LiveData<List<Formula>> getBookmarkedFormulas() {
        return formulaDAO.getBookmarkedFormulas();
    }

    public LiveData<List<Formula>> getRecentlyViewed() {
        return formulaDAO.getRecentlyViewed();
    }

    public LiveData<List<String>> getAllSubjects() {
        return formulaDAO.getAllSubjects();
    }

    /** Observable single formula */
    public LiveData<Formula> getFormulaById(int id) {
        return formulaDAO.getFormulaById(id);
    }

    // --- WRITE OPERATIONS (background thread) ---

    public void setBookmarked(int id, boolean bookmarked) {
        SigmaSolveDatabase.databaseWriteExecutor.execute(() ->
                formulaDAO.setBookmarked(id, bookmarked));
    }

    public void updateLastViewed(int id) {
        SigmaSolveDatabase.databaseWriteExecutor.execute(() ->
                formulaDAO.updateLastViewed(id, System.currentTimeMillis()));
    }

    public void updateFormula(Formula formula) {
        SigmaSolveDatabase.databaseWriteExecutor.execute(() ->
                formulaDAO.updateFormula(formula));
    }

    /** Fetch a single formula by ID synchronously (call from background thread only) */
    public Formula getFormulaByIdSync(int id) {
        return formulaDAO.getFormulaByIdSync(id);
    }
}
