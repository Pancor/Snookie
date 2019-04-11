package pancordev.pl.snookie.utils.auth

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import pancordev.pl.snookie.utils.auth.providers.FacebookAuthProvider
import pancordev.pl.snookie.utils.auth.providers.SnookieAuthProvider
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidator
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidatorContract
import javax.inject.Singleton

@Module(includes = [AuthManagerModule.Providers::class])
class AuthManagerModule {

    @Singleton
    @Provides
    fun provideAuthFirebase() = FirebaseAuth.getInstance()!!

    @Module
    interface Providers {

        @Singleton
        @Binds
        fun provideAuthManager(authManager: AuthManager): AuthContract.AuthManager

        @Singleton
        @Binds
        fun provideFacebookAuthProvider(fbProvider: FacebookAuthProvider) : AuthContract.Facebook

        @Singleton
        @Binds
        fun provideSnookieAuthProvider(snookieProvider: SnookieAuthProvider) : AuthContract.Snookie

        @Singleton
        @Binds
        fun provideCredentialsValidator(credentialsValidator: CredentialsValidator): CredentialsValidatorContract
    }
}