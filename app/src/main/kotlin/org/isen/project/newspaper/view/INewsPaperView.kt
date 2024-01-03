package org.isen.project.newspaper.view

import java.beans.PropertyChangeListener

interface INewsPaperView : PropertyChangeListener {
        fun display()
        fun close()
}