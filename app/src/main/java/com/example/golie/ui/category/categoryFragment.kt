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
import com.example.golie.MainActivity

import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentToCategory
import com.example.golie.data.documentToFavoriteCateoryId
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val categoryRepository = CategoryRepository()
    lateinit var currentUserId : String

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("ResourceAsColor")
    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        val view = inflater.inflate(R.layout.category_fragment, container, false)


        ///////////// Check if user is logged in or not, otherwise set user id to guest //////////////////


        if(FirebaseAuth.getInstance().currentUser == null){
            currentUserId = "Guest"
        }

        else{
            currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        }


        ///////////// Check if we came here from home or favorite, do stuff accordingly //////////////////


        var currentCategoryId = "-1"

        // WE CAME HERE FROM HOME
        if (arguments != null) {

            // Setting category id
            currentCategoryId = (arguments!!.getString("categoryId"))!!
            displayCategory(currentCategoryId, view, savedInstanceState)

            //Making sure that the favorite button in the navbar is not checked!
            (activity as MainActivity).navView.menu.getItem(1).setChecked(false)
            (activity as MainActivity).navView.menu.getItem(0).setChecked(true)

        }

        //WE CAME HERE FROM FAVORITE BUTTON
        else { //We came here from favorite button!

            categoryRepository.getFavoriteCategoryId(currentUserId)

                .addOnSuccessListener { document ->

                    if (document.exists()) { // Id of favorite was found

                        currentCategoryId = documentToFavoriteCateoryId(document)
                        displayCategory(currentCategoryId, view, savedInstanceState)
                    }

                    else { // Id of favorite was not found
                        val titleTextView = view.category_titleTextView
                        titleTextView.text = "You have no favorite category!"

                    }
                }

                .addOnFailureListener{//We couldnt find the id of the fsvorite category
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

                val titleTextView = view.category_titleTextView
                categoryRepository.getCategoryById(currentUserId, currentCategoryId)
                    .addOnSuccessListener { document ->

                        val category = documentToCategory(document)
                        titleTextView.text = category.name
                    }

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
