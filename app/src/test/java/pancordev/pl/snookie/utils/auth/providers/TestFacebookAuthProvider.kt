package pancordev.pl.snookie.utils.auth.providers

import android.app.Activity
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import pancordev.pl.snookie.utils.auth.AuthContract


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

    @BeforeEach
    fun initialize() {
        MockitoAnnotations.initMocks(this)
        facebookAuthProvider = FacebookAuthProvider(auth, loginManager, callbackManager, activity)
    }

    @Test
    fun `sa`() {

    }
}