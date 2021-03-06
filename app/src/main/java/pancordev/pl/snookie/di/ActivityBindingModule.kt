package pancordev.pl.snookie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pancordev.pl.snookie.form.login.LoginActivity
import pancordev.pl.snookie.form.login.LoginModule
import dagger.android.support.AndroidSupportInjectionModule




@Module(includes = [AndroidSupportInjectionModule::class])
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun bindLoginActivity(): LoginActivity
}