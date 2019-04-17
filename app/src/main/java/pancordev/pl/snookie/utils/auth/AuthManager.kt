package pancordev.pl.snookie.utils.auth

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class AuthManager @Inject constructor(private val auth: FirebaseAuth,
                                      private val snookieProvider: AuthContract.Snookie,
                                      private val fbProvider: AuthContract.Facebook) : AuthContract.AuthManager {

    companion object {
        const val SIGN_IN_SUCCEED = "SIGN_IN_SUCCEED"
        const val NOT_SIGNED_IN = "NOT_SIGNED_IN"
        const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
        const val INVALID_USER_EMAIL = "INVALID_USER_EMAIL"
        const val INVALID_PASSWD = "INVALID_PASSWD"
        const val FB_SIGN_IN_CANCELED = "FB_SIGN_IN_CANCELED"
    }

    override fun checkIfUserIsSignedIn(): Single<Result> {
        return Single.create{ emitter ->
            val result = if (auth.currentUser != null) {
                Result(isSucceed = true, code = SIGN_IN_SUCCEED)
            } else {
                Result(isSucceed = false, code = NOT_SIGNED_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}