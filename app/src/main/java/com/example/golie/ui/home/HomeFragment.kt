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
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ArrayAdapter<ToDo>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)

        val listView = view.home_allCategoriesListView

        val addCategoryButton = view.home_addCategoryButton

        addCategoryButton.setOnClickListener{

            val navController = findNavController()

        }

        val settingsButton = view.home_settingsButton

        settingsButton.setOnClickListener{
            
        }

        adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            toDoRepository.getAllToDos()
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            var clickedToDo = listView.adapter.getItem(position) as ToDo
            var id = clickedToDo.id

            AlertDialog.Builder(context!!)
                .setTitle("Manage Goal")
                .setMessage("Decide what you want to do with your goal.")
                .setPositiveButton(
                    "Finished"
                ) { dialog, whichButton ->
                    val navController = findNavController()
                    val args = Bundle().apply {
                        putString("categoryName", "today") // TODO: Hämta databas kategorin med detta värde
                    } // Send this to the next navigation object with variables
                    navController.navigate(R.id.nav_category, args)
                }.setNegativeButton(
                    "Failed"
                ) { dialog, whichButton ->
                }.show()
            }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
