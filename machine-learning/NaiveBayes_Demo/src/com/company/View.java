package com.company;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

/**
 * Created by Pratama Agung on 6/17/2017.
 */
public class View {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private Controller testController;
    private ImageIcon spam;
    private ImageIcon not_spam;

    /**
     * Constructor of class View
     */
    public View(){
        prepareGUI();
        testController = new Controller();
        not_spam = new ImageIcon("src/data/img/no_spam.png");
        spam = new ImageIcon("src/data/img/spam.jpg");
    }

    /**
     * main program
     * @param args arguments
     */
    public static void main(String[] args){
        View view = new View();
        view.setContent();
    }

    /**
     * Method to prepare gui
     */
    private void prepareGUI(){
        mainFrame = new JFrame("SMSAssassin");
        mainFrame.setSize(600,600);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.getContentPane().setBackground(Color.WHITE);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3,1,10,10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        controlPanel.setBackground(Color.WHITE);

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    /**
     * Method to fill the gui
     */
    private void setContent() {
        headerLabel.setText("SMSAssassin");
        headerLabel.setFont(headerLabel.getFont().deriveFont(30.0f));
        ImageIcon smsIcon = new ImageIcon("src/data/img/spamassassinlogo.png");
        headerLabel.setIcon(smsIcon);

        JLabel commentlabel = new JLabel("Message: ", JLabel.CENTER);

        final JTextArea messageTextAres  = new JTextArea("", 5, 20);
        JScrollPane scrollPane = new JScrollPane(messageTextAres);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JButton testButton = new JButton("Test");
        testButton.setSize(10,10);
        testButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int res = testController.test(messageTextAres.getText());
                if (res == 0){
                    statusLabel.setText("I'm sure it's a spam");
                    statusLabel.setIcon(spam);
                } else if (res == 1) {
                    statusLabel.setText("It's likely to be a spam");
                    statusLabel.setIcon(spam);
                } else if (res == 2) {
                    statusLabel.setText("It's not likely to be a spam");
                    statusLabel.setIcon(not_spam);
                } else {
                    statusLabel.setText("I'm sure it is not a spam");
                    statusLabel.setIcon(not_spam);
                }
            }
        });
        controlPanel.add(commentlabel);
        controlPanel.add(scrollPane);
        controlPanel.add(testButton);

        mainFrame.setVisible(true);
    }
}
