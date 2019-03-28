package pancordev.pl.snookie.utils.auth.providers

import io.reactivex.Single
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthManagerContract
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacebookAuthProvider @Inject constructor() : AuthManagerContract.Provider {

    override fun signIn(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUp(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}