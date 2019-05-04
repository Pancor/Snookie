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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun serverNotResponding() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unknownError() {
        Toast.makeText(this, "unknown error", Toast.LENGTH_LONG).show()
    }

    override fun facebookSignInCancelled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun notGrantedFacebookUserEmailPermissions() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signInError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wrongProvider() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}