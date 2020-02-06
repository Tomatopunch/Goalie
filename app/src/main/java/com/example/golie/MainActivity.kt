package com.example.golie

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.golie.ui.category.goal.AddGoalFragment

class MainActivity : AppCompatActivity() {

    var newFragment = AddGoalFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_category, R.id.navigation_home, R.id.navigation_shop
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    // Sparat för att Linus ska se skillnaden.

    /*override fun theButtonWasClicked() {
        Log.d("test", "händer inget")
        /*val homeFragment = supportFragmentManager.findFragmentById(R.id.addGoalFragment) as AddGoalFragment?

        if(homeFragment != null){
            // We're in two-pane layout. This is for bigger screens.
            Log.d("kaos", "Det blev feeeeeel")
        }

        else{*/
            // This is a one-pane layout. This is what we always use.
            val args = Bundle()
            // args.putInt(categoryFragment.ARG_POSITION, position)
            newFragment.arguments = args

            val transaction = supportFragmentManager.beginTransaction()

            transaction.add(R.id.nav_host_fragment, newFragment)
                transaction.setReorderingAllowed(true)
            transaction.addToBackStack(null)

            transaction.commit()
        //}
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.remove(newFragment)
        transaction.commit()

    }*/

}



//class MainActivity : AppCompatActivity(), categoryFragment.Interface{

//}