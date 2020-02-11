package com.soleeklab.nilebot.features.home.settings.devices;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetBotsQuery;

import java.util.List;

public interface DevicesContract {

    interface View extends BaseView<Presenter> {

        void initBots(List<GetBotsQuery.Bot> botList);

        void successUpdate();

        void successEditBot();

        void successDeleteBot();

        void successNewBot(String id, String token);

    }


    interface Presenter extends BasePresenter<View> {

        void getBots(boolean showProgress);

        void addBot(String name, int rate);

        void editBot(String id, String name, int rate, String timeZone);

        void removeBot(String id);

    }
}
