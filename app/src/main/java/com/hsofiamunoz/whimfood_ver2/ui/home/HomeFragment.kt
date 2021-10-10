package com.hsofiamunoz.whimfood_ver2.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hsofiamunoz.whimfood_ver2.data.Product
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private lateinit var productAdapter: ProductAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
          //  textView.text = it
        })


        productAdapter = ProductAdapter(
            onItemClicked = { onProductItemClicked(it) })
        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            adapter = productAdapter
            setHasFixedSize(false)
        }

        loadFromFirebase()



        return root
    }

    private fun onProductItemClicked(product: Product) {
        findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToDetailProductFragment(product= product))
    }


    private fun loadFromFirebase() {
        //Cargar la base de datos
        val db = Firebase.firestore

        db.collection("product").get().addOnSuccessListener { result ->
            var listProducts : MutableList<Product> = arrayListOf()
            for (document in result){
                Log.d("product",document.data.toString())
                listProducts.add(document.toObject<Product>())

            }
            productAdapter.appendItems(listProducts)
            Log.d("Agregado",productAdapter.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}