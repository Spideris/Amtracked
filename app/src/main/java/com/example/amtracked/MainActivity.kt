package com.example.amtracked

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.amtraker.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
            HomeFragment.newInstance()).commit()

        val buttonHome: Button = findViewById(R.id.home)
        buttonHome.setOnClickListener{
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,
                HomeFragment.newInstance()).commit()
        }
    }
}