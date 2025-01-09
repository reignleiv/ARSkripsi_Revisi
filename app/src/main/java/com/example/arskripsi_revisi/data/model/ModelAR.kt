package com.example.arskripsi_revisi.data.model

import java.net.URL

data class Barang(
    val model: Model?,
    val name: String?,
    val price: String?,
    val stock: String?
){
    constructor() : this(null, null, null, null)
}

data class Model(
    val content: String?,
    val filename: String?
){
    constructor() : this(null, null)
}