package pancordev.pl.snookie.utils.auth.providers

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnookieAuthProvider @Inject constructor(private val auth: FirebaseAuth) : AuthContract.Snookie {

    override fun signIn(email: String, password: String): Single<Result> {
        return Single.create{ emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED))
                    }
                }
        }
    }

    override fun signUp(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}