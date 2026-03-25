package com.sigmasolve.app.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sigmasolve.app.data.entity.CalculatorHistory;

import java.util.List;

/**
 * CalculatorHistoryDAO — Data Access Object for calculator history.
 */
@Dao
public interface CalculatorHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(CalculatorHistory history);

    /** Get last 50 calculations, newest first */
    @Query("SELECT * FROM calculator_history ORDER BY timestamp DESC LIMIT 50")
    LiveData<List<CalculatorHistory>> getRecentHistory();

    /** Delete all history */
    @Query("DELETE FROM calculator_history")
    void clearHistory();

    /** Delete entries older than given timestamp */
    @Query("DELETE FROM calculator_history WHERE timestamp < :cutoff")
    void deleteOlderThan(long cutoff);

    @Query("SELECT COUNT(*) FROM calculator_history")
    int countHistory();
}
