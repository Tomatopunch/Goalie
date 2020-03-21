package com.example.golie.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.golie.R
import kotlinx.android.synthetic.main.shop_too_little_dialog_fragment.view.*

class ShopTooLittleDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.shop_too_little_dialog_fragment, container, false)
        val button = view.confirm_button

        button.setOnClickListener {
            dismiss()
        }

        return view
    }
}
