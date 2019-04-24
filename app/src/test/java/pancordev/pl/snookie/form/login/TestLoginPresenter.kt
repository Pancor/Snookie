package pancordev.pl.snookie.form.login

import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.schedulers.BaseSchedulerProvider
import pancordev.pl.snookie.utils.schedulers.TrampolineSchedulerProvider


class TestLoginPresenter {

    private lateinit var loginPresenter: LoginPresenter

    private lateinit var scheduler: BaseSchedulerProvider

    @Mock
    private lateinit var authManager: AuthManager

    @Mock
    private lateinit var view: LoginContract.View

    private val EMAIL = "email@emial.email"
    private val PASSWD = "dupa1@3"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        scheduler = TrampolineSchedulerProvider()
        loginPresenter = LoginPresenter(authManager, scheduler)
        loginPresenter.onSetView(view)
    }

    @Test
    fun signInByFormWithSuccessThenCheckIfSucceeded() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED))
        }
        `when`(authManager.signInBySnookie(EMAIL, PASSWD)).thenReturn(response)

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(authManager).signInBySnookie(EMAIL, PASSWD)
        verify(view).signedIn()
    }

    @Test
    fun checkThatUserIsSignedInThenUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED))
        }
        `when`(authManager.checkIfUserIsSignedIn()).thenReturn(response)

        loginPresenter.checkIfUserIsSignedIn()

        verify(authManager).checkIfUserIsSignedIn()
        verify(view).signedIn()
    }

    @Test
    fun checkThatUserIsNotSignedInThenDoNotUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = false, code = AuthManager.NOT_SIGNED_IN))
        }
        `when`(authManager.checkIfUserIsSignedIn()).thenReturn(response)

        loginPresenter.checkIfUserIsSignedIn()

        verify(authManager).checkIfUserIsSignedIn()
        verify(view, never()).signedIn()
    }

    @Test
    fun signInBySnookieWithWrongEmailThenUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = false, code = AuthManager.INVALID_USER_EMAIL))
        }
        `when`(authManager.signInBySnookie(EMAIL, PASSWD)).thenReturn(response)

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(authManager).signInBySnookie(EMAIL, PASSWD)
        verify(view, never()).signedIn()
        verify(view).wrongCredentials()
    }

    @Test
    fun signInBySnookieWithWrongPasswordThenUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = false, code = AuthManager.INVALID_PASSWD))
        }
        `when`(authManager.signInBySnookie(EMAIL, PASSWD)).thenReturn(response)

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(authManager).signInBySnookie(EMAIL, PASSWD)
        verify(view, never()).signedIn()
        verify(view).wrongCredentials()
    }

    @Test
    fun signInBySnookieWithUnknownErrorThenUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR))
        }
        `when`(authManager.signInBySnookie(EMAIL, PASSWD)).thenReturn(response)

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(authManager).signInBySnookie(EMAIL, PASSWD)
        verify(view, never()).signedIn()
        verify(view).unknownError()
    }

    @Test
    fun signInBySnookieWithReallyUnknownErrorThenUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSuccessful = false, code = "random characters to simulate unknown result code"))
        }
        `when`(authManager.signInBySnookie(EMAIL, PASSWD)).thenReturn(response)

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(authManager).signInBySnookie(EMAIL, PASSWD)
        verify(view, never()).signedIn()
        verify(view).unknownError()
    }
}