package com.example.golie.ui.home.settings.chooseFavoriteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.documentsToCategories
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.choose_favorite_fragment.view.*

class ChooseFavoriteFragment : Fragment() {

    private lateinit var adapter: ArrayAdapter<Category>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.choose_favorite_fragment, container, false)
        val categoryRepository = CategoryRepository()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val context = requireContext()

        val listView = view.chooseFavCategory_allCategoriesListView
        var allCategories: MutableList<Category> = ArrayList()

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
                    categoryRepository.setFavoriteCategoryId(userId, categoryId)

                    val navController = findNavController()
                    navController.navigate(R.id.nav_home)
                }
                view.chooseFavCategory_progressBar.visibility = View.GONE
            }

            .addOnFailureListener {
                Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.chooseFavCategory_progressBar.visibility = View.GONE
            }
        return view
    }
}
