package com.gk.daas.di;

import com.gk.daas.core.BaseActivity;
import com.gk.daas.dialog.ErrorDialog;
import com.gk.daas.dialog.ProgressDialog;
import com.gk.daas.dialog.ProgressDialogImpl;
import com.gk.daas.dialog.ServiceSelectorDialog;
import com.gk.daas.framework.access.StringResAccess;
import com.gk.daas.framework.access.ViewToolkit;
import com.gk.daas.framework.access.ViewToolkitImpl;
import com.gk.daas.log.LogFactory;
import com.gk.daas.screen.ErrorTranslator;
import com.gk.daas.screen.ErrorTranslatorImpl;
import com.gk.daas.screen.main.MainView;
import com.gk.daas.screen.main.MainViewImpl;

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
    public ServiceSelectorDialog provideServiceSelectorDialog() {
        return new ServiceSelectorDialog(activity);
    }
}
