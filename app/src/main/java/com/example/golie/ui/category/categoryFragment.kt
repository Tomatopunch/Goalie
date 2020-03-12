package com.example.golie.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentToCategory
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*

class categoryFragment : Fragment() {

    companion object {
        fun newInstance() = categoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: ArrayAdapter<Goal>
    private var activeAlertDialog = false
    lateinit var allGoals : MutableList<Goal>
    val userId = "josefin"
    lateinit var categoryId: String
    lateinit var goalId: String
    private val goalRepository = GoalRepository()

    lateinit var listView: ListView

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // This is used with the back button. Can now handle it with onOptionsItemSelected

        val view = inflater.inflate(R.layout.category_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val arguments = requireArguments()
        val context = requireContext()
        categoryId = (arguments.getString("categoryId"))!!
        Log.d("categoryId", categoryId)

        //Setting title

        val userNameTextView = view.category_titleTextView
        categoryRepository.getCategoryById(userId, categoryId)
            .addOnSuccessListener {document ->
                val category = documentToCategory(document)
                Log.d("categoryCheck", "$category")
                userNameTextView.text = category.name
                view.category_progressBar.visibility = View.GONE
            }
            .addOnFailureListener{
                view.category_progressBar.visibility = View.GONE
                Log.d("failureListener", "$it")
            }

        //Fetching all goals from database


        goalRepository.getAllGoalsWithinCategory(userId, categoryId)
            .addOnSuccessListener { documents ->

                //casting documents into goal objects

                allGoals = documentsToGoals(documents)


                listView = view.category_listView

                ///////////////////////// RUNTIME CONFIG HANDLER ////////////////////////

                var checkActiveDialog = savedInstanceState?.getBoolean("activeAlertDialog")
                if(checkActiveDialog != null){
                    activeAlertDialog = checkActiveDialog
                }

                if(activeAlertDialog){ // Dialogfragment
                    AlertDialog.Builder(context)
                        .setTitle("Manage Goal")
                        .setMessage("Decide what you want to do with your goal.")
                        .setPositiveButton(
                            "Finished"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(context.getColor( R.color.green))
                            val navController = findNavController()
                            val args = Bundle().apply {
                                putString("categoryName", "today") // TODO: Hämta databas kategorin med detta värde
                            } // Send this to the next navigation object with variables
                            activeAlertDialog = false
                            navController.navigate(R.id.nav_finishedGoal, args)
                        }.setNegativeButton(
                            "Failed"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(context.getColor( R.color.red))
                            activeAlertDialog = false
                        }.setNeutralButton(
                            "Do nothing"
                        ){dialog, whichButton ->
                            activeAlertDialog = false
                        }.setOnCancelListener{
                            activeAlertDialog = false
                        }.show()
                }


                //Putting all goals in list view

                adapter = CategoryAdapter( //TODO: Gör en egen arrayadapter som uppdaterar färgen vid init
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allGoals
                )

                listView.adapter = adapter

                goalRepository.verifyGoalStatus(allGoals, this)

                //Enabling clicking one one list item


                listView.setOnItemClickListener{ parent, view, position, _ ->

                    var clickedGoal = listView.adapter.getItem(position) as Goal
                    goalId = clickedGoal.id

                    val goalDialogFragment = GoalDialogFragment()

                    var args = Bundle().apply{
                        putString("categoryId", categoryId)
                        putString("userId", userId)
                        putString("goalId", goalId)
                        putInt("position", position)


                        //vill skicka med view för att ändra färgen på categoryListView??

                    }

                    goalDialogFragment.arguments = args
                    goalDialogFragment.show(childFragmentManager, "FragmentManager")




                    /*activeAlertDialog = true
                    AlertDialog.Builder(context!!)
                        .setTitle("Manage Goal")
                        .setMessage("Decide what you want to do with your goal.")
                        .setPositiveButton(
                            "Finished"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(context!!.getColor( R.color.green))
                            val navController = findNavController()
                            val args = Bundle().apply {
                                putString("goalId", goalId) // TODO: Hämta databas kategorin med detta värde
                            } // Send this to the next navigation object with variables
                            activeAlertDialog = false
                            navController.navigate(R.id.nav_finishedGoal, args)
                        }.setNegativeButton(
                            "Failed"
                        ) { dialog, whichButton ->
                            view.setBackgroundColor(context!!.getColor( R.color.red))
                            activeAlertDialog = false
                        }.setNeutralButton(
                            "Do nothing"
                        ){dialog, whichButton ->
                            activeAlertDialog = false
                        }.setOnCancelListener{
                            activeAlertDialog = false
                        }.show()*/
                }

                view.category_progressBar.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                view.category_progressBar.visibility = View.GONE
                Log.d("Error getting goals: ", exception.toString())
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAdd = category_addButton

        val categoryId : String  = (requireArguments().getString("categoryId"))!!

        buttonAdd.setOnClickListener {

            // Here we cast main activity to the interface (below) and this is possible because
            // main activity extends this interface
            val navController = findNavController()
            val args = Bundle().apply{
                putString("categoryId", categoryId)
                putString("userId", userId)
            } // Send this to the next navigation object
            navController.navigate(R.id.nav_addGoal, args) // Skicka med args - argument

            // Hämta alla argument som skickats med:

            //val def = arguments!!.getString("key")
        }
    }

    fun deleteGoal(position: Int){

        allGoals.removeAt(position)
        adapter.notifyDataSetChanged()

    }

    fun setBackgroundColor(position: Int, colorId: Int, init: Boolean){
        var listItem: View
        val view = requireView()
        if(init) {
            listItem = listView.adapter.getView(position, null, listView)
        }
        else {
            listItem = listView.getChildAt(position)
        }
        goalRepository.updateColorId(userId, categoryId, allGoals[position].id, colorId)
            .addOnSuccessListener {
                allGoals[position].colorId = colorId
                adapter.notifyDataSetChanged()
                listItem.setBackgroundColor(requireContext().getColor(colorId))
                view.category_progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                view.category_progressBar.visibility = View.GONE
                Log.d("updateColorId", "Error updating colorId in categoryFragment")
            }

    }



    override fun onResume() {
        super.onResume()
        if(::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
        else{
            Log.d("State of adapter", "Adapter is not initialized")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("activeAlertDialog", activeAlertDialog)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_home)

        return true
    }
}
