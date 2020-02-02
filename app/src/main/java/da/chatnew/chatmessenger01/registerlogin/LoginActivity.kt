package da.chatnew.chatmessenger01.registerlogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import da.chatnew.chatmessenger01.R
import da.chatnew.chatmessenger01.messages.LatestMessagesActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        verifyUserIsLoggedIn()

        login_button_login.setOnClickListener {
            perfomLogin()
        }
        back_to_register_text_view.setOnClickListener {
            intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun perfomLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста,введите логин и пароль", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                intent = Intent (this, LatestMessagesActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "\"Ошибка в логине пароле${it.message}\"", Toast.LENGTH_SHORT).show()
            }
    }

    private fun verifyUserIsLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {

            val intent = Intent(this, LatestMessagesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK .or (Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
