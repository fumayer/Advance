package com.quduquxie.base.module.main.fragment.shelf.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.shelf.module.BookShelfModule;
import com.quduquxie.base.module.main.fragment.shelf.presenter.BookShelfPresenter;
import com.quduquxie.base.module.main.fragment.shelf.view.BookShelfFragment;

import dagger.Component;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = BookShelfModule.class, dependencies = ApplicationComponent.class)
public interface BookShelfComponent {

    BookShelfFragment inject(BookShelfFragment bookShelfFragment);

    BookShelfPresenter presenter();
}