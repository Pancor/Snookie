package pancordev.pl.snookie.utils.auth.providers

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager


class TestAnonymousAuthProvider {

    private lateinit var anonymousProvider: AuthContract.Anonymous

    private val auth: FirebaseAuth = mockk()
    private val result: Task<AuthResult> = mockk()

    @BeforeEach
    fun setup() {
        clearMocks(auth, result)
        anonymousProvider = AnonymousAuthProvider(auth)
    }

    @Test
    fun `sign in with success then return that success`() {
        val expectedResult = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
        every { auth.signInAnonymously() } returns result
        every { result.isSuccessful } returns true
        every { result.addOnCompleteListener(any()) } answers {
            firstArg<OnCompleteListener<AuthResult>>().onComplete(result)
            result
        }

        anonymousProvider.signIn()
            .test()
            .assertValue(expectedResult)
    }

    @Test
    fun `sign in with unknown error then return that error`() {
        val expectedResult = Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
        every { auth.signInAnonymously() } returns result
        every { result.isSuccessful } returns false
        every { result.addOnCompleteListener(any()) } answers {
            firstArg<OnCompleteListener<AuthResult>>().onComplete(result)
            result
        }

        anonymousProvider.signIn()
            .test()
            .assertValue(expectedResult)
    }
}