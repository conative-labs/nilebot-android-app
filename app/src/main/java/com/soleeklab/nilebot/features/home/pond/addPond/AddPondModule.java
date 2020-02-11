package com.soleeklab.nilebot.features.home.pond.addPond;

import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo.AddPondFormTwoContract;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo.AddPondFormTwoPresenter;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo.AddPondFragmentTwo;
import com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne.AddPondFormOneContract;
import com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne.AddPondFormOnePresenter;
import com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne.AddPondFragmentOne;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddPondModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddPondFragment addPondFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddPondFragmentOne addPondFragmentOne();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddPondFragmentTwo addPondFragmentTwo();

    @ActivityScoped
    @Binds
    abstract AddPondFormOneContract.Presenter provideAddPondFormOnePresenter(AddPondFormOnePresenter presenter);


    @ActivityScoped
    @Binds
    abstract AddPondFormTwoContract.Presenter provideAddPondFormTwoPresenter(AddPondFormTwoPresenter presenter);


    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);

}
