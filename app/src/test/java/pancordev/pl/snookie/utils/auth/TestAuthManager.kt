package pancordev.pl.snookie.utils.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import pancordev.pl.snookie.model.Result

class TestAuthManager {

    private lateinit var authManager: AuthContract.AuthManager

    private val fbProvider: AuthContract.Facebook.Provider = mockk()
    private val snookieProvider: AuthContract.Snookie = mockk()
    private val auth: FirebaseAuth = mockk()
    private val user: FirebaseUser = mockk()

    private val EMAIL = "email@email.email"
    private val PASSWD = "dupa1@3"

    @Before
    fun setup() {
        authManager = AuthManager(auth, snookieProvider, fbProvider)
    }

    @Test
    fun signInBySnookieWithSuccessThenCheckIfSucceeded() {
        every { snookieProvider.signIn(EMAIL, PASSWD) } returns Single.just(Result(true, ""))

        authManager.signInBySnookie(EMAIL, PASSWD)

        verify { snookieProvider.signIn(EMAIL, PASSWD) }
    }

    @Test
    fun checkThatUserIsSignedIn() {
        every { auth.currentUser } returns user
        val expectedResult = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)

        authManager.checkIfUserIsSignedIn()
            .test()
            .assertValue(expectedResult)
    }

    @Test
    fun checkThatUserIsNotSignedIn() {
        every { auth.currentUser } returns null
        val expectedResult = Result(isSuccessful = false, code = AuthManager.NOT_SIGNED_IN)

        authManager.checkIfUserIsSignedIn()
            .test()
            .assertValue(expectedResult)
    }
}