package com.hsofiamunoz.whimfood_ver2.ui.discover

import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.hsofiamunoz.whimfood_ver2.data.api.Ingredient
import com.hsofiamunoz.whimfood_ver2.data.api.Product1
import com.hsofiamunoz.whimfood_ver2.data.api2.Product
import com.hsofiamunoz.whimfood_ver2.data.api2.Recipes1List
import com.hsofiamunoz.whimfood_ver2.data.api2.Recipes2List
import com.hsofiamunoz.whimfood_ver2.data.api2.Result
import com.hsofiamunoz.whimfood_ver2.data.apiServer.ApiService
import com.hsofiamunoz.whimfood_ver2.databinding.FragmentDiscoverBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiscoverFragment : Fragment() {

    private lateinit var discoverViewModel: DiscoverViewModel
    private var _binding: FragmentDiscoverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: recipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        discoverViewModel =
            ViewModelProvider(this).get(DiscoverViewModel::class.java)


        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //val textView: TextView = binding.textDashboard
        discoverViewModel.text.observe(viewLifecycleOwner, Observer {
           // textView.text = it
        })
        recipeAdapter= recipeAdapter()
        binding.apiRecyclerView.apply {
            layoutManager= LinearLayoutManager(this@DiscoverFragment.context)
            adapter = recipeAdapter
            setHasFixedSize(false)
        }
        loadRecipes()
        return root
    }

    private fun loadRecipes() {
        ApiService.create()
            .getTopRated()
            //.enqueue(object: Callback<newsList>{
            .enqueue(object: Callback<Recipes2List> {
                //override fun onFailure(call: Call<PlacesList>, t: Throwable) {
                override fun onFailure(call: Call<Recipes2List>, t: Throwable) {
                    Log.d("Error",t.message.toString())
                }
                override fun onResponse(call: Call<Recipes2List>, response: Response<Recipes2List>) {
                    if (response.isSuccessful){
                        Log.d("response",response.body()?.results.toString())
                        //var listMovies : MutableList<Responses>? = response.body()?.responses as? MutableList<Responses>
                        var ListRecipe : MutableList<Result> = response.body()?.results as MutableList<com.hsofiamunoz.whimfood_ver2.data.api2.Result>
                        //Log.d("list_movies", listMovies.toString())
                        //Log.d("Success","listMovies: " + listMovies.toString())

                        recipeAdapter.appendItems(ListRecipe)
                    }
                }
            })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}