package com.quduquxie.base.module.main.fragment.selected.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.selected.module.SelectedModule;
import com.quduquxie.base.module.main.fragment.selected.presenter.SelectedPresenter;
import com.quduquxie.base.module.main.fragment.selected.view.SelectedFragment;

import dagger.Component;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = SelectedModule.class, dependencies = ApplicationComponent.class)
public interface SelectedComponent {

    SelectedFragment inject(SelectedFragment selectedFragment);

    SelectedPresenter presenter();
}