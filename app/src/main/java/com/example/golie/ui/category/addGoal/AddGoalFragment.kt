package com.example.golie.ui.category.addGoal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.repositoryClasses.GoalRepository
import kotlinx.android.synthetic.main.add_goal_fragment.view.*

class AddGoalFragment : Fragment() {

    companion object {
        fun newInstance() = AddGoalFragment()
    }

    private lateinit var viewModel: AddGoalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // This is used with the back button. Can now handle it with onOptionsItemSelected

        val view = inflater.inflate(R.layout.add_goal_fragment, container, false)
        val userId = arguments!!.getString("userId")!!
        val categoryId : String  = (arguments!!.getString("categoryId"))!!
        Log.d("currentCategory", categoryId)
        val goalRepository = GoalRepository()

        val createButton = view.addGoal_CreateGoalButton
        val timeSpan = view.addGoal_timeSpanDate

        if(arguments!!.getString("checkIfUpdating") != null){ // Checking if I got here to update
            val titleText = arguments!!.getString("title")
            val timeSpanText = arguments!!.getString("timeSpan")
            val reoccurring = arguments!!.getBoolean("reoccurring")
            val points = arguments!!.getInt("points")
            view.addGoal_titleEditText.setText(titleText)
            view.addGoal_timeSpanDate.setText(timeSpanText)
            view.addGoal_reoccurringCheckBox.isChecked = reoccurring
            view.addGoal_pointsEditText.setText(points.toString())

            view.addGoal_CreateGoalButton.text = getString(R.string.updateGoal)
            view.addGoal_createGoalText.text = getString(R.string.updateGoal)


        }

        timeSpan.setOnClickListener{


            val dialogFragment = DatePickerFragment(it)
            dialogFragment.show(activity!!.supportFragmentManager, "FragmentManager")
            /*var args = Bundle().apply {
                putString("title", "$title")
                putString("reOccurring", "$reOccurring")
                putString("points", "$points")
                putString("invalidInput", "$invalidInputTextView")
            }
            //val navController = findNavController()

            navController.navigate(R.id.navigation_addGoal, args)
             */

        }

        Log.d("test", "hellooooo")

        createButton.setOnClickListener{
            val title = view.addGoal_titleEditText.editableText.toString() // Måste vara editable för att se texten
            val timeSpanText = view.addGoal_timeSpanDate.editableText.toString()

            val reOccurring = view.addGoal_reoccurringCheckBox.isChecked.toString().toBoolean()

            val pointsText = view.addGoal_pointsEditText.editableText.toString()
            var invalidInputTextView = view.addGoal_invalidInputText

            Log.d("checkSpan", timeSpanText)



            val invalidInput = validateInput(title, pointsText)
            if(invalidInput.isEmpty()){
                //TODO: Sätt in alla värden i databasen här
                val points = pointsText.toInt()
                val goal = Goal(title, timeSpanText, reOccurring, points) // Behöver vi GoalId?
                if(arguments!!.getString("checkIfUpdating") != null) { // Checking if I'm updating or deleting
                    val goalId = arguments!!.getString("goalId")!!
                    goalRepository.updateGoal(userId, categoryId, goalId, goal)
                }
                else {
                    goalRepository.createGoal(userId, categoryId, goal)
                }

                    val args = Bundle().apply {
                        putString("categoryId", categoryId)
                    }

                    val navController = findNavController()
                    navController.navigate(R.id.nav_category, args)

            }
            else {
                    invalidInputTextView.text = invalidInput[0]
            }

        }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        val userId = arguments!!.getString("userId")!!
        val categoryId : String  = (arguments!!.getString("categoryId"))!!
        val args = Bundle().apply{
            putString("categoryId", categoryId)
            putString("userId", userId)
        }

        navController.navigate(R.id.nav_category, args)

        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddGoalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
