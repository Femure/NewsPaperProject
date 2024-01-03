package org.isen.project.newspaper.view.impl

import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.apache.logging.log4j.kotlin.logger
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.model.data.ArticleInformation
import org.isen.project.newspaper.view.INewsPaperView
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.beans.PropertyChangeEvent
import java.io.File
import java.io.FileOutputStream
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder

class FocusNewPaper(InfoAffiche: ArticleInfo, title: String = "Lecture New Paper en particulier") :
        INewsPaperView, ActionListener {
    companion object Logging

    private val frame: JFrame

    init {
        frame = JFrame().apply {
            isVisible = true
            contentPane = makeGUIFocus(InfoAffiche)
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            this.title = title
            preferredSize = Dimension(900, 400)
            pack()
        }
    }

    override fun display() {
        frame.isVisible = true
    }

    override fun close() {
        frame.isVisible = false
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
        if (evt.newValue is ArticleInformation) {
            logger.info("Affichage détaillé NewsPapers Info")
        } else {
            logger.info("Unknown data")
        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        TODO("Not yet implemented")
    }

    public fun makeGUIFocus(InfoAAffiche: ArticleInfo): JPanel {
        val contentPane = JPanel(BorderLayout())

        val titleLabel = JLabel(InfoAAffiche.title)
        titleLabel.font = Font("Arial", Font.BOLD, 20)
        titleLabel.horizontalAlignment = SwingConstants.CENTER
        titleLabel.foreground = Color(41, 128, 185) // Couleur du texte similaire au renderer
        contentPane.add(titleLabel, BorderLayout.NORTH)

        val descriptionArea = JTextArea(InfoAAffiche.description)
        descriptionArea.font = Font("Arial", Font.PLAIN, 14)
        descriptionArea.lineWrap = true
        descriptionArea.wrapStyleWord = true
        descriptionArea.isEditable = false
        descriptionArea.background = Color(169, 169, 169) // Gris pour la couleur de fond
        descriptionArea.foreground = Color.white // Blanc pour la couleur du texte
        val descriptionScrollPane = JScrollPane(descriptionArea)
        contentPane.add(descriptionScrollPane, BorderLayout.SOUTH)

        val contentArea = JTextArea(InfoAAffiche.content)
        contentArea.font = Font("Arial", Font.PLAIN, 20)
        contentArea.lineWrap = true
        contentArea.wrapStyleWord = true
        contentArea.isEditable = false
        contentArea.background = Color(169, 169, 169) // Gris pour la couleur de fond
        contentArea.foreground = Color.white // Blanc pour la couleur du texte
        val contentScrollPane = JScrollPane(contentArea)
        contentPane.add(contentScrollPane, BorderLayout.CENTER)

        val exportButton = JButton("Exporter en PDF")
        exportButton.addActionListener {
            exportToPDF(InfoAAffiche.content, "C:\\Users\\valen\\Documents\\ProjetGradle\\AllemandProjectNewsPaper\\app\\src\\main\\SavingArticle\\" + InfoAAffiche.title + ".pdf")
            println("Debug path : " + "C:\\Users\\valen\\Documents\\ProjetGradle\\AllemandProjectNewsPaper\\app\\src\\main\\SavingArticle\\" + InfoAAffiche.title + ".pdf")
            // A remplacer avec le logger
        }
        exportButton.background = Color(41, 128, 185) // Bleu pour la couleur de fond du bouton
        exportButton.foreground = Color.white // Blanc pour la couleur du texte du bouton
        exportButton.border = CompoundBorder(LineBorder(Color(41, 128, 185), 1, true), EmptyBorder(5, 10, 5, 10))
        val buttonPanel = JPanel()
        buttonPanel.layout = GridLayout(1, 1)
        buttonPanel.add(exportButton)
        contentPane.add(buttonPanel, BorderLayout.LINE_END)

        return contentPane
    }

    fun exportToPDF(text: String, filePath: String) {
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
