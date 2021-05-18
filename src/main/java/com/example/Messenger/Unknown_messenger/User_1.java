package com.example.Messenger.Unknown_messenger;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class User_1 extends JFrame implements ActionListener {

    String user_name;
    Socket chat_socket;
    PrintWriter pw;
    BufferedReader br;
    JTextArea chat_msg;
    JTextField chat_field;
    JButton send, exit;


    public User_1(String uname, String servername) throws Exception {
        super(uname);
        this.user_name = uname;
        chat_socket = new Socket(servername,80);
        br = new BufferedReader( new InputStreamReader( chat_socket.getInputStream()) ) ;
        pw = new PrintWriter(chat_socket.getOutputStream(),true);
        pw.println(uname);
        buildInterface();
        new MessagesThread().start();
    }

    public void buildInterface() throws SQLException {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chat_msg = new JTextArea();
        chat_msg.setRows(30);
        chat_msg.setColumns(50);
        chat_msg.setEditable(false);
        chat_field = new JTextField(50);
        JScrollPane sp = new JScrollPane(chat_msg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(chat_field);

        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Instant Messenger");
        add(bp,"North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == exit ) {
            pw.println("end");
            System.exit(0);
        }else {
            pw.println(chat_field.getText());
            chat_field.setText(null);
        }
    }

    public void saveInDB(String msg, String sender, String starrer){

    }

    class  MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while(true) {
                    line = br.readLine();
                    chat_msg.append(line + "\n");
                }
            } catch(Exception ex) {}
        }
    }

    public static void main(String[] args) {
        String userName = JOptionPane.showInputDialog(null,"Please enter your name to begin:", "Instant Chat Application",
                JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";
        try {
            new User_1( userName ,servername);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

