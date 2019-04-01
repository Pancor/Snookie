package pancordev.pl.snookie.utils.auth

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val fbProvider: AuthContract.Facebook,
                                      private val snookieProvider: AuthContract.Snookie,
                                      private val auth: FirebaseAuth) : AuthContract.AuthManager {

    companion object {
        const val SIGN_IN_SUCCEED = "SIGN_IN_SUCCEED"
        const val NOT_SIGNED_IN = "NOT_SIGNED_IN"
    }

    override fun checkIfUserIsSignedIn(): Single<Result> {
        return Single.create{ emitter ->
            var result = Result(isSucceed = false, code = NOT_SIGNED_IN)
            if (auth.currentUser != null) {
                result = Result(isSucceed = true, code = SIGN_IN_SUCCEED)
            }
            emitter.onSuccess(result)
        }
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