package com.quduquxie.function.search.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.ActivityScope;
import com.quduquxie.function.search.module.SearchModule;
import com.quduquxie.function.search.presenter.SearchPresenter;
import com.quduquxie.function.search.view.SearchActivity;

import dagger.Component;

/**
 * Created on 17/3/30.
 * Created by crazylei.
 */

@ActivityScope
@Component(modules = SearchModule.class, dependencies = ApplicationComponent.class)
public interface SearchComponent {

    SearchActivity inject(SearchActivity searchActivity);

    SearchPresenter presenter();

}
