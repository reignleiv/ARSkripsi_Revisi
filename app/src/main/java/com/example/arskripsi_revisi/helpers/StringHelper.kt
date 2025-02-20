package com.example.arskripsi_revisi.helpers

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

abstract class StringHelper {
    companion object {
        fun getUrlSafeName(name: String): String {
            val prefix = "models/"
            return if (name.startsWith(prefix)) {
                prefix + URLEncoder.encode(
                    name.substring(prefix.length),
                    StandardCharsets.UTF_8.toString()
                )
                    .replace("+", "%20")
            } else {
                URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20")
            }
        }
    }
}