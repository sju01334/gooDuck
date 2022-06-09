package com.nepplus.gooduck.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Window
import android.widget.LinearLayout
import com.nepplus.gooduck.databinding.CustomAlertDialogBinding

class CustomAlertDialog(val mContext : Context, val activity : Activity) {

    val dialog = Dialog(mContext)
    lateinit var binding : CustomAlertDialogBinding

    fun myDialog(onClickListener : ButtonClickListener) {
        binding = CustomAlertDialogBinding.inflate(activity.layoutInflater)
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 40))

        dialog.setCancelable(true)

        dialog.show()

        binding.positiveBtn.setOnClickListener {
            onClickListener.positiveBtnClicked()
            dialog.dismiss()
        }

        binding.negativeBtn.setOnClickListener {
            onClickListener.negativeBtnClicked()
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ButtonClickListener {
        fun positiveBtnClicked()
        fun negativeBtnClicked()
    }

}