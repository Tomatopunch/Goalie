package com.example.golie.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.documentToGoal
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.data.repositoryClasses.PointsRepository
import com.example.golie.data.repositoryClasses.UserRepository
import com.example.golie.data.userDocumentToPoints
import kotlinx.android.synthetic.main.finished_goal_fragment.view.*
import kotlinx.android.synthetic.main.goaldialog_fragment.view.*

class GoalDialogFragment : DialogFragment {

    lateinit var userId: String
    lateinit var goalId: String
    lateinit var categoryId: String
    var position = -1
    lateinit var categoryFragment: CategoryFragment

    constructor()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.goaldialog_fragment, container, false)

        var fragments = requireActivity().supportFragmentManager.fragments
        val pointsRepository = PointsRepository()
        val userRepository = UserRepository()

        categoryFragment = parentFragment as CategoryFragment

        if(savedInstanceState != null){ // Happens if the user changed settings on the phone.
            categoryId = savedInstanceState.getString("categoryId")!!
            userId = savedInstanceState.getString("userId")!!
            goalId = savedInstanceState.getString("goalId")!!
            position = savedInstanceState.getInt("position")
        }
        else {
            var arguments = requireArguments()
            categoryId = arguments.getString("categoryId")!!
            userId = arguments.getString("userId")!!
            goalId = arguments.getString("goalId")!!
            position = arguments.getInt("position")

        }

        val goal = categoryFragment.allGoals[position] // Next item to be displayed on pos deleted

        // Check if this goal has been completed already
        if(goal.colorId != -1){
            disableCompletionButtons(view)
        }

        val goalRepository = GoalRepository()

        val returnButton = view.dialog_returnButton
        val deleteButton = view.dialog_deleteButton
        val finishedButton = view.dialog_finishedGoalButton
        val failedButton = view.dialog_failedGoalButton
        val editButton = view.dialog_editButton

        returnButton.setOnClickListener{
            dismiss()
        }

        deleteButton.setOnClickListener{
            view.goaldialog_progressBar.visibility = View.VISIBLE
            goalRepository.deleteGoal(userId, categoryId, goalId).addOnSuccessListener {
                categoryFragment.deleteGoal(position)
                view.goaldialog_progressBar.visibility = View.GONE
                dismiss()

            }.addOnFailureListener{
                view.goaldialog_progressBar.visibility = View.GONE
                Log.d("deleteError", "Error deleting goal")
            }
        }

        finishedButton.setOnClickListener{
            view.goaldialog_progressBar.visibility = View.VISIBLE
            //TODO: Take the user to a new dialogFragment with an animation.
            //categoryFragment.setBackgroundColor(R.color.red)
            userRepository.getUserById(userId)
                .addOnSuccessListener { document ->
                    val existingPoints = userDocumentToPoints(document)
                    val pointsSum = existingPoints + goal.points
                    pointsRepository.setPoints(userId, pointsSum)
                        .addOnSuccessListener {
                            val args = Bundle().apply{
                                putString("categoryId", categoryId)
                                putString("userId", userId)
                                putString("goalId", goalId)
                            }
                            val navController = findNavController()
                            navController.navigate(R.id.nav_finishedGoal, args)
                            dismiss()
                            categoryFragment.setBackgroundColor(position, R.color.green, false)
                            view.goaldialog_progressBar.visibility = View.GONE
                        }
                        .addOnFailureListener{
                            view.goaldialog_progressBar.visibility = View.GONE
                            // print that something went wrong adding points
                        }
                }
                .addOnFailureListener{
                    view.goaldialog_progressBar.visibility = View.GONE
                    // print that something went wrong adding points
                }

        }

        failedButton.setOnClickListener{
            categoryFragment.setBackgroundColor(position, R.color.red, false)
            dismiss()
        }


        editButton.setOnClickListener{
            view.goaldialog_progressBar.visibility = View.VISIBLE
            goalRepository.getGoalById(userId, categoryId, goalId).addOnSuccessListener { document ->

                val currentGoal = documentToGoal(document)

                val navController = findNavController()

                val args = Bundle().apply{
                    putString("title", currentGoal.title)
                    putString("timeSpan", currentGoal.timeSpan)
                    putInt("points", currentGoal.points)
                    putBoolean("reoccurring", currentGoal.reoccurring)
                    putString("goalId", currentGoal.id)
                    putString("userId", userId)
                    putString("checkIfUpdating", "updating")
                    putString("categoryId", categoryId)
                }

                navController.navigate(R.id.nav_addGoal, args)
                view.goaldialog_progressBar.visibility = View.GONE
                dismiss()
            }.addOnFailureListener{
                Log.d("failureListener", "error")
                view.goaldialog_progressBar.visibility = View.GONE
            }
            //TODO: Fetch data in current goalId
            // pass all of the data to add_goal_fragment
            // add all of that data to the inputFields in add_goal_fragment
        }
        return view
    }

    private fun disableCompletionButtons(view: View){

        view.dialog_finishedGoalButton.isEnabled = false
        view.dialog_failedGoalButton.isEnabled = false
        view.dialog_finishedGoalButton.isClickable = false
        view.dialog_failedGoalButton.isClickable = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.apply {
            putString("userId", userId)
            putString("categoryId", categoryId)
            putString("goalId", goalId)
            putInt("position", position)
        }

    }

}