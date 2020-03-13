package com.example.golie.ui.home.addCategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.R.id.nav_category
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.google.firebase.auth.FirebaseAuth
//import com.example.golie.data.repositoryClasses.createCategory
import kotlinx.android.synthetic.main.add_category_fragment.view.*


//////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AddCategoryFragment : Fragment() {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        fun newInstance() = AddCategoryFragment()
    }

    private lateinit var viewModel: AddCategoryViewModel
    private val categoryRepository = CategoryRepository()


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.add_category_fragment, container, false)
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Enabling clicking on save button

        val addCategoryButton = view.addCategory_createCategoryButton

        addCategoryButton.setOnClickListener {
            view.addCategory_progressBar.visibility = View.VISIBLE
            val categoryName = (view.addCategory_nameEditText).editableText.toString() //Fetching text in edit text field
            val category = Category(categoryName)
            categoryRepository.createCategory(currentUserId, category)
                .addOnSuccessListener {

            val categoryName = (view.addCategory_nameEditText).editableText.toString()//Fetching text in edit text field
            Log.d("name", categoryName)

            var validationErrors = validateCategoryInput(categoryName)

            if (validationErrors.isNotEmpty()) { // There are validation errors

                var validationTextView = view.addCategory_validationTextView
                validationTextView.text = "The following validation error occurred: " + validationErrors
            }

            else { //There are no validation errors!

                Log.d("validation errors", validationErrors)
                val category = Category(categoryName)
                categoryRepository.createCategory(currentUserId, category)
                    .addOnSuccessListener {

                        Log.d("success", "did add category")
                        //val categoryId = it.id

                    val navController = findNavController()
                    //val args = Bundle().apply { putString("categoryId", categoryId) } // Add
                    //navController.navigate(R.id.nav_category) // Add
                    view.addCategory_progressBar.visibility = View.GONE

                    navController.navigate(R.id.nav_home) // Remove
                }

                .addOnFailureListener{
                    view.addCategory_progressBar.visibility = View.GONE
                    Log.d("failureListener", "$it")
                }

        }

        return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
