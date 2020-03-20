package com.example.golie.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.golie.R
import com.example.golie.data.repositoryClasses.PointsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.shop_boughtdialog_fragment.view.*

class ShopBoughtDialogFragment: DialogFragment() {

    private val pointsRepository = PointsRepository()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
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
        setPoints(calcNewBalance, view)

        button.setOnClickListener {
            activity?.startActivityForResult(requireActivity().intent, 10);
            dismiss()
        }
        return view
    }

    // Function that sets the new balance for the user logged in.
    private fun setPoints(calcNewBalance: Int, view: View) {

        pointsRepository.setPoints(userId, calcNewBalance)
            .addOnSuccessListener {
                view.shopBoughtDialog_progressBar.visibility = View.GONE
            }

            .addOnFailureListener { exception ->
                Toast.makeText(context, getString(R.string.onDbFailureMessage), Toast.LENGTH_SHORT).show()
                view.shopBoughtDialog_progressBar.visibility = View.GONE
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("accountWallet", accountWallet)
        outState.putInt("shopPoints", shopPoints)
    }
}