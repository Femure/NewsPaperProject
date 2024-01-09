/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package org.isen.project.newspaper

import org.isen.project.newspaper.ctl.NewsPaperController
import org.isen.project.newspaper.model.impl.DefaultNewsPaperModel
import org.isen.project.newspaper.view.impl.DefaultNewsPaperInfoView


fun main() {
    val newsPaperModel = DefaultNewsPaperModel()
    val newsPaperController = NewsPaperController(newsPaperModel)
    DefaultNewsPaperInfoView(newsPaperController, "News Papers Application")
    newsPaperController.displayViews()
    newsPaperController.loadNewsPaperInformationByEndpoint("headline")
}

