package com.soleeklab.nilebot.features.home.farms.addFarm;

import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddFarmModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddFarmFragment addFarmFragment();

    @ActivityScoped
    @Binds
    abstract AddFarmContract.Presenter provideAddFarmPresenter(AddFarmPresenter presenter);

    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);

}
