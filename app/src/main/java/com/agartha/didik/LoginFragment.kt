package com.agartha.didik

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.agartha.didik.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Deklarasikan sharedPref di sini agar bisa dipakai di bawah
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val inputUser = binding.etUsername.text.toString().trim()
            val inputPass = binding.etPassword.text.toString().trim()

            if (inputUser.isEmpty() || inputPass.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi email dan password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil data yang tersimpan (Login pakai Email)
            val savedEmail = sharedPref.getString("reg_email", "")
            val savedPass = sharedPref.getString("reg_pass", "")

            // Logika Pengecekan
            if (inputUser == savedEmail && inputPass == savedPass) {
                // Berhasil Login!
                // Ambil Nama Asli dari reg_user untuk sapaan di Dashboard
                val realName = sharedPref.getString("reg_user", "User")
                sharedPref.edit().putString("user_name", realName).apply()

                // Pindah ke Dashboard
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DashboardFragment())
                    .commit()
            } else {
                // Gagal Login
                Toast.makeText(requireContext(), "Email atau Password salah!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvToRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}