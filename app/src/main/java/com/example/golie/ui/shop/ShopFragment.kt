package com.example.golie.ui.shop

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog

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

    // Do something with your button!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreateReward = shop_floatingActionButton

        buttonCreateReward.setOnClickListener {
            // Here we cast main activity to the interface (below) and this is possible because
            // main activity extends this interface
            var intf = context!! as Interface
            intf.theButtonWasClicked()
            //activity.theButtonWasClicked()
        }
    }

    // No clue what this is yet
    interface  Interface {
        fun theButtonWasClicked()
    }


    override fun onDetach() {
        super.onDetach()
    }

    //Notify changes when you come back
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
