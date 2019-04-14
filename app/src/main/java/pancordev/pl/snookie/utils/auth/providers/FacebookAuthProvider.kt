package pancordev.pl.snookie.utils.auth.providers

import android.app.Activity
import android.content.Context
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class FacebookAuthProvider @Inject constructor(private val auth: FirebaseAuth,
                                               private val loginManager: LoginManager,
                                               private val callbackManager: CallbackManager,
                                               private val activity: Activity) : AuthContract.Facebook {

    override fun signIn(): Single<Result> {
        //loginManager.logInWithReadPermissions(activity, arrayListOf("email", "public_profile"))


        loginManager.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        return Single.just(Result(true, ""))
    }

    override fun signUp(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int) {
        callbackManager.onActivityResult(requestCode, resultCode, null)// TODO: ???
    }
}