package za.co.varsitycollege.st10034334.geoguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

//This activity handles the user registration process.
class RegisterActivity : AppCompatActivity() {

    //Declare variables.
    lateinit var imgbtnReg : ImageButton
    lateinit var edtFirstName : EditText
    lateinit var edtSurname : EditText
    lateinit var edtEmail : EditText
    lateinit var edtPsswd : EditText
    lateinit var edtConfirmPsswd : EditText

    lateinit var userId: String
    lateinit var user: User

    //Declares instances of Firebase components.
    lateinit var auth: FirebaseAuth
    lateinit var docReference: DocumentReference
    lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //View Binding.
        imgbtnReg = findViewById(R.id.imgbtnRegister)
        edtFirstName = findViewById(R.id.edtRegFirstName)
        edtSurname = findViewById(R.id.edtRegSurname)
        edtEmail = findViewById(R.id.edtRegEmail)
        edtPsswd = findViewById(R.id.edtRegPassword)
        edtConfirmPsswd = findViewById(R.id.edtRegConfirmPassword)

        //Event calls the register() function.
        imgbtnReg.setOnClickListener(){

            register()
        }
    }

    //register() function attempts to register the user through the Firebase auth object and displays a response to the
    //user depending on registration status.
    //If successful registration, logs in the user, uploads user data to firebase, and directs user to the MainActivity.
    //GeeksForGeeks (2022) demonstrates the login process with Firebase.
    private fun register(){

        //Initializes instances of Firebase components.
        //Sharma demonstrates how to upload data to firestore (see User profile firebase android studio || Social media app using firebase 2020, 2020).
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //Getting and assigning the full name, email, password, and confirm password to variables.
        val firstName = edtFirstName.text.toString()
        val surname = edtSurname.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPsswd.text.toString()
        val confirmPassword = edtConfirmPsswd.text.toString()

        //Error handling: displays toast message to user to indicate error if the email, password, or confirm password are blank.
        //Returns so that the user must click on the "Register" button again.
        if (firstName.isBlank() || surname.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Fields can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        //Checking passwords match: displays toast message to user to indicate error if passwords don't match.
        //Returns so that the user must click on the "Register" button again.
        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        //Calling the createUserWithEmailAndPassword() function using Firebase auth object to create user in firebase.
        //Displays a toast message to the user to indicate whether or not registration was successful.
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()

                //Calling the signInWithEmailAndPassword() function using Firebase auth object to log in the user automatically.
                auth.signInWithEmailAndPassword(email, password)

                //Retrieves the user's Firebase UID to reference a document with the user's UID as its name.
                userId = auth.currentUser?.uid ?: ""

                //Calls the uploadProfileData() function.
                uploadProfileData()

                //Switching from this activity to MainActivity.
                val intent = Intent(this, MainActivity::class.java)

                //Starts the MainActivity.
                startActivity(intent)

                //Closes the current activity.
                finish()
            } else
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
        }

    }

    //uploadProfileData() function uploads user profile data to firestore.
    //Sharma demonstrates how to upload data to firestore (see User profile firebase android studio || Social media app using firebase 2020, 2020).
    private fun uploadProfileData() {

        //Initializes the document reference with the appropriate collection and userId of user logged in.
        docReference = db.collection("user_profiles").document(userId)

        //Creates a User object with the user-entered first name and surname.
        user = User(edtFirstName.text.toString(), edtSurname.text.toString())

        //Sets the document reference with the User object.
        //If data upload is successful, displays a success message, else displays a failure message.
        docReference.set(user)
                .addOnSuccessListener {

                    Toast.makeText(this, "User Created!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener()
                {
                    Toast.makeText(this, "User Creation Failed!", Toast.LENGTH_SHORT).show()
                }
        }
}
//REFERENCE LIST
//GeeksForGeeks. 2022. Login and Registration in Android using Firebase in Kotlin, 28 February 2022 (Version 2.0)
//[Source code] https://www.geeksforgeeks.org/login-and-registration-in-android-using-firebase-in-kotlin/
//(Accessed 11 April 2024).
//User profile firebase android studio || Social media app using firebase 2020. 2020. YouTube video, added by Rajjan Sharma.
//[Online]. Available at: https://www.youtube.com/watch?v=BuW43bvYqIs&t=1693s&ab_channel=RajjanSharma
//[Accessed 11 April 2024].