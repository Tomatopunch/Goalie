package com.example.golie.ui.home


import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.golie.MainActivity

import com.example.golie.ToDo
import com.example.golie.toDoRepository
import com.example.golie.ui.home.HomeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.category_fragment.*
import kotlinx.android.synthetic.main.category_fragment.view.*
import kotlinx.android.synthetic.main.home_fragment.view.*
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.OnSuccessListener
import android.content.ContentValues.TAG
import com.example.golie.R
import java.util.*
import kotlin.collections.HashMap
//import javax.swing.UIManager.put





///////////////////////////////////////////////////////////////////////////////////////////////////////

class HomeFragment : Fragment() {

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    companion object {
        fun newInstance() = HomeFragment()
    }
    private val db = FirebaseFirestore.getInstance()
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ArrayAdapter<ToDo>

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.home_fragment, container, false) // "view" is now our modifiable fragment


        // Setting up the list view with all its data and enabling cicking on one list item

        val listView = view.home_allCategoriesListView//fetching the list view with id "home_allCategoriesListView"

        adapter = ArrayAdapter(
            context!!, // Casting our fragment into a context?
            android.R.layout.simple_list_item_1, // Has to do with presentation (we want to display it as a simple_list_item_1)
            android.R.id.text1,
            toDoRepository.getAllToDos() //Here we fetch data!!
        )

        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->

            var clickedCategory = listView.adapter.getItem(position) as ToDo
            var id = clickedCategory.id
            //TODO: Here we want to go to the next fragment which is the sepecific category fragment
        }


        //Enabling clicking on add button

        val addButton = view.home_addCategoryButton

        addButton.setOnClickListener {


            Log.d("status", "you clicked button")


            // Test 1

            val user1 = hashMapOf(
                "name" to "Dennis"
            )

            Log.d("user1", "$user1")

            db.collection("users1").add(user1)


            // Test 2

            val user2 = HashMap<String, Any>()
            user2.put("first", "Ada")
            user2.put("last", "Lovelace")
            user2.put("born", 1815)

            // Add a new document with a generated ID
            db.collection("users2")
                .add(user2)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        TAG,
                        "DocumentSnapshot added with ID: " + documentReference.id
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    e.printStackTrace()
                 }


        }


        //Returning the view

        return view
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
}
