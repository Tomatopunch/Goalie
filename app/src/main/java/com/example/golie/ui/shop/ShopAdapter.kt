package com.example.golie.ui.shop

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.golie.R
import com.example.golie.data.dataClasses.Reward
import com.example.golie.data.documentToPoints
import com.example.golie.data.documentsToRewards
import com.example.golie.data.repositoryClasses.RewardRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.shop_item.view.*

val rewardRepository = RewardRepository()

open class ShopAdapter(val context: Context) : RecyclerView.Adapter<ShopAdapter.CustomViewHolder>() {

    private var removedPosition: Int = 0
    private var removedItem: String = ""
    private var removedItem2: String = ""

    val currentUserId = "josefin"
    private var rewards = mutableListOf<Reward>()

    //Creates your recyclerview by adding a layout xml to it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.shop_item, parent, false)

        return CustomViewHolder(cellForRow)
    }

    //Binds your data together
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        // get all rewards from the current user
        // separate this into another function
        rewardRepository.getAlLRewards(currentUserId)
            .addOnSuccessListener { document ->

                if (document != null) {
                    rewards = documentsToRewards(document)

                    val shopTitle = rewards[position].title
                    val shopPoints = rewards[position].price

                    holder.shopTitle.text = shopTitle
                    holder.shopPoints.text = shopPoints.toString()
                }
                else {
                    Log.d(ContentValues.TAG, "Could not find rewards!")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "An exception was thrown when fetching rewards! ", exception)
            }

        holder.itemView.setOnClickListener{
            alertDialog(context)
        }
    }

    //number of item idk why mutable list size length doesn't work here????
    override fun getItemCount() = 23

    //Custom view holder class used for displaying items on the recyclerview
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shopTitle: TextView = itemView.shop_title
        val shopPoints: TextView = itemView.point_content
    }

    private fun alertDialog(context: Context) {
        alertItemClicked = true
        AlertDialog.Builder(context)
            .setTitle("Buy")
            .setMessage("Are you sure you want to buy this item?")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                alertItemClicked = false
                alertItemBought = true
                AlertDialog.Builder(context)
                    .setTitle("That's great!")
                    .setMessage("Your new balance: XXX")
                    .setPositiveButton(
                        "Enjoy your new reward!"
                    ) { _, _ ->
                        alertItemBought = false
                    }.setOnCancelListener{
                        alertItemBought = false
                    }.show()
            }.setNegativeButton(
                "No"
            ) { _, _ ->
                alertItemClicked = false
            }.setOnCancelListener{
                alertItemClicked = false
            }.show()
    }

    //TODO Now when we have a mutable list from the database instead this logic will work somewhat differently.
    // previously when we swiped an item we took that array position and used removeAt on that specific adapter position
    fun removeItem(viewHolder: CustomViewHolder) {

        //before you remove, cache the position it was previously on.
        removedPosition = viewHolder.adapterPosition
    //    removedItem = shopPoints[viewHolder.adapterPosition]
    //    removedItem2 = videoTitles[viewHolder.adapterPosition]
        removedItem = rewards[2].title[viewHolder.adapterPosition].toString()

    //    videoTitles.removeAt(viewHolder.adapterPosition)
    //    shopPoints.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)


        //Give the user information that an item was deleted.
        //When pressing UNDO - > revert the changes
     /*
        Snackbar.make(viewHolder.itemView, "$removedItem2 deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            shopPoints.add(removedPosition, removedItem)
            videoTitles.add(removedPosition, removedItem2)

            notifyItemInserted(removedPosition)
        }.show()

      */
    }
}

