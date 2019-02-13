package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.base.BaseActivity
import javax.inject.Inject


class LoginActivity: BaseActivity(), LoginContract.View {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun signedIn() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}