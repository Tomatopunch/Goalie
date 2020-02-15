package com.example.golie.ui.shop.reward

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.toDoRepository
import kotlinx.android.synthetic.main.create_reward_fragment.view.*

//Fix validation on these fields, and make the button clickable if the fields are valid
// maybe points should be converted to int?
//display points on the view when created
//figure out how to separate content in a view show more than one thing?? wait for database for this

class CreateReward : Fragment() {

    companion object {
        fun newInstance() = CreateReward()
    }

    private lateinit var viewModel: CreateRewardViewModel
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
            val navController = findNavController()

            toDoRepository.addToDo(titleText.editableText.toString(), "")

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateRewardViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
