package com.hsofiamunoz.whimfood_ver2.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hsofiamunoz.whimfood_ver2.R
import com.hsofiamunoz.whimfood_ver2.data.Product
import com.hsofiamunoz.whimfood_ver2.databinding.CardViewProductItemBinding
import com.squareup.picasso.Picasso

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var listProduct: MutableList<Product> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_product_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }


    fun appendItems(newItems:MutableList<Product>){
        listProduct.clear()
        listProduct.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = CardViewProductItemBinding.bind(view)

        fun bind(product: Product){
            with(binding){
                productNameTextView.text = product.product_name
                productPriceTextView.text = product.product_price.toString()
                if(product.url_product_pic!=null){
                    Picasso.get().load(product.url_product_pic).into(productImageView)
                }

            }

        }
    }
}

