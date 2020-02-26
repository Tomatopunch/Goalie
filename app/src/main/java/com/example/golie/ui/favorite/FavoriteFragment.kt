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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

class FavoriteFragment : Fragment() {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: ArrayAdapter<Category>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        //This fragment will show the favorite category, fetch id of favorite from database THEN call function in superclass"Display..."
        val view = inflater.inflate(R.layout.favorite_fragment, container, false)

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
