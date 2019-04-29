package pancordev.pl.snookie.utils.auth

import android.content.Intent
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.model.ResultAbs
import pancordev.pl.snookie.utils.auth.models.FbLoginResult
import pancordev.pl.snookie.utils.auth.models.GraphResult


interface AuthContract {

    interface AuthManager {

        fun checkIfUserIsSignedIn(): Single<Result>

        fun signInByFacebook(): Single<ResultAbs>

        fun signUpByFacebook(): Single<Result>

        fun signInBySnookie(email: String, password: String): Single<Result>

        fun signUpBySnookie(): Single<Result>

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    interface Facebook {

        interface Provider {

            fun signIn(): Single<ResultAbs>

            fun signUp(): Single<Result>

            fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        }

        interface Helper {

            fun signInToFacebook(): Single<FbLoginResult>

            fun getFacebookUserEmailAndCredential(accessToken: AccessToken): Single<GraphResult>

            fun fetchSignInMethodsForEmail(email: String, credential: AuthCredential): Single<ResultAbs>

            fun signInToFirebase(credential: AuthCredential): Single<Result>

            fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        }
    }

    interface Snookie {

        fun signIn(email: String, password: String): Single<Result>

        fun signUp(): Single<Result>
    }
}