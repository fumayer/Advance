package com.quduquxie.base.module.main.fragment.library.module;

import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.library.presenter.LibraryPresenter;
import com.quduquxie.base.module.main.fragment.library.view.LibraryFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@Module
@FragmentScope
public class LibraryModule {

    private LibraryFragment libraryFragment;

    public LibraryModule(LibraryFragment libraryFragment) {
        this.libraryFragment = libraryFragment;
    }

    @Provides
    @FragmentScope
    LibraryFragment provideStoreFragment() {
        return libraryFragment;
    }

    @Provides
    @FragmentScope
    LibraryPresenter provideStorePresenter() {
        return new LibraryPresenter(libraryFragment);
    }
}