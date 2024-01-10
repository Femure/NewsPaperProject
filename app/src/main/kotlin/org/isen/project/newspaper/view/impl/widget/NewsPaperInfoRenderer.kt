package org.isen.project.newspaper.view.impl.widget

import org.isen.project.newspaper.model.data.ArticleInfo
import java.awt.*
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer
import javax.swing.border.EmptyBorder

class NewsPaperInfoRenderer : JPanel(), ListCellRenderer<ArticleInfo> {

    private val titleLabel = JLabel()
    private val publishedAtLabel = JLabel()
    private val authorLabel = JLabel()

    init {

        isOpaque = true
        border = EmptyBorder(5, 10, 5, 10)
        preferredSize = Dimension(0,60)


        titleLabel.font = Font("Arial Unicode MS", Font.BOLD, 16)
        publishedAtLabel.font = Font("Arial Unicode MS", Font.ITALIC, 10)
        authorLabel.font = Font("Arial Unicode MS", Font.PLAIN, 14)
        layout = GridBagLayout()

        //Title
        val gbTitle = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            weightx = 1.0
            anchor = GridBagConstraints.WEST
            fill = GridBagConstraints.BOTH
            insets = Insets(5, 10, 0, 0)
        }
        add(titleLabel,gbTitle)

        //Publication date
        val gbDate = GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            anchor = GridBagConstraints.EAST
            insets = Insets(5, 5, 0, 10)
        }
        add(publishedAtLabel,gbDate)

        //Author
        val gbAuthor = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            anchor = GridBagConstraints.WEST
            insets = Insets(5, 10, 10, 10)
        }
        add(authorLabel,gbAuthor)
    }

    override fun getListCellRendererComponent(
            list: JList<out ArticleInfo>,
            value: ArticleInfo?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean,
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
            authorLabel.text = "Source : ${value.source.name}"
        }


        return this
    }


}