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
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder
import javax.swing.plaf.nimbus.NimbusLookAndFeel

class DefaultNewsPaperInfoView(val controller:NewsPaperController, title:String="NewsPaper"):INewsPaperView,ActionListener {

    companion object Logging
    private val themeSearchButton = createModernButton("Rechercher par thème")
    private val authorSearchButton = createModernButton("Rechercher par source (journal)")
    private val languageSearchButton=createModernButton("Rechercher par langue")
    private val AllSearchButton=createModernButton("Rechercher tous les JtextField Remplis")
    private val themeTextField = createModernTextField()
    private val authorTextField = createModernCenterTextField()
    private val languageTextFiled=createModernTextField()

    private var articleList:JComboBox<ArticleInfo> = JComboBox<ArticleInfo>().apply{
        addActionListener(this@DefaultNewsPaperInfoView)
        renderer= NewsPaperInfoRenderer()
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



    private fun createNewsPapersComboBox(): JPanel {
        // Appliquer le Look and Feel Nimbus
        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel())
        } catch (e: UnsupportedLookAndFeelException) {
            e.printStackTrace()
        }

        val contentPane = JPanel()
        contentPane.layout = BorderLayout()

        // Utilisation de JLabel pour le titre avec un style similaire au renderer
        val titleLabel = JLabel("Liste des articles:")
        titleLabel.font = Font("Arial", Font.BOLD, 16) // Utilisation d'une police en gras
        titleLabel.foreground = Color(41, 128, 185) // Couleur du texte similaire au renderer

        titleLabel.border = EmptyBorder(5, 10, 5, 10) // Marges pour un meilleur espacement

        articleList.border = EmptyBorder(5, 5, 5, 5)

        // Ajout du titre et de la liste des articles au panel
        contentPane.add(titleLabel, BorderLayout.WEST)
        contentPane.add(articleList, BorderLayout.CENTER)

        return contentPane
    }
    private fun makeGUI():JPanel{
        val contentPane=JPanel()
        contentPane.background = Color(245, 245, 220);
        contentPane.layout=BorderLayout()
        contentPane.add(createNewsPapersComboBox(),BorderLayout.NORTH)
        // Partie centrale : Recherche d'articles précis
        val searchPanel = createSearchPanel()
        contentPane.add(searchPanel, BorderLayout.CENTER)
        // Partie inférieure : Affichage du nombre d'articles trouvés

        return contentPane
    }
    private fun createSearchPanel(): JPanel {
        val searchPanel = JPanel()
        searchPanel.layout = GridLayout(4, 2) // 2 lignes et 4 colonnes (2 pour les champs texte, 3 pour les boutons)

        // Composants pour le thème
        val themeLabel = createModernCenteredLabel("Thème:")
        themeSearchButton.addActionListener(this)

        // Composants pour l'auteur
        val authorLabel = createModernCenteredLabel("Source:")
        authorSearchButton.addActionListener(this)

        // Composants pour la langue
        val languageLabel = createModernCenteredLabel("Langue:")
        languageSearchButton.addActionListener(this)
        AllSearchButton.addActionListener(this)

        // Ajoutez les composants au panel
        searchPanel.add(themeLabel)
        searchPanel.add(themeTextField)
        searchPanel.add(themeSearchButton)
        searchPanel.add(authorLabel)
        searchPanel.add(authorTextField)
        searchPanel.add(authorSearchButton)
        searchPanel.add(languageLabel)
        searchPanel.add(languageTextFiled)
        searchPanel.add(languageSearchButton)
        searchPanel.add(AllSearchButton)

        // Ajoutez d'autres composants ou critères de recherche selon vos besoins

        return searchPanel
    }



    override fun display() {
        frame.isVisible=true
    }

    override fun close() {
        this.controller.closeView()
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
        if(evt.newValue is ArticleInformation){
            println("Reception NewPapersInfo : ${evt.newValue}")
            logger.info("reception NewsPapers Info")
            articleList.model = DefaultComboBoxModel<ArticleInfo>((evt.newValue as ArticleInformation).articles.toTypedArray())

        }
        else{
            logger.info("unknow data")
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        if(e.source is JComboBox<*>){
            logger.info("click sur la combobox")
            articleList.model.getElementAt(articleList.selectedIndex).source.id?.let {
                this.controller.selectNewPaper(
                        it

                )
            }
            val selectedNewsPaper = articleList.model.getElementAt(articleList.selectedIndex)
            val newWindow = FocusNewPaper(selectedNewsPaper,selectedNewsPaper.source.id+"-"+selectedNewsPaper.title)


        }
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

    private fun createModernButton(text: String): JButton {
        val button = JButton(text).apply {
            styleModernButton(this)
            addActionListener(this@DefaultNewsPaperInfoView)

            // Ajout de l'effet de survol
            addMouseListener(object : java.awt.event.MouseAdapter() {
                override fun mouseEntered(e: java.awt.event.MouseEvent) {
                    styleHoveredButton(this@apply)
                }

                override fun mouseExited(e: java.awt.event.MouseEvent) {
                    styleModernButton(this@apply)
                }
            })
        }

        return button
    }


    private fun styleModernButton(button: JButton) {
        button.background = Color(52, 152, 219)  // Couleur de fond
        button.foreground = Color.white           // Couleur du texte
        button.isFocusPainted = false             // Enlève la bordure de focus
        button.font = Font("Arial", Font.BOLD, 14) // Police et taille du texte
        button.isBorderPainted = false            // Enlève la bordure du bouton
        button.cursor = Cursor(Cursor.HAND_CURSOR) // Curseur de type main
        button.preferredSize = Dimension(150, 40)  // Taille préférée
    }
    private fun styleHoveredButton(button: JButton) {
        button.background = Color(41, 128, 185) // Couleur de fond lors du survol
        // Vous pouvez ajuster d'autres propriétés pour le survol selon vos préférences
    }
    fun createModernTextField(): JTextField {
        val textField = JTextField().apply {
            background = Color(169, 169, 169) // Gris pour la couleur de fond
            foreground = Color.white // Blanc pour la couleur du texte
            font = Font("Arial", Font.PLAIN, 12) // Police et taille du texte

            // Centrer le texte horizontalement
            horizontalAlignment = JTextField.CENTER

            // Bordure intérieure et extérieure
            border = CompoundBorder(LineBorder(Color(0, 0, 0), 1, false), EmptyBorder(5, 5, 5, 5))

            caretColor = Color(41, 128, 185) // Couleur du curseur
            preferredSize = Dimension(200, 30) // Taille préférée
        }

        return textField
    }
    fun createModernCenterTextField(): JTextField {
        val textField = JTextField().apply {
            background = Color(169, 169, 169) // Gris pour la couleur de fond
            foreground = Color.white // Blanc pour la couleur du texte
            font = Font("Arial", Font.PLAIN, 12) // Police et taille du texte

            // Centrer le texte horizontalement
            horizontalAlignment = JTextField.CENTER

            // Bordure intérieure et extérieure
            border = CompoundBorder(LineBorder(Color(0, 0, 0), 1, false), EmptyBorder(0, 5, 0, 5))

            caretColor = Color(41, 128, 185) // Couleur du curseur
            preferredSize = Dimension(200, 30) // Taille préférée
        }

        return textField
    }
    fun createModernCenteredLabel(text: String): JLabel {
        val label = JLabel(text).apply {
            foreground = Color(41, 128, 185) // Couleur du texte
            background=Color(245, 245, 220);
            font = Font("Arial", Font.BOLD, 16) // Police et taille du texte
            horizontalAlignment = JLabel.CENTER // Centre le texte horizontalement
            verticalAlignment = JLabel.CENTER // Centre le texte verticalement
            preferredSize = Dimension(200, 30)  // Taille préférée
        }

        return label
    }
}
