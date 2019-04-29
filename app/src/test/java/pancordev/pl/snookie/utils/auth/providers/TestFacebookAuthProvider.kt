package pancordev.pl.snookie.utils.auth.providers

import android.content.Intent
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import io.mockk.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pancordev.pl.snookie.model.Result
import pancordev.pl.snookie.utils.auth.AuthContract
import pancordev.pl.snookie.utils.auth.models.FbLoginResult
import pancordev.pl.snookie.utils.auth.models.GraphResult

class TestFacebookAuthProvider {

    private lateinit var facebookAuthProvider: AuthContract.Facebook.Provider

    private val fbHelper: AuthContract.Facebook.Helper = mockk()
    private val intent: Intent = mockk()
    private val accessToken: AccessToken = mockk()
    private val credential: AuthCredential = mockk()

    private val EMAIL = "email@email.email"

    @BeforeEach
    fun initialize() {
        clearMocks(fbHelper, intent, accessToken, credential)
        facebookAuthProvider = FacebookAuthProvider(fbHelper)
    }

    @Test
    fun `call onActivityResult then check if onActivityResult of fbHelper is called`() {
        every { fbHelper.onActivityResult(0, 0, intent) } just Runs

        facebookAuthProvider.onActivityResult(0, 0, intent)

        verify(exactly = 1) {
            fbHelper.onActivityResult(0, 0, intent)
        }
    }


    @Test
    fun `sign in with signInToFacebook error then return that error`() {
        val expectedResult = FbLoginResult(isSuccessful = false, code = "")
        every { fbHelper.signInToFacebook() } returns Single.just(expectedResult)

        facebookAuthProvider.signIn()
            .test()
            .assertResult(expectedResult)
    }


    @Test
    fun `sign in with getFacebookUserEmailAndCredential error then return that error`() {
        val expectedResult = GraphResult(isSuccessful = false, code = "")
        val loginFbResult = FbLoginResult(isSuccessful = true, code = "", accessToken = accessToken)
        every { fbHelper.signInToFacebook() } returns Single.just(loginFbResult)
        every { fbHelper.getFacebookUserEmailAndCredential(accessToken) } returns Single.just(expectedResult)

        facebookAuthProvider.signIn()
            .test()
            .assertResult(expectedResult)
    }

    @Test
    fun `sign in with fetchSignInMethodsForEmail error then return that error`() {
        val expectedResult = Result(isSuccessful = false, code = "")
        val loginFbResult = FbLoginResult(isSuccessful = true, code = "", accessToken = accessToken)
        val fbUserResult = GraphResult(isSuccessful = true, code = "", email = EMAIL, credential = credential)
        every { fbHelper.signInToFacebook() } returns Single.just(loginFbResult)
        every { fbHelper.getFacebookUserEmailAndCredential(accessToken) } returns Single.just(fbUserResult)
        every { fbHelper.fetchSignInMethodsForEmail(EMAIL, credential) } returns Single.just(expectedResult)

        facebookAuthProvider.signIn()
            .test()
            .assertResult(expectedResult)
    }

    @Test
    fun `sign in with signInToFirebase error then return that error`() {
        val expectedResult = Result(isSuccessful = false, code = "")
        val loginFbResult = FbLoginResult(isSuccessful = true, code = "", accessToken = accessToken)
        val fbUserResult = GraphResult(isSuccessful = true, code = "", email = EMAIL, credential = credential)
        val fetchResult = GraphResult(isSuccessful = true, code = "", credential = credential)
        every { fbHelper.signInToFacebook() } returns Single.just(loginFbResult)
        every { fbHelper.getFacebookUserEmailAndCredential(accessToken) } returns Single.just(fbUserResult)
        every { fbHelper.fetchSignInMethodsForEmail(EMAIL, credential) } returns Single.just(fetchResult)
        every { fbHelper.signInToFirebase(credential) } returns Single.just(expectedResult)

        facebookAuthProvider.signIn()
            .test()
            .assertResult(expectedResult)
    }
}