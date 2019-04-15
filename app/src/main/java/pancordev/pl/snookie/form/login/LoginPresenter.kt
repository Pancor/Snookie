package pancordev.pl.snookie.form.login

import android.app.Activity
import android.content.Intent
import pancordev.pl.snookie.base.BasePresenter
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidator
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        authManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun signIn(email: String, password: String) {
        disposable.add(authManager.signInBySnookie(email, password)
            .observeOn(scheduler.ui())
            .subscribeOn(scheduler.io())
            .subscribe { result ->
                if (result.isSucceed) {
                    view.signedIn()
                } else {
                    handleSignInError(result)
                }
            })
    }

    private fun handleSignInError(result: Result) {
        when (result.code) {
            AuthManager.INVALID_PASSWD -> { view.wrongCredentials() }
            AuthManager.INVALID_USER_EMAIL -> { view.wrongCredentials() }
            CredentialsValidator.WRONG_EMAIL -> { view.wrongCredentials() }
            CredentialsValidator.WRONG_PASSWORD -> { view.wrongCredentials() }
            else -> { view.unknownError() }
        }
    }
}