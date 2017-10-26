package com.quduquxie.base.module.main.fragment.content.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.main.fragment.content.module.ContentModule;
import com.quduquxie.base.module.main.fragment.content.presenter.ContentPresenter;
import com.quduquxie.base.module.main.fragment.content.view.ContentFragment;

import dagger.Component;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = ContentModule.class, dependencies = ApplicationComponent.class)
public interface ContentComponent {

    ContentFragment inject(ContentFragment contentFragment);

    ContentPresenter presenter();
}