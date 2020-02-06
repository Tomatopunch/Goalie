package com.example.golie.ui.category

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity

import com.example.golie.R
import com.example.golie.ToDo
import com.example.golie.toDoRepository
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*

class categoryFragment : Fragment() {

    companion object {
        fun newInstance() = categoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: ArrayAdapter<ToDo>

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.category_fragment, container, false)

        val listView = view.category_listView

            adapter = ArrayAdapter(
                context!!,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                toDoRepository.getAllToDos()
            )

        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->

            var clickedToDo = listView.adapter.getItem(position) as ToDo
            var id = clickedToDo.id

            AlertDialog.Builder(context!!)
                .setTitle("Manage Goal")
                .setMessage("Decide what you want to do with your goal.")
                .setPositiveButton(
                    "Finished"
                ) { dialog, whichButton ->
                    val navController = findNavController()
                    val args = Bundle() // Send this to the next navigation object with variables
                    navController.navigate(R.id.navigation_addGoal)

                }.setNegativeButton(
                    "Failed"
                ) { dialog, whichButton ->

                    view.setBackgroundColor(R.color.red)

                }.setNeutralButton(
                    "Do Nothing"
                ){ dialog, whichButton ->



                }.show()

            //TODO: Add an alert to decide to check an item off or not.
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAdd = category_addButton

        buttonAdd.setOnClickListener {

            // Here we cast main activity to the interface (below) and this is possible because
            // main activity extends this interface
            val navController = findNavController()
            val args = Bundle() // Send this to the next navigation object
            navController.navigate(R.id.navigation_addGoal)


            //var intf = context!! as Interface
            //intf.theButtonWasClicked()
        }
    }

    /*public interface  Interface {
        fun theButtonWasClicked()
    }

    override fun onDetach() {
        super.onDetach()



    }*/


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
