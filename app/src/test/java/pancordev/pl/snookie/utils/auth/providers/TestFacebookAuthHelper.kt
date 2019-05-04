package pancordev.pl.snookie.utils.auth.providers

import android.app.Activity
import android.content.Intent
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.FacebookAuthProvider
import io.mockk.*
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.AuthManager
import pancordev.pl.snookie.utils.auth.models.FbLoginResult
import pancordev.pl.snookie.utils.auth.models.GraphResult
import java.lang.Exception

class TestFacebookAuthHelper {

    private lateinit var fbHelper: AuthContract.Facebook.Helper

    private val auth: FirebaseAuth = mockk()
    private val loginManager: LoginManager = mockk()
    private val callbackManager: CallbackManager = mockk()
    private val activity: Activity = mockk()
    private val accessToken: AccessToken = mockk()

    @BeforeEach
    fun setup() {
        clearMocks(auth, loginManager, callbackManager, activity, accessToken)
        fbHelper = FacebookAuthHelper(auth, loginManager, callbackManager, activity)
    }

    @Nested
    inner class SignInToFacebook {

        private val loginResult: LoginResult = mockk()

        @BeforeEach
        fun setup() {
            clearMocks(loginResult)
        }

        @Test
        fun `call onError then return unknown error`() {
            val expectedResult = FbLoginResult(isSuccessful= false, code = AuthManager.UNKNOWN_ERROR)
            every { loginManager.logInWithReadPermissions(activity, arrayListOf("email")) } just Runs
            every { loginManager.registerCallback(callbackManager, any()) } answers {
                secondArg<FacebookCallback<LoginResult>>().onError(FacebookException())
            }

            fbHelper.signInToFacebook()
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `call onCancel then return that sign in was cancelled`() {
            val expectedResult = FbLoginResult(isSuccessful= false, code = AuthManager.FB_SIGN_IN_CANCELED)
            every { loginManager.logInWithReadPermissions(activity, arrayListOf("email")) } just Runs
            every { loginManager.registerCallback(callbackManager, any()) } answers {
                secondArg<FacebookCallback<LoginResult>>().onCancel()
            }

            fbHelper.signInToFacebook()
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `call onSuccess with granted email permission then return success`() {
            val expectedResult = FbLoginResult(isSuccessful= true, code = AuthManager.SIGN_IN_SUCCEED,
                accessToken = accessToken)
            every { loginManager.logInWithReadPermissions(activity, arrayListOf("email")) } just Runs
            every { loginManager.registerCallback(callbackManager, any()) } answers {
                secondArg<FacebookCallback<LoginResult>>().onSuccess(loginResult)
            }
            every { loginResult.accessToken } returns accessToken
            every { loginResult.recentlyDeniedPermissions.contains("email") } returns false

            fbHelper.signInToFacebook()
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `call onSuccess with not granted email permission then return error`() {
            val expectedResult = FbLoginResult(isSuccessful= false, code = AuthManager.FB_EMAIL_PERMISSIONS_NOT_GRANTED)
            every { loginManager.logInWithReadPermissions(activity, arrayListOf("email")) } just Runs
            every { loginManager.registerCallback(callbackManager, any()) } answers {
                secondArg<FacebookCallback<LoginResult>>().onSuccess(loginResult)
            }
            every { loginResult.recentlyDeniedPermissions.contains("email") } returns true

            fbHelper.signInToFacebook()
                .test()
                .assertValue(expectedResult)
        }
    }

    @Nested
    inner class GetFacebookUserEmailAndCredential {

        private val user: JSONObject = mockk()
        private val request: GraphResponse = mockk()
        private val exception: FacebookRequestError = mockk()
        private val authCredential: AuthCredential = mockk()

        @BeforeEach
        fun setup() {
            clearMocks(user, request, exception, authCredential)
        }

        @Test
        fun `with error then return unknown error`() {
            val expectedResult = GraphResult(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
            mockkStatic(GraphRequest::class)
            every { GraphRequest.newMeRequest(accessToken, any()) } answers {
                secondArg<GraphRequest.GraphJSONObjectCallback>().onCompleted(user, request)
                GraphRequest()
            }
            every { request.error } returns exception

            fbHelper.getFacebookUserEmailAndCredential(accessToken)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with empty email result then return unknown error`() {
            val expectedResult = GraphResult(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR)
            mockkStatic(GraphRequest::class)
            every { GraphRequest.newMeRequest(accessToken, any()) } answers {
                secondArg<GraphRequest.GraphJSONObjectCallback>().onCompleted(user, request)
                GraphRequest()
            }
            every { request.error } returns null
            every { user.has("email") } returns false
            every { user.isNull("email") } returns true

            fbHelper.getFacebookUserEmailAndCredential(accessToken)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with success then return email and credential`() {
            val email = "email@email.email"
            val expectedResult = GraphResult(isSuccessful = true, code = "", email = email, credential = authCredential)
            mockkStatic(GraphRequest::class)
            every { GraphRequest.newMeRequest(accessToken, any()) } answers {
                secondArg<GraphRequest.GraphJSONObjectCallback>().onCompleted(user, request)
                GraphRequest()
            }
            every { request.error } returns null
            every { user.has("email") } returns true
            every { user.isNull("email") } returns false
            every { user.getString("email") } returns email
            every { accessToken.token } returns ""

            mockkStatic(FacebookAuthProvider::class)
            every { FacebookAuthProvider.getCredential(accessToken.token) } returns authCredential

            fbHelper.getFacebookUserEmailAndCredential(accessToken)
                .test()
                .assertValue(expectedResult)
        }
    }

    @Nested
    inner class FetchSignInMethodsForEmail {

        private val fetchResult: Task<SignInMethodQueryResult> = mockk()
        private val authCredential: AuthCredential = mockk()
        private val queryResult: SignInMethodQueryResult = mockk()
        private val credentialException: FirebaseAuthInvalidCredentialsException = mockk()

        private val email = "email@email.email"
        private val provider = "provider"

        @BeforeEach
        fun setup() {
            clearMocks(fetchResult, authCredential, queryResult, credentialException)
        }

        @Test
        fun `with success call and good provider for sign in then return success`() {
            val expectedResult = GraphResult(isSuccessful = true, code = "", credential = authCredential)
            every { auth.fetchSignInMethodsForEmail(email) } returns fetchResult
            every { fetchResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<SignInMethodQueryResult>>().onComplete(fetchResult)
                fetchResult
            }
            every { fetchResult.isSuccessful } returns true
            every { fetchResult.result } returns queryResult
            every { queryResult.signInMethods } returns listOf(provider)
            every { authCredential.provider } returns provider

            fbHelper.fetchSignInMethodsForEmail(email, authCredential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with success call and empty provider for sign in then return success`() {
            val expectedResult = GraphResult(isSuccessful = true, code = "", credential = authCredential)
            every { auth.fetchSignInMethodsForEmail(email) } returns fetchResult
            every { fetchResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<SignInMethodQueryResult>>().onComplete(fetchResult)
                fetchResult
            }
            every { fetchResult.isSuccessful } returns true
            every { fetchResult.result } returns queryResult
            every { queryResult.signInMethods } returns listOf()
            every { authCredential.provider } returns provider

            fbHelper.fetchSignInMethodsForEmail(email, authCredential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with success call and wrong provider for sign in then return error`() {
            val expectedResult = Result(isSuccessful = false, code = AuthManager.WRONG_PROVIDER)
            every { auth.fetchSignInMethodsForEmail(email) } returns fetchResult
            every { fetchResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<SignInMethodQueryResult>>().onComplete(fetchResult)
                fetchResult
            }
            every { fetchResult.isSuccessful } returns true
            every { fetchResult.result } returns queryResult
            every { queryResult.signInMethods } returns listOf("wrong provider")
            every { authCredential.provider } returns provider

            fbHelper.fetchSignInMethodsForEmail(email, authCredential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with invalid credential error then return that error`() {
            val expectedResult = Result(isSuccessful = false, code = AuthManager.SIGN_IN_ERROR)
            every { auth.fetchSignInMethodsForEmail(email) } returns fetchResult
            every { fetchResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<SignInMethodQueryResult>>().onComplete(fetchResult)
                fetchResult
            }
            every { fetchResult.isSuccessful } returns false
            every { fetchResult.exception } returns credentialException

            fbHelper.fetchSignInMethodsForEmail(email, authCredential)
                .test()
                .assertValue(expectedResult)
        }
    }

    @Nested
    inner class SignInToFirebase {

        private val authResult: Task<AuthResult> = mockk()
        private val credential: AuthCredential = mockk()
        private val invalidUserException: FirebaseAuthInvalidUserException = mockk()
        private val invalidCredentialException: FirebaseAuthInvalidCredentialsException = mockk()
        private val collisionException: FirebaseAuthUserCollisionException = mockk()

        @BeforeEach
        fun setup() {
            clearMocks(authResult, credential, invalidUserException, invalidCredentialException, collisionException)
        }

        @Test
        fun `with success then return that success`() {
            val expectedResult = Result(isSuccessful = true, code = AuthManager.SIGN_IN_SUCCEED)
            every { authResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<AuthResult>>().onComplete(authResult)
                authResult
            }
            every { auth.signInWithCredential(credential) } returns authResult
            every { authResult.isSuccessful } returns true

            fbHelper.signInToFirebase(credential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with invalid user error then return that error`() {
            val expectedResult = Result(isSuccessful = false, code = AuthManager.SIGN_IN_ERROR)
            every { authResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<AuthResult>>().onComplete(authResult)
                authResult
            }
            every { auth.signInWithCredential(credential) } returns authResult
            every { authResult.isSuccessful } returns false
            every { authResult.exception } returns invalidUserException

            fbHelper.signInToFirebase(credential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with invalid credential error then return that error`() {
            val expectedResult = Result(isSuccessful = false, code = AuthManager.SIGN_IN_ERROR)
            every { authResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<AuthResult>>().onComplete(authResult)
                authResult
            }
            every { auth.signInWithCredential(credential) } returns authResult
            every { authResult.isSuccessful } returns false
            every { authResult.exception } returns invalidCredentialException

            fbHelper.signInToFirebase(credential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with collision error then return that error`() {
            val expectedResult = Result(isSuccessful = false, code = AuthManager.WRONG_PROVIDER )
            every { authResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<AuthResult>>().onComplete(authResult)
                authResult
            }
            every { auth.signInWithCredential(credential) } returns authResult
            every { authResult.isSuccessful } returns false
            every { authResult.exception } returns collisionException

            fbHelper.signInToFirebase(credential)
                .test()
                .assertValue(expectedResult)
        }

        @Test
        fun `with unknown error then return that error`() {
            val expectedResult = Result(isSuccessful = false, code = AuthManager.UNKNOWN_ERROR )
            every { authResult.addOnCompleteListener(any()) } answers {
                firstArg<OnCompleteListener<AuthResult>>().onComplete(authResult)
                authResult
            }
            every { auth.signInWithCredential(credential) } returns authResult
            every { authResult.isSuccessful } returns false
            every { authResult.exception } returns Exception()

            fbHelper.signInToFirebase(credential)
                .test()
                .assertValue(expectedResult)
        }
    }

    @Test
    fun `call onActivityResult then check if call was made`() {
        every { callbackManager.onActivityResult(any(), any(), any()) } returns true

        fbHelper.onActivityResult(0, 0, Intent())

        verify { callbackManager.onActivityResult(any(), any(), any()) }
    }
}