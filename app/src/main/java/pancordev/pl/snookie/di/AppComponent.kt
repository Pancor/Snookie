package pancordev.pl.snookie.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pancordev.pl.snookie.SnookieApp
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.AuthManagerContract
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,
    ActivityBindingModule::class,
    AndroidSupportInjectionModule::class])
interface AppComponent: AndroidInjector<SnookieApp> {

    fun getAuthManager(): AuthManager

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}