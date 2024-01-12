package org.isen.project.newspaper.model.impl

import com.github.kittinunf.fuel.httpGet
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import org.isen.project.newspaper.model.INewsPaperModel
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.model.data.ArticleInformation
import java.awt.Font
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.swing.JOptionPane
import kotlin.properties.Delegates

class DefaultNewsPaperModel : INewsPaperModel {
    companion object : Logging {
        var prefix_url: String? = null
        const val apiKey = "8309d583f12e4887a867a21c8cf9fb95"
        var date: String? = null
        var sortBy: String? = null
        var language: String? = null
        var q: String? = null
        var category: String? = null
        var source: String? = null
    }


    private val pcs = PropertyChangeSupport(this)

    private var articleInformation: ArticleInformation? by Delegates.observable(null) { _, oldValue, newValue ->
        logger.info("Selection de tous les articles")
        pcs.firePropertyChange(INewsPaperModel.DATATYPE_ARTICLE, oldValue, newValue)
    }

    private var selectedArticle: ArticleInfo? by Delegates.observable(null) { _, oldValue, newValue ->
        logger.info("Selection d'un article")
        println(newValue)
        pcs.firePropertyChange(INewsPaperModel.DATATYPE_ARTICLE, oldValue, newValue)
    }

    override fun register(listener: PropertyChangeListener) {
        logger.info("Ajout d'un nouvel observer $listener ")
        pcs.addPropertyChangeListener(listener)
    }

    override fun unregister(listener: PropertyChangeListener) {
        logger.info("Suppression d'un observer $listener ")
        pcs.removePropertyChangeListener(listener)
    }

    override fun selectEndPoint(endpoint: String) {
        logger.info("Selection d'un endpoint")
        if (endpoint == "All" && prefix_url != "https://newsapi.org/v2/everything") {
            prefix_url = "https://newsapi.org/v2/everything"
            GlobalScope.launch { downloadArticleInformation() }
        } else if (endpoint != "All" && prefix_url != "https://newsapi.org/v2/top-headlines") {
            prefix_url = "https://newsapi.org/v2/top-headlines"
            q = null
            category = "general"
            GlobalScope.launch { downloadArticleInformation() }
        }
    }

    private fun downloadArticleInformation() {
        logger.info("Téléchargement des articles")
        val urlBuffer = StringBuilder("$prefix_url?")
        if (prefix_url?.contains("everything") == true) {
            if (q == null) {
                q = if (category != null) category else "all"
            }
            urlBuffer.append("q=$q")
            if (source != null) {
                urlBuffer.append("&sources=$source")
            }
            if (date != null) {
                urlBuffer.append("&from=$date")
            }
            if (language != null) {
                urlBuffer.append("&language=$language")
            }
            if (sortBy != null) {
                urlBuffer.append("&sortBy=$sortBy")
            }
        } else {
            if (category != null) {
                urlBuffer.append("category=$category")
            }
            if (q != null) {
                urlBuffer.append("&q=$q")
            }
            if (language != null) {
                urlBuffer.append("&country=$language")
            }
        }
        urlBuffer.append("&apiKey=$apiKey")
        println(urlBuffer.toString())
        val (_, response, result) = (urlBuffer.toString()).httpGet()
            .responseObject(ArticleInformation.Deserializer())
        logger.info("Status Code: ${response.statusCode}")
        result.let { (data, _) ->
            articleInformation = data?.copy(
                articles = data.articles.filterNot { it.title == "[Removed]" }
            )
        }
    }

    override fun changeCurrentSelection(id: String) {
        logger.info("Selection d'un article")
        selectedArticle = articleInformation?.articles?.find {
            it.title == id
        }
    }

    override fun sortArticleInformation(sort: String) {
        logger.info("Trie des articles")
        prefix_url = "https://newsapi.org/v2/everything"
        sortBy = sort.lowercase()
        if (sortBy == "newest") sortBy = "publishedAt"
        GlobalScope.launch { downloadArticleInformation() }
    }

    override fun findArticleByLanguage(lang: String) {
        logger.info("Recherche des articles en fonction de la langue")
        language = lang.lowercase()
        if(lang == "All") language = null
        GlobalScope.launch { downloadArticleInformation() }
    }

    override fun findArticleByCategory(cate: String) {
        logger.info("Recherche des articles en fonction de la langue")
        prefix_url = "https://newsapi.org/v2/top-headlines"
        q = null
        source = null
        language = null
        category = cate.lowercase()
        if (category == "all") category = "general"
        GlobalScope.launch { downloadArticleInformation() }
    }

    override fun findArticleBySource(src: String) {
        logger.info("Recherche des articles en fonction de la source")
        prefix_url = "https://newsapi.org/v2/everything"
        source = src
        if(src=="All") source = null
        GlobalScope.launch { downloadArticleInformation() }
    }

    override fun searchArticle(search: String) {
        logger.info("Recherche d'un article en fonction de mots clefs")
        q = if (search.isNotBlank()) search else "All"
        GlobalScope.launch { downloadArticleInformation() }
    }

    override fun exportArticleToPDF(filePath: String) {
        val document = Document()

        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }

            PdfWriter.getInstance(document, FileOutputStream(file))

            //Metadata
            document.apply {
                open()
                addTitle(selectedArticle?.title)
                if (selectedArticle?.source?.name != null) {
                    addCreator(selectedArticle?.source?.name)
                }
                if (selectedArticle?.author != null) {
                    addAuthor(selectedArticle?.author)
                }
            }

            val ArialUniFont = BaseFont.createFont("app/src/main/resources/font/Arial Unicode MS.ttf",  BaseFont.IDENTITY_H, BaseFont.EMBEDDED)

            //Title
            val title = Paragraph(selectedArticle?.title,Font(ArialUniFont, 20f, Font.BOLD)).apply {
                alignment = Element.ALIGN_CENTER
                spacingAfter = 10f
            }
            document.add(title)

            //Description
            document.add(Paragraph(selectedArticle?.description,Font(ArialUniFont, 12f)))

            //Image
            if (selectedArticle?.urlToImage != null) {
                val image = Image.getInstance(URL(selectedArticle?.urlToImage)).apply {
                    spacingBefore = 20f
                    spacingAfter = 20f
                    alignment = Element.ALIGN_CENTER
                    scaleToFit(document.pageSize.width - 50, document.pageSize.height)
                }
                document.add(image)
            }

            //Content
            val content = Paragraph(selectedArticle?.content,Font(ArialUniFont, 12f)).apply {
                spacingAfter = 5f
            }
            document.add(content)

            //Footer
            val footerFont = Font(ArialUniFont, 12f, Font.BOLD,BaseColor(41, 128, 185))

            //URL
            document.add(Paragraph("Reference : ${selectedArticle?.url}", footerFont))

            //Author
            document.add(Paragraph("Author : ${selectedArticle?.author}", footerFont))

            logger.info("Export de l'article au format PDF réussi")
        } catch (e: Exception) {
            logger.warn("Erreur durant l'export du PDF: ${e.printStackTrace()}")
            JOptionPane.showMessageDialog(
                null,
                "Error during the export process",
                "PDF Error",
                JOptionPane.ERROR_MESSAGE
            )
        } finally {
            document.close()
        }
    }

}