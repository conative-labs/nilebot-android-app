package com.soleeklab.nilebot.features.auth.signup;


import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class SignUpModule {


    @FragmentScoped
    @ContributesAndroidInjector
    abstract SignUpFragment signUpFragment();


    @ActivityScoped
    @Binds
    abstract SignUpContract.Presenter provideSignUpPresenter(SignUpPresenter presenter);

    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);


}
