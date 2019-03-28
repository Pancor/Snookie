package pancordev.pl.snookie.utils.auth

import dagger.Binds
import dagger.Module
import pancordev.pl.snookie.utils.auth.providers.FacebookAuthProvider
import pancordev.pl.snookie.utils.auth.providers.FacebookQualifier
import pancordev.pl.snookie.utils.auth.providers.SnookieAuthProvider
import pancordev.pl.snookie.utils.auth.providers.SnookieQualifier
import javax.inject.Singleton

@Module
abstract class AuthManagerModule {

    @Singleton
    @Binds
    abstract fun provideAuthManager(authManager: AuthManager): AuthManagerContract.AuthMaanger

    @Singleton
    @Binds
    @FacebookQualifier
    abstract fun provideFacebookAuthProvider(fbProvider: FacebookAuthProvider) : AuthManagerContract.Provider

    @Singleton
    @Binds
    @SnookieQualifier
    abstract fun provideSnookieAuthProvider(snookieProvider: SnookieAuthProvider) : AuthManagerContract.Provider
}