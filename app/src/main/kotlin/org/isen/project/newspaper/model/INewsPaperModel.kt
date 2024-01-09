package org.isen.project.newspaper.model

import java.beans.PropertyChangeListener

interface INewsPaperModel {

    companion object {
        const val DATATYPE_ARTICLE = "article"
    }

    fun changeCurrentSelection(id: String)
    fun register(listener: PropertyChangeListener)
    fun unregister(listener: PropertyChangeListener)
    fun selectEndPoint(endpoint: String)
    fun sortArticleInformation(sort: String)
    fun findArticleByLanguage(lang: String)
    fun findArticleByCategory(cate: String)
    fun findArticleBySource(source: String)
    fun searchArticle(search: String)
    fun exportArticleToPDF(filePath: String)
}