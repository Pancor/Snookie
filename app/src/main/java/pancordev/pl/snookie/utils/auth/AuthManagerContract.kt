package pancordev.pl.snookie.utils.auth

import io.reactivex.Single
import pancordev.pl.snookie.model.Result


interface AuthManagerContract {

    interface AuthManager {

        fun signInByFacebook(): Single<Result>

        fun signUpByFacebook(): Single<Result>

        fun signInBySnookie(): Single<Result>

        fun signUpBySnookie(): Single<Result>
    }

    interface Provider {

        fun signIn(): Single<Result>

        fun signUp(): Single<Result>
    }
}