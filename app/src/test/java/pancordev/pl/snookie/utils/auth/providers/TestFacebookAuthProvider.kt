package pancordev.pl.snookie.utils.auth.providers

import android.app.Activity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager


class TestFacebookAuthProvider {

    private lateinit var facebookAuthProvider: AuthContract.Facebook

    @Mock
    private lateinit var auth: FirebaseAuth

    @Mock
    private lateinit var loginManager: LoginManager

    @Mock
    private lateinit var callbackManager: CallbackManager

    @Mock
    private lateinit var activity: Activity

    @Mock
    private lateinit var loginResult: LoginResult

    @Mock
    private lateinit var authResult: Task<AuthResult>

    @Mock
    private lateinit var accessToken: AccessToken

    @BeforeEach
    fun initialize() {
        MockitoAnnotations.initMocks(this)
        facebookAuthProvider = FacebookAuthProvider(auth, loginManager, callbackManager, activity)
    }

    @Test
    fun `sign in with success`() {
        callOnSuccessofFacebookCallback()
        `when`(loginResult.accessToken).thenReturn(accessToken)
        callOnCompleteOfAuthResult()

        facebookAuthProvider.signIn()
            .test()
            .assertValue(Result(isSucceed = true, code = AuthManager.SIGN_IN_SUCCEED))
    }

    @Suppress("UNCHECKED_CAST")
    private fun callOnSuccessofFacebookCallback() {
        doAnswer {
            val listener = it.arguments[1] as FacebookCallback<LoginResult>
            listener.onSuccess(loginResult) }
            .`when`(loginManager)
            .registerCallback(ArgumentMatchers.any<CallbackManager>(), ArgumentMatchers.any<FacebookCallback<LoginResult>>())
    }

    @Suppress("UNCHECKED_CAST")
    private fun callOnCompleteOfAuthResult() {
        doAnswer {
            val listener = it.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(authResult)
            authResult  }
            .`when`(authResult)
            .addOnCompleteListener(ArgumentMatchers.any<OnCompleteListener<AuthResult>>())
    }
}