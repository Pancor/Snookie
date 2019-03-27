package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.base.BasePresenter
import pancordev.pl.snookie.base.BaseView


interface LoginContract {

    interface View: BaseView<Presenter> {

        fun signedIn()

        fun wrongCredidentials()

        fun noInternetConncection()

        fun serverNotResponding()

        fun unknownError()
    }

    interface Presenter: BasePresenter<View> {

        fun signIn(email: String, password: String)

        fun signInByFacebook()

        fun signInByGoogle()

        fun signInAsAnonymous()

        fun singUp()
    }
}