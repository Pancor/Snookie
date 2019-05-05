package pancordev.pl.snookie.utils.auth.providers

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.model.ResultAbs
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class AnonymousAuthProvider @Inject constructor(private val auth: FirebaseAuth) : AuthContract.Anonymous {

    override fun signIn() = Single.create<ResultAbs> {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                val result = if (task.isSuccessful) {
                    Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
                } else {
                    Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
                }
                it.onSuccess(result)
            }
    }
}