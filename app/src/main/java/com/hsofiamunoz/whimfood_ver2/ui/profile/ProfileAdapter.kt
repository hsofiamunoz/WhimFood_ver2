package com.hsofiamunoz.whimfood_ver2.ui.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.data.Product
import com.hsofiamunoz.whimfood_ver2.data.ProfileInfo
import com.hsofiamunoz.whimfood_ver2.databinding.CardViewProductItemBinding
import com.hsofiamunoz.whimfood_ver2.databinding.CardViewProfileItemBinding
import com.hsofiamunoz.whimfood_ver2.ui.home.ProductAdapter
import com.squareup.picasso.Picasso

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    private var listProfile: MutableList<ProfileInfo> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_profile_item, parent,false)
        return ProfileAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {

        holder.bind(listProfile[position])
    }

    override fun getItemCount(): Int {
        return listProfile.size
    }
    fun appendItems(newItems:MutableList<ProfileInfo>){
        listProfile.clear()
        listProfile.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = CardViewProfileItemBinding.bind(view)


        fun bind(profile: ProfileInfo){
            with(binding){
                if(profile.url_product_pic!=null){
                        Picasso.get().load(profile.url_product_pic).into(foto1ImageView)


                        //Picasso.get().load(profile.url_product_pic).into(foto2ImageView)

                }
                //productPriceTextView.text = product.product_price.toString()


            }

        }
    }
}
