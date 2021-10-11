package com.hsofiamunoz.whimfood_ver2.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.data.Product
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentDetailProductBinding
import com.squareup.picasso.Picasso


class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val args: DetailProductFragmentArgs by navArgs()
    private var param1: String? = null
    private var param2: String? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val callback= object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_detailProductFragment_to_navigation_home)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val producto= args.product
        binding.nameTextView.text = producto.product_name
        binding.descripcionTextView.text= producto.product_descrip
        binding.priceTextView.text= producto.product_price.toString()
        Picasso.get().load(producto.url_product_pic).into(binding.perfilDetailImageView)

        //Toast.makeText(requireContext(),"holaladcv funciojop",Toast.LENGTH_LONG).show()
        var resultText= ""
        binding.comentarioTextInputLayout.setEndIconOnClickListener {
            if(binding.comentarioInputText.text.toString() != null){
                resultText = resultText + (binding.comentarioInputText.text)+"\n"
                binding.comentTextView.text= resultText
                Log.d("resultado comenatiop", resultText + (binding.comentarioInputText.text))
                binding.comentarioInputText.setText("")
            }
        }


        return root

    }




}