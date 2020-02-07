package com.example.golie.ui.shop

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController

import com.example.golie.R
import com.example.golie.ToDo
import com.example.golie.toDoRepository
import kotlinx.android.synthetic.main.shop_fragment.*
import kotlinx.android.synthetic.main.shop_fragment.view.*


class ShopFragment : Fragment() {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private lateinit var viewModel: ShopViewModel
    private lateinit var adapter: ArrayAdapter<ToDo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.shop_fragment, container, false)
        val listView = view.shop_listView
        val points = view.shop_balance

        points.setText("9999")

        adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            toDoRepository.getAllToDos()
        )

        listView.adapter = adapter
        listView.setOnItemClickListener{ _, _, _, _ ->

            AlertDialog.Builder(context!!)
                .setTitle("Buy")
                .setMessage("Are you sure you want to buy this item?")
                .setPositiveButton(
                    "Yes"
                ) { _, _->
                    AlertDialog.Builder(context!!)
                        .setTitle("That's great!")
                        .setMessage("Your new balance: XXX")
                        .setPositiveButton(
                            "Enjoy your new reward!"
                        ) { _, _->
                        }.show()
                }.setNegativeButton(
                    "No"
                ) { _, _->
                }.show()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreateReward = shop_floatingActionButton

        buttonCreateReward.setOnClickListener {
            val navController = findNavController()
            val args = Bundle().apply {}

            navController.navigate(R.id.navigation_createReward, args)
            // You get argument by: val def = argument!!.getString("abc")
        }
    }

    //Notify changes when you come back
    //Fetch changes here and update the adapter possibly?
    override fun onStart() {
        super.onStart()
        adapter.notifyDataSetChanged()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShopViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
