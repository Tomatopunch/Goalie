package com.example.golie.ui.home

import android.content.ContentValues
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.R.id.nav_addCategory
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.dataClasses.Reward
import com.example.golie.data.documentsToCategories
import com.example.golie.data.doucumentToPoints
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.data.repositoryClasses.PointsRepository
import com.example.golie.data.repositoryClasses.RewardRepository
//import com.example.golie.data.repositoryClasses.getAllCategories
import com.example.golie.ui.category.categoryFragment
import kotlinx.android.synthetic.main.home_fragment.view.*

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ArrayAdapter<Category>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val currentUserId = "josefin" //TODO


        //for testing only
        val goalRepository = GoalRepository()
        val rewardRepository = RewardRepository()
        val pointsRepository = PointsRepository()



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Enabling clicking on plus button to add category

        val addCategoryButton = view.home_addCategoryButton

        addCategoryButton.setOnClickListener {

            //adding a category
            val category = Category("this week")
            categoryRepository.createCategory(currentUserId, category)

            //adding a goal to the a new category "this year"
            val goal = Goal("clean room", "2020-02-20", true, 20)
            goalRepository.createGoal(currentUserId, "this year", goal)

            //updating category "this year"
            val newCategory = Category("this is a NEW category!")
            categoryRepository.updateCategory(currentUserId, "this year", newCategory)

            //adding a reward
            val reward = Reward("eat ice chocolate ice creammmmmm", 50)
            rewardRepository.createReward(currentUserId, reward)

            //setting points to 20
            pointsRepository.setPoints(currentUserId,100)

            //getting points

            var points = 0
            pointsRepository.getPoints(currentUserId)
                .addOnSuccessListener { document ->
                    if (document != null) {
                        points = doucumentToPoints(document)
                        Log.d(ContentValues.TAG, "Successfully fetched points, the nr of points was: ${document.data}")

                        Log.d("points??????", points.toString())
                    }

                    else {
                        Log.d(ContentValues.TAG, "Could not find points!")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "An exception was thrown when fetching points! ", exception)
                }






            /*val navController = findNavController()
            navController.navigate(nav_addCategory)*/
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Enabling clicking on settings button

        val settingsButton = view.home_settingsButton

        settingsButton.setOnClickListener {
            lateinit var navController: NavController
            AlertDialog.Builder(context!!)
                .setTitle("Settings")
                .setMessage("What do you want to do?")
                .setPositiveButton(
                    "Select favorite category"
                ) { dialog, whichButton ->

                    navController = findNavController()
                    navController.navigate(R.id.nav_chooseFavCategory)

                }.setNegativeButton(
                    "View info page"
                ) { dialog, whichButton ->

                    navController = findNavController()
                    navController.navigate(R.id.nav_info)

                }.setNeutralButton(
                    "Logout"
                ) { dialog, whichButton ->

                    //TODO: Direct this to the login page

                }.show()

        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Setting up the list view with all its data and enabling cicking on one list item

        val listView = view.home_allCategoriesListView
        lateinit var allCategories : MutableList<Category>

        categoryRepository.getAllCategories(currentUserId)
            .addOnSuccessListener { documents ->
                allCategories = documentsToCategories(documents)
            }
            .addOnFailureListener { exception ->
                Log.d("Error getting categories: ", exception.toString())
            }



        adapter = ArrayAdapter(
            context!!, // Casting our fragment into a context?
            android.R.layout.simple_list_item_1, // Has to do with presentation (we want to display it as a simple_list_item_1)
            android.R.id.text1,
            allCategories
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            var clickedCategory = listView.adapter.getItem(position) as Category
            var categoryId = clickedCategory.id


            val navController = findNavController()
            val args = Bundle().apply {
                putString("id", categoryId)
            }
            navController.navigate(R.id.nav_category, args)
        }


        return view
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
