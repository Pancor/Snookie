package pancordev.pl.snookie.utils.auth

import android.app.Activity
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.form.login.LoginActivity
import pancordev.pl.snookie.utils.auth.providers.FacebookAuthProvider
import pancordev.pl.snookie.utils.auth.providers.SnookieAuthProvider
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidator
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidatorContract
import pancordev.pl.snookie.utils.auth.tools.FacebookCredentialWrapper
import javax.inject.Singleton

@Module(includes = [AuthManagerModule.Providers::class])
class AuthManagerModule {

    @ActivityScoped
    @Provides
    fun provideAuthFirebase() = FirebaseAuth.getInstance()!!

    @ActivityScoped
    @Provides
    fun provideLoginManager() = LoginManager.getInstance()!!

    @ActivityScoped
    @Provides
    fun provideCallbackManager() = CallbackManager.Factory.create()!!


    @ActivityScoped
    @Provides
    fun provideFacebookCredentialWrapper() = FacebookCredentialWrapper()

    @Module
    interface Providers {

        @ActivityScoped
        @Binds
        fun provideFacebookAuthProvider(fbProvider: FacebookAuthProvider) : AuthContract.Facebook

        @ActivityScoped
        @Binds
        fun provideSnookieAuthProvider(snookieProvider: SnookieAuthProvider) : AuthContract.Snookie

        @ActivityScoped
        @Binds
        fun provideCredentialsValidator(credentialsValidator: CredentialsValidator): CredentialsValidatorContract
    }
}