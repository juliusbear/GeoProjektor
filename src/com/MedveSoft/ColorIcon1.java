package com.MedveSoft;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;

/**
 * Defini�lja a toll rajzol�sz�n�t reprezent�l� ikont.
 * @author Maci
 */
public class ColorIcon1 implements Icon {

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
    public ColorIcon1(Color color) {
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
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setColor(color);
        gr.fillOval(5, 5, 28, 28);
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
