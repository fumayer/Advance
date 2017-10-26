package com.quduquxie.base.module.main.fragment.library.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.library.module.LibraryModule;
import com.quduquxie.base.module.main.fragment.library.presenter.LibraryPresenter;
import com.quduquxie.base.module.main.fragment.library.view.LibraryFragment;

import dagger.Component;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = LibraryModule.class, dependencies = ApplicationComponent.class)
public interface LibraryComponent {

    LibraryFragment inject(LibraryFragment libraryFragment);

    LibraryPresenter presenter();
}