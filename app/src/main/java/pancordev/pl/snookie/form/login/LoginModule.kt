package pancordev.pl.snookie.form.login

import android.app.Activity
import dagger.Binds
import dagger.Module
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.AuthManagerModule

@Module(includes = [AuthManagerModule::class])
abstract class LoginModule {

    @ActivityScoped
    @Binds
    abstract fun bindLoginPresenter(presenter: LoginPresenter): LoginContract.Presenter

    @ActivityScoped
    @Binds
    abstract fun bindLoginActivity(activity: LoginActivity): Activity

    @ActivityScoped
    @Binds
    abstract fun bindAuthManager(authManager: AuthManager): AuthContract.AuthManager
}