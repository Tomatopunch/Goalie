package com.example.golie.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golie.R
import kotlinx.android.synthetic.main.shop_fragment.*
import kotlinx.android.synthetic.main.shop_fragment.view.*


//FIX so you can click on each item in the recyclerview
//points right now is a string? wait and see how things turn out with the database

class ShopFragment : Fragment() {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private lateinit var viewModel: ShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.shop_fragment, container, false)
        val points = view.shop_balance


        view.shop_view.layoutManager = LinearLayoutManager(activity)
        view.shop_view.adapter = ShopAdapter()

        points.setText("9999")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonCreateReward = shop_floatingActionButton


        buttonCreateReward.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_createReward)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShopViewModel::class.java)
        // TODO: Use the ViewModel
    }
}

//Save alert code for later
/*
AlertDialog.Builder(context!!)
.setTitle("Buy")
.setMessage("Are you sure you want to buy this item?")
.setPositiveButton(
"Yes"
) { _, _->
    AlertDialog.Builder(context!!)
        .setTitle("That's great!")
        .setMessage("Your new balance: XXX")
        .setPositiveButton(
            "Enjoy your new reward!"
        ) { _, _->
        }.show()
}.setNegativeButton(
"No"
) { _, _->
}.show()
*/