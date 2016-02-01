package com.gk.daas.di;

import com.gk.daas.data.network.DataAccessService;

import dagger.Component;

/**
 * @author Gabor_Keszthelyi
 */
@ServiceScope
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    void satisfy(DataAccessService dataAccessService);

}
