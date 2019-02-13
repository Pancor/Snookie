package pancordev.pl.snookie

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import pancordev.pl.snookie.di.DaggerAppComponent


class SnookieApp: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
        = DaggerAppComponent.builder().application(this).build()

}