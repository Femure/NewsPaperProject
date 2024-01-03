package org.isen.project.newspaper.view.impl

import org.isen.project.newspaper.model.data.ArticleInfo
import java.awt.Color
import java.awt.Component
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer
import javax.swing.border.EmptyBorder

class NewsPaperInfoRenderer:JLabel(),ListCellRenderer<ArticleInfo> {

    init {
        isOpaque = true
        horizontalAlignment = JLabel.LEFT
        verticalAlignment = JLabel.CENTER
        border = EmptyBorder(5, 10, 5, 10) // Ajoute des marges pour un meilleur espacement
        font = Font("Arial", Font.PLAIN, 14) // Utilise une police moderne
    }

    override fun getListCellRendererComponent(
        list: JList<out ArticleInfo>,
        value: ArticleInfo?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if (isSelected) {
            background = Color.LIGHT_GRAY
            foreground = list.foreground
        } else {
            background = list.background
            foreground = list.foreground
        }
        if (value != null) {
            text = "${value.title} - ${value.author}"
        }
        return this
    }
}