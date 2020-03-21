package com.example.golie.ui.category.finishedGoal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.documentToGoal
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.data.repositoryClasses.UserRepository
import kotlinx.android.synthetic.main.finished_goal_fragment.view.*

class FinishedGoalFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var categoryId: String
    private lateinit var goalId: String

    @SuppressLint("SetTextI18n")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.finished_goal_fragment, container, false)
        val goToHomeButton = view.finishedGoal_button
        val goalRepository = GoalRepository()
        val userRepository = UserRepository()
        val arguments = requireArguments()
        val context = requireContext()

        userId = arguments.getString("userId")!!
        categoryId = arguments.getString("categoryId")!!
        goalId = arguments.getString("goalId")!!

        goalRepository.getGoalById(userId, categoryId, goalId)
            .addOnSuccessListener {
                val goal = documentToGoal(it)

                userRepository.getUserById(userId)
                    .addOnSuccessListener {
                        view.finished_goal_progressBar.visibility = View.GONE
                        view.finishedGoal_earnedPointsText.text = "${goal.points}"
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                        view.finished_goal_progressBar.visibility = View.GONE
                    }
            }
            .addOnFailureListener{
                Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.finished_goal_progressBar.visibility = View.GONE
            }


        goToHomeButton.setOnClickListener{

            val navController = findNavController()
            val args = Bundle().apply {
                putString("userId", userId)
                putString("categoryId", categoryId)
            }
            navController.navigate(R.id.nav_category, args)
        }
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_category)

        return true
    }
}


