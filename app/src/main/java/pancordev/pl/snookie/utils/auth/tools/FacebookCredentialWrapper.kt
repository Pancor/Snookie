package pancordev.pl.snookie.utils.auth.tools

import com.google.firebase.auth.FacebookAuthProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacebookCredentialWrapper @Inject constructor(){

    fun getCredential(token: String) = FacebookAuthProvider.getCredential(token)
}