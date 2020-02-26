package com.example.golie.ui.category.addGoal

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
import com.example.golie.ui.category.addGoal.validateInput
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

        val view = inflater!!.inflate(R.layout.add_goal_fragment, container, false)

        val createButton = view.addGoal_CreateGoalButton
        val timeSpan = view.addGoal_timeSpanDate

        timeSpan.setOnClickListener{


            val dialogFragment = DatePickerFragment(view)
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

            //////////// @@@@@@@@@@ !!! --- IMPORTANT --- !!! @@@@@@@@@@ //////////////////
            //TODO: Ingen todo, endast för att uppmärksamma. DETTA ÄR EN BOOL. GÖR OM TILL TEXT SEN FÖR DATABASEN VID BEHOV. Ta bort denna kommentar när den inte behövs längre
            val reOccurring = view.addGoal_reoccurringCheckBox.isChecked.toString().toBoolean()

            val pointsText = view.addGoal_pointsEditText.editableText.toString()
            var invalidInputTextView = view.addGoal_invalidInputText

            Log.d("checkSpan", "$timeSpanText")



            val invalidInput = validateInput(title, pointsText)
            if(invalidInput.isEmpty()){
                //TODO: Sätt in alla värden i databasen här
                val points = pointsText.toInt()
                //GoalRepository.createGoal(title, timeSpanText, reOccurring, points)

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
