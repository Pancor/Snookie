package pancordev.pl.snookie.utils.auth.providers

import android.content.Intent
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.model.ResultAbs
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.models.GraphResult
import javax.inject.Inject

@ActivityScoped
class FacebookAuthProvider @Inject constructor(private val fbHelper: AuthContract.Facebook.Helper)
    : AuthContract.Facebook.Provider {

    override fun signIn(): Single<ResultAbs> {
        return fbHelper.signInToFacebook()
            .flatMap { loginResult ->
                if (loginResult.isSuccessful) {
                    fbHelper.getFacebookUserEmailAndCredential(loginResult.accessToken!!)
                } else {
                    Single.just(loginResult)
                }
            }
            .flatMap { result ->
                if (result.isSuccessful) {
                    val graphResult = result as GraphResult
                    fbHelper.fetchSignInMethodsForEmail(graphResult.email!!, graphResult.credential!!)
                } else {
                    Single.just(result)
                }
            }
            .flatMap { result ->
                if (result.isSuccessful) {
                    val graphResult = result as GraphResult
                    fbHelper.signInToFirebase(graphResult.credential!!)
                } else {
                    Single.just(result)
                }
            }
    }

    override fun signUp(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fbHelper.onActivityResult(requestCode, resultCode, data)
    }
}