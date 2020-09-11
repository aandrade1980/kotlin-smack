package alvus.com.smack.Utilities

import android.view.View
import android.widget.Button
import android.widget.ProgressBar

object Utilities {

    fun enableSpinner(enable: Boolean, spinner: ProgressBar, buttonsToDisable: Array<Button>) {
        if (enable) {
            spinner.visibility = View.VISIBLE
        } else {
            spinner.visibility = View.INVISIBLE
        }

        buttonsToDisable.forEach { button ->
            button.isEnabled = !enable
        }
    }
}