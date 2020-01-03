package da.chatnew.chatmessenger01

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()       // Регистрация пользователя + проверка на empty
        }
        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            //очистка флагов интент?
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity","Button CLCIKED!!!")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0) // Заканчиваем код -> читаем onActivityResult
        }
    }

    var selectedPhotoUri: Uri? = null // Выбранная фотография - из телефона пользователя

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            // Проверяем выбранную картинку в профиле.
            Log.d("RegisterActivity","Photo was selected")

            selectedPhotoUri = data.data // Выбранная фотография - из телефона пользователя

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri) // наша фотка

            selectphoto_imageview_register.setImageBitmap(bitmap) // загружаем фотку выбранную пользователем - в (скаченно-библиотечный) imageview

            selectphoto_button_register.alpha = 0f // Делаем нашу кнопку прозрачной.
        }
    }


    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in Email/Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity","Email is:" +email)
        Log.d("RegisterActivity", "Password is: $password")

        //FireBase - Auth (Регистрация нового пользователя)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener // Если авторизация не удалась - возвращаемся
                // else if Successful - Авторизация удалась
                Log.d("RegisterActivity","User was created! with uid: ${it.result?.user?.uid}") // Был выбор между 1- ? и 2- !! -> После переменных result,user. Поставил ? = сейв? !! = не пустое?

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("RegisterActivity","Failed to create user: ${it.message}")
                Toast.makeText(this, "\"Failed to create user: ${it.message}\"", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage(){
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString() // Создаем рандомную переменную - хранящую в себе уникальный ID (UUID).
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Uspeshno zagruzili photo: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File location: $it")

                    saveUserToFireDatabase(it.toString())
        }
      }
            .addOnFailureListener{
                //do some loggin here
            }
    }

    private fun saveUserToFireDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,username_edittext_register.text.toString(),profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Finally we saved user to  Firebase Data!")
            }
    }

}

class User(val uid: String, val username: String, val profileImageUrl:String)       //Создаем класс для функции - saveUserToFireDatabase
