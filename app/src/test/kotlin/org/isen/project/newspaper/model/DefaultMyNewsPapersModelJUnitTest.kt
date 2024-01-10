package org.isen.project.newspaper.model

import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.model.data.ArticleInformation
import org.isen.project.newspaper.model.impl.DefaultNewsPaperModel
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.beans.PropertyChangeListener
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DefaultNewsPaperModelJUnitTest {
    private var passObserver = false
    private var dataResult: Any? = null
    private val myObserver = PropertyChangeListener { evt ->
        passObserver = true
        dataResult = evt?.newValue
    }
    private val model = DefaultNewsPaperModel().apply {
        register(myObserver)
    }


    @Test
    fun selectEndPoint() {
        model.register(myObserver)
        model.selectEndPoint("All")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun sortArticleInformation() {
        model.register(myObserver)
        model.sortArticleInformation("Date")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun findArticleByLanguage() {
        model.register(myObserver)
        model.findArticleByLanguage("FR")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun findArticleByCategory() {
        model.register(myObserver)
        model.findArticleByCategory("Sports")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun findArticleBySource() {
        model.register(myObserver)
        model.selectEndPoint("All")
        model.findArticleBySource("bbc-news")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun searchArticle() {
        model.register(myObserver)
        model.searchArticle("bitcoin")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun changeCurrentSelection(){
        model.selectEndPoint("All")
        model.changeCurrentSelection("The End of One-Size-Fits-All Health Care")
        Thread.sleep(1000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInfo::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun exportArticleToPDF(){

        model.exportArticleToPDF("app/src/main/resources/pdf/test.pdf")
    }
}