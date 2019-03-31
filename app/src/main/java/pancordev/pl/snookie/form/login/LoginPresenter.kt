package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.base.BasePresenter
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.utils.auth.AuthManagerContract
import pancordev.pl.snookie.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

@ActivityScoped
class LoginPresenter @Inject constructor(val authManager: AuthManagerContract.AuthManager,
                                         val scheduler: BaseSchedulerProvider)
    : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun signInByGoogle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInAsAnonymous() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun singUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInByFacebook() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn(email: String, password: String) {
        view.signedIn()
    }
}