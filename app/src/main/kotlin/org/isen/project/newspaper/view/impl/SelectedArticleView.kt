package org.isen.project.newspaper.view.impl

import org.apache.logging.log4j.kotlin.logger
import org.isen.project.newspaper.ctl.NewsPaperController
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.view.INewsPaperView
import java.awt.*
import java.awt.event.*
import java.beans.PropertyChangeEvent
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.border.EmptyBorder


class SelectedArticleView(
    private val controller: NewsPaperController,
    private val articleInfo: ArticleInfo,
) :
    INewsPaperView, ActionListener, WindowAdapter() {
    companion object Logging

    private var imageLabel = JLabel()

    private val frame: JFrame

    init {
        frame = JFrame().apply {
            isVisible = true
            contentPane = makeGUIFocus()
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            addWindowListener(this@SelectedArticleView)
            title = "Focus on an article"
            setLocation(870, 0)
            preferredSize = Dimension(500, 400)
            pack()
        }
        this.controller.registerView(this)
    }

    private fun makeGUIFocus(): JPanel {
        val contentPane = JPanel(BorderLayout())

        //Header
        //Title
        val titleLabel = JLabel().apply {
            text = "<html>${articleInfo.title}</html>"
            font = Font("Arial Unicode MS", Font.BOLD, 20)
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
            foreground = Color(41, 128, 185)
            border = EmptyBorder(5, 10, 10, 10)
        }
        contentPane.add(titleLabel, BorderLayout.NORTH)

        //Body
        contentPane.add(body(contentPane), BorderLayout.CENTER)

        //Export button
        val exportButton = JButton().apply {
            text = "Exporter en PDF"
            addActionListener(this@SelectedArticleView)
            setSize(20, 20)
            background = Color(41, 128, 185)
            foreground = Color.white
        }
        contentPane.add(exportButton, BorderLayout.SOUTH)

        return contentPane
    }

    private fun body(contentPane: JPanel): JScrollPane {
        val bodyPanel = JPanel().apply {
            layout = GridBagLayout()
            background = Color.WHITE
            preferredSize = Dimension(0, 500)
        }

        //Description
        val descriptionPanel = JLabel().apply {
            text = "<html>${articleInfo.description}</html>"
            font = Font("Arial", Font.PLAIN, 18)
        }
        val gbDescription = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            weightx = 1.0
            weighty = 1.0
            anchor = GridBagConstraints.NORTH
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(5, 10, 0, 5)
        }
        bodyPanel.add(descriptionPanel, gbDescription)

        //Image
        val gbImage = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            weightx = 1.0
            weighty = 1.0
            anchor = GridBagConstraints.CENTER
            fill = GridBagConstraints.BOTH
        }
        bodyPanel.add(imageLabel, gbImage)

        contentPane.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                loadImageAndResize(bodyPanel)
            }
        })

        //Content
        val contentArea = JLabel().apply {
            text = "<html>${articleInfo.content}</html>"
            font = Font("Arial", Font.PLAIN, 15)
        }
        val gbContent = GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            weightx = 1.0
            weighty = 1.0
            anchor = GridBagConstraints.SOUTH
            fill = GridBagConstraints.BOTH
            insets = Insets(0, 10, 0, 5)
        }
        bodyPanel.add(contentArea, gbContent)

        //Footer
        //URL
        val urlLabel = JLabel().apply {
            text = "Reference : ${articleInfo.url}"
            font = Font("Arial", Font.BOLD, 12)
            horizontalAlignment = SwingConstants.LEFT
            foreground = Color(41, 128, 185)
        }
        val gbUrl = GridBagConstraints().apply {
            gridx = 0
            gridy = 3
            weightx = 1.0
            anchor = GridBagConstraints.CENTER
            fill = GridBagConstraints.BOTH
            insets = Insets(0, 10, 0, 5)
        }
        bodyPanel.add(urlLabel, gbUrl)

        //Author
        val authorLabel = JLabel().apply {
            text = "Author : ${articleInfo.author}"
            font = Font("Arial", Font.BOLD, 12)
            horizontalAlignment = SwingConstants.LEFT
            foreground = Color(41, 128, 185)
        }
        val gbAuthor = GridBagConstraints().apply {
            gridx = 0
            gridy = 4
            weightx = 1.0
            anchor = GridBagConstraints.CENTER
            fill = GridBagConstraints.BOTH
            insets = Insets(0, 10, 10, 5)
        }
        bodyPanel.add(authorLabel, gbAuthor)

        return JScrollPane(bodyPanel)
    }

    private fun loadImageAndResize(bodyPanel: JPanel) {
        if (articleInfo.urlToImage != null) {
            val image = ImageIO.read(URL(articleInfo.urlToImage))
            if (image != null) {
                imageLabel.icon = ImageIcon(
                    image.getScaledInstance(
                        (bodyPanel.width - 0.3 * bodyPanel.width).toInt(),
                        (bodyPanel.height - 0.5 * bodyPanel.height).toInt(),
                        Image.SCALE_SMOOTH
                    )
                )
                imageLabel.horizontalAlignment = SwingConstants.CENTER
            }
        }
    }

    override fun display() {
        frame.isVisible = true
    }

    override fun close() {
        frame.isVisible = false
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
    }

    override fun windowClosing(e: WindowEvent) {
    }


    override fun actionPerformed(e: ActionEvent) {
        if (e.actionCommand == "Exporter en PDF") {
            logger.info("Click sur le boutton d'export de PDF")
            val inputText = JOptionPane.showInputDialog(frame, "Save the PDF as :")

            if (inputText != null) {
                logger.info("Export d'un article au format pdf : app/src/main/resources/pdf/$inputText.pdf")
                controller.exportArticleToPDF("app/src/main/resources/pdf/$inputText.pdf")
            } else {
                logger.info("L'utilisateur a annulé l'export")
            }
        }
    }

}
