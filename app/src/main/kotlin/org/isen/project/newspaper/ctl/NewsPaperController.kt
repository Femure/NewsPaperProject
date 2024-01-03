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

    fun displayView(){
        views.forEach{
            it.display()
        }
    }

    fun closeView(){
        views.forEach{
            it.close()
        }
    }


    fun loadNewsPaperInformation(){
        this.model.findArticleInformation()
    }

    fun selectNewPaper(id:String){
        this.model.changeCurrentSelection(id)

    }
//    fun rechercheThemeNewPaper(theme :String){
//        this.model.findParticularArticleTheme(theme)
//    }
//    fun rechercheAuthorNewPaper(author :String){
//        this.model.findParticularArticleAuthor(author)
//    }
//    fun rechercherLanguageNewPaper(lang:String){
//        this.model.findParticularArticleLanguage(lang)
//    }
//    fun rechercherAllNewPaperAvance(theme:String,author:String,lang:String){
//        this.model.findAllParticularArticle(theme, author, lang)
//    }

}