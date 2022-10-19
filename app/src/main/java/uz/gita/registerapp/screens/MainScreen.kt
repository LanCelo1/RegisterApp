package uz.gita.registerapp.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uz.gita.registerapp.R
import uz.gita.registerapp.data.local.MySharedPreference
import uz.gita.registerapp.databinding.ScreenMainBinding
import uz.gita.registerapp.utils.openScreen

class MainScreen : Fragment(R.layout.screen_main) {
    private var _binding: ScreenMainBinding? = null
    private val binding: ScreenMainBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScreenMainBinding.bind(view)

        binding.btnLogOut.setOnClickListener {
            Firebase.auth.signOut()
            MySharedPreference(this.requireActivity()).isUserActive = false
            openScreen(SignInScreen())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}