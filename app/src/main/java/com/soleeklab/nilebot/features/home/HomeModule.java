package com.soleeklab.nilebot.features.home;

import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.home.farms.FarmsContract;
import com.soleeklab.nilebot.features.home.farms.FarmsFragment;
import com.soleeklab.nilebot.features.home.farms.FarmsPresenter;
import com.soleeklab.nilebot.features.home.notification.NotificationsFragment;
import com.soleeklab.nilebot.features.home.pond.PondsContract;
import com.soleeklab.nilebot.features.home.pond.PondsFragment;
import com.soleeklab.nilebot.features.home.pond.PondsPresenter;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondDetailsContract;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondDetailsFragment;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondDetailsPresenter;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.FilterReadingsFragment;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.PondSensorsReadingsFragment;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.PondSensorsReadingsParentContract;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.PondSensorsReadingsParentFragment;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.PondSensorsReadingsPresenter;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.SummaryFragment;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.chart.ChartFragment;
import com.soleeklab.nilebot.features.home.settings.SettingsFragment;
import com.soleeklab.nilebot.features.home.settings.changePassword.ChangePasswordContract;
import com.soleeklab.nilebot.features.home.settings.changePassword.ChangePasswordFragment;
import com.soleeklab.nilebot.features.home.settings.changePassword.ChangePasswordPresenter;
import com.soleeklab.nilebot.features.home.settings.devices.DevicesContract;
import com.soleeklab.nilebot.features.home.settings.devices.DevicesFragment;
import com.soleeklab.nilebot.features.home.settings.devices.DevicesPresenter;
import com.soleeklab.nilebot.features.home.settings.phones.PhonesContract;
import com.soleeklab.nilebot.features.home.settings.phones.PhonesFragment;
import com.soleeklab.nilebot.features.home.settings.phones.PhonesPresenter;
import com.soleeklab.nilebot.features.home.settings.profile.EditEmailFragment;
import com.soleeklab.nilebot.features.home.settings.profile.EditNameFragment;
import com.soleeklab.nilebot.features.home.settings.profile.EditNumberFragment;
import com.soleeklab.nilebot.features.home.settings.profile.ProfileContract;
import com.soleeklab.nilebot.features.home.settings.profile.ProfileFragment;
import com.soleeklab.nilebot.features.home.settings.profile.ProfilePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeModule {

    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FarmsFragment farmsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ProfileFragment profileFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract EditNameFragment editNameFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract EditEmailFragment editEmailFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract EditNumberFragment editNumberFragment();

    //
    @FragmentScoped
    @ContributesAndroidInjector
    abstract ChangePasswordFragment changePasswordFragment();

    //
    @FragmentScoped
    @ContributesAndroidInjector
    abstract DevicesFragment serialNumbersFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NotificationsFragment notificationsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PondsFragment pondsFragment();

    //
    @FragmentScoped
    @ContributesAndroidInjector
    abstract PondDetailsFragment pondDetailsFragment();

    //
    @FragmentScoped
    @ContributesAndroidInjector
    abstract PondSensorsReadingsFragment pondSensorsReadingsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FilterReadingsFragment filterReadingsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PondSensorsReadingsParentFragment pondSensorsReadingsParentFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SummaryFragment summaryFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ChartFragment chartFragment();

    //
    @FragmentScoped
    @ContributesAndroidInjector
    abstract PhonesFragment phonesFragment();


    @ActivityScoped
    @Binds
    abstract FarmsContract.Presenter provideFarmsPresenter(FarmsPresenter presenter);

    @ActivityScoped
    @Binds
    abstract PondsContract.Presenter providePondsPresenter(PondsPresenter presenter);

    //
    @ActivityScoped
    @Binds
    abstract PondDetailsContract.Presenter providePondDetailsPresenter(PondDetailsPresenter presenter);

    //
    @ActivityScoped
    @Binds
    abstract PondSensorsReadingsParentContract.Presenter providePondSensorsReadingsPresenter(PondSensorsReadingsPresenter presenter);

    @ActivityScoped
    @Binds
    abstract DevicesContract.Presenter provideDevicesPresenter(DevicesPresenter presenter);

    //
    @ActivityScoped
    @Binds
    abstract ProfileContract.Presenter provideProfilePresenter(ProfilePresenter presenter);

    //
    @ActivityScoped
    @Binds
    abstract ChangePasswordContract.Presenter provideChangePasswordPresenter(ChangePasswordPresenter presenter);

    //
//
    @ActivityScoped
    @Binds
    abstract PhonesContract.Presenter providePhonesPresenter(PhonesPresenter presenter);


}
