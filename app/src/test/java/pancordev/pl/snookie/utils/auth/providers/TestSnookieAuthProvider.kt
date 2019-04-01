package pancordev.pl.snookie.utils.auth.providers

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthManager

class TestSnookieAuthProvider {

    private lateinit var snookieAuth: SnookieAuthProvider

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var authResult: Task<AuthResult>

    private val EMAIL = "email@email.email"
    private val PASSWD = "dupa1@3"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        snookieAuth = SnookieAuthProvider(auth)
    }

    @Test
    fun signInWithSuccessThenCheckIfSucceeded() {
        `when`(auth.signInWithEmailAndPassword(EMAIL, PASSWD)).thenReturn(authResult)
        `when`(authResult.isSuccessful).thenReturn(true)
        callOnCompleteOfAuthResult()
        val expectedResult = Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED)

        snookieAuth.signIn(EMAIL, PASSWD)
            .test()
            .assertValue(expectedResult)
    }

    @Suppress("UNCHECKED_CAST")
    private fun callOnCompleteOfAuthResult() {
        Mockito.doAnswer {
            val listener = it.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(authResult)
            authResult
        }.`when`(authResult)
            .addOnCompleteListener(ArgumentMatchers.any<OnCompleteListener<AuthResult>>())
    }
}