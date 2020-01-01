package da.chatnew.chatmessenger01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class
MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        register_button_register.setOnClickListener {
            performRegister()       // Регистрация пользователя + проверка на empty
        }
        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            //очистка флагов интент?
            startActivity(intent)
        }
    }


    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in Email/Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity","Email is:" +email)
        Log.d("MainActivity", "Password is: $password")

        //FireBase - Auth (Регистрация нового пользователя)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener // Если авторизация не удалась - возвращаемся
                // else if Successful - Авторизация удалась
                Log.d("MainActivity","User was created! with uid: ${it.result?.user?.uid}") // Был выбор между 1- ? и 2- !! -> После переменных result,user. Поставил ? = сейв? !! = не пустое?
            }
            .addOnFailureListener {
                Log.d("MainActivity","Failed to create user: ${it.message}")
                Toast.makeText(this, "\"Failed to create user: ${it.message}\"", Toast.LENGTH_SHORT).show()
            }
    }
}
