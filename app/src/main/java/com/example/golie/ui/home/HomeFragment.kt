package com.example.golie.ui.home

import android.os.Bundle
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
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_fragment.view.*

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
class HomeFragment : Fragment() {

    private lateinit var adapter: ArrayAdapter<Category>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val userId: String
        val context = requireContext()
        val userNameTextView = view.home_userNameText

        // Check if user is logged in or not, otherwise set user id to guest AND setting the title

        if (FirebaseAuth.getInstance().currentUser == null) {
            userId = "Guest"
            userNameTextView.text = getString(R.string.guest)
        }

        else {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
            userNameTextView.text = FirebaseAuth.getInstance().currentUser!!.displayName
            view.home_guestText.isVisible = false
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Setting up the list view with all its data and enabling clicking on one list item

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
                Toast.makeText(context,getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.home_progressBar.visibility = View.GONE
            }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val addCategoryButton = view.home_addCategoryButton

        if (userId == "Guest") {
            addCategoryButton.isVisible = false
        }

        else {
            addCategoryButton.setOnClickListener {
                val navController = findNavController()
                navController.navigate(nav_addCategory)
            }
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val settingsButton = view.home_settingsButton

        if (userId == "Guest") {
            settingsButton.setOnClickListener {

                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.homeFragment_dialog_title))
                    .setMessage(getString(R.string.homeFragment_dialog_message))
                    .setPositiveButton(
                        getString(R.string.homeFragment_dialog_buttonText)
                    ){ dialog, whichButton ->
                        val navController = findNavController()
                        navController.navigate(R.id.nav_info)
                    }
                    .setNegativeButton(
                        getString(R.string.homeFragment_dialog_login)
                    ){ dialog, whichButton ->
                        (activity as MainActivity).login()
                    }.show()
            }
        }

        //logged in
        else {
            settingsButton.setOnClickListener {
                var navController: NavController

                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.homeFragment_dialog_title))
                    .setMessage(getString(R.string.homeFragment_dialog_message))
                    .setPositiveButton(
                        getString(R.string.homeFragment_dialog_selectFavorite)
                    ) { dialog, whichButton ->

                        navController = findNavController()
                        navController.navigate(R.id.nav_chooseFavCategory)

                    }.setNegativeButton(
                        getString(R.string.homeFragment_dialog_viewInfo)
                    ) { dialog, whichButton ->

                        navController = findNavController()
                        navController.navigate(R.id.nav_info)

                    }.setNeutralButton(
                        getString(R.string.homeFragment_dialog_logout)
                    ) { dialog, whichButton ->
                        signOut()
                    }.show()
            }
        }
        return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun signOut() {

        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
            }
    }
}
