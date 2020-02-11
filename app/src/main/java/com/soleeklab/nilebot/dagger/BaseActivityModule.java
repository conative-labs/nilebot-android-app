package com.soleeklab.nilebot.dagger;

import com.soleeklab.nilebot.data.repo.HeaderInjector;
import com.soleeklab.nilebot.data.repo.HeaderInjectorImpl;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BaseActivityModule {

    @ActivityScoped
    @Binds
    abstract HeaderInjector provideHeaderInjectorImpl(HeaderInjectorImpl localRepo);


    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepoImpl(LocalRepoImpl localRepo);
}
