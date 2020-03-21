package com.example.golie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.golie.data.repositoryClasses.UserRepository
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navView: BottomNavigationView

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.nav_view)

        //Disables back button in navbar fragments
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_shop, R.id.nav_finishedGoal
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in!
            if (resultCode == Activity.RESULT_OK) {

                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val userRepository = UserRepository()

                userRepository.getUserById(userId)

                    .addOnSuccessListener { document ->

                        // To check if that user exists or not
                        if(!document.exists()) {
                            userRepository.createUserWithData(userId, this)
                        }
                        else {
                            this.recreate()
                        }
                    }

                    .addOnFailureListener{
                        Toast.makeText(this, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    companion object {
         private const val RC_SIGN_IN = 123
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    fun login() {

        if (FirebaseAuth.getInstance().currentUser == null) { // No user is logged in

            // Choose authentication providers
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN)
        }
    }
}