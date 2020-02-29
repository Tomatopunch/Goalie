package com.example.golie.ui.shop.reward

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.ui.shop.ShopAdapter
import com.example.golie.ui.shop.ShopFragment
import com.example.golie.ui.shop.ShopItem
import kotlinx.android.synthetic.main.create_reward_fragment.view.*

//TODO: validation on all these fields
//TODO: make the button only clickable if fields are true
//TODO: display points from the view when created

class CreateReward : Fragment() {

    companion object {
        fun newInstance() = CreateReward()
    }

    private lateinit var titleText: EditText
    private lateinit var pointContent: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.create_reward_fragment, container, false)
        val confirmButton = view.create_button

        titleText = view.create_editTitle
        pointContent = view.create_editPoints

        //have some sort of validation here
        /////////////////////////////////

        confirmButton.setOnClickListener {

            val shopItem = ShopAdapter(requireContext())
            shopItem.addItem(titleText.editableText.toString(), pointContent.editableText.toString())

            val navController = findNavController()
            navController.navigate(R.id.nav_shop)
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        val navController = findNavController()
        navController.navigate(R.id.nav_shop)

        return true
    }
}
