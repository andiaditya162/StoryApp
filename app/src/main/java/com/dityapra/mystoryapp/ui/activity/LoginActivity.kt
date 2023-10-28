package com.dityapra.mystoryapp.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dityapra.mystoryapp.data.model.UserPreference
import com.dityapra.mystoryapp.databinding.ActivityLoginBinding
import com.dityapra.mystoryapp.helper.Helper
import com.dityapra.mystoryapp.MainActivity
import com.dityapra.mystoryapp.ui.viewmodel.LoginViewModel
import com.dityapra.mystoryapp.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setMyButtonEnable()
        editTextListener()
        buttonListener()
        showLoading()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun editTextListener() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.etLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setMyButtonEnable() {
        val resultPass = binding.etPassword.text
        val resultEmail = binding.etEmail.text

        binding.btnLogin.isEnabled = resultPass != null && resultEmail != null &&
                binding.etPassword.text.toString().length >= 6 &&
                Helper.isEmailValid(binding.etEmail.text.toString())
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle("Information")
                setMessage("Login Success")
                setPositiveButton("Continue") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Information")
                setMessage("Login Failed, $message")
                setPositiveButton("Continue") { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()

            }
        }
    }

    private fun buttonListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            loginViewModel.login(email, pass, object : Helper.ApiCallbackString {
                override fun onResponse(success: Boolean,message: String) {
                    showAlertDialog(success, message)
                }
            })
        }
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) {
            binding.apply {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }
    }

}