package com.hsofiamunoz.whimfood_ver2.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hsofiamunoz.whimfood_ver2.data.UserProfile
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textNotifications
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })


        val current_user = Firebase.auth.currentUser
        var email = ""
        var id = ""

        // Obtener el id y correo
        current_user?.let {
            id = current_user.uid.toString()
            Log.d("id del user",id)

            for(profile in it.providerData){
                email = profile.email.toString()
                Log.d("email del user",email)
            }
        }

        var db = Firebase.firestore
        //Acceder a la BD --------------------------------------------------------------------------
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var persona1: UserProfile = document.toObject<UserProfile>()
                    if (document.id == id) {
                        Log.d("name", document.data.get("name") as String)
                        binding.nameProfileTextView.setText(document.data.get("name") as String)
                        if(persona1.urlPicture!=null){
                            Picasso.get().load(persona1.urlPicture).into(binding.imageView2)
                        }

                        //binding.nameProfileTextView.setText(persona1.name)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents.", exception)
            }

        // -----------------------------------------------------------------------------------------

        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToEditProfileFragment()   )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}