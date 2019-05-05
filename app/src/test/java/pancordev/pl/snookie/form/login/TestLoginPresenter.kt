package pancordev.pl.snookie.form.login

import android.content.Intent
import io.mockk.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidator
import pancordev.pl.snookie.utils.schedulers.BaseSchedulerProvider
import pancordev.pl.snookie.utils.schedulers.TrampolineSchedulerProvider

class TestLoginPresenter {

    private lateinit var loginPresenter: LoginPresenter

    private lateinit var scheduler: BaseSchedulerProvider
    private val authManager: AuthManager = mockk()
    private val view: LoginContract.View = mockk()

    private val EMAIL = "email@emial.email"
    private val PASSWD = "dupa1@3"

    @BeforeEach
    fun setup() {
        clearMocks(authManager, view)
        scheduler = TrampolineSchedulerProvider()
        loginPresenter = LoginPresenter(authManager, scheduler)
        loginPresenter.onSetView(view)
    }

    @Nested
    inner class SignInBySnookie {

        @Test
        fun `with success then check if succeeded`() {
            val response = Single.just(Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.signedIn() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.signedIn()
            }
        }

        @Test
        fun `with wrong email then update UI`() {
            val response = Single.just(Result(isSuccessful = false, code = AuthManager.INVALID_USER_EMAIL))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.wrongCredentials() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.wrongCredentials()
            }
            verify (exactly = 0) { view.signedIn() }
        }

        @Test
        fun `with wrong password then update UI`() {
            val response = Single.just(Result(isSuccessful = false, code = AuthManager.INVALID_PASSWD))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.wrongCredentials() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.wrongCredentials()
            }
            verify (exactly = 0) { view.signedIn() }
        }

        @Test
        fun `with unknown error then update UI`() {
            val response = Single.just(Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.unknownError() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.unknownError()
            }
            verify (exactly = 0) { view.signedIn() }
        }

        @Test
        fun `with really unknown error then update UI`() {
            val response = Single.just(Result(isSuccessful = false, code = "random characters to simulate unknown result code"))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.unknownError() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.unknownError()
            }
            verify (exactly = 0) { view.signedIn() }
        }

        @Test
        fun `with not passed regex for email then return that error`() {
            val response = Single.just(Result(isSuccessful = false, code = CredentialsValidator.WRONG_EMAIL))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.wrongCredentials() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.wrongCredentials()
            }
            verify (exactly = 0) { view.signedIn() }
        }

        @Test
        fun `with not passed regex for password then return that error`() {
            val response = Single.just(Result(isSuccessful = false, code = CredentialsValidator.WRONG_PASSWORD))
            every { authManager.signInBySnookie(EMAIL, PASSWD) } returns response
            every { view.wrongCredentials() } just Runs

            loginPresenter.signIn(EMAIL, PASSWD)

            verify {
                authManager.signInBySnookie(EMAIL, PASSWD)
                view.wrongCredentials()
            }
            verify (exactly = 0) { view.signedIn() }
        }
    }

    @Nested
    inner class SignInByFacebook {
        @Test
        fun `with success then return that success`() {
            val response = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.signedIn() } just Runs

            loginPresenter.signInByFacebook()

            verify {
                authManager.signInByFacebook()
                view.signedIn()
            }
        }

        @Test
        fun `call onActivityResult then check if it was called`() {
            every { authManager.onActivityResult(any(), any(), any()) } just Runs

            loginPresenter.onActivityResult(0, 0, Intent())

            verify { authManager.onActivityResult(any(), any(), any()) }
        }

        @Test
        fun `with unknown error then return that error`() {
            val response = Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.unknownError() } just Runs

            loginPresenter.signInByFacebook()

            verify(exactly = 0) { view.signedIn() }
            verify { view.unknownError() }
        }

        @Test
        fun `with really unknown error then return that error`() {
            val response = Result(isSuccessful = false, code = "really unknown error")
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.unknownError() } just Runs

            loginPresenter.signInByFacebook()

            verify(exactly = 0) { view.signedIn() }
            verify { view.unknownError() }
        }

        @Test
        fun `with cancel then inform view about it`() {
            val response = Result(isSuccessful = false, code = AuthManager.FB_SIGN_IN_CANCELED)
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.facebookSignInCancelled() } just Runs

            loginPresenter.signInByFacebook()

            verify(exactly = 0) { view.signedIn() }
            verify { view.facebookSignInCancelled() }
        }

        @Test
        fun `with error then inform view about it`() {
            val response = Result(isSuccessful = false, code = AuthManager.SIGN_IN_ERROR)
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.signInError() } just Runs

            loginPresenter.signInByFacebook()

            verify(exactly = 0) { view.signedIn() }
            verify { view.signInError() }
        }

        @Test
        fun `with wrong provider then inform view about it`() {
            val response = Result(isSuccessful = false, code = AuthManager.WRONG_PROVIDER)
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.wrongProvider() } just Runs

            loginPresenter.signInByFacebook()

            verify(exactly = 0) { view.signedIn() }
            verify { view.wrongProvider() }
        }

        @Test
        fun `with not granted email permissions then inform view about it`() {
            val response = Result(isSuccessful = false, code = AuthManager.FB_EMAIL_PERMISSIONS_NOT_GRANTED)
            every { authManager.signInByFacebook() } returns Single.just(response)
            every { view.notGrantedFacebookUserEmailPermissions() } just Runs

            loginPresenter.signInByFacebook()

            verify(exactly = 0) { view.signedIn() }
            verify { view.notGrantedFacebookUserEmailPermissions() }
        }
    }

    @Nested
    inner class SignInAnonymously {

        @Test
        fun `with success then inform view about it`() {
            val result = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
            every { authManager.signInAnonymously() } returns Single.just(result)
            every { view.signedIn() } just Runs

            loginPresenter.signInAsAnonymous()

            verify { view.signedIn() }
        }

        @Test
        fun `with unknown error then inform view about it`() {
            val result = Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
            every { authManager.signInAnonymously() } returns Single.just(result)
            every { view.unknownError() } just Runs

            loginPresenter.signInAsAnonymous()

            verify(exactly = 0) { view.signedIn() }
            verify { view.unknownError() }
        }
    }

    @Test
    fun `check that user is signed in then update UI`() {
        val response = Single.just(Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED))
        every { authManager.checkIfUserIsSignedIn() } returns response
        every { view.signedIn() } just Runs

        loginPresenter.checkIfUserIsSignedIn()

        verify {
            authManager.checkIfUserIsSignedIn()
            view.signedIn()
        }
    }

    @Test
    fun `check that user is not signed in then do not update UI`() {
        val response = Single.just(Result(isSuccessful = false, code = AuthManager.NOT_SIGNED_IN))
        every { authManager.checkIfUserIsSignedIn() } returns response

        loginPresenter.checkIfUserIsSignedIn()

        verify { authManager.checkIfUserIsSignedIn() }
        verify (exactly = 0) { view.signedIn() }
    }
}