package com.example.golie.ui.favorite

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.documentToFavoriteCateoryId
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.example.golie.ui.category.DisplayCategoryFragment

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

class FavoriteFragment :  DisplayCategoryFragment(){

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: ArrayAdapter<Category>
    private val categoryRepository = CategoryRepository()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        lateinit var view: View

        //Getting the id of the currently chosen favorite category
        categoryRepository.getFavoriteCategoryId(currentUserId)

            .addOnSuccessListener { document->
                val categoryId = documentToFavoriteCateoryId(document)
                Log.d("id of fav cat:", categoryId)

                //Calling method of superclass DisplayCategoryFragment to display that category
                //displayCategory(categoryId)
                view = displayCategory(categoryId, inflater, container, savedInstanceState)
            }

            .addOnFailureListener{//add code here for what should happen if favorite category id is not found (maybe that category was deleted by user!)
            }

        //val view = inflater.inflate(R.layout.favorite_fragment, container, false)
        return view
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
