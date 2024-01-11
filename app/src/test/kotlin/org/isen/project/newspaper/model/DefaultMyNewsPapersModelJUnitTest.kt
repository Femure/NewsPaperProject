package org.isen.project.newspaper.model

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
        model.selectEndPoint("All")
        Thread.sleep(5000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun sortArticleInformation() {
        model.sortArticleInformation("Date")
        Thread.sleep(5000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun findArticleByLanguage() {
        model.findArticleByLanguage("FR")
        Thread.sleep(5000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun findArticleByCategory() {
        model.findArticleByCategory("Sports")
        Thread.sleep(5000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }

    @Test
    fun searchArticle() {
        model.searchArticle("bitcoin")
        Thread.sleep(5000)

        assertTrue(passObserver, "after update model, observer must receive notification")
        dataResult?.let {
            assertEquals(ArticleInformation::class.java, it::class.java)
            assertNotEquals(0, (it as ArticleInformation).totalResults)
        } ?: fail("data result cannot be null")
    }
}