package da.chatnew.chatmessenger01.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import da.chatnew.chatmessenger01.R
import da.chatnew.chatmessenger01.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("LoginActivity","Attempt login with email/pw : $email /***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    intent = Intent (this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Если авторизация не удалась - вывести ошибку пользователю
                }
        }

        back_to_register_text_view.setOnClickListener {
            intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
