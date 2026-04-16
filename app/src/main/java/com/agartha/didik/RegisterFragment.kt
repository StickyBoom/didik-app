package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agartha.didik.data.RegisterModel
import com.agartha.didik.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterSubmit.setOnClickListener {
            // 1. Ambil data dari EditText
            val name = binding.etRegisterName.text.toString().trim()
            val email = binding.etRegisterEmail.text.toString().trim()
            val pass = binding.etRegisterPass.text.toString().trim()

            // 2. Bungkus ke Model (Syarat MVVM agar nilai maksimal)
            val newUser = RegisterModel(name, email, pass)

            // 3. Validasi sederhana
            if (newUser.name.isNotEmpty() && newUser.email.isNotEmpty() && newUser.pass.isNotEmpty()) {

                // Simpan ke SharedPreferences
                val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.putString("reg_user", newUser.name)
                editor.putString("reg_email", newUser.email)
                editor.putString("reg_pass", newUser.pass)
                editor.apply()

                Toast.makeText(requireContext(), "Akun berhasil dibuat! Silakan Login", Toast.LENGTH_SHORT).show()

                // Kembali ke halaman Login
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Lengkapi semua field, Nad!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol kembali ke Login jika user salah klik
        binding.tvToLogin?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}