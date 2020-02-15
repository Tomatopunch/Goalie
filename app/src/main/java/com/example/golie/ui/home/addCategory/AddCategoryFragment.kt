package com.example.golie.ui.home.addCategory

import android.content.ContentValues.TAG
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_category_fragment.view.*


//////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AddCategoryFragment : Fragment() {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        fun newInstance() = AddCategoryFragment()
    }

    private lateinit var viewModel: AddCategoryViewModel
    private val db = FirebaseFirestore.getInstance()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // "view" is now our modifiable fragment
        val view = inflater.inflate(R.layout.add_category_fragment, container, false)


        //Enabling clicking on save button

        val addCategoryButton = view.addCategory_createCategoryButton
        addCategoryButton.setOnClickListener{

            val categoryName = (view.addCategory_nameEditText).editableText.toString() //Fetching text in edit text field
            val category = Category(categoryName)
            val refToCategoriesSubcollection = db.collection("users/idOfCurrentlyLoggedInUser/categories")

            refToCategoriesSubcollection.add(category)
                .addOnSuccessListener { documentReference ->

                    Log.d(TAG, "Successfully added category with ID: " + documentReference.id + "within subcollection 'categories' ")

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding category", e)
                    e.printStackTrace()

                }


            val navController = findNavController()
            navController.navigate(nav_category)
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
