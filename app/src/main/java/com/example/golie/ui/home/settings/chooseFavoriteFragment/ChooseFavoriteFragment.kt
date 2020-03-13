package com.example.golie.ui.home.settings.chooseFavoriteFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.documentsToCategories
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.choose_favorite_fragment.view.*

class ChooseFavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = ChooseFavoriteFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: ChooseFavoriteViewModel
    private lateinit var adapter: ArrayAdapter<Category>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.choose_favorite_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Setting up the list view with all its data and enabling cicking on one list item

        Log.d("inne", "")
        val listView = view.chooseFavCategory_allCategoriesListView
        var allCategories: MutableList<Category> = ArrayList()

        categoryRepository.getAllCategories(currentUserId)
            .addOnSuccessListener { documents ->
                Log.d("catty", "")

                allCategories = documentsToCategories(documents)

                adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    allCategories
                )

                listView.adapter = adapter

                listView.setOnItemClickListener { parent, view, position, id ->

                    var clickedCategory = listView.adapter.getItem(position) as Category

                    var categoryId = clickedCategory.id

                    categoryRepository.setFavoriteCategoryId(currentUserId, categoryId)

                    val navController = findNavController()
                    navController.navigate(R.id.nav_home)
                }
                view.chooseFavCategory_progressBar.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                view.chooseFavCategory_progressBar.visibility = View.GONE
                Log.d("Error getting categories: ", exception.toString())
            }

        return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChooseFavoriteViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
