package com.gk.daas.screen.home;

/**
 * @author Gabor_Keszthelyi
 */
public interface HomeUi {

    void init();

    void setTempResultText(String text);

    void setTempOfflineStoreResultText(String text);

    void setDiffResultText(String text);

    void setForecastResultText(String text);

    void clearResultTexts();

    void setUserActionListener(UserActionListener listener);

    interface UserActionListener {

        void onGetTempButton();

        void onGetWarmestCityForecast();

        void onClearButtonClick();

        void onGoToOtherScreen();

        void onGetDiffButton();


        void onStartBackgroundSync();


        void onStopBackgroundSync();


        void onServiceRadioButtonClick(boolean isMockService);


        void onGetTempStoreButton();
    }
}
