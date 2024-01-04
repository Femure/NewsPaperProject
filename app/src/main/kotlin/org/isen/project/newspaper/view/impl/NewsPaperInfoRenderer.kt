package org.isen.project.newspaper.view.impl

import org.isen.project.newspaper.model.data.ArticleInfo
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class NewsPaperInfoRenderer : JPanel(), ListCellRenderer<ArticleInfo> {


    private val titleLabel = JLabel()
    private val publishedAtLabel = JLabel()
    private val authorLabel = JLabel()

    init {
        isOpaque = true
        border = EmptyBorder(5, 10, 5, 10)

        titleLabel.font = Font("Arial", Font.BOLD, 16)
        publishedAtLabel.font = Font("Arial", Font.ITALIC, 10)
        authorLabel.font = Font("Arial", Font.PLAIN, 14)
        authorLabel.border = EmptyBorder(5, 0, 0, 0)

        layout = GridLayout(2, 2)
        add(titleLabel)
        add(publishedAtLabel)
        add(authorLabel)
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
            titleLabel.text = value.title
            publishedAtLabel.text = value.publishedAt
            authorLabel.text = value.author
        }


        return this
    }


}