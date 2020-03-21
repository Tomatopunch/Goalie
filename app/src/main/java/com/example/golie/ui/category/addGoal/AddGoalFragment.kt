package com.example.golie.ui.category.addGoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.repositoryClasses.GoalRepository
import kotlinx.android.synthetic.main.add_goal_fragment.view.*

class AddGoalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val arguments = requireArguments()
        val view = inflater.inflate(R.layout.add_goal_fragment, container, false)
        val userId = arguments.getString("userId")!!
        val categoryId : String  = (arguments.getString("categoryId"))!!
        val goalRepository = GoalRepository()
        val createButton = view.addGoal_CreateGoalButton
        val timeSpan = view.addGoal_timeSpanDate

        // Checking if I got here to update
        if(arguments.getString("checkIfUpdating") != null){
            val titleText = arguments.getString("title")
            val timeSpanText = arguments.getString("timeSpan")
            val reoccurring = arguments.getBoolean("reoccurring")
            val points = arguments.getInt("points")
            view.addGoal_titleEditText.setText(titleText)
            view.addGoal_timeSpanDate.setText(timeSpanText)
            view.addGoal_reoccurringCheckBox.isChecked = reoccurring
            view.addGoal_pointsEditText.setText(points.toString())
            view.addGoal_CreateGoalButton.text = getString(R.string.updateGoal)
            view.addGoal_createGoalText.text = getString(R.string.updateGoal)
        }


        timeSpan.setOnClickListener{
            val dialogFragment = DatePickerFragment(it)
            dialogFragment.show(requireActivity().supportFragmentManager, "FragmentManager")
        }

        createButton.setOnClickListener{
            view.addGoal_progressBar.visibility = View.VISIBLE
            val title = view.addGoal_titleEditText.editableText.toString()
            val timeSpanText = view.addGoal_timeSpanDate.editableText.toString()
            val reOccurring = view.addGoal_reoccurringCheckBox.isChecked.toString().toBoolean()
            val pointsText = view.addGoal_pointsEditText.editableText.toString()
            val invalidInputTextView = view.addGoal_invalidInputText


            val invalidInput = validateInput(title, pointsText, context)
            if(invalidInput.isEmpty()){

                val points = pointsText.toInt()
                val goal = Goal(title, timeSpanText, reOccurring, points)

                // Checking if I'm updating or deleting
                if(arguments.getString("checkIfUpdating") != null) {
                    val goalId = arguments.getString("goalId")!!
                    goalRepository.updateGoal(userId, categoryId, goalId, goal)
                        .addOnSuccessListener {
                            view.addGoal_progressBar.visibility = View.GONE
                        }
                        .addOnFailureListener{
                            Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                            view.addGoal_progressBar.visibility = View.GONE
                        }
                }
                else {
                    goalRepository.createGoal(userId, categoryId, goal)
                        .addOnSuccessListener {
                            view.addGoal_progressBar.visibility = View.GONE
                        }
                        .addOnFailureListener{
                            Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                            view.addGoal_progressBar.visibility = View.GONE
                        }
                }

                    val args = Bundle().apply {
                        putString(getString(R.string.categoryId), categoryId)
                    }

                    val navController = findNavController()
                    navController.navigate(R.id.nav_category, args)

            }
            else {
                view.addGoal_progressBar.visibility = View.GONE
                invalidInputTextView.text = invalidInput[0]
            }
        }
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        val arguments = requireArguments()
        val userId = arguments.getString("userId")!!
        val categoryId : String  = (arguments.getString("categoryId"))!!
        val args = Bundle().apply{
            putString(getString(R.string.categoryId), categoryId)
            putString(getString(R.string.userId), userId)
        }

        navController.navigate(R.id.nav_category, args)

        return true
    }
}
