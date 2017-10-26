package com.quduquxie.base.module.billboard.component;

import com.quduquxie.application.component.ApplicationComponent;
import com.quduquxie.base.FragmentScope;
import com.quduquxie.base.module.billboard.module.BillboardContentModule;
import com.quduquxie.base.module.billboard.presenter.BillboardContentPresenter;
import com.quduquxie.base.module.billboard.view.fragment.BillboardContentFragment;

import dagger.Component;

/**
 * Created on 17/7/25.
 * Created by crazylei.
 */

@FragmentScope
@Component(modules = BillboardContentModule.class, dependencies = ApplicationComponent.class)
public interface BillboardContentComponent {

    BillboardContentFragment inject(BillboardContentFragment billboardContentFragment);

    BillboardContentPresenter presenter();
}