package pancordev.pl.snookie.utils.auth.providers

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.ResultAbs
import pancordev.pl.snookie.utils.auth.AuthContract
import javax.inject.Inject

@ActivityScoped
class AnonymousAuthProvider @Inject constructor(private val auth: FirebaseAuth) : AuthContract.Anonymous {

    override fun signIn(): Single<ResultAbs> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}