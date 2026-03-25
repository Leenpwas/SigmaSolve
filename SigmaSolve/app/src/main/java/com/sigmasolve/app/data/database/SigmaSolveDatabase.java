package com.sigmasolve.app.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sigmasolve.app.data.dao.CalculatorHistoryDAO;
import com.sigmasolve.app.data.dao.FormulaDAO;
import com.sigmasolve.app.data.dao.ProblemDAO;
import com.sigmasolve.app.data.entity.CalculatorHistory;
import com.sigmasolve.app.data.entity.Formula;
import com.sigmasolve.app.data.entity.Problem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SigmaSolveDatabase — Main Room database.
 * Version bumped to 9 to include the complete Electronics Engineering formulas.
 */
@Database(
    entities = {Formula.class, Problem.class, CalculatorHistory.class},
    version = 9,
    exportSchema = false
)
public abstract class SigmaSolveDatabase extends RoomDatabase {

    public abstract FormulaDAO formulaDAO();
    public abstract ProblemDAO problemDAO();
    public abstract CalculatorHistoryDAO calculatorHistoryDAO();

    private static volatile SigmaSolveDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static SigmaSolveDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (SigmaSolveDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            SigmaSolveDatabase.class,
                            "sigmasolve_database"
                    )
                    .addCallback(seedCallback)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback seedCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            seedDatabase();
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
            seedDatabase();
        }
    };

    private static void seedDatabase() {
        databaseWriteExecutor.execute(() -> {
            if (INSTANCE != null) {
                DatabaseSeeder.seedAll(INSTANCE);
            }
        });
    }
}
