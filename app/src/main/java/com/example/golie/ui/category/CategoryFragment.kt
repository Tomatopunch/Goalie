package com.example.golie.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity
import com.example.golie.R
import com.example.golie.data.dataClasses.Goal
import com.example.golie.data.documentToCategory
import com.example.golie.data.documentsToGoals
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
import com.example.golie.data.repositoryClasses.UserRepository
import com.example.golie.data.userDocumentToFavoriteCategoryId
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*
import com.google.firebase.auth.FirebaseAuth

class CategoryFragment : Fragment() {

    private lateinit var adapter: ArrayAdapter<Goal>
    lateinit var allGoals : MutableList<Goal>
    lateinit var userId: String
    lateinit var categoryId: String
    lateinit var goalId: String
    private val goalRepository = GoalRepository()
    private val categoryRepository = CategoryRepository()
    private val userRepository = UserRepository()
    lateinit var listView: ListView

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.category_fragment, container, false)
        val context = requireContext()


        //////////// Check if user is logged in or not, otherwise set user id to guest ////////////

        if(FirebaseAuth.getInstance().currentUser == null){
            userId = getString(R.string.guest)
        }

        else{
            userId = FirebaseAuth.getInstance().currentUser!!.uid
        }

        //////////// Check if we can here from Home or Favorite ////////////

        // WE CAME HERE FROM HOME
        if(arguments != null){

            categoryId = requireArguments().getString("categoryId")!!
            displayCategory(categoryId, view)

            // Making sure that the favorite button in the navbar is not checked!
            (activity as MainActivity).navView.menu.getItem(0).setChecked(false)
            (activity as MainActivity).navView.menu.getItem(1).setChecked(true)
        }

        // WE CAME HERE FROM FAVORITE
        else {
            userRepository.getUserById(userId)
                .addOnSuccessListener { document ->

                    categoryId = userDocumentToFavoriteCategoryId(document)
                    categoryRepository.getCategoryById(userId, categoryId)

                        .addOnSuccessListener { category ->

                            // The id corresponds to an existing category, can go ahead an display it
                            if (category.exists()) {
                                displayCategory(categoryId, view)
                            }

                            // The id was found but there is no category with that id any more!
                            else {
                                val titleTextView = view.category_titleText
                                titleTextView.text = getString(R.string.categoryFragment_deleted)
                                val deleteButton = view.category_deleteCategoryButton
                                deleteButton.isVisible = false
                            }
                        }

                        .addOnFailureListener{
                            Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                            view.category_progressBar.visibility = View.GONE
                        }

                    view.category_progressBar.visibility = View.GONE
                }

                .addOnFailureListener{
                    Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                    view.category_progressBar.visibility = View.GONE
                }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addGoalButton = category_addButton

        if(userId == getString(R.string.guest)){
            addGoalButton.isVisible = false
        }
        else {
            addGoalButton.setOnClickListener {

                val navController = findNavController()
                val args = Bundle().apply {
                    putString("categoryId", categoryId)
                    putString("userId", userId)
                }
                navController.navigate(R.id.nav_addGoal, args)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun displayCategory(categoryId: String, view: View) {

        val context = requireContext()
        val goalRepository = GoalRepository()


        goalRepository.getAllGoalsWithinCategory(userId, categoryId)
            .addOnSuccessListener { documents ->

                allGoals = documentsToGoals(documents)
                listView = view.category_listView

                adapter = CategoryAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allGoals
                )

                listView.adapter = adapter

                if(userId != getString(R.string.guest)) {
                    listView.setOnItemClickListener { parent, view, position, _ ->

                        val clickedGoal = listView.adapter.getItem(position) as Goal
                        goalId = clickedGoal.id
                        val goalDialogFragment = GoalDialogFragment()

                        val args = Bundle().apply {
                            putString("categoryId", categoryId)
                            putString("userId", userId)
                            putString("goalId", goalId)
                            putInt("position", position)
                        }

                        goalDialogFragment.arguments = args
                        goalDialogFragment.show(childFragmentManager, "FragmentManager")
                    }
                }


                val titleTextView = view.category_titleText
                categoryRepository.getCategoryById(userId, categoryId)
                    .addOnSuccessListener { document ->

                        val category = documentToCategory(document)
                        titleTextView.text = category.name
                        view.category_progressBar.visibility = View.GONE
                    }

                    .addOnFailureListener {
                        Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                        view.category_progressBar.visibility = View.GONE
                    }


                val deleteCategoryButton = view.category_deleteCategoryButton

                if (userId == getString(R.string.guest)) {
                    deleteCategoryButton.isVisible = false
                }

                else {
                    deleteCategoryButton.setOnClickListener {
                        view.category_progressBar.visibility = View.VISIBLE
                        categoryRepository.deleteCategory(userId, categoryId, findNavController(), view, context)
                    }
                }
                view.category_progressBar.visibility = View.GONE
            }
            .addOnFailureListener{
                Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.category_progressBar.visibility = View.GONE
            }
    }

    fun deleteGoal(position: Int){
        allGoals.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    fun setBackgroundColor(position: Int, colorId: Int){
        val view = requireView()
        val listItem = listView.getChildAt(position)

        goalRepository.updateColorId(userId, categoryId, allGoals[position].id, colorId)
            .addOnSuccessListener {

                allGoals[position].colorId = colorId
                adapter.notifyDataSetChanged()
                listItem.setBackgroundColor(requireContext().getColor(colorId))
                view.category_progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.category_progressBar.visibility = View.GONE
            }
    }

    override fun onResume() {
        super.onResume()

        // Check if adapter has been initialized to notify changes
        if(::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_home)
        return true
    }
}
