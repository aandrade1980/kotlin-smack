package alvus.com.smack.Controller

import alvus.com.smack.R
import alvus.com.smack.Services.AuthService
import alvus.com.smack.Utilities.Utilities.enableSpinner
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun loginLoginBtnClicked(view: View) {
        enableSpinner(true, loginSpinner, buttonsToDisable = arrayOf(loginLoginBtn, loginCreateUserBtn))
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false, loginSpinner, buttonsToDisable = arrayOf(loginLoginBtn, loginCreateUserBtn))
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            errorToast("Please fill in both email and password")
        }
    }

    private fun errorToast(message: String = "Something went wrong, please try again.") {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        enableSpinner(false, loginSpinner, buttonsToDisable = arrayOf(loginLoginBtn, loginCreateUserBtn))
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}