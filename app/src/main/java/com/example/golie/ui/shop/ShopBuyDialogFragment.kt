package com.example.golie.ui.shop

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.golie.R
import com.example.golie.data.repositoryClasses.UserRepository
import com.example.golie.data.userDocumentToPoints
import com.google.firebase.auth.FirebaseAuth
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
        val userRepository = UserRepository()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        if (savedInstanceState != null) {
            shopPoints = savedInstanceState.getInt("shopPoints")
        }
        else {
            shopPoints = requireArguments().getInt("shopPoints")
        }

        //get points from user who is currently logged in
        getPoints(userId, userRepository, view)

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
    private fun getPoints(userId: String, userRepository: UserRepository, view: View) {
        userRepository.getUserById(userId)

            .addOnSuccessListener { document ->
                if (document != null) {
                    accountWallet  = userDocumentToPoints(document)
                }
                else {
                    Log.d(ContentValues.TAG, "Could not find points!")
                }
                view.shop_buyDialog_progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context,getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.shop_buyDialog_progressBar.visibility = View.GONE

            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("shopPoints", shopPoints)
    }

}