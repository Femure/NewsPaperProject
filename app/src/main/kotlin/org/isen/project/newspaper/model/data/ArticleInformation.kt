package org.isen.project.newspaper.model.data

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class ArticleInformation(val status: String, val totalResults: Int, val articles: List<ArticleInfo>) {

    class Deserializer : ResponseDeserializable<ArticleInformation> {
        override fun deserialize(content: String): ArticleInformation = Gson().fromJson(content, ArticleInformation::class.java)
    }
}


data class ArticleInfo(
        val source: SourceInfo,
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String,
        val content: String
)

data class SourceInfo(
        val id:String?,
        val name:String
)