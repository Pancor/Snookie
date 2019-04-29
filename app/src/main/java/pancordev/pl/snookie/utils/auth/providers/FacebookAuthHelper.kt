package pancordev.pl.snookie.utils.auth.providers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.*
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.model.ResultAbs
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.models.FbLoginResult
import pancordev.pl.snookie.utils.auth.models.GraphResult
import pancordev.pl.snookie.utils.auth.tools.FacebookCredentialWrapper
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class FacebookAuthHelper @Inject constructor(private val auth: FirebaseAuth,
                                             private val loginManager: LoginManager,
                                             private val callbackManager: CallbackManager,
                                             private val activity: Activity,
                                             private val fbCredentialWrapper: FacebookCredentialWrapper)
    : AuthContract.Facebook.Helper {

    override fun signInToFacebook() = Single.create<FbLoginResult> {
        loginManager.logInWithReadPermissions(activity, arrayListOf("email"))
        loginManager.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val result =
                    if (loginResult.recentlyDeniedPermissions.contains("email")) {
                        FbLoginResult(isSuccessful = false, code = AuthManager.EMAIL_PERMISSIONS_NOT_GRANTED)
                    } else {
                        FbLoginResult(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED,
                            accessToken = loginResult.accessToken)
                    }
                it.onSuccess(result)
            }
            override fun onCancel() {
                it.onSuccess(FbLoginResult(isSuccessful= false, code = AuthManager.FB_SIGN_IN_CANCELED))
            }
            override fun onError(error: FacebookException) {
                it.onSuccess(FbLoginResult(isSuccessful= false, code = AuthManager.UNKNOWN_ERROR))
            }
        })
    }

    override fun getFacebookUserEmailAndCredential(accessToken: AccessToken) = Single.create<GraphResult> {
        val emailRequest = GraphRequest.newMeRequest(accessToken) { user, request ->
            if (request.error == null) {
                val result =
                    if (user.has("email") && !user.isNull("email")) {
                        val email = user.getString("email")
                        val credential = fbCredentialWrapper.getCredential(accessToken.token)
                        GraphResult(isSuccessful = true, code = "", email = email, credential = credential)
                    } else {
                        GraphResult(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
                    }
                it.onSuccess(result)
            } else {
                it.onSuccess(GraphResult(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR))
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "email")
        emailRequest.parameters = parameters
        emailRequest.executeAsync()
    }

    override fun fetchSignInMethodsForEmail(email: String, credential: AuthCredential) = Single.create<ResultAbs> {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fetchResult = task.result!!
                    val signInMethods = fetchResult.signInMethods!!
                    if (signInMethods.contains(credential.provider) || signInMethods.isEmpty()) {
                        it.onSuccess(GraphResult(isSuccessful = true, code = "", credential = credential))
                    } else {
                        it.onSuccess(Result(isSuccessful = false, code = AuthManager.WRONG_PROVIDER))
                    }
                } else {
                    val result = convertExceptionToResult(task.exception)
                    it.onSuccess(result)
                }
            }
    }

    override fun signInToFirebase(credential: AuthCredential) = Single.create<Result> {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                val result =
                    if (task.isSuccessful) {
                        Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
                    } else {
                        convertExceptionToResult(task.exception)
                    }
                it.onSuccess(result)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}