package com.example.golie

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.golie.ui.shop.ShopFragment
import com.example.golie.ui.shop.reward.CreateReward

class MainActivity : AppCompatActivity(), ShopFragment.Interface {

    var newFragment = CreateReward()

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


    override fun theButtonWasClicked() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frameLayout) as CreateReward?

        // We're in two-pane layout. This is for bigger screens.
        if(fragment != null){ }

        else {
            // This is a one-pane layout. This is what we always use.
            val args = Bundle()

            // args.putInt(categoryFragment.ARG_POSITION, position)
            newFragment.arguments = args

            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, newFragment)

            //transaction.addToBackStack(null)

            transaction.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.remove(newFragment)
        transaction.commit()

    }
}