package pancordev.pl.snookie.utils.auth.providers

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidator
import pancordev.pl.snookie.utils.auth.tools.CredentialsValidatorContract

class TestSnookieAuthProvider {

    private lateinit var snookieAuth: SnookieAuthProvider

    private val auth: FirebaseAuth = mockk()
    private val credentialsValidator: CredentialsValidatorContract = mockk()
    private val authResult: Task<AuthResult> = mockk()

    private val EMAIL = "email@email.email"
    private val PASSWD = "dupa1@3"
    private val EMAIL_OK = Single.just(Result(isSuccessful = true, code = CredentialsValidator.OK))
    private val PASSWD_OK = Single.just(Result(isSuccessful = true, code = CredentialsValidator.OK))

    @BeforeEach
    fun setup() {
        clearMocks(auth, credentialsValidator, authResult)
        snookieAuth = SnookieAuthProvider(auth, credentialsValidator)
    }

    @Nested
    inner class WhenEmailAndPasswordValidationIsOK {

        @BeforeEach
        fun setupResponses() {
            every { credentialsValidator.validateEmail(EMAIL) } returns EMAIL_OK
            every { credentialsValidator.validatePassword(PASSWD) } returns PASSWD_OK
            every { auth.signInWithEmailAndPassword(EMAIL, PASSWD) } returns authResult
            callOnCompleteOfAuthResult()
        }

        @Test
        fun `sign in with success then check if succeeded`() {
            every { authResult.isSuccessful } returns true
            val expectedResult = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `sign in with unknown error then check if sign in failed`() {
            every { authResult.isSuccessful } returns false
            every { authResult.exception } returns Exception()
            val expectedResult = Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `sign in with unregistered email then check if sign in failed`() {
            every { authResult.isSuccessful } returns false
            val exception = mockk<FirebaseAuthInvalidUserException>()
            every { authResult.exception } returns exception
            val expectedResult = Result(isSuccessful = false, code = AuthManager.INVALID_USER_EMAIL)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `sign in with wrong password then check if sign in failed`() {
            every { authResult.isSuccessful } returns false
            val exception = mockk<FirebaseAuthInvalidCredentialsException>()
            every { authResult.exception } returns exception
            val expectedResult = Result(isSuccessful = false, code = AuthManager.INVALID_PASSWD)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }
    }

    @Nested
    inner class WhenEmailOrPasswordValidationIsWrong {

        private val EMAIL_WRONG = Single.just(Result(isSuccessful = false, code = CredentialsValidator.WRONG_EMAIL))
        private val PASSWD_WRONG = Single.just(Result(isSuccessful = false, code = CredentialsValidator.WRONG_PASSWORD))

        @Test
        fun `sign in with wrong email then return error`() {
            every { credentialsValidator.validateEmail(EMAIL) } returns EMAIL_WRONG
            every { credentialsValidator.validatePassword(PASSWD) } returns PASSWD_OK
            val expectedResult = Result(isSuccessful = false, code = CredentialsValidator.WRONG_EMAIL)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `sign in with wrong password then return error`() {
            every { credentialsValidator.validateEmail(EMAIL) } returns EMAIL_OK
            every { credentialsValidator.validatePassword(PASSWD) } returns PASSWD_WRONG
            val expectedResult = Result(isSuccessful = false, code = CredentialsValidator.WRONG_PASSWORD)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `sign in with wrong email and password then return error`() {
            every { credentialsValidator.validateEmail(EMAIL) } returns EMAIL_WRONG
            every { credentialsValidator.validatePassword(PASSWD) } returns PASSWD_WRONG
            val expectedResult = Result(isSuccessful = false, code = CredentialsValidator.WRONG_EMAIL)

            snookieAuth.signIn(EMAIL, PASSWD)
                .test()
                .assertValue(expectedResult)
        }
    }

    private fun callOnCompleteOfAuthResult() {
        every { authResult.addOnCompleteListener( any()) } answers {
            firstArg<OnCompleteListener<AuthResult>>().onComplete(authResult)
        authResult
        }
    }
}