package org.isen.project.newspaper.view.impl

import org.apache.logging.log4j.kotlin.logger
import org.isen.project.newspaper.ctl.NewsPaperController
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.view.INewsPaperView
import org.isen.project.newspaper.model.data.ArticleInformation
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.beans.PropertyChangeEvent
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.plaf.nimbus.NimbusLookAndFeel

class DefaultNewsPaperInfoView(private val controller:NewsPaperController, title:String="NewsPaper"):INewsPaperView,ActionListener, ListSelectionListener {

    companion object Logging
//    private val themeSearchButton = createModernButton("Rechercher par thème")
//    private val authorSearchButton = createModernButton("Rechercher par source (journal)")
//    private val languageSearchButton=createModernButton("Rechercher par langue")
//    private val AllSearchButton=createModernButton("Rechercher tous les JtextField Remplis")
//    private val themeTextField = createModernTextField()
//    private val authorTextField = createModernCenterTextField()
//    private val languageTextFiled=createModernTextField()
    private val renderer = NewsPaperInfoRenderer()

    private var articleList = JList<ArticleInfo>().apply {
        cellRenderer = renderer
        addListSelectionListener(this@DefaultNewsPaperInfoView)
    }



    private val frame: JFrame
    init {
        frame=JFrame().apply {
            isVisible=false
            contentPane=makeGUI()
            defaultCloseOperation=WindowConstants.EXIT_ON_CLOSE
            this.title=title
            this.preferredSize= Dimension(900,400)
            this.pack()
        }
        this.controller.registerView(this)
    }


    private fun makeGUI():JPanel{
        val contentPane=JPanel()
        contentPane.background = Color(245, 245, 220);
        contentPane.layout=BorderLayout()

        //Header
        // Appliquer le Look and Feel Nimbus
        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel())
        } catch (e: UnsupportedLookAndFeelException) {
            e.printStackTrace()
        }

        val header = JPanel()
        header.layout = BorderLayout()

        // Utilisation de JLabel pour le titre avec un style similaire au renderer
        val titleLabel = JLabel("Liste des articles:")
        titleLabel.font = Font("Arial", Font.BOLD, 16) // Utilisation d'une police en gras
        titleLabel.foreground = Color(41, 128, 185) // Couleur du texte similaire au renderer

        titleLabel.border = EmptyBorder(5, 10, 5, 10) // Marges pour un meilleur espacement


        // Ajout du titre et de la liste des articles au panel
        header.add(titleLabel, BorderLayout.WEST)
        header.add(JTextField("Search"))

        contentPane.add(header,BorderLayout.NORTH)

        // Body
//        val scrollPane = JScrollPane(articleList)
        contentPane.add(articleList, BorderLayout.CENTER)

        // Footer

        return contentPane
    }



    override fun display() {
        frame.isVisible=true
    }

    override fun close() {
        this.controller.closeView()
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
        if(evt.newValue is ArticleInformation){
            println("1")
            logger.info("reception NewsPapers Info")
            articleList.model = DefaultListModel<ArticleInfo>().apply {
                addAll((evt.newValue as ArticleInformation).articles)
            }

        }
        else{
            logger.info("unknow data")
        }
    }

    override fun valueChanged(e: ListSelectionEvent) {
        if(e.source is JList<*> && !e.valueIsAdjusting){
            if(articleList.selectedIndex != -1){
                println(articleList.selectedIndex)
                logger.info("click sur la list")
                val selectedNewsPaper = articleList.model.getElementAt(articleList.selectedIndex)
                this.controller.selectNewPaper(selectedNewsPaper.source.name)
                FocusNewsPaper(this.controller,selectedNewsPaper,articleList)
            }
        }
    }

    override fun actionPerformed(e: ActionEvent) {
//        else if(e.source==themeSearchButton){
//            logger.info("Click sur boutton recherche theme ${themeTextField.text}")
//            println("Click sur boutton recherche theme ${themeTextField.text}")
//            this.controller.rechercheThemeNewPaper(themeTextField.text)
//
//
//        }
//        else if(e.source==authorSearchButton){
//            logger.info("Click sur boutton recherche author ${authorTextField.text}")
//            println("Click sur boutton recherche author ${authorTextField.text}")
//            this.controller.rechercheAuthorNewPaper(authorTextField.text)
//        }
//        else if(e.source==languageSearchButton){
//            logger.info("Click sur boutton recherche language ${languageTextFiled.text}")
//            println("Click sur boutton recherche language ${languageTextFiled.text}")
//            this.controller.rechercherLanguageNewPaper(languageTextFiled.text)
//        }
//        else if(e.source==AllSearchButton){
//            logger.info("Click sur boutton recherche language : ${themeTextField.text},{authorTextField.text}, ${languageTextFiled.text}")
//
//            this.controller.rechercherAllNewPaperAvance(themeTextField.text,authorTextField.text,languageTextFiled.text)
//        }
    }

//    private fun createModernButton(text: String): JButton {
//        val button = JButton(text).apply {
//            styleModernButton(this)
//            addActionListener(this@DefaultNewsPaperInfoView)
//
//            // Ajout de l'effet de survol
//            addMouseListener(object : java.awt.event.MouseAdapter() {
//                override fun mouseEntered(e: java.awt.event.MouseEvent) {
//                    styleHoveredButton(this@apply)
//                }
//
//                override fun mouseExited(e: java.awt.event.MouseEvent) {
//                    styleModernButton(this@apply)
//                }
//            })
//        }
//
//        return button
//    }


//    private fun styleModernButton(button: JButton) {
//        button.background = Color(52, 152, 219)  // Couleur de fond
//        button.foreground = Color.white           // Couleur du texte
//        button.isFocusPainted = false             // Enlève la bordure de focus
//        button.font = Font("Arial", Font.BOLD, 14) // Police et taille du texte
//        button.isBorderPainted = false            // Enlève la bordure du bouton
//        button.cursor = Cursor(Cursor.HAND_CURSOR) // Curseur de type main
//        button.preferredSize = Dimension(150, 40)  // Taille préférée
//    }
//    private fun styleHoveredButton(button: JButton) {
//        button.background = Color(41, 128, 185) // Couleur de fond lors du survol
//        // Vous pouvez ajuster d'autres propriétés pour le survol selon vos préférences
//    }
//    fun createModernTextField(): JTextField {
//        val textField = JTextField().apply {
//            background = Color(169, 169, 169) // Gris pour la couleur de fond
//            foreground = Color.white // Blanc pour la couleur du texte
//            font = Font("Arial", Font.PLAIN, 12) // Police et taille du texte
//
//            // Centrer le texte horizontalement
//            horizontalAlignment = JTextField.CENTER
//
//            // Bordure intérieure et extérieure
//            border = CompoundBorder(LineBorder(Color(0, 0, 0), 1, false), EmptyBorder(5, 5, 5, 5))
//
//            caretColor = Color(41, 128, 185) // Couleur du curseur
//            preferredSize = Dimension(200, 30) // Taille préférée
//        }
//
//        return textField
//    }
//    fun createModernCenterTextField(): JTextField {
//        val textField = JTextField().apply {
//            background = Color(169, 169, 169) // Gris pour la couleur de fond
//            foreground = Color.white // Blanc pour la couleur du texte
//            font = Font("Arial", Font.PLAIN, 12) // Police et taille du texte
//
//            // Centrer le texte horizontalement
//            horizontalAlignment = JTextField.CENTER
//
//            // Bordure intérieure et extérieure
//            border = CompoundBorder(LineBorder(Color(0, 0, 0), 1, false), EmptyBorder(0, 5, 0, 5))
//
//            caretColor = Color(41, 128, 185) // Couleur du curseur
//            preferredSize = Dimension(200, 30) // Taille préférée
//        }
//
//        return textField
//    }
//    fun createModernCenteredLabel(text: String): JLabel {
//        val label = JLabel(text).apply {
//            foreground = Color(41, 128, 185) // Couleur du texte
//            background=Color(245, 245, 220);
//            font = Font("Arial", Font.BOLD, 16) // Police et taille du texte
//            horizontalAlignment = JLabel.CENTER // Centre le texte horizontalement
//            verticalAlignment = JLabel.CENTER // Centre le texte verticalement
//            preferredSize = Dimension(200, 30)  // Taille préférée
//        }
//
//        return label
//    }

}
