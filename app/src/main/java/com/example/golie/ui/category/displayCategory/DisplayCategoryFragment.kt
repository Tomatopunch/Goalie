package com.example.golie.ui.category

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.ui.category.displayCategory.DisplayCategoryViewModel
import kotlinx.android.synthetic.main.category_fragment.view.*

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class DisplayCategoryFragment : Fragment() {

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        fun newInstance() = DisplayCategoryFragment()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: DisplayCategoryViewModel
    private lateinit var adapter : ArrayAdapter<Goal>
    //private lateinit var view: View
    @SuppressLint("ResourceAsColor")


    val currentUserId = "josefin"

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.category_fragment, container, false)
        return view
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DisplayCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @SuppressLint("ResourceAsColor")
    fun displayCategory (currentCategoryId: String){

        //Fetching all goals from database
        val context = requireContext()
        val goalRepository = GoalRepository()
        lateinit var allGoals : MutableList<Goal>

        goalRepository.getAllGoalsWithinCategory(currentUserId, currentCategoryId)
            .addOnSuccessListener { documents ->

                //casting documents into goal objects

                allGoals = documentsToGoals(documents)


                //Putting all goals in list view

                adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allGoals
                )

                val listView = requireView().category_listView
                listView.adapter = adapter

                //Enabling clicking one one list item

                listView.setOnItemClickListener{ parent, view, position, _ ->


                    var clickedGoal = listView.adapter.getItem(position) as Goal
                    var goalId = clickedGoal.id

                    AlertDialog.Builder(context)
                        .setTitle("Manage Goal")
                        .setMessage("Decide what you want to do with your goal.")
                        .setPositiveButton(
                            "Finished"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(R.color.green)
                            val navController = findNavController()
                            val args = Bundle().apply {
                                putString("goalId", goalId) //goal id is sent to finished goal fragment
                            }
                            navController.navigate(R.id.nav_finishedGoal, args)
                        }.setNegativeButton(
                            "Failed"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(R.color.red)
                        }.setNeutralButton(
                            "Do nothing"
                        ){dialog, whichButton ->
                        }.show()
                }

            }
            .addOnFailureListener { exception ->
                Log.d("Error getting goals: ", exception.toString())
            }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
