package com.example.golie.ui.shop.reward


import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Reward
import com.example.golie.data.repositoryClasses.RewardRepository
import kotlinx.android.synthetic.main.create_reward_fragment.view.*

//TODO: validation on all these fields
//TODO: make the button only clickable if fields are true
//TODO: display points from the view when created

class CreateReward : Fragment() {

    companion object {
        fun newInstance() = CreateReward()
    }

    private lateinit var titleText: EditText
    private lateinit var pointContent: EditText
    private val rewardRepository = RewardRepository()

    val currentUserId = "josefin"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.create_reward_fragment, container, false)
        val confirmButton = view.create_button

        titleText = view.create_editTitle
        pointContent = view.create_editPoints

        Log.d("title", titleText.toString())
        Log.d("point", pointContent.toString())

        //have some sort of validation here
        /////////////////////////////////
/*
        confirmButton.setOnClickListener {

            rewardRepository.createReward(currentUserId, reward)

                .addOnSuccessListener {
                    val navController = findNavController()
                    navController.navigate(R.id.nav_shop)

                }.addOnFailureListener {
                    Log.d(ContentValues.TAG, "An exception was thrown when creating a reward! ")
                }
        }
*/
        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        val navController = findNavController()
        navController.navigate(R.id.nav_shop)

        return true
    }
}
