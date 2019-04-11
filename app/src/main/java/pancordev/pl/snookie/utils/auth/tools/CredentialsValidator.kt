package pancordev.pl.snookie.utils.auth.tools

import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialsValidator @Inject constructor() : CredentialsValidatorContract {

    companion object {
        const val OK = "OK"
        const val WRONG_EMAIL = "WRONG_EMAIL"
    }

    override fun validateEmail(email: String): Single<Result> {
        return Single.create { emitter ->
            val validEmailAddressRegex = Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'" +
                    "*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[" +
                    "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:" +
                    "[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4]" +
                    "[0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-" +
                    "\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
            val matcher = validEmailAddressRegex.matcher(email)
            val isEmailValid = matcher.find()
            val code = if (isEmailValid) { OK } else { WRONG_EMAIL }
            emitter.onSuccess(Result(isSucceed = isEmailValid, code = code))
        }
    }

    override fun validatePassword(password: String): Single<Result> {
        return Single.create { emitter ->

            emitter.onSuccess(Result(isSucceed = true, code = "OK"))
        }
    }
}