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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity

import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentToCategory
import com.example.golie.data.documentToFavoriteCateoryId
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.finished_goal_fragment.view.*
import kotlinx.android.synthetic.main.goaldialog_fragment.view.*
import kotlinx.android.synthetic.main.shop_boughtdialog_fragment.*

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var adapter: ArrayAdapter<Goal>
    private var activeAlertDialog = false
    lateinit var allGoals : MutableList<Goal>
    lateinit var userId: String
    lateinit var categoryId: String
    lateinit var goalId: String
    private val goalRepository = GoalRepository()
    private val categoryRepository = CategoryRepository()
    lateinit var listView: ListView

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // This is used with the back button. Can now handle it with onOptionsItemSelected

        val view = inflater.inflate(R.layout.category_fragment, container, false)
        val context = requireContext()




        //////////// Check if user is logged in or not, otherwise set user id to guest ////////////

        if(FirebaseAuth.getInstance().currentUser == null){
            userId = "Guest"
        }

        else{
            userId = FirebaseAuth.getInstance().currentUser!!.uid
        }


        //////////// Check if we can here from Home, Favorite, or settings changed ////////////

        // WE CAME HERE FROM HOME
        if(arguments != null){

            // Setting categoryId:

            categoryId = requireArguments().getString("categoryId")!!
            displayCategory(categoryId, view)

            // Making sure that the favorite button in the navbar is not checked!
            (activity as MainActivity).navView.menu.getItem(1).setChecked(false)
            (activity as MainActivity).navView.menu.getItem(0).setChecked(true)
        }

        // WE CAME HERE FROM FAVORITE
        else{
            categoryRepository.getFavoriteCategoryId(userId)

                .addOnSuccessListener { document ->
                    if(document.exists()){
                        categoryId = documentToFavoriteCateoryId(document)
                        displayCategory(categoryId, view) // Bort med savedInstanceState
                    }

                    else{
                        val titleTextView = view.category_titleTextView
                        titleTextView.text = "You have no favorite category!"
                        val deleteButton = view.category_deleteCategoryButton
                        deleteButton.isVisible = false
                    }
                    view.category_progressBar.visibility = View.GONE
                }

                .addOnFailureListener{
                    Log.d("FailureListener", "$it")
                    view.category_progressBar.visibility = View.GONE
                }
        }

    return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addGoalButton = category_addButton

        //val categoryId : String  = (requireArguments().getString("categoryId"))!!
        if(userId == "Guest"){
            addGoalButton.isVisible = false
        }
        else {
            addGoalButton.setOnClickListener {

                // Here we cast main activity to the interface (below) and this is possible because
                // main activity extends this interface
                val navController = findNavController()
                val args = Bundle().apply {
                    putString("categoryId", categoryId)
                    putString("userId", userId)
                } // Send this to the next navigation object
                navController.navigate(R.id.nav_addGoal, args) // Skicka med args - argument
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun displayCategory(currentCategoryId: String, view: View) {

        val context = requireContext()

        //Fetching all goals from database

        val goalRepository = GoalRepository()


        goalRepository.getAllGoalsWithinCategory(userId, currentCategoryId)
            .addOnSuccessListener { documents ->

                //casting documents into goal objects

                allGoals = documentsToGoals(documents)

                listView = view.category_listView

                //Putting all goals in list view

                adapter = CategoryAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allGoals
                )

                listView.adapter = adapter

                //Enabling clicking one one list item
                if(userId != "Guest") {
                    listView.setOnItemClickListener { parent, view, position, _ ->
                        Log.d("show me the way", "${view.category_progressBar}")

                        var clickedGoal = listView.adapter.getItem(position) as Goal
                        goalId = clickedGoal.id

                        val goalDialogFragment = GoalDialogFragment()

                        var args = Bundle().apply {
                            putString("categoryId", categoryId)
                            putString("userId", userId)
                            putString("goalId", goalId)
                            putInt("position", position)
                        }

                        goalDialogFragment.arguments = args
                        goalDialogFragment.show(childFragmentManager, "FragmentManager")

                    }
                }

                //Setting title

                val titleTextView = view.category_titleTextView
                categoryRepository.getCategoryById(userId, currentCategoryId)
                    .addOnSuccessListener { document ->

                        val category = documentToCategory(document)
                        titleTextView.text = category.name
                    }

                //TODO: addOnFailure??


                //Accessing delete category button
                val deleteCategoryButton = view.category_deleteCategoryButton

                //Hiding delete category button if no user is logged in
                if (userId == "Guest") {
                    deleteCategoryButton.isVisible = false
                }

                //Enabling clicking on delete category button
                else {

                    deleteCategoryButton.setOnClickListener {

                        categoryRepository.deleteCategory(
                            userId,
                            currentCategoryId,
                            (activity as MainActivity)
                        )
                            // TODO: Need addOnSuccess for progressionbar. Find a solution :)

                        val navController = findNavController()
                        navController.navigate(R.id.nav_home)
                    }
                }
                view.category_progressBar.visibility = View.GONE
            }
            .addOnFailureListener{
                Log.d("FailureListener", "$it")
                view.category_progressBar.visibility = View.GONE

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_home)

        return true
    }
}
