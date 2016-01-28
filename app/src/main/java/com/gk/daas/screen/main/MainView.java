package com.gk.daas.screen.main;

import java.util.List;

/**
 * @author Gabor_Keszthelyi
 */
public interface MainView {

    void init();

    void setUseCaseTitles(List<String> titles);

    void hideEverything();

    void showEverything();

    void setUserActionListener(UserActionListener listener);

    String getCity1();

    String getCity2();

    void setResultText(String result);

    void showProgressBar();

    void hideProgressBar();

    void showGeneralDescription();


    interface UserActionListener {

        void onUseCaseSelected(int position);

        void onExecuteButtonClick();
    }
}
