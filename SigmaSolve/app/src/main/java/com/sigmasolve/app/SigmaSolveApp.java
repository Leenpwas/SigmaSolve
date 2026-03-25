package com.sigmasolve.app;

import android.app.Application;

/**
 * SigmaSolveApp — Application class.
 * Initializes the app-level singleton (Room DB is lazily initialized on first access).
 */
public class SigmaSolveApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Room DB is initialized lazily on first ViewModel access
        // No additional setup needed here
    }
}
