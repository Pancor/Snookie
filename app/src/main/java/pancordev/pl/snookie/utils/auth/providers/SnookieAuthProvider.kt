package pancordev.pl.snookie.utils.auth.providers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnookieAuthProvider @Inject constructor(private val auth: FirebaseAuth) : AuthContract.Snookie {

    override fun signIn(email: String, password: String): Single<Result> {
        return Single.create{ emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    val result =
                        if (it.isSuccessful) {
                            Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED)
                        } else {
                            convertExceptionToResult(it.exception)
                        }
                    emitter.onSuccess(result)
                }
        }
    }

    private fun convertExceptionToResult(exception: Exception?) : Result {
        val isSucceed = false
        var code = AuthManager.UNKNOWN_ERROR
        when (exception) {
            is FirebaseAuthInvalidUserException -> { code = AuthManager.INVALID_USER_EMAIL }
            is FirebaseAuthInvalidCredentialsException -> { code = AuthManager.INVALID_PASSWD }
        }
        return Result(isSucceed, code)
    }

    override fun signUp(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}