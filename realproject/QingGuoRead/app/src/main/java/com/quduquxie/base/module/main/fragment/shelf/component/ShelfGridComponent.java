package com.quduquxie.base.module.main.fragment.shelf.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.shelf.module.ShelfGridModule;
import com.quduquxie.base.module.main.fragment.shelf.presenter.ShelfGridPresenter;
import com.quduquxie.base.module.main.fragment.shelf.view.ShelfGridFragment;

import dagger.Component;

/**
 * Created on 17/7/19.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = ShelfGridModule.class, dependencies = ApplicationComponent.class)
public interface ShelfGridComponent {

    ShelfGridFragment inject(ShelfGridFragment shelfGridFragment);

    ShelfGridPresenter presenter();
}