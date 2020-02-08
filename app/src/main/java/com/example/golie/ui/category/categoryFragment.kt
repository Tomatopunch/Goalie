package com.example.golie.ui.category

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.ui.category.goal.Goal
import com.example.golie.ui.category.goal.goalRepository
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*

class categoryFragment : Fragment() {

    companion object {
        fun newInstance() = categoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: ArrayAdapter<Goal>

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
                goalRepository.getGoals()
            )

        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->

            // TODO: JOSEFIN: De två nedanstående raderna kanske används för att hämta data ur databasen sen. De användes för att skicka med data innan iallafall :)
            var clickedGoal = listView.adapter.getItem(position) as Goal
            var id = clickedGoal.id

            AlertDialog.Builder(context!!)
                .setTitle("Manage Goal")
                .setMessage("Decide what you want to do with your goal.")
                .setPositiveButton(
                    "Finished"
                ) { dialog, whichButton ->
                    view.setBackgroundColor(R.color.green)
                    val navController = findNavController()
                    val args = Bundle().apply {
                        putString("categoryName", "today") // TODO: Hämta databas kategorin med detta värde
                    } // Send this to the next navigation object with variables
                    navController.navigate(R.id.nav_finishedGoal, args)
                }.setNegativeButton(
                    "Failed"
                ) { dialog, whichButton ->
                    view.setBackgroundColor(R.color.red)
                }.setNeutralButton(
                    "Do nothing"
                ){dialog, whichButton ->
                }.show()

            /*val navController = findNavController()
            val args = Bundle() // Send this to the next navigation object with variables
            navController.navigate(R.id.nav_addGoal)*/
        }


            //TODO: Add an alert to decide to check an item off or not.
        return view
    }

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

    override fun onResume() {
        super.onResume()

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
