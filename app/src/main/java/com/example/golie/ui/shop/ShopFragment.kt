package com.example.golie.ui.shop

import android.content.ContentValues
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.golie.R
import com.example.golie.data.documentToPoints
import com.example.golie.data.repositoryClasses.PointsRepository
import kotlinx.android.synthetic.main.shop_fragment.*
import kotlinx.android.synthetic.main.shop_fragment.view.*


//TODO: Your points should be added from the database

var alertItemClicked = false
var alertItemBought = false

class ShopFragment : Fragment() {

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable
    private val pointsRepository = PointsRepository()
    val currentUserId = "josefin"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.shop_fragment, container, false)
        val points = view.shop_balance

        var checkAlertItemClicked = savedInstanceState?.getBoolean("alertItemClicked")

        if (checkAlertItemClicked != null) {
            alertItemClicked = checkAlertItemClicked
        }

        if (alertItemClicked) {
            android.app.AlertDialog.Builder(context)
                .setTitle("Buy")
                .setMessage("Are you sure you want to buy this item?")
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    alertItemClicked = false
                    alertItemBought = true
                    android.app.AlertDialog.Builder(requireContext())
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

        var checkAlertItemBought = savedInstanceState?.getBoolean("alertItemBought")

        if (checkAlertItemBought != null) {
            alertItemBought = checkAlertItemBought
        }

        if (alertItemBought) {
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("That's great!")
                .setMessage("Your new balance: XXX")
                .setPositiveButton(
                    "Enjoy your new reward!"
                ) { _, _ ->
                    alertItemBought = false
                }.setOnCancelListener{
                    alertItemBought = false
                }.show()
        }

        deleteIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete)!!

        view.shop_view.layoutManager = LinearLayoutManager(activity)
        view.shop_view.adapter = ShopAdapter(requireContext())
        view.shop_view.setHasFixedSize(true)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            // This is only used for moving, but we don't use that.
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                (view.shop_view.adapter as ShopAdapter).removeItem(viewHolder as ShopAdapter.CustomViewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                // calculate the distance between the top of the icon and the bottom of the recyclerview.
                // so that we can get the icon to appear in the center of each item vertically.
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)

                    deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin, itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMargin)
                }
                else {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin,
                        itemView.bottom - iconMargin)
                }

                //c is our canvas that we want to draw.
                swipeBackground.draw(c)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(view.shop_view)

        // get points from the current user
        // separate this into another function
        pointsRepository.getPoints(currentUserId)
            .addOnSuccessListener { document ->

                if (document != null) {
                    points.text = documentToPoints(document).toString()
                    Log.d("found points", points.toString())
                }
                else {
                    Log.d(ContentValues.TAG, "Could not find points!")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "An exception was thrown when fetching points! ", exception)
            }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreateReward = shop_floatingActionButton

        buttonCreateReward.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_createReward)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("alertItemClicked", alertItemClicked)
        outState.putBoolean("alertItemBought", alertItemBought)
    }

}
