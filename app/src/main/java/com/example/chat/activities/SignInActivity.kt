package com.example.chat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.chat.MainActivity
import com.example.chat.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding;
    private lateinit var mAuth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.buttonSignUp.setOnClickListener {
            onClickSignUp()
        }

        binding.buttonSignIn.setOnClickListener {
            onClickSignIn()
        }
        binding.emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validEmail()
            }
        }
        binding.passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validPassword()
            }
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

    private fun onClickSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun onClickSignIn() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        Log.d(TAG, "These are $email/$password")

        if (email.isNotEmpty() && password.isNotEmpty()) {
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

    private fun validEmail(): String? {
        val emailText = binding.emailInput.text.toString()

        binding.emailInput.doOnTextChanged { text, start, before, count ->
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                binding.emailInputLayout.error = "Invalid Email Address"
            } else {
                binding.emailInputLayout.error = null
            }
        }
        return ""
    }

    private fun validPassword(): String? {
        val password = binding.passwordInput.text.toString()
        return ""
    }

    companion object {
        const val TAG: String = "SIGN_IN_ACTIVITY"
    }
}
