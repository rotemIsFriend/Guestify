package com.example.guestify.ui.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import com.example.guestify.R
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.guestify.databinding.SignupBinding
import com.google.firebase.firestore.FirebaseFirestore


class SingupFragment : Fragment(){

    private lateinit var auth: FirebaseAuth
    private var _binding: SignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnConfirm.setOnClickListener{
            validateSignUp()
        }



    }

    private fun validateSignUp(){

        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val confirmpassword = binding.editTextConfrimpassword.text.toString().trim()

        if(email.isEmpty()){
            binding.emailInputLayout.error = getString(R.string.email_is_required)
            return
        }
        else{
            binding.emailInputLayout.error = null
        }

        if(password.isEmpty() || password.length < 6){
            binding.passwordTextInputLayout.error =
                getString(R.string.password_must_be_at_least_6_characters)
            return
        }
        else{
            binding.passwordTextInputLayout.error = null
        }

        if(confirmpassword.isEmpty())
        {
            getString(R.string.please_confirm_your_password).also { binding.confirmpasswordTextInputLayout.error = it }
            return
        }

        if(confirmpassword != password)
        {
            getString(R.string.passwords_do_not_match).also { binding.confirmpasswordTextInputLayout.error = it }
            return
        }
        else{
            binding.confirmpasswordTextInputLayout.error = null
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    saveUserToFireStore(user?.uid, email)
                }
                else{
                    Toast.makeText(requireContext(),"${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFireStore(userId: String?, email: String)
    {
        val db = FirebaseFirestore.getInstance()

        if(userId != null){
            val userMap = hashMapOf(
                "email" to email,
                "createdAt" to System.currentTimeMillis()
            )

            db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),
                        getString(R.string.sign_up_successful), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_singupFragment_to_loginFragment)
                }
                .addOnFailureListener{ e ->
                    Toast.makeText(requireContext(),
                        getString(R.string.error_saving_user, e.message), Toast.LENGTH_SHORT).show()
                }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}