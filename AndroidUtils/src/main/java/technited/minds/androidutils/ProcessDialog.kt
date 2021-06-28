package technited.minds.androidutils

import android.app.Dialog
import android.content.Context
import technited.minds.androidutils.R

class ProcessDialog(var context: Context) {
    var dialog: Dialog = Dialog(context)
    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    init {
        dialog.setContentView(R.layout.process_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
    }
}