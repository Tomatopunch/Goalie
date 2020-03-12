package com.example.golie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.golie.data.repositoryClasses.UserRepository
import com.example.golie.ui.category.addGoal.AddGoalFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var navView: BottomNavigationView


    /////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting the navView
        navView = findViewById(R.id.nav_view)

        //Disables back button in navbar fragments
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_shop
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)

        //Handling login functionality
        Log.d("==========================google id BEFORE LOGIN", FirebaseAuth.getInstance().currentUser.toString() )

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

        Log.d("=======================================google id AFTER LOGIN", FirebaseAuth.getInstance().currentUser.toString() )


    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("bla", "blaa")

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {  // Successfully signed in!

                //Getting user id for current user and putting it in db
                val userId = FirebaseAuth.getInstance().currentUser!!.uid

                val userRepository = UserRepository()

                userRepository.checkIfUserExists(userId)
                    .addOnSuccessListener { document ->//A user exist with this id
                        Log.d("user document", document.toString())
                        Log.d("onsuccess", userId)
                        if(document == null) {
                            userRepository.createUser(userId)
                        }
                    }
                    .addOnFailureListener{ //No user exists with this id! Proceed to create one!
                        //db error
                        Log.d("failure", userId )
                    }

            }

            else {

                if(response == null){ //User exited login by pressing back button, we want to show login again
                    this.recreate()
                }

               else { //Login actually failed (eg wrong password or username) //do i need to do this???
                    val errors = response!!.getError()!!.getErrorCode()
                }
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    /*override fun onResume() {
        super.onResume()

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


    }*/


    /////////////////////////////////////////////////////////////////////////////////////////////////////


    companion object {

         private const val RC_SIGN_IN = 123
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

}


