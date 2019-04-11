package pancordev.pl.snookie.utils.auth.tools

import org.junit.Before
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.FromDataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith

@RunWith(Theories::class)
class TestCredentialsValidator {

    private lateinit var credsValidator: CredentialsValidatorContract

    companion object {
        @JvmField
        @DataPoints("wrongEmails")
        val wrongEmails = arrayListOf("", "plainaddress", "#@%^%#\$@#\$@#.com", "@domain.com ", "email.domain.com",
            "email.@domain.com", "あいうえお@domain.com", "email@domain", "email@-domain.com", "email@domain..com")
    }

    @Before
    fun setup() {
        credsValidator = CredentialsValidator()
    }

    @Theory
    fun testWrongEmailsRegex(@FromDataPoints("wrongEmails") email: String) {
        credsValidator.validateEmail(email)
            .test()
            .assertValue { !it.isSucceed }
    }
}