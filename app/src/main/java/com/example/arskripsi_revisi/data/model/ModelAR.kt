package com.example.arskripsi_revisi.data.model

data class Barang(
    val model: String?,
    val name: String?,
    val price: String?,
    val height: String?,
    val long: String?,
    val width: String?,
    val deskripsi: String?,
    val thumbnail: String?,
    val url: String?
) {
    constructor() : this(null, null, null, null, null, null, null, null, null)
}
//
//data class Model(
//    val content: String?,
//    val filename: String?
//){
//    constructor() : this(null, null)
//}