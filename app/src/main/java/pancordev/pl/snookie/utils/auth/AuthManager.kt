package pancordev.pl.snookie.utils.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() : AuthManagerContract.AuthMaanger {

    //fbProvider: AuthManagerContract.Provider,
    //                                      snookieProvider: AuthManagerContract.Provider

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