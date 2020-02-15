package com.example.golie.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.golie.R
import kotlinx.android.synthetic.main.shop_listrow.view.*


class ShopAdapter : RecyclerView.Adapter<CustomViewHolder>(){

//hardcoded text for the recyclerview - > will be replaced with database text later on somehow

    private val videoTitles = listOf("bla", "bla", "bla", "Ice cream", "Dance", "Movie Night", "Moore Title", "Ice cream", "Dance", "Movie Night", "Moore Title", "Ice cream", "Dance", "Movie Night", "Moore Title", "Ice cream", "Dance", "Movie Night", "Moore Title" )
    private val shopPoints = listOf("50", "40", "50", "XXXX", "50", "40", "50" , "XXXX", "50", "40", "50", "XXXX", "50", "40", "50", "XXXX")

    //number of item
    override fun getItemCount(): Int {
        return 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //create view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.shop_listrow, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val videoTitle = videoTitles.get(position)
        val shopPoints = shopPoints.get(position)

        holder.view.point_content.text = shopPoints
        holder.view.shop_title.text = videoTitle

    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)