package pancordev.pl.snookie.utils.auth.providers

import android.app.Activity
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.tools.FacebookCredentialWrapper
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class FacebookAuthProvider @Inject constructor(private val auth: FirebaseAuth,
                                               private val loginManager: LoginManager,
                                               private val callbackManager: CallbackManager,
                                               private val activity: Activity,
                                               private val fbCredentialWrapper: FacebookCredentialWrapper)
    : AuthContract.Facebook {

    override fun signIn(): Single<Result> {
        return Single.create { emitter ->
            loginManager.logInWithReadPermissions(activity, arrayListOf("email", "public_profile"))
            loginManager.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val accessToken = loginResult.accessToken
                    val credential = fbCredentialWrapper.getCredential(accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            val result =
                                if (task.isSuccessful) {
                                    Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED)
                                } else {
                                    convertExceptionToResult(task.exception)
                                }
                            emitter.onSuccess(result)
                        }
                }
                override fun onCancel() {
                    emitter.onSuccess(Result(isSucceed = false, code = AuthManager.FB_SIGN_IN_CANCELED))
                }
                override fun onError(error: FacebookException) {
                    emitter.onSuccess(Result(isSucceed = false, code = AuthManager.UNKNOWN_ERROR))
                }
            })
        }
    }

    private fun convertExceptionToResult(exception: Exception?) : Result {
        val isSucceed = false
        val code = when (exception) {
            is FirebaseAuthInvalidUserException -> { AuthManager.SIGN_IN_ERROR }
            is FirebaseAuthInvalidCredentialsException -> { AuthManager.SIGN_IN_ERROR }
            is FirebaseAuthUserCollisionException -> { AuthManager.EMAIL_IN_USE }
            else -> AuthManager.UNKNOWN_ERROR
        }
        return Result(isSucceed, code)
    }

    override fun signUp(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}