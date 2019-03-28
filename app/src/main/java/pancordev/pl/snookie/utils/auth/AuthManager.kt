package pancordev.pl.snookie.utils.auth

import pancordev.pl.snookie.utils.auth.providers.FacebookQualifier
import pancordev.pl.snookie.utils.auth.providers.SnookieQualifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(@FacebookQualifier val fbProvider: AuthManagerContract.Provider,
                                      @SnookieQualifier val snookieProvider: AuthManagerContract.Provider)
    : AuthManagerContract.AuthMaanger {

    override fun signInByFacebook() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUpByFacebook() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInBySnookie() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUpBySnookie() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}