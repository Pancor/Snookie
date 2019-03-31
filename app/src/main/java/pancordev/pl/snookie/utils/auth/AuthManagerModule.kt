package pancordev.pl.snookie.utils.auth

import dagger.Binds
import dagger.Module
import pancordev.pl.snookie.utils.auth.providers.FacebookAuthProvider
import pancordev.pl.snookie.utils.auth.providers.SnookieAuthProvider
import javax.inject.Singleton

@Module
abstract class AuthManagerModule {

    @Singleton
    @Binds
    abstract fun provideAuthManager(authManager: AuthManager): AuthContract.AuthManager

    @Singleton
    @Binds
    abstract fun provideFacebookAuthProvider(fbProvider: FacebookAuthProvider) : AuthContract.Facebook

    @Singleton
    @Binds
    abstract fun provideSnookieAuthProvider(snookieProvider: SnookieAuthProvider) : AuthContract.Snookie
}