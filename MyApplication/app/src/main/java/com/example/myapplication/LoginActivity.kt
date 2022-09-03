package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mongodb.tasktracker.TaskActivity
import io.realm.mongodb.Credentials
import io.realm.mongodb.User

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var createUserButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        createUserButton = findViewById(R.id.button_create)

        username.setText("sumit91jha@yahoo.com")
        password.setText("SJ@fb613d")


        loginButton.setOnClickListener{login(false)}
        createUserButton.setOnClickListener { login(true) }

        // user login persistance for successive app logins
        //gets the current logged in user from app(Realm app) global context
        //if the current user object is not null ,you may login directly with last active user
        //else,login with login flow
        val currentUser = taskApp.currentUser()

        if (currentUser != null) {
            // if current user is currently logged in, skip the login activity
            val currentUserId = currentUser?.id
           // TODO :function call after login success
            onLoginSuccess()
        }

       // https://docs.mongodb.com/realm/authentication/index.html#active-user
    }
    // handle user authentication (login) and account creation
    private fun login(createUser: Boolean) {
        if (!validateCredentials()) {
            onLoginFailed("Invalid username or password")
            return
        }

        // while this operation completes, disable the buttons to login or create a new account
        createUserButton.isEnabled = false
        loginButton.isEnabled = false

        val username = this.username.text.toString()
        val password = this.password.text.toString()


        if (createUser) {
            // register a user using the Realm App we created in the TaskTracker class
            taskApp.emailPassword.registerUserAsync(username, password) {
                // re-enable the buttons after user registration completes
                createUserButton.isEnabled = true
                loginButton.isEnabled = true
                if (!it.isSuccess) {
                    onLoginFailed("Could not register user.")
                    Log.e(TAG(), "Error: ${it.error}")
                } else {
                    Log.i(TAG(), "Successfully registered user.")
                    // when the account has been created successfully, log in to the account
                    login(false)
                }
            }
        } else {
            val creds = Credentials.emailPassword(username, password)
            taskApp.loginAsync(creds) {
                // re-enable the buttons after
                loginButton.isEnabled = true
                createUserButton.isEnabled = true
                if (!it.isSuccess) {
                    onLoginFailed(it.error.message ?: "An error occurred.")
                } else {
                    onLoginSuccess()
                }
            }
        }
    }
    private fun onLoginSuccess() {
        // successful login ends this activity, bringing the user back to the task activity
        // if no user is currently logged in, start the login activity so the user can authenticate
        val email =  taskApp.currentUser()?.profile?.email.toString()
        val firstName =  taskApp.currentUser()?.profile?.firstName.toString()

        Toast.makeText(baseContext, email+firstName, Toast.LENGTH_LONG).show()

        startActivity(Intent(this, TaskActivity::class.java))

    }

    private fun onLoginFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    private fun validateCredentials():Boolean= when {
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else ->true
    }
}