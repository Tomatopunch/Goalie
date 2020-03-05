package com.example.golie.ui.category

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentToCategory
import com.example.golie.data.documentToFavoriteCateoryId
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*

class categoryFragment : Fragment() {

    companion object {
        fun newInstance() = categoryFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: ArrayAdapter<Goal>
    private var activeAlertDialog = false

    private val currentUserId = "josefin"
    private val categoryRepository = CategoryRepository()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("ResourceAsColor")
    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val view = inflater.inflate(R.layout.category_fragment, container, false)


        var currentCategoryId = "-1"

        if (arguments != null) { //We came here from home
            currentCategoryId = (arguments!!.getString("categoryId"))!!
            displayCategory(currentCategoryId, view, savedInstanceState)
        }

        else { //We came here from favorite button!

            categoryRepository.getFavoriteCategoryId(currentUserId)

                .addOnSuccessListener { document ->
                    currentCategoryId = documentToFavoriteCateoryId(document)
                    Log.d("id of fav cat:", currentCategoryId)
                    displayCategory(currentCategoryId, view, savedInstanceState)
                }

                .addOnFailureListener{
                }
        }

        return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @SuppressLint("ResourceAsColor")
    fun displayCategory(currentCategoryId: String, view: View, savedInstanceState: Bundle?) {



        //Fetching all goals from database

        val goalRepository = GoalRepository()
        lateinit var allGoals: MutableList<Goal>

        goalRepository.getAllGoalsWithinCategory(currentUserId, currentCategoryId)
            .addOnSuccessListener { documents ->

                //casting documents into goal objects

                allGoals = documentsToGoals(documents)

                val listView = view.category_listView

                ///////////////////////// RUNTIME CONFIG HANDLER ////////////////////////

                var checkActiveDialog = savedInstanceState?.getBoolean("activeAlertDialog")
                if (checkActiveDialog != null) {
                    activeAlertDialog = checkActiveDialog
                }

                if (activeAlertDialog) {
                    AlertDialog.Builder(context!!)
                        .setTitle("Manage Goal")
                        .setMessage("Decide what you want to do with your goal.")
                        .setPositiveButton(
                            "Finished"
                        ) { dialog, whichButton ->
                            listView.setBackgroundColor(R.color.green)
                            val navController = findNavController()
                            val args = Bundle().apply {
                                putString(
                                    "categoryName",
                                    "today"
                                ) // TODO: Hämta databas kategorin med detta värde
                            } // Send this to the next navigation object with variables
                            activeAlertDialog = false
                            navController.navigate(R.id.nav_finishedGoal, args)
                        }.setNegativeButton(
                            "Failed"
                        ) { dialog, whichButton ->
                            listView.setBackgroundColor(R.color.green)
                            activeAlertDialog = false
                        }.setNeutralButton(
                            "Do nothing"
                        ) { dialog, whichButton ->
                            activeAlertDialog = false
                        }.setOnCancelListener {
                            activeAlertDialog = false
                        }.show()
                }


                //Putting all goals in list view

                adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allGoals
                )

                listView.adapter = adapter

                //Enabling clicking one one list item

                listView.setOnItemClickListener { parent, view, position, _ ->

                    var clickedGoal = listView.adapter.getItem(position) as Goal
                    var goalId = clickedGoal.id
                    activeAlertDialog = true
                    AlertDialog.Builder(context!!)
                        .setTitle("Manage Goal")
                        .setMessage("Decide what you want to do with your goal.")
                        .setPositiveButton(
                            "Finished"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(R.color.green)
                            val navController = findNavController()
                            val args = Bundle().apply {
                                putString(
                                    "goalId",
                                    goalId
                                ) // TODO: Hämta databas kategorin med detta värde
                            } // Send this to the next navigation object with variables
                            activeAlertDialog = false
                            navController.navigate(R.id.nav_finishedGoal, args)
                        }.setNegativeButton(
                            "Failed"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(R.color.red)
                            activeAlertDialog = false
                        }.setNeutralButton(
                            "Do nothing"
                        ) { dialog, whichButton ->
                            activeAlertDialog = false
                        }.setOnCancelListener {
                            activeAlertDialog = false
                        }.show()
                }


                //Setting title

                val userNameTextView = view.category_titleTextView
                categoryRepository.getCategoryById(currentUserId, currentCategoryId)
                    .addOnSuccessListener { document ->
                        val category = documentToCategory(document)
                        Log.d("categoryCheck", "$category")
                        userNameTextView.text = category.name
                    }

            }
            .addOnFailureListener { exception ->
                view.category_titleTextView.text = "Could not find favorite category."
            }


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAdd = category_addButton

        buttonAdd.setOnClickListener {

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onStart() {
        super.onStart()

        if(::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
        else{
            Log.d("State of adapter", "Adapter is not initialized")
        }

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("activeAlertDialog", activeAlertDialog)

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
