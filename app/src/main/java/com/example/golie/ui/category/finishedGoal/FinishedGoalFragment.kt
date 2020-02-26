package com.example.golie.ui.category.finishedGoal

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import kotlinx.android.synthetic.main.category_fragment.view.*

class FinishedGoalFragment : Fragment() {

    companion object {
        fun newInstance() = FinishedGoalFragment()
    }

    private lateinit var viewModel: FinishedGoalViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.finished_goal_fragment, container, false)

        val listView = view.category_listView

        /*adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            toDoRepository.getAllToDos()
        )*/

        //listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            /*val finalHost = NavHostFragment.create(R.navigation.example_graph)
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host, finalHost)
                .setPrimaryNavigationFragment(finalHost) // equivalent to app:defaultNavHost="true"
                .commit()*/

        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FinishedGoalViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_category)

        return true
    }

    override fun onStart() {
        super.onStart()

        //adapter.notifyDataSetChanged()

    }
}


