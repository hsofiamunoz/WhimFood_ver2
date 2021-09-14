package com.hsofiamunoz.whimfood_ver2.data.profileServer

class profileServer (
    var id : String ?= null,
    var name: String ?= null,
    var email:String ?= null,
    var adress: String ?= null,
    var description: String ?= null,
    var url_picture: String ?= null,
    var followers: Int ?= 0,
    var following: Int?= 0
)