package com.example.golie.ui.home.addCategory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Category
import com.example.golie.data.repositoryClasses.CategoryRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.add_category_fragment.view.*

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AddCategoryFragment : Fragment() {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val categoryRepository = CategoryRepository()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.add_category_fragment, container, false)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val addCategoryButton = view.addCategory_createCategoryButton
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////



        addCategoryButton.setOnClickListener {
            view.addCategory_progressBar.visibility = View.VISIBLE

            val categoryName = (view.addCategory_nameEditText).editableText.toString()
            val validationErrors = validateCategoryInput(categoryName, context)

            if (validationErrors.isNotEmpty()) {
                val validationTextView = view.addCategory_validationText
                validationTextView.text =
                    getString(R.string.addCategory_validationError) + validationErrors
                view.addCategory_progressBar.visibility = View.GONE
            }

            else {
                val category = Category(categoryName)
                categoryRepository.createCategory(userId, category)
                    .addOnSuccessListener {
                        view.addCategory_progressBar.visibility = View.GONE
                        val navController = findNavController()
                        navController.navigate(R.id.nav_home)
                    }

                    .addOnFailureListener {
                        Toast.makeText(requireContext(),getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                        view.addCategory_progressBar.visibility = View.GONE
                    }
            }
        }
        return view
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val navController = findNavController()
        navController.navigate(R.id.nav_home)
        return true
    }
}
