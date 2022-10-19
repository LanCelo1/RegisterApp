package uz.gita.registerapp.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.gita.registerapp.MainActivity
import uz.gita.registerapp.R
import uz.gita.registerapp.data.local.MySharedPreference
import uz.gita.registerapp.databinding.ScreenMainBinding
import uz.gita.registerapp.databinding.ScreenSignInBinding
import uz.gita.registerapp.databinding.ScreenSignUpEmailBinding
import uz.gita.registerapp.utils.checkPassword
import uz.gita.registerapp.utils.checkPhoneNumber
import uz.gita.registerapp.utils.openScreen
import uz.gita.registerapp.utils.openScreenWithBackStack

class SignInScreen : Fragment(R.layout.screen_sign_in) {
    private var _binding: ScreenSignInBinding? = null
    private val binding: ScreenSignInBinding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val TAG = "TTT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenSignInBinding.bind(view)

        auth = Firebase.auth

        binding.signInGoogle.setOnClickListener {
            (requireActivity() as MainActivity).authenticateWithGoogle()
        }
        binding.btnSignUp.setOnClickListener {
            openScreenWithBackStack(SignUpEmailScreen())
        }
        binding.btnSubmit.setOnClickListener {
            if (!binding.etEmail.text.toString().checkPhoneNumber()) {
                binding.etEmail.error = "Error email"
                return@setOnClickListener
            }
            if (!binding.etPassword.text.toString().checkPassword()) {
                binding.etPassword.error = "Error password"
                return@setOnClickListener
            }

            signInWithEmail(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    openScreen(MainScreen())
                    Toast.makeText(
                        requireContext(),
                        "you sign up successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    MySharedPreference(this.requireActivity()).isUserActive = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}