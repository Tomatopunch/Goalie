package com.example.golie.ui.shop

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.golie.R
import com.example.golie.data.repositoryClasses.PointsRepository
import kotlinx.android.synthetic.main.shop_boughtdialog_fragment.view.*

class ShopBoughtDialogFragment: DialogFragment() {

    private val pointsRepository = PointsRepository()
    val currentUserId = "josefin"
    var accountWallet = -1
    var shopPoints = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.shop_boughtdialog_fragment, container, false)

        if (savedInstanceState != null) {
            accountWallet = savedInstanceState.getInt("accountWallet")
            shopPoints = savedInstanceState.getInt("shopPoints")
        }
        else {
            val arguments = requireArguments()
            accountWallet = arguments.getInt("accountWallet")
            shopPoints = arguments.getInt("shopPoints")
        }

        val newBalance = view.new_point_balance
        val calcNewBalance = accountWallet - shopPoints
        val button = view.acceptButton

        newBalance.text = calcNewBalance.toString()

        // set the accounts points to the new one
        setPoints(calcNewBalance)

        button.setOnClickListener {
            // weird workaround that probably isn't correct? startActivityForResult() launches a new activity and returns the input from there. which in this case fires onResume()
            // inside shopFragment - > which lets us update the view when we get back from our dialog fragment. I leave this here because it works.
            activity?.startActivityForResult(requireActivity().intent, 10);
            dismiss()
        }
        return view
    }

    // Function that sets the new balance for the user logged in.
    private fun setPoints(calcNewBalance: Int) {
        pointsRepository.setPoints(currentUserId, calcNewBalance)
            .addOnSuccessListener {}

            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "An exception was thrown when fetching points! ", exception)
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("accountWallet", accountWallet)
        outState.putInt("shopPoints", shopPoints)
    }
}