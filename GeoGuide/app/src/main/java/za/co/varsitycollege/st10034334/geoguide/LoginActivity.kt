package za.co.varsitycollege.st10034334.geoguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

//This activity handles the user login process.
class LoginActivity : AppCompatActivity() {

    //Declare variables.
    lateinit var btnLogin : ImageButton
    lateinit var txtNoAccount : TextView
    lateinit var edtEmail : EditText
    lateinit var edtPsswd : EditText

    //Declaring firebaseAuth object.
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //View Binding.
        btnLogin = findViewById(R.id.imgbtnLogin)
        txtNoAccount = findViewById(R.id.txtDontHaveAccount)
        edtEmail = findViewById(R.id.edtLoginEmail)
        edtPsswd = findViewById(R.id.edtLoginPassword)


        //Underlines the "Don't have an account" text view to indicate to the user this text view is clickable.
        //Luciano (2022) demonstrates how to underline text in a TextView.
        val underline = SpannableString("Don't have an account?")
        underline.setSpan(UnderlineSpan(), 0, underline.length, 0)
        txtNoAccount.text = underline


        //Initialising Firebase auth object.
        //GeeksForGeeks (2022) demonstrates the login process with Firebase.
        auth = FirebaseAuth.getInstance()

        //Event calls the login() function.
        btnLogin.setOnClickListener()
        {
            login()
        }

        //Event navigates from this activity to the RegisterActivity.
        txtNoAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)

            //Starts the RegisterActivity.
            startActivity(intent)

            //Closes the current activity.
            finish()
        }


    }

    //login() function attempts to login the user through the Firebase auth object and displays a response to the
    //user depending on login status. If successful login, directs user to the MainActivity.
    //GeeksForGeeks (2022) demonstrates the login process with Firebase.
    private fun login(){

        //Getting and assigning the email and password to variables.
        val email = edtEmail.text.toString()
        val password = edtPsswd.text.toString()

        //Calling the signInWithEmailAndPassword() function using Firebase auth object to log in the user.
        //Displays a toast message to the user to indicate whether or not login was successful.
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()

                //Switching from this activity to MainActivity.
                val intent = Intent(this, MainActivity::class.java)

                //Starts the MainActivity.
                startActivity(intent)

                //Closes the current activity.
                finish()
            } else
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()
        }

    }
}
//REFERENCE LIST:
//GeeksForGeeks. 2022. Login and Registration in Android using Firebase in Kotlin, 28 February 2022 (Version 2.0)
//[Source code] https://www.geeksforgeeks.org/login-and-registration-in-android-using-firebase-in-kotlin/
//(Accessed 11 April 2024).
//Luciano. 2022. Can I underline text in an Android layout. Stack Overflow, 21 April 2022 (Version 2.0)
//[Source code] https://stackoverflow.com/questions/2394935/can-i-underline-text-in-an-android-layout
//(Accessed 22 March 2024).