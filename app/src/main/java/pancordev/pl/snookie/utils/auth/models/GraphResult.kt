package pancordev.pl.snookie.utils.auth.models

import com.google.firebase.auth.AuthCredential
import pancordev.pl.snookie.model.ResultAbs


data class GraphResult(override val isSuccessful: Boolean,
                       override val code: String,
                       val email: String? = null,
                       val credential: AuthCredential? = null): ResultAbs()