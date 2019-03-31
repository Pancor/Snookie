package pancordev.pl.snookie.utils.auth

import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val fbProvider: AuthContract.Facebook,
                                      private val snookieProvider: AuthContract.Snookie)
    : AuthContract.AuthManager {

    companion object {
        const val SIGN_IN_SUCCEED = "SIGN_IN_SUCCEED"
    }

    override fun signInByFacebook(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUpByFacebook(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInBySnookie(email: String, password: String): Single<Result> {
        return snookieProvider.signIn(email, password)
    }

    override fun signUpBySnookie(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}