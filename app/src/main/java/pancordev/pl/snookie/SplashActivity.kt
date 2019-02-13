package pancordev.pl.snookie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pancordev.pl.snookie.form.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LoginActivity::class.java))
    }
}
