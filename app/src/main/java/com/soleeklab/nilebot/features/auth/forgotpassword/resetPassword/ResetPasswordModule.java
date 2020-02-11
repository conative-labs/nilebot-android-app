package com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword;

import com.soleeklab.nilebot.dagger.ActivityScoped;
import com.soleeklab.nilebot.dagger.FragmentScoped;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeContract;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeFragment;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodePresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ResetPasswordModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ResetPasswordFragment resetPasswordFragment();

    @ActivityScoped
    @Binds
    abstract ResetPasswordContract.Presenter provideResetPasswordPresenter(ResetPasswordPresenter presenter);

    @ActivityScoped
    @Binds
    abstract LocalRepo provideLocalRepo(LocalRepoImpl localRepo);
}
