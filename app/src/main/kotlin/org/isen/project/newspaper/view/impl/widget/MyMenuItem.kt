package org.isen.project.newspaper.view.impl.widget

import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JMenuItem

class MyMenuItem(name:String, actionListener:ActionListener): JMenuItem() {

    init {
        text = name
        if(name.length == 2){
            icon = ImageIcon(this::class.java.getResource("/icon/$name.png"))
        }
        addActionListener(actionListener)
    }

}
