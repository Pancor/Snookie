package pancordev.pl.snookie.utils.auth.models

import com.facebook.AccessToken
import com.facebook.login.LoginResult
import pancordev.pl.snookie.model.ResultAbs

data class FbLoginResult(override val isSuccessful: Boolean,
                         override val code: String,
                         val accessToken: AccessToken? = null): ResultAbs()