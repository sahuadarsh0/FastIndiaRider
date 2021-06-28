package technited.minds.androidutils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.afollestad.materialdialogs.MaterialDialog

object MD {

    @JvmStatic
    fun alert(context: Context, title: String?, message: String?) {
        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
        }
    }

    @JvmStatic
    fun alert(context: Context, title: String?, message: String?, button: String?) {
        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
            positiveButton(text = button) {
                dismiss()
            }

        }.cancelOnTouchOutside(true).autoDismissEnabled
    }

    @JvmStatic
    fun update(context: Context, title: String, message: String, button: String, url: String) {
        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
            positiveButton(text = button) {
                var browserIntent: Intent? = null
                browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                context.startActivity(browserIntent)
            }
            cancelable(false)
        }.cancelOnTouchOutside(false)
    }

}


