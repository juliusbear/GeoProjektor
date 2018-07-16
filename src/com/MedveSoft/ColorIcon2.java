package com.MedveSoft;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

/**
 * Defini�lja a kijel�l� soksz�get kit�lt� sz�n ikonj�t.
 * @author Maci
 */
public class ColorIcon2 implements Icon {

    /**
     * az ikon magass�ga
     */
    private static int HEIGHT = 32;
    /**
     * az ikon sz�less�ge
     */
    private static int WIDTH = 32;
    /**
     * az ikon sz�ne
     */
    public Color color;

    /**
     * Konstruktor. Az ikon l�trehoz�sa a megadott sz�nkonstanssal.
     * @param color
     */
    public ColorIcon2(Color color) {
        this.color = color;
    }

    /**
     * Az ikont kirajzol� elj�r�s.
     * @param c
     * @param g
     * @param x
     * @param y
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D gr = (Graphics2D) g;
        gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        gr.setColor(color);
        gr.fillRect(x, y, WIDTH, HEIGHT);
    }

    /**
     * Az ikon sz�less�g�t lek�rdez� met�dus.
     * @return
     */
    @Override
    public int getIconWidth() {
        return HEIGHT;
    }

    /**
     * Az ikon magass�g�t lek�rdez� met�dus.
     * @return
     */
    @Override
    public int getIconHeight() {
        return WIDTH;
    }
}
