package com.quduquxie.function.search.module;

import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.search.presenter.SearchPresenter;
import com.quduquxie.function.search.view.SearchActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/3/30.
 * Created by crazylei.
 */

@Module
public class SearchModule {
    private SearchActivity searchActivity;

    public SearchModule(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    @Provides
    @ActivityScope
    SearchActivity provideSearchActivity() {
        return searchActivity;
    }

    @Provides
    @ActivityScope
    SearchPresenter provideSearchPresenter() {
        return new SearchPresenter(searchActivity);
    }
}
