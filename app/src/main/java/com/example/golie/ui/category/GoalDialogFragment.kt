package com.example.golie.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.documentToGoal
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.data.repositoryClasses.PointsRepository
import com.example.golie.data.repositoryClasses.UserRepository
import com.example.golie.data.userDocumentToPoints
import kotlinx.android.synthetic.main.goaldialog_fragment.view.*

class GoalDialogFragment : DialogFragment() {

    lateinit var userId: String
    lateinit var goalId: String
    lateinit var categoryId: String
    var position = -1
    lateinit var categoryFragment: CategoryFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.goaldialog_fragment, container, false)

        val pointsRepository = PointsRepository()
        val userRepository = UserRepository()
        val goalRepository = GoalRepository()
        val returnButton = view.goalDialog_returnButton
        val deleteButton = view.goalDialog_deleteButton
        val finishedButton = view.goalDialog_finishedGoalButton
        val failedButton = view.goalDialog_failedGoalButton
        val editButton = view.goalDialog_editButton

        categoryFragment = parentFragment as CategoryFragment

        // User changed settings on the phone.
        if(savedInstanceState != null){
            categoryId = savedInstanceState.getString("categoryId")!!
            userId = savedInstanceState.getString("userId")!!
            goalId = savedInstanceState.getString("goalId")!!
            position = savedInstanceState.getInt("position")
        }
        // Came here from Category
        else {
            val arguments = requireArguments()
            categoryId = arguments.getString("categoryId")!!
            userId = arguments.getString("userId")!!
            goalId = arguments.getString("goalId")!!
            position = arguments.getInt("position")
        }

        val goal = categoryFragment.allGoals[position]

        // Check if this goal has been completed already
        if(goal.colorId != -1){
            disableCompletionButtons(view)
        }

        returnButton.setOnClickListener{
            dismiss()
        }

        deleteButton.setOnClickListener{
            view.goalDialog_progressBar.visibility = View.VISIBLE
            goalRepository.deleteGoal(userId, categoryId, goalId)
                .addOnSuccessListener {
                    categoryFragment.deleteGoal(position)

                    val args = Bundle().apply {
                        putString("userId", userId)
                        putString("categoryId", categoryId)
                        putString("goalId", goalId)
                        putInt("position", position)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.nav_category, args)

                    view.goalDialog_progressBar.visibility = View.GONE
                    dismiss()
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                    view.goalDialog_progressBar.visibility = View.GONE
                }
        }

        finishedButton.setOnClickListener{
            view.goalDialog_progressBar.visibility = View.VISIBLE
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
                            categoryFragment.setBackgroundColor(position, R.color.green)
                            view.goalDialog_progressBar.visibility = View.GONE
                        }
                        .addOnFailureListener{
                            Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                            view.goalDialog_progressBar.visibility = View.GONE
                        }
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                    view.goalDialog_progressBar.visibility = View.GONE
                }
        }


        failedButton.setOnClickListener{
            categoryFragment.setBackgroundColor(position, R.color.red)
            dismiss()
        }


        editButton.setOnClickListener{
            view.goalDialog_progressBar.visibility = View.VISIBLE
            goalRepository.getGoalById(userId, categoryId, goalId).addOnSuccessListener { document ->

                val goal = documentToGoal(document)

                val navController = findNavController()

                val args = Bundle().apply{
                    putString("title", goal.title)
                    putString("timeSpan", goal.timeSpan)
                    putInt("points", goal.points)
                    putBoolean("reoccurring", goal.reoccurring)
                    putString("goalId", goal.id)
                    putString("userId", userId)
                    putString("checkIfUpdating", "updating")
                    putString("categoryId", categoryId)
                }

                navController.navigate(R.id.nav_addGoal, args)
                view.goalDialog_progressBar.visibility = View.GONE
                dismiss()

            }.addOnFailureListener{
                Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.goalDialog_progressBar.visibility = View.GONE
            }
        }
        return view
    }

    private fun disableCompletionButtons(view: View){
        view.goalDialog_finishedGoalButton.isEnabled = false
        view.goalDialog_failedGoalButton.isEnabled = false
        view.goalDialog_finishedGoalButton.isClickable = false
        view.goalDialog_failedGoalButton.isClickable = false
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