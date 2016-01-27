package com.gk.daas.screen.main;

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

    void setResultText(String result);

    void showEverything();

    void hideProgressBar();

    void showProgressBar();

    interface UserActionListener {

        void onUseCaseSelected(int position);

        void onExecuteButtonClick();
    }
}
