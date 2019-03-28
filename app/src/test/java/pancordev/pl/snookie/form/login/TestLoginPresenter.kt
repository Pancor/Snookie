package pancordev.pl.snookie.form.login

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pancordev.pl.snookie.utils.auth.AuthManager


class TestLoginPresenter {

    private lateinit var loginPresenter: LoginPresenter

    @Mock
    private lateinit var authManager: AuthManager

    @Mock
    private lateinit var view: LoginContract.View

    private val EMAIL = "email@emial.email"
    private val PASSWD = "dupa1@3"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        loginPresenter = LoginPresenter(authManager)
        loginPresenter.onSetView(view)
    }

    @Test
    fun signInByFormWithSuccessThenCheckIfSucceeded(){

        loginPresenter.signIn(EMAIL, PASSWD)

        verify(view).signedIn()
    }
}