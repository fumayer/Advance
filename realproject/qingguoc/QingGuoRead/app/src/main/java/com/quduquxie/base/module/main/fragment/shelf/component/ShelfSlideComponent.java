package com.quduquxie.base.module.main.fragment.shelf.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.shelf.module.ShelfSlideModule;
import com.quduquxie.base.module.main.fragment.shelf.presenter.ShelfSlidePresenter;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfSlideFragment;

import dagger.Component;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = ShelfSlideModule.class, dependencies = ApplicationComponent.class)
public interface ShelfSlideComponent {

    ShelfSlideFragment inject(ShelfSlideFragment shelfSlideFragment);

    ShelfSlidePresenter presenter();
}