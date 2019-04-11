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
        @DataPoints("correctEmails")
        val correctEmails = arrayListOf("email@example.com", "email@[123.123.123.123]", "email@subdomain.example.com",
            "firstname.lastname@example.com", "firstname+lastname@example.com", "email@123.123.123.123",
            "1234567890@example.com", "email@example-one.com", "_______@example.com", "email@example.name",
            "email@example.museum", "email@example.co.jp", "firstname-lastname@example.com")

        @JvmField
        @DataPoints("wrongEmails")
        val wrongEmails = arrayListOf("", "plainaddress", "#@%^%#\$@#\$@#.com", "@domain.com ", "email.domain.com",
            "email.@domain.com", "あいうえお@domain.com", "email@domain", "email@-domain.com", "email@domain..com")

        @JvmField
        @DataPoints("correctPasswords")
        val correctPasswords = arrayListOf("fkslAfsn3", "Atests3", "aaaaaaaaA1", "123Ab2")

        @JvmField
        @DataPoints("wrongPasswords")
        val wrongPasswords = arrayListOf("", "Shor1", "nouppercase2", "MissNumber", "NOLOWERCASE3", "111111", "11111FF",
            "6587648956ds")
    }

    @Before
    fun setup() {
        credsValidator = CredentialsValidator()
    }

    @Theory
    fun testCorrectEmailsRegex(@FromDataPoints("correctEmails") email: String) {
        credsValidator.validateEmail(email)
            .test()
            .assertValue { it.isSucceed }
    }

    @Theory
    fun testWrongEmailsRegex(@FromDataPoints("wrongEmails") email: String) {
        credsValidator.validateEmail(email)
            .test()
            .assertValue { !it.isSucceed }
    }

    @Theory
    fun testCorrectPasswordsRegex(@FromDataPoints("correctPasswords") password: String) {
        credsValidator.validatePassword(password)
            .test()
            .assertValue { it.isSucceed }
    }

    @Theory
    fun testWrongPasswordsRegex(@FromDataPoints("wrongPasswords") password: String) {
        credsValidator.validatePassword(password)
            .test()
            .assertValue { !it.isSucceed }
    }
}