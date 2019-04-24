package pancordev.pl.snookie.utils.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pancordev.pl.snookie.model.Result


class TestAuthManager {

    private lateinit var authManager: AuthContract.AuthManager

    @Mock
    private lateinit var fbProvider: AuthContract.Facebook

    @Mock
    private lateinit var snookieProvider: AuthContract.Snookie

    @Mock
    private lateinit var auth: FirebaseAuth
    @Mock
    private lateinit var user: FirebaseUser

    private val EMAIL = "email@email.email"
    private val PASSWD = "dupa1@3"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        authManager = AuthManager(auth, snookieProvider, fbProvider)
    }

    @Test
    fun signInBySnookieWithSuccessThenCheckIfSucceeded() {
        authManager.signInBySnookie(EMAIL, PASSWD)

        verify(snookieProvider).signIn(EMAIL, PASSWD)
    }

    @Test
    fun checkThatUserIsSignedIn() {
        `when`(auth.currentUser).thenReturn(user)
        val expectedResult = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)

        authManager.checkIfUserIsSignedIn()
            .test()
            .assertValue(expectedResult)
    }

    @Test
    fun checkThatUserIsNotSignedIn() {
        `when`(auth.currentUser).thenReturn(null)
        val expectedResult = Result(isSuccessful = false, code = AuthManager.NOT_SIGNED_IN)

        authManager.checkIfUserIsSignedIn()
            .test()
            .assertValue(expectedResult)
    }
}