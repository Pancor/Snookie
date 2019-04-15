package pancordev.pl.snookie.utils.auth

import android.content.Intent
import io.reactivex.Single
import pancordev.pl.snookie.model.Result


interface AuthContract {

    interface AuthManager {

        fun checkIfUserIsSignedIn(): Single<Result>

        fun signInByFacebook(): Single<Result>

        fun signUpByFacebook(): Single<Result>

        fun signInBySnookie(email: String, password: String): Single<Result>

        fun signUpBySnookie(): Single<Result>

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    }

    interface Facebook {

        fun signIn(): Single<Result>

        fun signUp(): Single<Result>

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    }

    interface Snookie {

        fun signIn(email: String, password: String): Single<Result>

        fun signUp(): Single<Result>
    }
}