package pancordev.pl.snookie.form.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.act_login.*
import pancordev.pl.snookie.R
import pancordev.pl.snookie.base.BaseActivity
import javax.inject.Inject


class LoginActivity: BaseActivity(), LoginContract.View {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)
        presenter.onSetView(this)

        signInView.setOnClickListener { presenter.signIn(emailView.text.toString(), passwordView.text.toString()) }
        fbSignInView.setOnClickListener { presenter.signInByFacebook() }
        signInAnonymouslyView.setOnClickListener { presenter.signInAsAnonymous() }
    }

    override fun restorePassword() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun signedIn() {
        Toast.makeText(this, "YAY", Toast.LENGTH_LONG).show()
    }

    override fun wrongCredentials() {
        Toast.makeText(this, "wrong credits", Toast.LENGTH_LONG).show()
    }

    override fun noInternetConnection() {
        Toast.makeText(this, "no internet connection", Toast.LENGTH_LONG).show()
    }

    override fun unknownError() {
        Toast.makeText(this, "unknown error", Toast.LENGTH_LONG).show()
    }

    override fun facebookSignInCancelled() {
        Toast.makeText(this, "fb sign in cancelled", Toast.LENGTH_LONG).show()
    }

    override fun notGrantedFacebookUserEmailPermissions() {
        Toast.makeText(this, "fb user not granted email permissions", Toast.LENGTH_LONG).show()
    }

    override fun signInError() {
        Toast.makeText(this, "sign in error", Toast.LENGTH_LONG).show()
    }

    override fun wrongProvider() {
        Toast.makeText(this, "wrong provider", Toast.LENGTH_LONG).show()
    }
}