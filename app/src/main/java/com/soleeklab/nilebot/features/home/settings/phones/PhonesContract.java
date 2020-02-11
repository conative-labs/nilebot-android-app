package com.soleeklab.nilebot.features.home.settings.phones;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetPhonesQuery;
import com.soleeklab.nilebot.type.LanguageType;

import java.util.List;

public interface PhonesContract {

    interface View extends BaseView<Presenter> {

        void initPhones(List<GetPhonesQuery.Phone> phoneArrayList);

        void successEditPhone();

        void successDeletePhone();

        void successNewPhone(String prefix,String number,String language);

    }


    interface Presenter extends BasePresenter<View> {

        void getPhones(boolean showProgress);

        void addPhone(String prefix,String phone, LanguageType languageType);

        void editPhone(String prefix,String phone, LanguageType languageType);

        void removePhone(String prefix ,String number);


    }
}
