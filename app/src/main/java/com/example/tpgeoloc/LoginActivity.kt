package com.example.tpgeoloc

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CpuUsageInfo
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.BtnConnexion).setOnClickListener {
            val email = findViewById<EditText>(R.id.email_edittext_connection).text.toString()
            val pwd = findViewById<EditText>(R.id.password_edittext_connection).text.toString()

            Log.d("Login","Attempt login with email/pw: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwd)
           //         .addOnCompleteListener {  }
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        findViewById<TextView>(R.id.account_textView).setOnClickListener {
            finish()
        }

        executor = ContextCompat.getMainExecutor(this)
       biometricPrompt = BiometricPrompt(this@LoginActivity,executor, object : androidx.biometric.BiometricPrompt.AuthenticationCallback(){
           override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
               super.onAuthenticationError(errorCode, errString)
               Toast.makeText(this,"Erreur d'authentification: $errString", Toast.LENGTH_SHORT).show()
           }

           override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
               super.onAuthenticationSucceeded(result)
               Toast.makeText(this,"Authentification réussi", Toast.LENGTH_SHORT).show()
           }

           override fun onAuthenticationFailed() {
               super.onAuthenticationFailed()
               Toast.makeText(this,"Authentification raté", Toast.LENGTH_SHORT).show()
           }
       })

        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentification Biométrique")
                .setSubtitle("Connectez vous avec votre empreinte")
                .setNegativeButtonText("Utilisez votre login et votre mot de passe")
                .build()

        findViewById<Button>(R.id.BtnBiom).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}