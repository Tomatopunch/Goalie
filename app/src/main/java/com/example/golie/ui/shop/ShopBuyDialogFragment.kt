package com.example.golie.ui.shop

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.golie.R
import com.example.golie.data.documentToPoints
import com.example.golie.data.repositoryClasses.PointsRepository
import kotlinx.android.synthetic.main.shop_buydialog_fragment.view.*

class ShopBuyDialogFragment : DialogFragment() {

    private var accountWallet = 0
    var shopPoints = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.shop_buydialog_fragment, container, false)
        val noButton = view.noButton
        val yesButton = view.yesButton
        val pointsRepository = PointsRepository()
        val currentUserId = "josefin"

        if (savedInstanceState != null) {
            shopPoints = savedInstanceState.getInt("shopPoints")
        }
        else {
            shopPoints = requireArguments().getInt("shopPoints")
        }

        //get points from user who is currently logged in
        getPoints(currentUserId, pointsRepository)

        // call next fragment dialog
        yesButton.setOnClickListener {
            validateWalletAmount()
        }
        // go back
        noButton.setOnClickListener {
            dismiss()
        }
        return view
    }

    // Function that validates if the user can buy something or not
    private fun validateWalletAmount() {
        if (accountWallet > shopPoints ) {
            dismiss()

            val args = Bundle().apply {
                putInt("accountWallet", accountWallet)
                putInt("shopPoints", shopPoints)
            }

            val shopBoughtDialogFragment = ShopBoughtDialogFragment()

            shopBoughtDialogFragment.arguments = args
            val fragmentManager = requireActivity().supportFragmentManager
            shopBoughtDialogFragment.show(fragmentManager, "secondFragmentManager")
        }

        else {
            dismiss()
            val shopTooLittleDialogFragment = ShopTooLittleDialogFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            shopTooLittleDialogFragment.show(fragmentManager, "thirdFragmentManager")
        }
    }

    // Function that retrieves the points from the user currently logged in.
    private fun getPoints(currentUserId: String, pointsRepository: PointsRepository) {
        pointsRepository.getPoints(currentUserId)

            .addOnSuccessListener { document ->
                if (document != null) {
                    accountWallet  = documentToPoints(document)
                }
                else {
                    Log.d(ContentValues.TAG, "Could not find points!")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "An exception was thrown when fetching points! ", exception)
            }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("shopPoints", shopPoints)
    }

}