package com.example.golie.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
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
import com.example.golie.data.repositoryClasses.getAllCategories
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
        val currentUserId = "josefin" //TODO

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Enabling clicking on plus button to add category

        val addCategoryButton = view.home_addCategoryButton

        addCategoryButton.setOnClickListener {

            val navController = findNavController()
            navController.navigate(nav_addCategory)
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
        //val allCategories = getAllCategories(currentUserId)


        adapter = ArrayAdapter(
            context!!, // Casting our fragment into a context?
            android.R.layout.simple_list_item_1, // Has to do with presentation (we want to display it as a simple_list_item_1)
            android.R.id.text1,
            getAllCategories(currentUserId)//getAllCategories(currentUserId)
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
