package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.base.BasePresenter
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

@ActivityScoped
class LoginPresenter @Inject constructor(private val authManager: AuthContract.AuthManager,
                                         private val scheduler: BaseSchedulerProvider)
    : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun checkIfUserIsSignedIn() {
        disposable.add(authManager.checkIfUserIsSignedIn()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe { result ->
                if (result.isSucceed) {
                    view.signedIn()
                }
            })
    }

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
        disposable.add(authManager.signInBySnookie(email, password)
            .observeOn(scheduler.ui())
            .subscribeOn(scheduler.io())
            .subscribe { result ->
                if (result.isSucceed) {
                    view.signedIn()
                }
            })
    }
}