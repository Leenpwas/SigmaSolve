package com.sigmasolve.app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.sigmasolve.app.data.entity.Formula;
import com.sigmasolve.app.repository.FormulaRepository;

import java.util.List;

/**
 * FormulaViewModel — exposes formula data to UI layer via LiveData.
 * Handles search state and subject filtering.
 */
public class FormulaViewModel extends AndroidViewModel {

    private final FormulaRepository repository;

    // Current search query — drives filtered results reactively
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    // Current subject filter (empty = all subjects)
    private final MutableLiveData<String> selectedSubject = new MutableLiveData<>("");

    // Reactive formula list based on search query
    public final LiveData<List<Formula>> formulas;

    // All available subjects for filter chips
    public final LiveData<List<String>> subjects;

    // Bookmarked formulas
    public final LiveData<List<Formula>> bookmarkedFormulas;

    // Recently viewed formulas
    public final LiveData<List<Formula>> recentlyViewed;

    public FormulaViewModel(@NonNull Application application) {
        super(application);
        repository = new FormulaRepository(application);

        subjects = repository.getAllSubjects();
        bookmarkedFormulas = repository.getBookmarkedFormulas();
        recentlyViewed = repository.getRecentlyViewed();

        // Switch between search and subject-filtered results reactively
        formulas = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.isEmpty()) {
                String subject = selectedSubject.getValue();
                if (subject == null || subject.isEmpty()) {
                    return repository.getAllFormulas();
                } else {
                    return repository.getFormulasBySubject(subject);
                }
            } else {
                return repository.searchFormulas(query);
            }
        });
    }

    /** Update search query — triggers formula list refresh */
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    /** Filter by subject */
    public void setSubjectFilter(String subject) {
        selectedSubject.setValue(subject);
        // Clear search when filtering by subject
        if (searchQuery.getValue() != null && !searchQuery.getValue().isEmpty()) {
            searchQuery.setValue("");
        } else {
            // Force re-evaluation
            searchQuery.setValue(searchQuery.getValue());
        }
    }

    /** Toggle bookmark for a formula */
    public void toggleBookmark(Formula formula) {
        repository.setBookmarked(formula.id, !formula.isBookmarked);
    }

    /** Record that the user viewed this formula */
    public void markViewed(int formulaId) {
        repository.updateLastViewed(formulaId);
    }

    public String getCurrentSubject() {
        return selectedSubject.getValue();
    }

    /** Get an observable formula by ID */
    public LiveData<Formula> getFormulaById(int id) {
        return repository.getFormulaById(id);
    }

    /**
     * Fetch a single formula synchronously — MUST be called from a background thread.
     */
    public Formula getFormulaByIdSync(int id) {
        return repository.getFormulaByIdSync(id);
    }
}
