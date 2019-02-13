package pancordev.pl.snookie.form.login

import dagger.Binds
import dagger.Module
import pancordev.pl.snookie.di.ActivityScoped

@Module
abstract class LoginModule {

    @ActivityScoped
    @Binds
    abstract fun bindLoginPresenter(presenter: LoginPresenter): LoginContract.Presenter
}