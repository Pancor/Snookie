package pancordev.pl.snookie.utils.auth

import io.reactivex.Single
import pancordev.pl.snookie.model.Result


interface AuthContract {

    interface AuthManager {

        fun signInByFacebook(): Single<Result>

        fun signUpByFacebook(): Single<Result>

        fun signInBySnookie(email: String, password: String): Single<Result>

        fun signUpBySnookie(): Single<Result>
    }

    interface Facebook {

        fun signIn(): Single<Result>

        fun signUp(): Single<Result>
    }

    interface Snookie {

        fun signIn(email: String, password: String): Single<Result>

        fun signUp(): Single<Result>
    }
}