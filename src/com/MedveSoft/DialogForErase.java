package com.MedveSoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A di�n val� t�rl�sek t�pus�nak kiv�laszt�s�t lehet�v� tev� p�rbesz�dablak.
 */
public class DialogForErase extends JDialog implements ActionListener {

    /**
     * az ablak tartalompanelje
     */
    private JPanel cp = (JPanel) getContentPane();
    /**
     * a v�lasztott nyom�gomb k�dja
     */
    private int option;
    /**
     * a megjelen�tend� k�rd�s fel�rata
     */
    private JLabel lbMessage = new JLabel(" ");
    /**
     * Ok nyom�gomb
     */
    private JButton btOk;
    /**
     * M�gse nyom�gomb
     */
    private JButton btCancel;
    /**
     * a r�di�gombos v�laszt�s eredm�nye
     */
    private int result = 0;
    private JRadioButton rbBoardClear = new JRadioButton("Szerkeszt�st + k�pleteket");
    private JRadioButton rbFoilClear = new JRadioButton("K�z�r�st");
    private JRadioButton rbPoligonClear = new JRadioButton("Kijel�l� soksz�geket");
    private JRadioButton rbDiaClear = new JRadioButton("Teljes di�t");

    /**
     * Konstruktor.
     * @param parent sz�l� ablak
     * @param title c�msor sz�vegez�se
     */
    public DialogForErase(JFrame parent, String title) {
        super(parent, title, true);
        ButtonGroup bg = new ButtonGroup();

        cp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        URLClassLoader urlLoader = (URLClassLoader) DialogForErase.class.getClassLoader();
        URL url = urlLoader.findResource("icons/question.gif");

        JLabel lbIcon = new JLabel(new ImageIcon(url));
        lbIcon.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cp.add(lbIcon, "West");

        url = urlLoader.findResource("icons/program.jpg");
        ImageIcon icon = new ImageIcon(url);
        this.setIconImage(icon.getImage());

        JPanel pnBody = new JPanel();
        cp.add(pnBody, "Center");
        pnBody.setLayout(new GridLayout(0, 1));
        pnBody.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnBody.add(lbMessage);
        bg.add(rbBoardClear);
        bg.add(rbFoilClear);
        bg.add(rbPoligonClear);
        bg.add(rbDiaClear);
        pnBody.add(rbBoardClear);
        pnBody.add(rbFoilClear);
        pnBody.add(rbPoligonClear);
        pnBody.add(rbDiaClear);
        rbBoardClear.addActionListener(this);
        rbFoilClear.addActionListener(this);
        rbPoligonClear.addActionListener(this);
        rbDiaClear.addActionListener(this);

        JPanel pnControl = new JPanel();
        cp.add(pnControl, "South");
        btOk = new JButton("OK");
        btOk.setMnemonic('O');
        pnControl.add(btOk);
        btOk.addActionListener(this);

        btCancel = new JButton("M�gse");
        btCancel.setMnemonic('M');
        pnControl.add(btCancel);
        btCancel.addActionListener(this);
    }

    /**
     * A p�rbesz�dablak megjelen�t�s�t v�gzi.
     * @return a v�laszt�s eredm�nye
     */
    public int show(Object message, Object initValue) {
        lbMessage.setText(message.toString());
        pack();
        setLocationRelativeTo(getParent());

        setVisible(true);
        if (option == JOptionPane.YES_OPTION) {
            return result;
        } else {
            return 0;
        }
    }

    /**
     * A nyom�gombok lenyom�s�nak lekezel�se.
     * @param ev akci�esem�ny
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == btOk) {
            option = JOptionPane.YES_OPTION;
            dispose();
        } else if (ev.getSource() == btCancel) {
            option = JOptionPane.NO_OPTION;
            dispose();
        } else if (ev.getSource() == rbBoardClear) {
            result = 1;
        } else if (ev.getSource() == rbFoilClear) {
            result = 2;
        } else if (ev.getSource() == rbPoligonClear) {
            result = 3;
        } else if (ev.getSource() == rbDiaClear) {
            result = 4;
        }
    }
}
