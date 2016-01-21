package com.gk.daas.di;

import com.gk.daas.bus.Bus;
import com.gk.daas.core.BaseActivity;
import com.gk.daas.data.access.DataAccessInitiator;
import com.gk.daas.data.network.SyncScheduler;
import com.gk.daas.dialog.ProgressDialog;
import com.gk.daas.dialog.ProgressDialogImpl;
import com.gk.daas.framework.access.Navigator;
import com.gk.daas.framework.access.NavigatorImpl;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.framework.access.Toaster;
import com.gk.daas.framework.access.ViewFactory;
import com.gk.daas.framework.access.ViewFactoryImpl;
import com.gk.daas.framework.access.ViewToolkit;
import com.gk.daas.framework.access.ViewToolkitImpl;
import com.gk.daas.log.LogFactory;
import com.gk.daas.screen.home.ErrorTranslator;
import com.gk.daas.screen.home.ErrorTranslatorImpl;
import com.gk.daas.screen.home.HomeController;
import com.gk.daas.screen.home.HomeUi;
import com.gk.daas.screen.home.HomeUiImpl;
import com.gk.daas.util.TemperatureFormatter;
import com.gk.daas.widget.HomeFragmentPagerAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Gabor_Keszthelyi
 */
@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    public ProgressDialog provideProgressDialog() {
        return new ProgressDialogImpl(activity.getFragmentManager());
    }

    @ActivityScope
    @Provides
    public HomeController provideHomeController(HomeUi ui, DataAccessInitiator dataAccessInitiator, SyncScheduler syncScheduler, Bus bus, ProgressDialog progressDialog, Navigator navigator, TemperatureFormatter temperatureFormatter, Toaster toaster, ErrorTranslator errorTranslator) {
        return new HomeController(ui, dataAccessInitiator, syncScheduler, bus, progressDialog, navigator, temperatureFormatter, toaster, errorTranslator);
    }

    @Provides
    public ErrorTranslator provideErrorTranslator(StringResAccess stringResAccess, LogFactory logFactory) {
        return new ErrorTranslatorImpl(stringResAccess, logFactory);
    }

    @ActivityScope
    @Provides
    public HomeUi provideHomeUi(ViewToolkit viewToolkit, HomeFragmentPagerAdapter homeFragmentPagerAdapter) {
        return new HomeUiImpl(viewToolkit, homeFragmentPagerAdapter);
    }

    @Provides
    public HomeFragmentPagerAdapter provideHomeFragmentPagerAdapter() {
        return new HomeFragmentPagerAdapter(activity.getFragmentManager());
    }

    @Provides
    public ViewToolkit provideViewToolkit(ViewFactory viewFactory) {
        return new ViewToolkitImpl(activity, viewFactory);
    }

    @Provides
    public Navigator provideNavigator() {
        return new NavigatorImpl(activity);
    }

    @Provides
    public ViewFactory provideViewFactory() {
        return new ViewFactoryImpl(activity);
    }
}
