package com.soleeklab.nilebot.features.auth.forgotpassword;

import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.auth.login.LoginContract;
import com.soleeklab.nilebot.features.auth.login.LoginPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ForgotPasswordModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ForgotPasswordFragment forgotPasswordFragment();

    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);

    @ActivityScoped
    @Binds
    abstract ForgotPasswordContract.Presenter provideForgotPasswordPresenter(ForgotPasswordPresenter presenter);



}
