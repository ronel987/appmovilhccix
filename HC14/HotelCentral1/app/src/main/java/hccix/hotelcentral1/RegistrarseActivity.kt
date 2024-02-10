package hccix.hotelcentral1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hccix.hotelcentral1.databinding.ActivityRegistrarseBinding
import hccix.hotelcentral1.databinding.ActivitySignInBinding
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

class RegistrarseActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  lateinit var binding: ActivityRegistrarseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistrarseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        binding.signUpButton.setOnClickListener {          //boton Registrarse
            val mEmail=binding.emailEditText.text.toString()
            val mPassword=binding.passwordEditText.text.toString()
            val mRepeatPassword=binding.repeatPasswordEditText.text.toString()
            val passwordRegex= Pattern.compile("^"+
                 "(?=.*[-@#$%^&+=])" +          //al menos 1 caracter especial
                 ".{6,}"  +                       //al menos 6 caracteres
                 "$")
            if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                Toast.makeText(baseContext,"Ingrese email valido.",Toast.LENGTH_LONG).show()
            } else if(mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()){
                Toast.makeText(baseContext,"Ingrese password valido.",Toast.LENGTH_LONG).show()
            } else if (mPassword != mRepeatPassword){
                Toast.makeText(baseContext,"Confirma la contraseña.",Toast.LENGTH_LONG).show()
            } else {
                crearCuenta(mEmail,mPassword)
            }
        }
        binding.backImageView.setOnClickListener {
            val intento= Intent(this,SignInActivity::class.java)
            startActivity(intento)
        }
    }
    private fun crearCuenta(email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task  ->
                if (task.isSuccessful){
                    Toast.makeText(baseContext,"Cuenta de Usuario Creada.",Toast.LENGTH_LONG).show()
                }else {
                    Log.w("TAG","createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_LONG).show()
                }
            }
    }
}