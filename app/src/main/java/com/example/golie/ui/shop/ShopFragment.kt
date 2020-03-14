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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.golie.R
import com.example.golie.data.dataClasses.Reward
import com.example.golie.data.documentToPoints
import com.example.golie.data.documentsToRewards
import com.example.golie.data.repositoryClasses.PointsRepository
import com.example.golie.data.repositoryClasses.RewardRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.shop_fragment.*
import kotlinx.android.synthetic.main.shop_fragment.view.*

class ShopFragment : Fragment() {

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable
    private var rewards = mutableListOf<Reward>()
    private val pointsRepository = PointsRepository()
    private val rewardRepository = RewardRepository()

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.shop_fragment, container, false)
        val points = view.shop_balance

        deleteIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete)!!

        rewardRepository.getAllRewards(userId)
            .addOnSuccessListener { document ->

                if (document != null) {
                    rewards = documentsToRewards(document)
                    view.shop_view.layoutManager = LinearLayoutManager(activity)
                    view.shop_view.adapter = ShopAdapter(requireContext(), rewards)
                    view.shop_view.setHasFixedSize(true)
                    (view.shop_view.adapter as ShopAdapter).notifyDataSetChanged()
                }
                else {
                    Log.d(ContentValues.TAG, "Could not find rewards!")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "An exception was thrown when fetching rewards! ", exception)
            }

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
        getPoints(points)

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

    // when we get back here fetch the points again to update the view
    override fun onResume() {
        super.onResume()

        val points = shop_balance
        getPoints(points)
    }

    //Function that retrieves the amount of points from the user currently logged in
    private fun getPoints(points: TextView) {
        pointsRepository.getPoints(userId)
            .addOnSuccessListener { document ->

                if (document != null) {
                    points.text = documentToPoints(document).toString()
                }
                else {
                    Log.d(ContentValues.TAG, "Could not find points!")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "An exception was thrown when fetching points! ", exception)
            }
    }

}
