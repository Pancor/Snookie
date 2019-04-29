package pancordev.pl.snookie.form.login

import io.mockk.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthManager
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
        scheduler = TrampolineSchedulerProvider()
        loginPresenter = LoginPresenter(authManager, scheduler)
        loginPresenter.onSetView(view)
    }

    @Test
    fun `sign in by form with success then check if succeeded`() {
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

    @Test
    fun `sign in by snookie with wrong email then update UI`() {
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
    fun `sign in by snookie with wrong password then update UI`() {
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
    fun `sign in by snookie with unknown error then update UI`() {
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
    fun `sign in by snookie with really unknown error then update UI`() {
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
}