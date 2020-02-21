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
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentsToCategories
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.GoalRepository
//import com.example.golie.ui.category.goal.goalRepository
import kotlinx.android.synthetic.main.category_fragment.*
//import kotlinx.android.synthetic.main.category_fragment.view
import kotlinx.android.synthetic.main.category_fragment.view.*

///////////////////////////////////////////////////////////////////////////////////////////////////////////////

class categoryFragment : Fragment() {

    companion object {
        fun newInstance() = categoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter : ArrayAdapter<Goal>
    @SuppressLint("ResourceAsColor")


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.category_fragment, container, false)
        val currentUserId = "josefin"
        val currentCategoryId : String  = (arguments!!.getString("id"))!!

        //Fetching all goals from database

        val goalRepository = GoalRepository()
        lateinit var allGoals : MutableList<Goal>

        goalRepository.getAllGoalsWithinCategory(currentUserId, currentCategoryId)
            .addOnSuccessListener { documents ->

                //casting documents into goal objects

                allGoals = documentsToGoals(documents)


                //Putting all goals in list view

                adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allGoals
                )

                val listView = view.category_listView
                listView.adapter = adapter

                //Enabling clicking one one list item

                listView.setOnItemClickListener{ parent, view, position, _ ->


                    var clickedGoal = listView.adapter.getItem(position) as Goal
                    var goalId = clickedGoal.id

                    AlertDialog.Builder(context!!)
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

        return view
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addGoalButton = category_addButton

        addGoalButton.setOnClickListener {

            // Here we cast main activity to the interface (below) and this is possible because
            // main activity extends this interface
            val navController = findNavController()
            val args = Bundle().apply{
                putString("key", "value")
            } // Send this to the next navigation object
            navController.navigate(R.id.nav_addGoal, args) // Skicka med args - argument

            // Hämta alla argument som skickats med:

            //val def = arguments!!.getString("key")
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onStart() {
        super.onStart()

        if (::adapter.isInitialized) {
            adapter!!.notifyDataSetChanged()
        }
        else{
            Log.d("State of adapter", "The adapter")

        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
