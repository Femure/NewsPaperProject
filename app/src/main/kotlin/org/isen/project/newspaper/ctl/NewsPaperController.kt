package org.isen.project.newspaper.ctl

import org.isen.project.newspaper.model.INewsPaperModel
import org.isen.project.newspaper.view.INewsPaperView

class NewsPaperController(val model: INewsPaperModel) {
    private val views = mutableListOf<INewsPaperView>()
    companion object Logging

    fun registerView(v: INewsPaperView){
        this.views.add(v)
        this.model.register(v)
    }

    fun displayViews(){
        views.forEach{
            it.display()
        }
    }

    fun closeViews(){
        views.forEach{
            it.close()
        }
    }

    fun loadNewsPaperInformationByEndpoint(endpoint: String){
        this.model.selectEndPoint(endpoint)
    }

    fun selectNewsPaper(name:String){
        this.model.changeCurrentSelection(name)

    }
    fun sortNewsPaper(sort :String){
        this.model.sortArticleInformation(sort)
    }

    fun filterByLanguageNewsPaper(lang:String){
        this.model.findArticleByLanguage(lang)
    }
    fun filterBySourceNewsPaper(source: String){
        this.model.findArticleBySource(source)
    }

    fun searchNewsPaper(search:String){
        this.model.searchArticle(search)
    }

    fun filterByCategoryNewsPaper(category: String) {
        this.model.findArticleByCategory(category)
    }

    fun exportArticleToPDF(filePath: String){
        this.model.exportArticleToPDF(filePath)
    }

}