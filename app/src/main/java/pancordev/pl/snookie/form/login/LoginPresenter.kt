package pancordev.pl.snookie.form.login

import android.content.Intent
import pancordev.pl.snookie.base.BasePresenter
import pancordev.pl.snookie.di.ActivityScoped
import pancordev.pl.snookie.model.ResultAbs
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
                if (result.isSuccessful) {
                    view.signedIn()
                }
            })
    }

    override fun signInByGoogle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInAsAnonymous() {
        disposable.add(authManager.signInAnonymously()
            .observeOn(scheduler.ui())
            .subscribeOn(scheduler.io())
            .subscribe { result ->
                if (result.isSuccessful) {
                    view.signedIn()
                } else {
                    handleSignInError(result)
                }
            })
    }

    override fun singUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInByFacebook() {
        disposable.add(authManager.signInByFacebook()
            .observeOn(scheduler.ui())
            .subscribeOn(scheduler.io())
            .subscribe { result ->
                if (result.isSuccessful) {
                    view.signedIn()
                } else {
                    handleSignInError(result)
                }
            })
    }

    override fun resolveUserCollision(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn(email: String, password: String) {
        disposable.add(authManager.signInBySnookie(email, password)
            .observeOn(scheduler.ui())
            .subscribeOn(scheduler.io())
            .subscribe { result ->
                if (result.isSuccessful) {
                    view.signedIn()
                } else {
                    handleSignInError(result)
                }
            })
    }

    private fun handleSignInError(result: ResultAbs) {
        when (result.code) {
            AuthManager.INVALID_PASSWD -> { view.wrongCredentials() }
            AuthManager.INVALID_USER_EMAIL -> { view.wrongCredentials() }
            AuthManager.SIGN_IN_ERROR -> { view.signInError() }
            AuthManager.WRONG_PROVIDER -> { view.wrongProvider() }
            AuthManager.FB_SIGN_IN_CANCELED -> { view.facebookSignInCancelled() }
            AuthManager.FB_EMAIL_PERMISSIONS_NOT_GRANTED -> { view.notGrantedFacebookUserEmailPermissions() }
            CredentialsValidator.WRONG_EMAIL -> { view.wrongCredentials() }
            CredentialsValidator.WRONG_PASSWORD -> { view.wrongCredentials() }
            else -> { view.unknownError() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        authManager.onActivityResult(requestCode, resultCode, data)
    }
}