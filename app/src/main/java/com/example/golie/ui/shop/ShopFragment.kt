package com.example.golie.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golie.R
import kotlinx.android.synthetic.main.shop_fragment.*
import kotlinx.android.synthetic.main.shop_fragment.view.*

//TODO: Fix so that when you create a reward it gets into the recycler view
//TODO: Your points should be added from the database
//TODO: When you get back here

var alertItemClicked = false
var alertItemBought = false

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

        var checkAlertItemClicked = savedInstanceState?.getBoolean("alertItemClicked")

        if(checkAlertItemClicked != null){
            alertItemClicked = checkAlertItemClicked
        }

        if(alertItemClicked) {
            android.app.AlertDialog.Builder(context)
                .setTitle("Buy")
                .setMessage("Are you sure you want to buy this item?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, whichButton ->
                    alertItemClicked = false
                    alertItemBought = true
                    android.app.AlertDialog.Builder(context!!)
                        .setTitle("That's great!")
                        .setMessage("Your new balance: XXX")
                        .setPositiveButton(
                            "Enjoy your new reward!"
                        ) { dialog, whichButton ->
                            alertItemBought = false
                        }.setOnCancelListener{
                            alertItemBought = false
                        }.show()
                }.setNegativeButton(
                    "No"
                ) {dialog, whichButton ->
                    alertItemClicked = false
                }.setOnCancelListener{
                    alertItemClicked = false
                }.show()
        }

        var checkAlertItemBought = savedInstanceState?.getBoolean("alertItemBought")

        if(checkAlertItemBought != null){
            alertItemBought = checkAlertItemBought
        }

        if(alertItemBought){
            android.app.AlertDialog.Builder(context!!)
                .setTitle("That's great!")
                .setMessage("Your new balance: XXX")
                .setPositiveButton(
                    "Enjoy your new reward!"
                ) { dialog, whichButton ->
                    alertItemBought = false
                }.setOnCancelListener{
                    alertItemBought = false
                }.show()
        }


        view.shop_view.layoutManager = LinearLayoutManager(activity)
        view.shop_view.adapter = ShopAdapter(context!!)
        view.shop_view.adapter?.notifyDataSetChanged()
        view.shop_view.setHasFixedSize(true)

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

    override fun onResume() {
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("alertItemClicked", alertItemClicked)
        outState.putBoolean("alertItemBought", alertItemBought)

    }
}