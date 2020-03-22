package com.example.golie.ui.shop.reward

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.golie.R
import com.example.golie.data.dataClasses.Reward
import com.example.golie.data.repositoryClasses.RewardRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.create_reward_fragment.view.*

class CreateReward : Fragment() {

    private lateinit var titleText: EditText
    private lateinit var pointContent: EditText
    private val rewardRepository = RewardRepository()

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.create_reward_fragment, container, false)
        val confirmButton = view.create_button

        confirmButton.setOnClickListener {
            view.create_reward_progressBar.visibility = View.VISIBLE

            titleText = view.create_editTitle
            pointContent = view.create_editPointsEditText

            val validationErrors = validateRewardInput(titleText.editableText.toString(), pointContent.editableText.toString(), context)

            if (validationErrors.isNotEmpty()) {
                val validationTextView = view.addreward_validationText
                validationTextView.text = getString(R.string.createReward_validationError) + validationErrors
                view.create_reward_progressBar.visibility = View.GONE
            }
            else {
                val reward = Reward(titleText.editableText.toString(), pointContent.editableText.toString().toInt())

                rewardRepository.createReward(userId, reward)

                    .addOnSuccessListener {
                        view.create_reward_progressBar.visibility = View.GONE
                        val navController = findNavController()
                        navController.navigate(R.id.nav_shop)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                        view.create_reward_progressBar.visibility = View.GONE
                    }
            }
        }

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
