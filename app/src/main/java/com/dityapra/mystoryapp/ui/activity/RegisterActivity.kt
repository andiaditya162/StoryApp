package com.dityapra.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dityapra.mystoryapp.databinding.ActivityRegisterBinding
import com.dityapra.mystoryapp.helper.Helper
import com.dityapra.mystoryapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setMyButtonEnable()
        editTextListener()
        buttonListener()
        showLoading()
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
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setMyButtonEnable() {
        binding.btnRegister.isEnabled =
            binding.etEmail.text.toString().isNotEmpty() &&
                    binding.etPassword.text.toString().isNotEmpty() &&
                    binding.etPassword.text.toString().length >= 8 &&
                    Helper.isEmailValid(binding.etEmail.text.toString())
    }

    private fun buttonListener() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            registerViewModel.register(name, email, password, object : Helper.ApiCallbackString {
                override fun onResponse(success: Boolean, message: String) {
                    showAlertDialog(success, message)
                }
            })
        }
    }

    private fun showAlertDialog(param: Boolean, message: String) {
        if (param) {
            AlertDialog.Builder(this).apply {
                setTitle("Information")
                setMessage("Register Success, You can login with your account")
                setPositiveButton("Continue") { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
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
                setMessage("Register Failed, $message")
                setPositiveButton("Continue") { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this) {
            binding.apply {
                if (it) progressBar.visibility = View.VISIBLE
                else progressBar.visibility = View.GONE
            }
        }
    }
}