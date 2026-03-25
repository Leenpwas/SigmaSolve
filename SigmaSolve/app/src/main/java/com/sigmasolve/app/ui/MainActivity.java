package com.sigmasolve.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigmasolve.app.R;
import com.sigmasolve.app.databinding.ActivityMainBinding;

/**
 * MainActivity — hosts the bottom navigation and NavController.
 * Each bottom nav item maps to a Fragment via the Navigation Component.
 *
 * Tabs:
 *  🏠 Home          → HomeFragment
 *  🔧 Solver        → SolverFragment
 *  📚 Formulas      → FormulaLibraryFragment
 *  🔢 Calculator    → CalculatorFragment
 *  🔖 Bookmarks     → BookmarksFragment
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configure bottom navigation with Navigation Component
        BottomNavigationView navView = binding.navView;

        // These IDs are the top-level destinations (no back arrow shown)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_solver,
                R.id.navigation_formula,
                R.id.navigation_calculator,
                R.id.navigation_bookmarks
        ).build();

        // Get NavController from NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}
