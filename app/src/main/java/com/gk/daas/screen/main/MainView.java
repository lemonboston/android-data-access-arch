package com.gk.daas.screen.main;

import android.support.annotation.StringRes;

import java.util.List;

/**
 * @author Gabor_Keszthelyi
 */
public interface MainView {

    void init();

    void setUseCaseTitles(List<String> titles);

    void hideEverything();

    void setUserActionListener(UserActionListener listener);

    String getCity1();

    String getCity2();

    void setResultText(String result);

    void showProgressBar();

    void hideProgressBar();

    void showGeneralDescription();

    void showOtherScreenButton();

    void setTechnicalUseCaseDesc(@StringRes int stringResId);

    void setHowToTestDesc(@StringRes int stringResId);

    void setExecuteButtonText(@StringRes int stringResId);

    void setWeatherUseCaseDesc(@StringRes int stringResId);

    void setImplementationDesc(@StringRes int stringResId);

    void showCity2();

    void clearResultText();

    interface UserActionListener {

        void onUseCaseSelected(int position);

        void onExecuteButtonClick();

        void onOtherScreenButtonClick();

        void onXbuttonClick();
    }
}
