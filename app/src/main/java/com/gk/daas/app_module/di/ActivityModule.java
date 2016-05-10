package com.gk.daas.app_module.di;

import com.gk.daas.app_module.core.BaseActivity;
import com.gk.daas.app_module.dialog.ErrorDialog;
import com.gk.daas.app_module.dialog.ProgressDialog;
import com.gk.daas.app_module.dialog.ProgressDialogImpl;
import com.gk.daas.app_module.dialog.ServiceSelectorDialog;
import com.gk.daas.app_module.framework.access.StringResAccess;
import com.gk.daas.app_module.framework.access.Toaster;
import com.gk.daas.app_module.framework.access.ViewToolkit;
import com.gk.daas.app_module.framework.access.ViewToolkitImpl;
import com.gk.daas.app_module.log.LogFactory;
import com.gk.daas.app_module.screen.ErrorTranslator;
import com.gk.daas.app_module.screen.ErrorTranslatorImpl;
import com.gk.daas.app_module.screen.main.MainView;
import com.gk.daas.app_module.screen.main.MainViewImpl;

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

    @Provides
    public ErrorTranslator provideErrorTranslator(StringResAccess stringResAccess, LogFactory logFactory) {
        return new ErrorTranslatorImpl(stringResAccess, logFactory);
    }

    @Provides
    public ViewToolkit provideViewToolkit() {
        return new ViewToolkitImpl(activity);
    }

    @ActivityScope
    @Provides
    public MainView provideMainView(ViewToolkit viewToolkit) {
        return new MainViewImpl(viewToolkit);
    }

    @Provides
    public ErrorDialog provideErrorDialog() {
        return new ErrorDialog(activity);
    }

    @Provides
    public ServiceSelectorDialog provideServiceSelectorDialog(Toaster toaster) {
        return new ServiceSelectorDialog(activity, toaster);
    }
}
