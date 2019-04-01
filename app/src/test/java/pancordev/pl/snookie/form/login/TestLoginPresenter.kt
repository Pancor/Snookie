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
            it.onSuccess(Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED))
        }
        `when`(authManager.signInBySnookie(EMAIL, PASSWD)).thenReturn(response)

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(authManager).signInBySnookie(EMAIL, PASSWD)
        verify(view).signedIn()
    }

    @Test
    fun checkThatUserIsSignedInThenUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED))
        }
        `when`(authManager.checkIfUserIsSignedIn()).thenReturn(response)

        loginPresenter.checkIfUserIsSignedIn()

        verify(authManager).checkIfUserIsSignedIn()
        verify(view).signedIn()
    }

    @Test
    fun checkThatUserIsNotSignedInThenDoNotUpdateUI() {
        val response = Single.create<Result> {
            it.onSuccess(Result(isSucceed = false, code = AuthManager.NOT_SIGNED_IN))
        }
        `when`(authManager.checkIfUserIsSignedIn()).thenReturn(response)

        loginPresenter.checkIfUserIsSignedIn()

        verify(authManager).checkIfUserIsSignedIn()
        verify(view, never()).signedIn()
    }
}