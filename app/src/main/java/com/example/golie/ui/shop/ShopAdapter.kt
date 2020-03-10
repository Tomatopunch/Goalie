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
import com.example.golie.data.repositoryClasses.RewardRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.shop_item.view.*

open class ShopAdapter(val context: Context, private var rewards: MutableList<Reward> ) : RecyclerView.Adapter<ShopAdapter.CustomViewHolder>() {

    private val rewardRepository = RewardRepository()
    private var removedPosition: Int = 0
    private lateinit var removedItem: Reward

    val currentUserId = "josefin"

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
        val shopTitle = rewards[position].title
        val shopPoints = rewards[position].price

        holder.shopTitle.text = shopTitle
        holder.shopPoints.text = shopPoints.toString()

        holder.itemView.setOnClickListener {
            val shopDialogFragment = ShopBuyDialogFragment()

        }
    }

    //number of item
    override fun getItemCount(): Int {
        return rewards.size
    }

    //Custom view holder class used for displaying items on the recyclerview
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shopTitle: TextView = itemView.shop_title
        val shopPoints: TextView = itemView.point_content
    }

    fun removeItem(viewHolder: CustomViewHolder) {

        //before you remove, cache the position it was previously on.
        removedPosition = viewHolder.adapterPosition
        removedItem = rewards[removedPosition]

        rewardRepository.deleteReward(currentUserId, rewards[removedPosition].id)

            .addOnSuccessListener {
                rewards.removeAt(viewHolder.adapterPosition)
                notifyItemRemoved(viewHolder.adapterPosition)

            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "An exception was thrown when deleting a reward! ")
            }

        //Give the user information that an item was deleted.
        //When pressing UNDO - > revert the changes
        Snackbar.make(viewHolder.itemView, "$removedItem deleted.", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {

                rewardRepository.createRewardWithSpecificId(
                    currentUserId,
                    removedItem,
                    removedItem.id
                )

                    .addOnSuccessListener {
                        rewards.add(removedPosition, removedItem)
                        notifyItemInserted(removedPosition)

                    }.addOnFailureListener {
                        Log.d(ContentValues.TAG, "An exception was thrown when deleting a reward! ")
                    }
            }.show()
    }
}
/*
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


}

 */

