package pancordev.pl.snookie.form.login

import android.app.Activity
import dagger.Binds
import dagger.Module
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.AuthManagerModule
import pancordev.pl.snookie.utils.net.NetConnection
import pancordev.pl.snookie.utils.net.NetContract
import pancordev.pl.snookie.utils.net.NetModule

@Module(includes = [AuthManagerModule::class, NetModule::class])
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

    @ActivityScoped
    @Binds
    abstract fun bindNetConnection(netConnection: NetConnection): NetContract
}