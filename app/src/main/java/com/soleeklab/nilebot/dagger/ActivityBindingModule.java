package com.soleeklab.nilebot.dagger;


import com.soleeklab.nilebot.features.SplashActivity;
import com.soleeklab.nilebot.features.SplashNoInternetConnectionActivity;
import com.soleeklab.nilebot.features.auth.forgotpassword.ForgotPasswordActivity;
import com.soleeklab.nilebot.features.auth.forgotpassword.ForgotPasswordModule;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeActivity;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeModule;
import com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword.ResetPasswordActivity;
import com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword.ResetPasswordModule;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.features.auth.login.LoginModule;
import com.soleeklab.nilebot.features.auth.signup.SignUpActivity;
import com.soleeklab.nilebot.features.auth.signup.SignUpModule;
import com.soleeklab.nilebot.features.home.HomeActivity;
import com.soleeklab.nilebot.features.home.HomeModule;
import com.soleeklab.nilebot.features.home.farms.addFarm.AddFarmActivity;
import com.soleeklab.nilebot.features.home.farms.addFarm.AddFarmModule;
import com.soleeklab.nilebot.features.home.farms.addFarm.MapActivity;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondActivity;
import com.soleeklab.nilebot.features.home.pond.addPond.AddPondModule;
import com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings.chart.ChartFullScreenActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract SplashActivity splashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract SplashNoInternetConnectionActivity splashNoInternetConnectionActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = SignUpModule.class)
    abstract SignUpActivity signUpActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ForgotPasswordModule.class)
    abstract ForgotPasswordActivity forgotPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ConfirmCodeModule.class)
    abstract ConfirmCodeActivity confirmCodeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ResetPasswordModule.class)
    abstract ResetPasswordActivity resetPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity homeActivity();

//
    @ActivityScoped
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract MapActivity mapActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddFarmModule.class)
    abstract AddFarmActivity addFarmActivity();
//
//
    @ActivityScoped
    @ContributesAndroidInjector(modules = AddPondModule.class)
    abstract AddPondActivity addPondActivity();
//

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddFarmModule.class)
    abstract ChartFullScreenActivity chartFullScreenActivity();


}