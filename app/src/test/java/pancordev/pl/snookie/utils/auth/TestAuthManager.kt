package pancordev.pl.snookie.utils.auth

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result

class TestAuthManager {

    private lateinit var authManager: AuthContract.AuthManager

    private val fbProvider: AuthContract.Facebook.Provider = mockk()
    private val snookieProvider: AuthContract.Snookie = mockk()
    private val anonymousProvider: AuthContract.Anonymous = mockk()
    private val auth: FirebaseAuth = mockk()
    private val user: FirebaseUser = mockk()

    private val EMAIL = "email@email.email"
    private val PASSWD = "dupa1@3"

    @BeforeEach
    fun setup() {
        clearMocks(fbProvider, snookieProvider, auth, user, anonymousProvider)
        authManager = AuthManager(auth, snookieProvider, fbProvider, anonymousProvider)
    }

    @Test
    fun `sign in by snookie with success then check if succeeded`() {
        every { snookieProvider.signIn(EMAIL, PASSWD) } returns Single.just(Result(true, ""))

        authManager.signInBySnookie(EMAIL, PASSWD)

        verify { snookieProvider.signIn(EMAIL, PASSWD) }
    }

    @Test
    fun `sign in by facebook then check if call was made`() {
        every { fbProvider.signIn()} returns Single.just(Result(true, ""))

        authManager.signInByFacebook()

        verify { fbProvider.signIn() }
    }

    @Test
    fun `sign in anonymously then check if call was made`() {
        every { anonymousProvider.signIn()} returns Single.just(Result(true, ""))

        authManager.signInAnonymously()

        verify { anonymousProvider.signIn() }
    }

    @Test
    fun `call onActivityResult then check if call was made`() {
        every { fbProvider.onActivityResult(any(), any(), any()) } just Runs

        authManager.onActivityResult(0, 0, Intent())

        verify { fbProvider.onActivityResult(any(), any(), any())}
    }

    @Test
    fun `check that user is signed in`() {
        every { auth.currentUser } returns user
        val expectedResult = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)

        authManager.checkIfUserIsSignedIn()
            .test()
            .assertValue(expectedResult)
    }

    @Test
    fun `check that user is not signed in`() {
        every { auth.currentUser } returns null
        val expectedResult = Result(isSuccessful = false, code = AuthManager.NOT_SIGNED_IN)

        authManager.checkIfUserIsSignedIn()
            .test()
            .assertValue(expectedResult)
    }
}