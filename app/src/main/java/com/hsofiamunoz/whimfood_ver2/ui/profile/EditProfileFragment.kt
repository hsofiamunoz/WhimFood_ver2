package com.hsofiamunoz.whimfood_ver2.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentEditProfileBinding
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        // Al guardar los cambios
        binding.saveChangesButton.setOnClickListener {
            //Se regresa al perfil
            val name_edit = binding.nameInputText
            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToNavigationProfile())
        }

        return root
    }

}