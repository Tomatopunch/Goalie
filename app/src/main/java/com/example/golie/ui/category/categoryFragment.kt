package com.example.golie.ui.category

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.golie.MainActivity

import com.example.golie.R
import com.example.golie.ToDo
import com.example.golie.toDoRepository
import kotlinx.android.synthetic.main.category_fragment.view.*

class categoryFragment : Fragment() {

    companion object {
        fun newInstance() = categoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel

    private lateinit var adapter: ArrayAdapter<ToDo>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainActivity = activity
        val categoryFragment = inflater!!.inflate(R.layout.category_fragment, container, false)
        val listView = categoryFragment.category_listView

        //val listView = getView()?.findViewById<ListView>(R.id.category_listView)

        if (mainActivity != null) {
            adapter = ArrayAdapter(
                mainActivity.applicationContext, // Var den ligger
                android.R.layout.simple_list_item_1, //Förutbestämd layout
                android.R.id.text1,
                toDoRepository.getAllToDos()
            )
        }

        listView.adapter = adapter

        /*listView.setOnItemClickListener{ parent, view, position, id ->

            var clickedToDo = listView.adapter.getItem(position) as ToDo
            var id = clickedToDo.id

            //TODO: Add an alert to decide to check an item off or not.
        }*/

        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        adapter.notifyDataSetChanged()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
