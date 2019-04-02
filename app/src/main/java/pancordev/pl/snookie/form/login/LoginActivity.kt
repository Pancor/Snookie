package pancordev.pl.snookie.form.login

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
    }

    override fun signedIn() {
        Toast.makeText(this, "YAY", Toast.LENGTH_LONG).show()
    }

    override fun wrongCredentials() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun noInternetConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun serverNotResponding() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unknownError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}