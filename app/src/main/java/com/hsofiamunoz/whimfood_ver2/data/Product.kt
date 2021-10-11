package com.hsofiamunoz.whimfood_ver2.data

import java.io.Serializable

class Product (
    var id : String?= null,
    var product_name : String?= null,
    var location_product: String?= null,
    var product_descrip: String?= null,
    var product_price: Long?=null,
    val url_product_pic: String?= null,
    val propietario: String?=null,
    val propietario_url: String?=null,
    val propietario_id: String?=null

):Serializable