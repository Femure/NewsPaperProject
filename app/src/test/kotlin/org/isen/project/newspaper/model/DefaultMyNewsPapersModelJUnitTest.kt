package org.isen.project.newspaper.model

import org.isen.project.newspaper.model.data.ArticleInformation
import org.isen.project.newspaper.model.impl.DefaultNewsPaperModel
import org.junit.jupiter.api.Test
import java.beans.PropertyChangeListener
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class DefaultNewsPaperModelJUnitTest {

    @Test
    fun findNewsPapersInformation(){
        var passObserver:Boolean = false
        var data_result:Any?=null
        val model = DefaultNewsPaperModel()
        var myObserver = PropertyChangeListener{ evt ->
            passObserver = true
            data_result=evt?.newValue ?: null

        }
        model.register(myObserver)
        model.findArticleInformation()
        Thread.sleep(10000)
        assertTrue(passObserver,"after update model, observer must receive notification")
        data_result?.let{
            assertEquals("ok",(it as ArticleInformation).status)
            assertEquals(ArticleInformation::class.java, it ::class.java)
        }?: fail("data_result cannot be null")
    }
}