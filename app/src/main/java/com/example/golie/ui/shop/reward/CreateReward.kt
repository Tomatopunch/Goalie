package com.example.golie.ui.shop.reward

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.toDoRepository
import kotlinx.android.synthetic.main.create_reward_fragment.view.*

//Fix validation on these fields, and make the button clickable if the fields are valid
// maybe points should be converted to int?

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

        val view = inflater!!.inflate(R.layout.create_reward_fragment, container, false)
        val confirmButton = view.create_button

        titleText = view.create_editTitle
        pointContent = view.create_editPoints


        //have some sort of validation here
        /////////////////////////////////

        confirmButton.setOnClickListener {
            val navController = findNavController()

            // probably unnecessary in this case since we don't send anything back to shop
            val args = Bundle().apply {}

            toDoRepository.addToDo(titleText.editableText.toString(), "")
            navController.navigate(R.id.navigation_shop, args)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateRewardViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
