package io.github.kurramkurram.futaltacticalboard.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.futaltacticalboard.R

class SavedVideoDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var mEditTitle: EditText

    fun show(activity: Activity) {
        val host = activity as AppCompatActivity
        val manager = host.supportFragmentManager
        super.show(manager, "SavedVideoDialogFragment")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity
        val builder = AlertDialog.Builder(activity)
        val inflater =
            activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_fragment_save_video, null)
        mEditTitle = view!!.findViewById(R.id.save_title_edit_text)

        val message = resources.getString(R.string.save_video_dialog_title)
        val positive = resources.getString(android.R.string.ok)
        val negative = resources.getString(android.R.string.cancel)
        builder.setView(view)
            .setMessage(message)
            .setPositiveButton(positive, this)
            .setNegativeButton(negative, this)

        return builder.create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.setCanceledOnTouchOutside(false)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val target = activity
                if (target is OnDialogResultCallback) {
                    target.onPositiveButtonClicked(mEditTitle.text.toString())
                }
                dismiss()
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                val target = activity
                if (target is OnDialogResultCallback) {
                    target.onNegativeButtonClicked()
                }
                dismiss()
            }
        }
    }

    interface OnDialogResultCallback {
        fun onPositiveButtonClicked(title: String)

        fun onNegativeButtonClicked()
    }
}