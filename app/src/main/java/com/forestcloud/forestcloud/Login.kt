package com.forestcloud.forestcloud

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.forestcloud.forestcloud.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var buttonEntrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.statusBarColor = Color.parseColor("#FFFFFF")

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when {
                email.isEmpty() -> {
                    binding.editEmail.error = "Preencha o E-mail"
                }

                senha.isEmpty() -> {
                    binding.editSenha.error = "Preencher a Senha!"
                }

                !email.contains("@gmail.com") -> {
                    val snackbar = Snackbar.make(it, "E-mail inválido!", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }

                senha.length <= 5 -> {
                    val snackbar = Snackbar.make(it, "A senha precisa ter pelo menos 6 caracteres!",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }

                else -> {
                    login()

                }
            }
        }
        buttonEntrar = findViewById(R.id.btEntrar)
        buttonEntrar.setOnClickListener{
            val intent = Intent(this,Tela_Projetos::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
    }
}