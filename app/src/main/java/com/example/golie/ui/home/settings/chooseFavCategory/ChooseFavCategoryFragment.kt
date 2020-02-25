package com.example.golie.ui.home.settings.chooseFavCategory


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
import kotlinx.android.synthetic.main.choose_fav_category_fragment.view.*

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

class ChooseFavCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = ChooseFavCategoryFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: ChooseFavCategoryViewModel
    private lateinit var adapter: ArrayAdapter<Category>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.choose_fav_category_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val currentUserId = "josefin" //TODO

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Setting up the list view with all its data and enabling cicking on one list item

        val listView = view.chooseFavCategory_allCategoriesListView
        var allCategories: MutableList<Category> = ArrayList()

        categoryRepository.getAllCategories(currentUserId)
            .addOnSuccessListener { documents ->

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

                    //TODO: put this category id in navbar in some way!

                    val navController = findNavController()
                    navController.navigate(R.id.nav_home)
                }


            }
            .addOnFailureListener { exception ->
                Log.d("Error getting categories: ", exception.toString())

            }



        return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChooseFavCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
