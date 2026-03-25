package com.sigmasolve.app.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sigmasolve.app.data.dao.ProblemDAO;
import com.sigmasolve.app.data.database.SigmaSolveDatabase;
import com.sigmasolve.app.data.entity.Problem;

import java.util.List;

/**
 * ProblemRepository — abstracts data access for Problem entities.
 */
public class ProblemRepository {

    private final ProblemDAO problemDAO;

    public ProblemRepository(Application application) {
        SigmaSolveDatabase db = SigmaSolveDatabase.getInstance(application);
        problemDAO = db.problemDAO();
    }

    public LiveData<List<Problem>> getAllProblems() {
        return problemDAO.getAllProblems();
    }

    public LiveData<List<Problem>> getProblemsBySubject(String subject) {
        return problemDAO.getProblemsBySubject(subject);
    }

    public LiveData<List<Problem>> searchProblems(String query) {
        return problemDAO.searchProblems(query);
    }

    public LiveData<List<String>> getProblemSubjects() {
        return problemDAO.getProblemSubjects();
    }

    public Problem getProblemByIdSync(int id) {
        return problemDAO.getProblemById(id);
    }
}
