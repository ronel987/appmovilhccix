package hccix.hotelcentral1
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hccix.hotelcentral1.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        binding.signInAppCompatButton.setOnClickListener {
            val mEmail=binding.emailEditText.text.toString()
            val mPassword=binding.passwordEditText.text.toString()
            when {
                mEmail.isEmpty() || mPassword.isEmpty() -> {
                    Toast.makeText(baseContext,"Correo/Contraseña Incorrectos", Toast.LENGTH_LONG).show()
                }else -> {
                    SignIn(mEmail,mPassword)    //tratará de ingresar
                }
            }
        }
        //
        binding.signUpTextView.setOnClickListener {        //link hacia Registrarse
            val inte=Intent(this,RegistrarseActivity::class.java)
            startActivity(inte)
        }
    }
   /*
    public override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser   //obtiene el usuario actual
        if(currentUser !=null)
            reload()
    }
    */
    private fun SignIn (email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)     //trata
            .addOnCompleteListener(this) { task  ->
                if(task.isSuccessful){     //si ya está registrado en Firebase
                    //actualiza UI con la info del user
                    Log.d(ContentValues.TAG,"signInWithEmail:success")
                    reload()
                }else {
                    //si falla,muestra un mensaje
                    Log.w(ContentValues.TAG,"signInWithEmail:failure",task.exception)
                    Toast.makeText(baseContext,"Correo/Contraseña incorrectos.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun reload(){
        val intento = Intent (this,MainActivity::class.java)
        this.startActivity(intento)
    }
}