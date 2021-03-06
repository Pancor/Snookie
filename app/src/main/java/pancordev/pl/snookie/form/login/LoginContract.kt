package pancordev.pl.snookie.form.login

import android.content.Intent
import pancordev.pl.snookie.base.BasePresenterContract
import pancordev.pl.snookie.base.BaseView


interface LoginContract {

    interface View: BaseView {

        fun signedIn()

        fun wrongCredentials()

        fun noInternetConnection()

        fun unknownError()

        fun signInError()

        fun wrongProvider()

        fun facebookSignInCancelled()

        fun notGrantedFacebookUserEmailPermissions()

        fun restorePassword()
    }

    interface Presenter: BasePresenterContract<View> {

        fun checkIfUserIsSignedIn()

        fun signIn(email: String, password: String)

        fun signInByFacebook()

        fun signInByGoogle()

        fun signInAsAnonymous()

        fun singUp()

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

        fun resolveUserCollision(email: String)
    }
}