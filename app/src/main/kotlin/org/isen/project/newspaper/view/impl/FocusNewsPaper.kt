package org.isen.project.newspaper.view.impl

import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.apache.logging.log4j.kotlin.logger
import org.isen.project.newspaper.ctl.NewsPaperController
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.model.data.ArticleInformation
import org.isen.project.newspaper.view.INewsPaperView
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.beans.PropertyChangeEvent
import java.io.File
import java.io.FileOutputStream
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder


class FocusNewsPaper(private val controller: NewsPaperController,private val articleInfo: ArticleInfo,private val articleList:JList<*>) :
        INewsPaperView, ActionListener, WindowAdapter() {
    companion object Logging

    private val frame: JFrame

    init {
        frame = JFrame().apply {
            isVisible = true
            contentPane = makeGUIFocus()
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            addWindowListener(this@FocusNewsPaper)
            this.title = "Focus on an article"
            setLocation(870,0)
            preferredSize = Dimension(500, 400)
            pack()
        }
        this.controller.registerView(this)
    }

    override fun display() {
        frame.isVisible = true
    }

    override fun close() {
        frame.isVisible = false
    }

    override fun windowClosing(e: WindowEvent) {
        articleList.clearSelection()
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
        if (evt.newValue is ArticleInformation) {
            logger.info("Affichage détaillé NewsPapers Info")
        } else {
            logger.info("Unknown data")
        }
    }

    override fun actionPerformed(e: ActionEvent) {
       if(e.actionCommand == "Exporter en PDF"){
           exportToPDF(articleInfo.content, "app/src/main/resources" + articleInfo.title + ".pdf")
           println("Debug path : " + "app/src/main/resources" + articleInfo.title + ".pdf")
       }
    }

    private fun makeGUIFocus(): JPanel {
        val contentPane = JPanel(BorderLayout())

        val titleLabel = JLabel(articleInfo.title)
        titleLabel.font = Font("Arial", Font.BOLD, 20)
        titleLabel.horizontalAlignment = SwingConstants.CENTER
        titleLabel.foreground = Color(41, 128, 185) // Couleur du texte similaire au renderer
        contentPane.add(titleLabel, BorderLayout.NORTH)

        val contentArea = JTextArea(articleInfo.content)
        contentArea.font = Font("Arial", Font.PLAIN, 20)
        contentArea.lineWrap = true
        contentArea.wrapStyleWord = true
        contentArea.isEditable = false
        contentArea.background = Color(169, 169, 169) // Gris pour la couleur de fond
        contentArea.foreground = Color.white // Blanc pour la couleur du texte
        val contentScrollPane = JScrollPane(contentArea)
        contentPane.add(contentScrollPane, BorderLayout.CENTER)

        val descriptionArea = JTextArea(articleInfo.description)
        descriptionArea.font = Font("Arial", Font.PLAIN, 14)
        descriptionArea.lineWrap = true
        descriptionArea.wrapStyleWord = true
        descriptionArea.isEditable = false
        descriptionArea.background = Color(169, 169, 169) // Gris pour la couleur de fond
        descriptionArea.foreground = Color.white // Blanc pour la couleur du texte


        val exportButton = JButton("Exporter en PDF")
        exportButton.addActionListener(this)
        exportButton.setSize(20,20)
        exportButton.background = Color(41, 128, 185) // Bleu pour la couleur de fond du bouton
        exportButton.foreground = Color.white // Blanc pour la couleur du texte du bouton
        exportButton.border = CompoundBorder(LineBorder(Color(41, 128, 185), 1, true), EmptyBorder(5, 10, 5, 10))

        val descriptionPanel = JPanel()
//        descriptionPanel.layout = GridLayout(2,1)
        descriptionPanel.add(descriptionArea)
        descriptionPanel.add(exportButton)

        val descriptionScrollPane = JScrollPane(descriptionPanel)
        contentPane.add(descriptionScrollPane, BorderLayout.SOUTH)

        return contentPane
    }

    private fun exportToPDF(text: String, filePath: String) {
        val document = Document()

        try {
            // Vérifier si le fichier existe
            val file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }

            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            // Ajouter le texte dans le document PDF
            document.add(Paragraph(text))
        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation en PDF", "Erreur PDF", JOptionPane.ERROR_MESSAGE)
        } finally {
            document.close()
        }
    }
}
