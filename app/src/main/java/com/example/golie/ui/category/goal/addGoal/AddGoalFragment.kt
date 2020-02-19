package com.example.golie.ui.category.goal.addGoal

import android.content.ContentValues
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
//import com.example.golie.ui.category.goal.goalRepository
import com.example.golie.ui.category.goal.validateInput
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_goal_fragment.view.*

///////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AddGoalFragment : Fragment() {

    companion object {
        fun newInstance() = AddGoalFragment()
    }

    private lateinit var viewModel: AddGoalViewModel
    private val db = FirebaseFirestore.getInstance()

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true) // This is used with the back button. Can now handle it with onOptionsItemSelected

        val view = inflater!!.inflate(R.layout.add_goal_fragment, container, false)

        val createButton = view.addGoal_CreateGoalButton
        val timeSpan = view.addGoal_timeSpanDate

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        timeSpan.setOnClickListener{

            val dialogFragment = DatePickerFragment()
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

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        createButton.setOnClickListener{

            //Getting input values
            val title = view.addGoal_titleEditText.editableText.toString() // Måste vara editable för att se texten
            val timeSpanText = view.addGoal_timeSpanDate.editableText.toString()
            val reoccurring = view.addGoal_reoccurringCheckBox.isChecked.toString().toBoolean() //TODO: Ingen todo, endast för att uppmärksamma. DETTA ÄR EN BOOL. GÖR OM TILL TEXT SEN FÖR DATABASEN VID BEHOV. Ta bort denna kommentar när den inte behövs längre
            val pointsText = view.addGoal_pointsEditText.editableText.toString()
            val points = pointsText.toInt()

            var invalidInputTextView = view.addGoal_invalidInputText


            //Validating input
            val invalidInput = validateInput(title, pointsText)
            if(invalidInput.isEmpty()){

                //Input is valid, putting it in database

                val goal = Goal(title,timeSpanText,reoccurring, points)
                val refToSpecificCategorySubcollection = db.collection("users/idOfCurrentlyLoggedInUser/categories/idOfCurrentActivity/allCategories")

                refToSpecificCategorySubcollection.add(goal)
                    .addOnSuccessListener { documentReference ->

                        Log.d(ContentValues.TAG, "Successfully added goal with ID: " + documentReference.id + "within subcollection 'allCategories'")

                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding goal", e)
                        e.printStackTrace()

                    }


                val navController = findNavController()
                navController.navigate(R.id.nav_category)
            }
            else {
                    invalidInputTextView.text = invalidInput[0]
            }



        }

        return view
    }

    /*override fun onResume() {
        //TODO: Måste uppdatera view'n så datePickerns värden visas på skärmen

        super.onResume()
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.add_goal_fragment, null)


    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_category)

        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddGoalViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
