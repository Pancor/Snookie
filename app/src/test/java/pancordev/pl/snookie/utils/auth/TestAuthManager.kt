package pancordev.pl.snookie.utils.auth

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class TestAuthManager {

    private lateinit var authManager: AuthContract.AuthManager

    @Mock
    private lateinit var fbProvider: AuthContract.Facebook

    @Mock
    private lateinit var snookieProvider: AuthContract.Snookie

    private val EMAIL = "email@email.email"
    private val PASSWD = "dupa1@3"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        authManager = AuthManager(fbProvider, snookieProvider)
    }

    @Test
    fun signInBySnookieWithSuccessThenCheckIfSucceeded() {
        authManager.signInBySnookie(EMAIL, PASSWD)

        verify(snookieProvider).signIn(EMAIL, PASSWD)
    }
}