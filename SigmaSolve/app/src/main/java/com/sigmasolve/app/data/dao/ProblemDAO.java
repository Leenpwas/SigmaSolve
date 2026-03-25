package com.sigmasolve.app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sigmasolve.app.data.entity.Problem;

import java.util.List;

/**
 * ProblemDAO — Data Access Object for the problems table.
 */
@Dao
public interface ProblemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProblem(Problem problem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllProblems(List<Problem> problems);

    @Query("SELECT * FROM problems ORDER BY subject ASC, title ASC")
    LiveData<List<Problem>> getAllProblems();

    @Query("SELECT * FROM problems WHERE subject = :subject ORDER BY title ASC")
    LiveData<List<Problem>> getProblemsBySubject(String subject);

    @Query("SELECT * FROM problems WHERE id = :id")
    Problem getProblemById(int id);

    @Query("SELECT * FROM problems WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    LiveData<List<Problem>> searchProblems(String query);

    @Query("SELECT DISTINCT subject FROM problems ORDER BY subject ASC")
    LiveData<List<String>> getProblemSubjects();

    @Query("SELECT COUNT(*) FROM problems")
    int countProblems();
}
