package pancordev.pl.snookie.utils.auth

import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.providers.FacebookQualifier
import pancordev.pl.snookie.utils.auth.providers.SnookieQualifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(@FacebookQualifier private val fbProvider: AuthManagerContract.Provider,
                                      @SnookieQualifier private val snookieProvider: AuthManagerContract.Provider)
    : AuthManagerContract.AuthManager {

    companion object {
        const val SIGN_IN_SUCCEED = "SIGN_IN_SUCCEED"
    }

    override fun signInByFacebook(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUpByFacebook(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInBySnookie(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUpBySnookie(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}