package org.isen.project.newspaper.model.data

import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ArticleInformationJUnitTest {

    @Test
    fun getArticlesFromJson(){
        val(_,response,result) = "https://newsapi.org/v2/everything?q=tesla&sortBy=publishedAt&apiKey=8309d583f12e4887a867a21c8cf9fb95"
                .httpGet().responseObject(ArticleInformation.Deserializer())
        assertTrue(response.isSuccessful)
        val(si,_) = result
        if(si!= null){
            assertEquals("ok",si.status)
            assertNotEquals(0,si.totalResults)
            assertNotEquals(" ",si.articles[0].title)
        }else
            fail("error because article list must be not null")
    }
    @Test
    fun getFirstArticleInfoFromJson(){
        val(_,response,result) = "https://newsapi.org/v2/everything?q=tesla&sortBy=publishedAt&apiKey=8309d583f12e4887a867a21c8cf9fb95"
                .httpGet().responseObject(ArticleInformation.Deserializer())
        assertTrue(response.isSuccessful)
        val(si,_) = result
        if(si!= null){
            assertNotNull(si.articles[0].source.name)
            assertNotNull(si.articles[0].description)
            assertNotNull(si.articles[0].author)
            assertNotNull(si.articles[0].title)
            assertNotNull(si.articles[0].description)
            assertNotNull(si.articles[0].url)
            assertNotNull(si.articles[0].publishedAt)
            assertNotNull(si.articles[0].content)
        }else
            fail("error because article list must be not null")
    }
}