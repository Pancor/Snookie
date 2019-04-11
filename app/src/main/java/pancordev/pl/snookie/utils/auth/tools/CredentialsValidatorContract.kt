package pancordev.pl.snookie.utils.auth.tools

import io.reactivex.Single
import pancordev.pl.snookie.model.Result


interface CredentialsValidatorContract {

    fun validateEmail(email: String): Single<Result>

    fun validatePassword(password: String): Single<Result>
}