package com.sigmasolve.app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sigmasolve.app.data.entity.Formula;

import java.util.List;

/**
 * FormulaDAO — Data Access Object for the formulas table.
 */
@Dao
public interface FormulaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormula(Formula formula);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllFormulas(List<Formula> formulas);

    @Update
    void updateFormula(Formula formula);

    @Query("SELECT * FROM formulas ORDER BY name ASC")
    LiveData<List<Formula>> getAllFormulas();

    @Query("SELECT * FROM formulas WHERE subject = :subject ORDER BY name ASC")
    LiveData<List<Formula>> getFormulasBySubject(String subject);

    @Query("SELECT * FROM formulas WHERE name LIKE '%' || :query || '%' " +
           "OR equation LIKE '%' || :query || '%' " +
           "OR variables LIKE '%' || :query || '%' ORDER BY name ASC")
    LiveData<List<Formula>> searchFormulas(String query);

    @Query("SELECT * FROM formulas WHERE is_bookmarked = 1 ORDER BY name ASC")
    LiveData<List<Formula>> getBookmarkedFormulas();

    @Query("SELECT * FROM formulas WHERE last_viewed > 0 ORDER BY last_viewed DESC LIMIT 10")
    LiveData<List<Formula>> getRecentlyViewed();

    @Query("SELECT * FROM formulas WHERE id = :id")
    LiveData<Formula> getFormulaById(int id);

    @Query("SELECT * FROM formulas WHERE id = :id")
    Formula getFormulaByIdSync(int id);

    @Query("UPDATE formulas SET is_bookmarked = :bookmarked WHERE id = :id")
    void setBookmarked(int id, boolean bookmarked);

    @Query("UPDATE formulas SET last_viewed = :timestamp WHERE id = :id")
    void updateLastViewed(int id, long timestamp);

    @Query("SELECT DISTINCT subject FROM formulas ORDER BY subject ASC")
    LiveData<List<String>> getAllSubjects();

    @Query("SELECT COUNT(*) FROM formulas")
    int countFormulas();
}
