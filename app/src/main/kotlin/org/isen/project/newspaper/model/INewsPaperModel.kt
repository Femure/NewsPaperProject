package org.isen.project.newspaper.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.beans.PropertyChangeListener

interface INewsPaperModel {

    companion object{
        const val DATATYPE_ARTICLE="article"
    }

    fun findArticleInformation()

    fun changeCurrentSelection(id: String)

    fun register(listener: PropertyChangeListener)

    fun unregister(listener: PropertyChangeListener)
}