package com.example.golie.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.ToDo
import com.example.golie.toDoRepository
import kotlinx.android.synthetic.main.category_fragment.view.*
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ArrayAdapter<ToDo>

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.category_fragment, container, false) // "view" is now our modifiable fragment


        // Setting up the list view with all its data and enabling cicking on one list item

        val listView = view.home_allCategoriesListView//fetching the list view with id "home_allCategoriesListView"

        adapter = ArrayAdapter(
            context!!, // Casting our fragment into a context?
            android.R.layout.simple_list_item_1, // Has to do with presentation (we want to display it as a simple_list_item_1)
            android.R.id.text1,
            toDoRepository.getAllToDos() //Here we fetch data!!
        )

        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->

            var clickedCategory = listView.adapter.getItem(position) as ToDo
            var id = clickedCategory.id
            //TODO: Here we want to go to the next fragment which is the sepecific category fragment
        }


        //Enabling clicking on add button

        val addButton = view.home_addCategoryButton

        addButton.setOnClickListener{

            val navController = findNavController()
            val args = Bundle()
            navController.navigate(R.id.navigation_addCategory)

        }


        //Returning the view

        return view
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
}
