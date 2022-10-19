package uz.gita.registerapp.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import uz.gita.registerapp.R
import uz.gita.registerapp.data.local.MySharedPreference
import uz.gita.registerapp.databinding.ScreenSignInBinding
import uz.gita.registerapp.databinding.ScreenSignUpEmailBinding
import uz.gita.registerapp.utils.*

class SignUpEmailScreen : Fragment(R.layout.screen_sign_up_email) {
    private var _binding: ScreenSignUpEmailBinding? = null
    private val binding: ScreenSignUpEmailBinding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val TAG = "TTT"
    private lateinit var db: FirebaseFirestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenSignUpEmailBinding.bind(view)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        binding.btnSubmit.setOnClickListener {
            if (!binding.etFirstName.text.toString().checkName()) {
                binding.etFirstName.error = "Error name"
                return@setOnClickListener
            }
            if (!binding.etLastName.text.toString().checkName()) {
                binding.etLastName.error = "Error name"
                return@setOnClickListener
            }
            if (!binding.etPhoneNumber.text.toString().checkPhoneNumber()) {
                binding.etPhoneNumber.error = "Error phone number"
                return@setOnClickListener
            }
            if (!binding.etPassword.text.toString().checkPassword()) {
                binding.etPassword.error = "Error password"
                return@setOnClickListener
            }
            signUpNewUser(
                binding.etFirstName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etPassword.text.toString(),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUpNewUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        if (checkUserFireStore(email, password)) {
            Toast.makeText(requireContext(), "this email already sign up", Toast.LENGTH_SHORT)
                .show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    // TODO save user firestore
                    saveUserFireStore(firstName, lastName, email, password)
                    // TODO openMainScreen
                    openScreen(MainScreen())
                    Toast.makeText(
                        requireContext(),
                        "you sign up successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    MySharedPreference(this.requireActivity()).isUserActive = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkUserFireStore(email: String, password: String): Boolean {
        var isActive = false
        db.collection("users").document(email).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                isActive = true
            } else {
                Log.d(TAG, "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        return isActive
    }

    private fun saveUserFireStore(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        val user = hashMapOf(
            "firstname" to firstName,
            "lastname" to lastName,
            "phone" to email,
            "password" to password
        )
        db.collection("users").document(email)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}