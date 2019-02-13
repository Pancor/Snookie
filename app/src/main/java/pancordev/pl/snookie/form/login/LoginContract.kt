package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.base.BasePresenter
import pancordev.pl.snookie.base.BaseView


interface LoginContract {

    interface View: BaseView<Presenter> {

        fun signedIn()
    }

    interface Presenter: BasePresenter<View> {

        fun signIn()
    }
}