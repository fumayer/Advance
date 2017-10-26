package com.quduquxie.base.module.main.fragment.mine.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.mine.module.MineModule;
import com.quduquxie.base.module.main.fragment.mine.presenter.MinePresenter;
import com.quduquxie.base.module.main.fragment.mine.view.MineFragment;

import dagger.Component;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = MineModule.class, dependencies = ApplicationComponent.class)
public interface MineComponent {

    MineFragment inject(MineFragment mineFragment);

    MinePresenter presenter();
}