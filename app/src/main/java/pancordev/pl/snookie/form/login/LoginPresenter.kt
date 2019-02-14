package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.di.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LoginPresenter @Inject constructor(): LoginContract.Presenter{

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart(view: LoginContract.View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}