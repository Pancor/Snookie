package pancordev.pl.snookie.utils.auth


interface AuthManagerContract {

    interface AuthMaanger {

        fun signInByFacebook()

        fun signUpByFacebook()

        fun signInBySnookie()

        fun signUpBySnookie()
    }

    interface Provider {

        fun signIn()

        fun signUp()
    }
}