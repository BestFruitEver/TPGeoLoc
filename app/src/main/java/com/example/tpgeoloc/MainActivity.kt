package com.example.tpgeoloc

import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.FirestoreGrpc
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.BtnInscrire).setOnClickListener{
        performRegister()
        }
        findViewById<TextView>(R.id.account_textView).setOnClickListener {
            Log.d ("MainActivity",  "try to show login activity")
            
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performRegister() {
        val email = findViewById<EditText>(R.id.email_edittext).text.toString()
        val pwd = findViewById<EditText>(R.id.password_edittext).text.toString()

        if(email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un email et un mot de passe", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email:" + email)
        Log.d("MainActivity", "Mot de passe: $pwd")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener {
                //si ce n'est successful
                if (!it.isSuccessful) return@addOnCompleteListener

                //sinon si c'est successful
                Log.d("Main","Utilisateur créer avec uid: ${it.result.user.uid}")
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("Main","Echec de la création de l'utilisateur: ${it.message}")
                Toast.makeText(this, "Echec de la création de l'utilisateur: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}