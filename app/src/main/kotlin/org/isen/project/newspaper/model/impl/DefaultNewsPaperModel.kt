package org.isen.project.newspaper.model.impl

import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.isen.project.newspaper.model.INewsPaperModel
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.model.data.ArticleInformation
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates

class DefaultNewsPaperModel: INewsPaperModel {
    companion object : Logging

    private val pcs = PropertyChangeSupport(this)
    private var newspapersInformation: ArticleInformation? by Delegates.observable(null) { property, oldValue, newValue ->
        logger.info("newspapersInformation change , on notifie les observers")
        pcs.firePropertyChange(INewsPaperModel.DATATYPE_ARTICLE, oldValue, newValue)

    }

    private var articleDescribeList = listOf<ArticleInfo>()
    private var selectedNewPaper: ArticleInfo? by Delegates.observable(null) { property, oldValue, newValue ->
        logger.info("nouvelle selection d'articles")
        pcs.firePropertyChange(INewsPaperModel.DATATYPE_ARTICLE, oldValue, newValue)

    }

    override fun register(listener: PropertyChangeListener) {
        logger.info("enregistrement d'un nouvelle observer $listener ")
        pcs.addPropertyChangeListener(listener)
    }

    override fun unregister(listener: PropertyChangeListener) {
        logger.info("enlevement d'un observer $listener ")
        pcs.removePropertyChangeListener(listener)
    }

    private suspend fun downloadArticleInformation() {
        logger.info("Téléchargement des articles depuis les requetes")
        val (request, reponse, result) = "https://newsapi.org/v2/everything?q=tesla&from=2023-12-03&sortBy=publishedAt&apiKey=8309d583f12e4887a867a21c8cf9fb95".httpGet()
                .responseObject(ArticleInformation.Deserializer())
        logger.info("Status Code: &{reponse.statusCode}")
        result.let { (data, error) ->
            newspapersInformation = data

        }
    }

    public override fun findArticleInformation() {
        GlobalScope.launch { downloadArticleInformation() }
    }

    public override fun changeCurrentSelection(id: String) {

        if (articleDescribeList.isEmpty()) {
            logger.info("Téléchargement des articles depuis les requetes")
            val (request, reponse, result) = "https://newsapi.org/v2/everything?q=voiture&apiKey=fcf1ae001a1e415bbafca6d3d198036b".httpGet()
                    .responseObject(ArticleInformation.Deserializer())
            logger.info("Status Code: &{reponse.statusCode}")
            result.let { (data, error) ->
                articleDescribeList = data?.articles ?: listOf()
            }

        }
        selectedNewPaper = articleDescribeList.find {
            it.source.id == id
        }
    }

//    override fun findParticularArticleTheme(theme: String) {
//        logger.info("Telechargement des articles à thème")
//        val start = "https://newsapi.org/v2/everything?"
//        val KEY = "apiKey=fcf1ae001a1e415bbafca6d3d198036b"
//        val rechercheTheme = "q=" + theme + "&"
//        val (request, reponse, result) = (start + rechercheTheme + KEY).httpGet()
//                .responseObject(ArticleInformation.Deserializer())
//        logger.info("Status Code: ${reponse.statusCode}")
//        println("Status Code pour rechercher theme: ${reponse.statusCode}")
//        println("Requete envoyée: "+start + rechercheTheme + KEY)
//        result.let { (data, error) ->
//            newspapersInformation = data
//            println(newspapersInformation)
//
//        }
//
//    }

//    override fun findParticularArticleAuthor(author: String) {
//        logger.info("Telechargement des articles à author")
//        val start = "https://newsapi.org/v2/everything?"
//        val KEY = "apiKey=fcf1ae001a1e415bbafca6d3d198036b"
//        val rechercheAuthor = "domains=" + author + "&"
//        val (request, reponse, result) = (start + rechercheAuthor + KEY).httpGet()
//                .responseObject(ArticleInformation.Deserializer())
//        logger.info("Status Code: ${reponse.statusCode}")
//        println("Status Code pour rechercher author: ${reponse.statusCode}")
//        println("Requete envoyée: "+start + rechercheAuthor + KEY)
//        result.let { (data, error) ->
//            newspapersInformation = data
//            println(newspapersInformation)
//
//        }
//    }
//
//    override fun findParticularArticleLanguage(lang: String) {
//        logger.info("Telechargement des articles à langguage")
//        val start = "https://newsapi.org/v2/everything?"
//        val KEY = "apiKey=fcf1ae001a1e415bbafca6d3d198036b"
//        val rechercheLang = "language=" + lang + "&"
//        val (request, reponse, result) = (start + rechercheLang + KEY).httpGet()
//                .responseObject(ArticleInformation.Deserializer())
//        logger.info("Status Code: ${reponse.statusCode}")
//        println("Status Code pour rechercher author: ${reponse.statusCode}")
//        println("Requete envoyée: "+start + rechercheLang + KEY)
//        result.let { (data, error) ->
//            newspapersInformation = data
//            println(newspapersInformation)
//
//        }
//    }
//
//    override fun findAllParticularArticle(theme: String, author: String, lang: String) {
//        logger.info("Telechargement des articles all")
//        val start = "https://newsapi.org/v2/everything?"
//        val KEY = "apiKey=fcf1ae001a1e415bbafca6d3d198036b"
//        val rechercheTheme = "q=" + theme + "&"
//        val rechercheAuthor = "domains=" + author + "&"
//        val rechercheLang = "language=" + lang + "&"
//        val (request, reponse, result) = (start +rechercheTheme+rechercheAuthor+ rechercheLang + KEY).httpGet()
//                .responseObject(ArticleInformation.Deserializer())
//        logger.info("Status Code: ${reponse.statusCode}")
//        println("Status Code pour rechercher author: ${reponse.statusCode}")
//        println("Requete envoyée: "+start + rechercheLang + KEY)
//        result.let { (data, error) ->
//            newspapersInformation = data
//            println(newspapersInformation)
//
//        }
//    }
}