package com.example.chat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.MainActivity
import com.example.chat.R
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth;

    private lateinit var textInputEmail: EditText;
    private lateinit var textInputPassword: EditText;
    private lateinit var buttonSignIn: Button;
    private lateinit var buttonSignUp: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth = FirebaseAuth.getInstance()

        textInputEmail = findViewById(R.id.email_input)
        textInputPassword = findViewById(R.id.password_input)
        buttonSignIn = findViewById(R.id.button_sign_in)
        buttonSignUp = findViewById(R.id.button_sign_up)

        buttonSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        buttonSignIn.setOnClickListener {
            val email = textInputEmail.text.toString()
            val password = textInputPassword.text.toString()

            login(email, password)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideSoftKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    private fun hideSoftKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun login(email: String, password: String) {
        Log.d("[login]", email + password)
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    baseContext,
                    "Authentication successful.",
                    Toast.LENGTH_SHORT,
                ).show()

                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}
