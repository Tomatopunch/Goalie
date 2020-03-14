package com.example.golie.ui.category.finishedGoal

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.data.documentToGoal
import com.example.golie.data.documentToPoints
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.data.repositoryClasses.PointsRepository
import com.example.golie.ui.category.CategoryFragment
import com.example.golie.ui.category.GoalDialogFragment
import kotlinx.android.synthetic.main.category_fragment.view.*
import kotlinx.android.synthetic.main.finished_goal_fragment.view.*

class FinishedGoalFragment : Fragment() {

    companion object {
        fun newInstance() = FinishedGoalFragment()
    }

    private lateinit var viewModel: FinishedGoalViewModel

    private lateinit var userId: String
    private lateinit var categoryId: String
    private lateinit var goalId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.finished_goal_fragment, container, false)

        val goToHomeButton = view.finishedGoal_button

        val goalRepository = GoalRepository()
        val pointsRepository = PointsRepository()

        val arguments = requireArguments()

        userId = arguments.getString("userId")!!
        categoryId = arguments.getString("categoryId")!!
        goalId = arguments.getString("goalId")!!

        goalRepository.getGoalById(userId, categoryId, goalId)
            .addOnSuccessListener {
                val goal = documentToGoal(it)

                pointsRepository.getPoints(userId)
                    .addOnSuccessListener { document ->
                        view.finished_goal_progressBar.visibility = View.GONE
                        view.finishedGoalTextView.text = "Congratulations!\n\nYou earned ${goal.points} points!"
                    }
                    .addOnFailureListener{
                        view.finished_goal_progressBar.visibility = View.GONE
                        //TODO: print that something went wrong adding points
                    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FinishedGoalViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_category)

        return true
    }

    override fun onStart() {
        super.onStart()

        //adapter.notifyDataSetChanged()

    }
}


