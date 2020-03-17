package com.example.golie.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity

import com.example.golie.R
import com.example.golie.R.id.nav_addCategory
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.documentsToCategories
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.data.repositoryClasses.GoalRepository
//import com.example.golie.data.repositoryClasses.getAllCategories
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_fragment.view.*

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ArrayAdapter<Category>
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val userId: String
        val context = requireContext()


        // Check if user is logged in or not, otherwise set user id to guest AND setting the title

        val userNameTextView = view.home_userNameTextView
        if (FirebaseAuth.getInstance().currentUser == null) {
            userId = "Guest"
            userNameTextView.text = "Guest"
        }

        else {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
            userNameTextView.text = FirebaseAuth.getInstance().currentUser!!.displayName
            view.home_guestText.isVisible = false
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Setting up the list view with all its data and enabling cicking on one list item

        val listView = view.home_allCategoriesListView
        var allCategories: MutableList<Category> = ArrayList()

        categoryRepository.getAllCategories(userId)
            .addOnSuccessListener { documents ->


                allCategories = documentsToCategories(documents)

                adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allCategories
                )

                listView.adapter = adapter

                listView.setOnItemClickListener { parent, view, position, id ->

                    val clickedCategory = listView.adapter.getItem(position) as Category
                    val categoryId = clickedCategory.id
                    val navController = findNavController()
                    val args = Bundle().apply { putString("categoryId", categoryId) }
                    navController.navigate(R.id.nav_category, args)
                }

                view.home_progressBar.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.home_progressBar.visibility = View.GONE

            }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Accessing add button

        val addCategoryButton = view.home_addCategoryButton

        //Hiding button if no user is logged in

        if (userId == "Guest") {
            addCategoryButton.isVisible = false
        }

        else {

            //Enabling clicking on plus button to add category

            addCategoryButton.setOnClickListener {

                val navController = findNavController()
                navController.navigate(nav_addCategory)
            }

        }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Enabling clicking on settings button

        val settingsButton = view.home_settingsButton

        if (userId == "Guest") {//not logged in
            settingsButton.setOnClickListener {

                lateinit var navController: NavController
                AlertDialog.Builder(context)
                    .setTitle("Settings")
                    .setMessage("What do you want to do?")
                    .setPositiveButton(
                        "View info page"
                    ){ dialog, whichButton ->
                        navController = findNavController()
                        navController.navigate(R.id.nav_info)

                    }
                    .setNegativeButton(
                        "Login"
                    ){ dialog, whichButton ->

                        (activity as MainActivity).login()

                    }.show()
            }
        }

        //logged in
        else {
            settingsButton.setOnClickListener {
                lateinit var navController: NavController

                AlertDialog.Builder(context)
                    .setTitle("Settings")
                    .setMessage("What do you want to do?")
                    .setPositiveButton(
                        "Select favorite category"
                    ) { dialog, whichButton ->
                        navController = findNavController()
                        navController.navigate(R.id.nav_chooseFavCategory)

                    }.setNegativeButton(
                        "View info page"
                    ) { dialog, whichButton ->

                        navController = findNavController()
                        navController.navigate(R.id.nav_info)

                    }.setNeutralButton(
                        "Logout"
                    ) { dialog, whichButton ->
                        signOut()
                    }.show()
            }
        }
    return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private fun signOut() { //ska denna vara här?

        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
            }

        // [END auth_fui_signout]

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
