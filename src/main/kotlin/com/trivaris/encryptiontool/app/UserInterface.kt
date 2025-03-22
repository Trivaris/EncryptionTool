package com.trivaris.encryptiontool.app

import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.net.InetAddress
import java.util.Calendar
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE
import java.util.Calendar.SECOND
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField

class UserInterface() {
    private lateinit var messageArea: JTextArea
    private val listeners = mutableListOf<InputListener>()

    fun show() {
        val frame = JFrame("Secure Communication")

        frame.layout = BorderLayout()
        frame.setSize(800, 450)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        // Create message display area
        messageArea = JTextArea()
        messageArea.isEditable = false
        frame.add(JScrollPane(messageArea), BorderLayout.CENTER)

        // Panel for input and buttons
        val panel = JPanel(GridBagLayout())
        val constraints = GridBagConstraints()
        constraints.insets = Insets(5, 5, 5, 5) // Padding

        val recipientField = JTextField("localhost", 15)
        val inputField = JTextField("Hello, World!", 15)
        val sendButton = JButton("Send")
        val recipientLabel = JLabel("Recipient IP:")
        val messageLabel = JLabel("Message:")

        // First row: Recipient label and field
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.anchor = GridBagConstraints.WEST
        panel.add(recipientLabel, constraints)

        constraints.gridx = 1
        constraints.fill = GridBagConstraints.HORIZONTAL
        panel.add(recipientField, constraints)

        // Second row: Message label and field
        constraints.gridx = 0
        constraints.gridy = 1
        constraints.fill = GridBagConstraints.NONE
        panel.add(messageLabel, constraints)

        constraints.gridx = 1
        constraints.fill = GridBagConstraints.HORIZONTAL
        panel.add(inputField, constraints)

        // Third row: Send button (centered)
        constraints.gridx = 0
        constraints.gridy = 2
        constraints.gridwidth = 2
        constraints.fill = GridBagConstraints.NONE
        constraints.anchor = GridBagConstraints.CENTER
        panel.add(sendButton, constraints)

        frame.add(panel, BorderLayout.SOUTH)
        frame.isVisible = true

        sendButton.addActionListener {
            val recipient = InetAddress.getByName(recipientField.text)
            listeners.forEach { it.onSendButtonClicked(inputField.text, recipient) }
        }

        fun scaleFonts(width: Int, height: Int) {
            val baseSize = minOf(width, height) / 30
            val newFont = Font("Arial", Font.PLAIN, baseSize)
            val labelFont = Font("Arial", Font.BOLD, baseSize)

            recipientLabel.font = labelFont
            messageLabel.font = labelFont
            recipientField.font = newFont
            inputField.font = newFont
            sendButton.font = newFont
            messageArea.font = newFont
        }

        frame.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                scaleFonts(frame.width, frame.height)
            }
        })

        frame.isVisible = true
        scaleFonts(frame.width, frame.height)
    }

    fun addListener(listener: InputListener) {
        listeners.add(listener)
    }

    fun appendMessage(message: String) =
        messageArea.append("${getTime()} | $message \n")

    fun getTime(): String {
        val cal = Calendar.getInstance()
        return "%02d:%02d:%02d".format(cal.get(HOUR_OF_DAY), cal.get(MINUTE), cal.get(SECOND))
    }
}