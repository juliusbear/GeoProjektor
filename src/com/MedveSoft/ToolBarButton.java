package com.MedveSoft;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * Defini�lja az eszk�zsorok nyom�gombjainak oszt�ly�t.
 */
public class ToolBarButton extends JButton {
    /**
     * a szeg�ly �s a c�mke k�z�tti marg� nagys�ga
     */
    private static final Insets margins = new Insets(0, 0, 0, 0);

    /**
     * Konstruktor. L�trehozza a nyom�gombot a megadott Icon p�ld�ny alapj�n.
     * @param icon
     */
    public ToolBarButton(Icon icon) {
        super(icon);
        setMargin(margins);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
    }

    /**
     * Konstruktor. Bet�lti az ikont a megadott URL alapj�n, majd l�trehozza a nyom�gombot.
     * @param url
     */
    public ToolBarButton(URL url) {
        this(new ImageIcon(url));
    }
}

