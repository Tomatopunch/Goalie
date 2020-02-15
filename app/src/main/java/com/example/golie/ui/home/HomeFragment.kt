package com.example.golie.ui.home

import android.content.ContentValues.TAG
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
import com.example.golie.ToDo
import com.example.golie.toDoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_fragment.view.*

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val db = FirebaseFirestore.getInstance()
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ArrayAdapter<ToDo>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // "view" is now our modifiable fragment
        val view = inflater.inflate(R.layout.home_fragment, container, false)


        //Enabling clicking on plus button to add category

        val addCategoryButton = view.home_addCategoryButton

        addCategoryButton.setOnClickListener {

            val navController = findNavController()
            navController.navigate(nav_addCategory)
        }


        //Enabling clicking on settings button

        val settingsButton = view.home_settingsButton

        settingsButton.setOnClickListener {
            lateinit var navController: NavController
            AlertDialog.Builder(context!!)
                .setTitle("Manage Goal")
                .setMessage("Decide what you want to do with your goal.")
                .setPositiveButton(
                    "select favorite (?)"
                ) { dialog, whichButton ->

                    navController = findNavController()
                    navController.navigate(R.id.nav_chooseFavCategory)

                }.setNegativeButton(
                    "Info"
                ) { dialog, whichButton ->

                    navController = findNavController()
                    navController.navigate(R.id.nav_info)

                }.setNeutralButton(
                    "Logout"
                ) { dialog, whichButton ->

                    //TODO: Direct this to the login page

                }.show()

        }

        // Setting up the list view with all its data and enabling cicking on one list item

        val listView = view.home_allCategoriesListView

        adapter = ArrayAdapter(
            context!!, // Casting our fragment into a context?
            android.R.layout.simple_list_item_1, // Has to do with presentation (we want to display it as a simple_list_item_1)
            android.R.id.text1,
            toDoRepository.getAllToDos() //Here we fetch data!!
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            var clickedToDo = listView.adapter.getItem(position) as ToDo
            var id = clickedToDo.id


            val navController = findNavController()
            val args = Bundle().apply {
                putInt("id", id) // TODO: Hämta databas kategorin med detta värde
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
