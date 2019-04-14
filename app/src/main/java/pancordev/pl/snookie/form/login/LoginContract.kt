package pancordev.pl.snookie.form.login

import pancordev.pl.snookie.base.BasePresenterContract
import pancordev.pl.snookie.base.BaseView


interface LoginContract {

    interface View: BaseView {

        fun signedIn()

        fun wrongCredentials()

        fun noInternetConnection()

        fun serverNotResponding()

        fun unknownError()
    }

    interface Presenter: BasePresenterContract<View> {

        fun checkIfUserIsSignedIn()

        fun signIn(email: String, password: String)

        fun signInByFacebook()

        fun signInByGoogle()

        fun signInAsAnonymous()

        fun singUp()

        fun onActivityResult(requestCode: Int, resultCode: Int)
    }
}