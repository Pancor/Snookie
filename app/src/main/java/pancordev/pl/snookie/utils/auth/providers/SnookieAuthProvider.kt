package pancordev.pl.snookie.utils.auth.providers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidator
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidatorContract
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class SnookieAuthProvider @Inject constructor(private val auth: FirebaseAuth,
                                              private val credentialsValidator: CredentialsValidatorContract)
    : AuthContract.Snookie {

    override fun signIn(email: String, password: String): Single<Result> {
        return credentialsValidator.validateEmail(email)
            .zipWith(credentialsValidator.validatePassword(password), BiFunction<Result, Result, Result> {
                    emailValidation, passwordValidation ->
                if (emailValidation.isSuccessful && passwordValidation.isSuccessful) {
                    Result(isSuccessful = true, code = CredentialsValidator.OK)
                } else {
                    if (emailValidation.isSuccessful) { passwordValidation } else { emailValidation }
                }
            })
            .flatMap { result ->
                if (result.isSuccessful) {
                    signInWithFirebase(email, password)
                } else {
                    Single.just(result)
                }
            }
    }

    private fun signInWithFirebase(email: String, password: String): Single<Result> {
        return Single.create<Result> { emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    val result =
                        if (task.isSuccessful) {
                            Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
                        } else {
                            convertExceptionToResult(task.exception)
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