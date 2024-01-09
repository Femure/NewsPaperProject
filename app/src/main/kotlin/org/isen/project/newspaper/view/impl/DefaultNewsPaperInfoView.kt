package org.isen.project.newspaper.view.impl

import org.apache.logging.log4j.kotlin.logger
import org.isen.project.newspaper.ctl.NewsPaperController
import org.isen.project.newspaper.model.data.ArticleInfo
import org.isen.project.newspaper.model.data.ArticleInformation
import org.isen.project.newspaper.view.INewsPaperView
import org.isen.project.newspaper.view.impl.widget.MyMenuItem
import org.isen.project.newspaper.view.impl.widget.NewsPaperInfoRenderer
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.beans.PropertyChangeEvent
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.plaf.nimbus.NimbusLookAndFeel


class DefaultNewsPaperInfoView(private val controller: NewsPaperController, title: String = "NewsPaper") : INewsPaperView, ActionListener, ListSelectionListener {

    companion object Logging

    private val renderer = NewsPaperInfoRenderer()

    private var articleList = JList<ArticleInfo>().apply {
        cellRenderer = renderer
        addListSelectionListener(this@DefaultNewsPaperInfoView)
    }

    private val searchArea = JTextArea().apply {
        layout = FlowLayout()
        margin = Insets(5, 5, 5, 5)
    }

    private var listEndpoint = JComboBox<String>().apply {
        model = DefaultComboBoxModel(listOf("All", "Top headlines").toTypedArray())
        addActionListener(this@DefaultNewsPaperInfoView)
    }

    private val listLanguage = listOf("AR", "DE", "EN", "ES", "FR", "HE", "IT", "NL", "NO", "PT", "RU", "SV", "ZH")
    private val listSort = listOf("Popularity", "Relevancy", "Newest")
    private val listCategory = listOf("General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology")

    private val frame: JFrame

    init {
        frame = JFrame().apply {
            isVisible = false
            contentPane = makeGUI()
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            this.title = title
            this.preferredSize = Dimension(900, 400)
            this.pack()
        }
        this.controller.registerView(this)
    }


    private fun makeGUI(): JPanel {
        // Appliquer le Look and Feel Nimbus
        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel())
        } catch (e: UnsupportedLookAndFeelException) {
            e.printStackTrace()
        }

        val contentPane = JPanel().apply {
            background = Color(245, 245, 220)
            layout = BorderLayout()

        }

        //Header
        contentPane.add(header(), BorderLayout.NORTH)

        // Body
        contentPane.add(JScrollPane(articleList), BorderLayout.CENTER)

        return contentPane
    }

    private fun header(): JPanel {
        val header = JPanel(GridBagLayout())

        val filterLabel = JLabel().apply {
            text = "Filter "
            font = Font("Arial", Font.BOLD, 16)
            foreground = Color(41, 128, 185)
        }
        val gbFilter = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            anchor = GridBagConstraints.WEST
            insets = Insets(10, 10, 5, 0)
        }
        header.add(filterLabel, gbFilter)

        val gbEndpoint = GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            anchor = GridBagConstraints.WEST
            insets = Insets(10, 0, 5, 0)
        }
        header.add(listEndpoint, gbEndpoint)


        // Menu bar
        val menuBar = JMenuBar().apply {
            add(menu("Category"))
            add(JSeparator(SwingConstants.VERTICAL))
            add(menu("Language"))
            add(JSeparator(SwingConstants.VERTICAL))
            add(menu("Sort"))
        }

        val gbMenuBar = GridBagConstraints().apply {
            gridx = 2
            gridy = 0
            weightx = 1.0
            anchor = GridBagConstraints.CENTER
            insets = Insets(10, 0, 5, 5)
        }

        header.add(menuBar, gbMenuBar)

        val titleLabel = JLabel().apply {
            text = "Liste des articles"
            font = Font("Arial", Font.BOLD, 16)
            foreground = Color(41, 128, 185)
        }

        val gbTitle = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            anchor = GridBagConstraints.WEST
            insets = Insets(0, 10, 5, 5)
        }

        header.add(titleLabel, gbTitle)

        // Search bar
        val searchBar = JPanel(BorderLayout())

        searchBar.add(searchArea, BorderLayout.CENTER)

        val searchButton = JButton().apply {
            text = "Search"
            addActionListener(this@DefaultNewsPaperInfoView)
        }
        searchBar.add(searchButton, BorderLayout.EAST)

        val gbSearch = GridBagConstraints().apply {
            gridx = 2
            gridy = 1
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            insets = Insets(0, 0, 10, 0)
        }

        header.add(searchBar, gbSearch)

        return header
    }

    private fun menu(nom: String): JMenu {
        val menu = JMenu(nom)
        menu.margin = Insets(0, 50, 0, 50)
        menu.font = Font("Arial", Font.BOLD, 12)

        when (nom) {
            "Language" -> {
                listLanguage.forEach {
                    menu.add(MyMenuItem(it, this@DefaultNewsPaperInfoView))
                }
            }
            "Sort" -> {
                listSort.forEach {
                    menu.add(MyMenuItem(it, this@DefaultNewsPaperInfoView))
                }
            }
            else -> {
                listCategory.forEach {
                    menu.add(MyMenuItem(it, this@DefaultNewsPaperInfoView))
                }
            }
        }

        return menu
    }

    override fun display() {
        frame.isVisible = true
    }

    override fun close() {
        controller.closeViews()
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
        if (evt.newValue is ArticleInformation) {
            logger.info("Reception de la liste d'articles")
            articleList.model = DefaultListModel<ArticleInfo>().apply {
                addAll((evt.newValue as ArticleInformation).articles)
            }
        }
        else if(evt.newValue is ArticleInfo) {
            logger.info("Reception de l'article selectionné")
            SelectedArticleView(this.controller, evt.newValue as ArticleInfo)
        }else{
            logger.warn("Data inconnue")
        }
    }

    override fun valueChanged(e: ListSelectionEvent) {
        if (e.source is JList<*> && !e.valueIsAdjusting) {
            if (articleList.selectedIndex != -1) {
                logger.info("Click sur un article")
                this.controller.selectNewsPaper(articleList.model.getElementAt(articleList.selectedIndex).title)
            }
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        when (e.actionCommand) {
            in listSort -> {
                logger.info("Ordonne la liste")
                this.controller.sortNewsPaper(e.actionCommand)
            }
            in listLanguage -> {
                logger.info("Filtre par langue")
                this.controller.filterByLanguageNewsPaper(e.actionCommand)
            }
            in listCategory -> {
                logger.info("Filtre par category")
                this.controller.filterByCategoryNewsPaper(e.actionCommand)
            }
            "Search" -> {
                logger.info("Fait une recherche : ${searchArea.text}")
                this.controller.searchNewsPaper(searchArea.text)
            }
            else -> {
                logger.info("Change de endpoint")
                this.controller.loadNewsPaperInformationByEndpoint(listEndpoint.model.getElementAt(listEndpoint.selectedIndex))
            }
        }
    }
}
