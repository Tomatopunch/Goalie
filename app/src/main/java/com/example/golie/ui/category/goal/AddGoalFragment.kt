package com.example.golie.ui.category.goal

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.ui.category.categoryFragment
import kotlinx.android.synthetic.main.add_goal_fragment.view.*
import kotlinx.android.synthetic.main.category_fragment.view.*
import java.util.zip.Inflater

class AddGoalFragment : Fragment() {

    companion object {
        fun newInstance() = AddGoalFragment()
    }

    private lateinit var viewModel: AddGoalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.add_goal_fragment, container, false)

        val createButton = view.addGoal_CreateGoalButton
        val timeSpan = view.addGoal_timeSpanDate
        timeSpan.setOnClickListener{
            val dialogFragment = DatePickerFragment()
            dialogFragment.show(activity!!.supportFragmentManager, "FragmentManager")


        }

        createButton.setOnClickListener{
            val title = view.addGoal_titleEditText.editableText.toString() // Måste vara editable för att se texten
            val timeSpan = view.addGoal_timeSpanDate.editableText.toString()
            val reOccurring = view.addGoal_reoccurringCheckBox.isChecked.toString()
            val points = view.addGoal_pointsEditText.editableText.toString().toInt()
            var invalidInput = view.addGoal_invalidInputText

            if(title.isEmpty()){
                invalidInput.text = getString(R.string.addGoal_invalidTitleMinLength)
            }else if(title.length > 30){
                invalidInput.text = getString(R.string.addGoal_invalidTitleMaxLength)
            }

            //TODO: Sätt in alla värden i databasen här

            val navController = findNavController()
            navController.navigate(R.id.navigation_category)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddGoalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
