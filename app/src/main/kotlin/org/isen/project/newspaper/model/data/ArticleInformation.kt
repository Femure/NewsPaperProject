package org.isen.project.newspaper.model.data

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.nio.charset.StandardCharsets

data class ArticleInformation(val status: String, val totalResults: Int, val articles: List<ArticleInfo>) {

    class Deserializer : ResponseDeserializable<ArticleInformation> {
        override fun deserialize(bytes: ByteArray): ArticleInformation {
            val contentString = String(bytes, StandardCharsets.UTF_8)
            return Gson().fromJson(contentString, ArticleInformation::class.java)
        }

    }
}


data class ArticleInfo(
    val source: SourceInfo,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class SourceInfo(
    val id: String?,
    val name: String?
)