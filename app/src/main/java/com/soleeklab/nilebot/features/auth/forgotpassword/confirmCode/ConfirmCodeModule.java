package com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode;

import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ConfirmCodeModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ConfirmCodeFragment confirmCodeFragment();

    @ActivityScoped
    @Binds
    abstract ConfirmCodeContract.Presenter provideConfirmCodePresenter(ConfirmCodePresenter presenter);

    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);

}
