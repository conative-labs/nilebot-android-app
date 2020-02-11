package com.soleeklab.nilebot;

/**
 * Created by omaraboulfotoh on 11/25/17.
 */

public interface ParentView {

    void showProgress();

    void hideProgress();

    void showError(String error);

    void showAlert(String message);

    void showAlert(int message);

    void showNoConnectionAlert();

    void showError();

    void showNoConnection();

    void showToast(String message);
}
