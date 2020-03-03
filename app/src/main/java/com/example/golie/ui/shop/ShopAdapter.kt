package com.example.golie.ui.shop

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golie.R
import kotlinx.android.synthetic.main.shop_item.view.*

private val videoTitles = mutableListOf(
    "Ice cream",
    "Dance",
    "Movie Night",
    "Moore Title",
    "Wanna hang"
)
private val shopPoints = mutableListOf(
    "50",
    "40",
    "50",
    "50",
    "40"
)

// might want to have a dataclass and pass that as an argument into ShopAdapter
// instead of having all the data here later on
open class ShopAdapter(context: Context) : RecyclerView.Adapter<ShopAdapter.CustomViewHolder>() {

    val context = context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        //create view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.shop_item, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val videoTitle = videoTitles[position]
        val shopPoints = shopPoints[position]

        holder.videoTitle.text = shopPoints
        holder.shopPoints.text = videoTitle

        holder.itemView.setOnClickListener{
            alertDialog(context)
        }
    }

    //number of item
    override fun getItemCount() = videoTitles.size

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoTitle: TextView = itemView.shop_title
        val shopPoints: TextView = itemView.point_content
    }

    private fun alertDialog(context: Context) {
        alertItemClicked = true
        AlertDialog.Builder(context)
            .setTitle("Buy")
            .setMessage("Are you sure you want to buy this item?")
            .setPositiveButton(
                "Yes"
            ) { dialog, whichButton ->
                alertItemClicked = false
                alertItemBought = true
                AlertDialog.Builder(context)
                    .setTitle("That's great!")
                    .setMessage("Your new balance: XXX")
                    .setPositiveButton(
                        "Enjoy your new reward!"
                    ) { dialog, whichButton ->
                        alertItemBought = false
                    }.setOnCancelListener{
                        alertItemBought = false
                    }.show()
            }.setNegativeButton(
                "No"
            ) { dialog, whichButton ->
                alertItemClicked = false
            }.setOnCancelListener{
                alertItemClicked = false
            }.show()
    }
    fun addItem(title: String, point: String) {
        videoTitles.add(title)
        shopPoints.add(point)
    }
}
