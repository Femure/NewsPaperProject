package org.isen.project.newspaper.model.data

import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ArticleInformationJUnitTest {

    @Test
    fun getArticlesFromJson(){
        val(request,response,result) = "https://newsapi.org/v2/everything?q=tesla&from=2023-12-03&sortBy=publishedAt&apiKey=8309d583f12e4887a867a21c8cf9fb95"
                .httpGet().responseObject(ArticleInformation.Deserializer())
        assertTrue(response.isSuccessful)
        val(si,error) = result
        if(si!= null){
            assertEquals("ok",si.status)
            assertNotEquals(0,si.totalResults)
            assertNotEquals(" ",si.articles[0].title)
        }else
            fail("error because article list must be not null")
    }
    @Test
    fun getFirstArticleInfoFromJson(){
        val(request,response,result) = "https://newsapi.org/v2/everything?q=tesla&from=2023-12-03&sortBy=publishedAt&apiKey=8309d583f12e4887a867a21c8cf9fb95"
                .httpGet().responseObject(ArticleInformation.Deserializer())
        assertTrue(response.isSuccessful)
        val(si,error) = result
        if(si!= null){
            assertEquals(null,si.articles[0].source.id)
            assertEquals("GlobeNewswire",si.articles[0].source.name)
            assertNotEquals(" ",si.articles[0].description)
            assertEquals("SkyQuest Technology Consulting Pvt. Ltd.",si.articles[0].author)
            assertEquals("Electric Car Market Set to Soar Past USD 1803149.65 Million by 2030",si.articles[0].title)
            assertNotEquals(" ",si.articles[0].description)
            assertEquals("https://www.globenewswire.com/news-release/2024/01/02/2802477/0/en/Electric-Car-Market-Set-to-Soar-Past-USD-1803149-65-Million-by-2030.html",si.articles[0].url)
            assertEquals("https://ml.globenewswire.com/Resource/Download/c673c6fa-f0e7-47bf-8490-fe528c593820",si.articles[0].urlToImage)
            assertEquals("2024-01-02T09:06:00Z",si.articles[0].publishedAt)
            assertNotEquals(" ",si.articles[0].content)
        }else
            fail("error because article list must be not null")
    }
}