package com.marcoux.simplysale

import java.io.Serializable

class Listing(
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var price: Float? = null,
    var desc: String? = null,
    var owner: String? = null
) : Serializable