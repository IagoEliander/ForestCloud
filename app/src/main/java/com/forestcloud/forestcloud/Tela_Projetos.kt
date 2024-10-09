package com.forestcloud.forestcloud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.forestcloud.forestcloud.databinding.ActivityLoginBinding
import com.forestcloud.forestcloud.databinding.ActivityMainBinding
import com.forestcloud.forestcloud.databinding.ActivityTelaProjetosBinding
import com.forestcloud.forestcloud.fragments.InicioFragment
import com.forestcloud.forestcloud.fragments.LocalFragment
import com.forestcloud.forestcloud.fragments.MapsFragment
import com.forestcloud.forestcloud.fragments.MenuFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Tela_Projetos : AppCompatActivity() {
    private lateinit var binding: ActivityTelaProjetosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaProjetosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(InicioFragment())

        binding.apply {
            bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home -> {
                        replaceFragment(InicioFragment())
                        Toast.makeText(this@Tela_Projetos,"Tela Inicial", Toast.LENGTH_SHORT).show()
                    }
                    R.id.local -> {
                        replaceFragment(LocalFragment())
                        Toast.makeText(this@Tela_Projetos,"Tela Local", Toast.LENGTH_SHORT).show()
                    }
                    R.id.menu -> {
                        replaceFragment(MenuFragment())
                        Toast.makeText(this@Tela_Projetos,"Tela Menu", Toast.LENGTH_SHORT).show()
                    }
                    R.id.maps -> {
                        replaceFragment(MapsFragment())
                        Toast.makeText(this@Tela_Projetos,"Tela Mapa", Toast.LENGTH_SHORT).show()  }
                }
                true
            }
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

    }
}