package com.example.golie.ui.home.settings.chooseFavCategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.golie.R

class ChooseFavCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = ChooseFavCategoryFragment()
    }

    private lateinit var viewModel: ChooseFavCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_fav_category_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChooseFavCategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
