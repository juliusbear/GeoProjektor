package com.MedveSoft;

/**
 * A k�pleteket, �sszef�gg�seket megjelen�t� objektum oszt�lya.
 */
public class Formula extends GraphicalObject {

    /**
     * a k�plet bal fels� sark�nak X koordin�t�ja
     */
    public double x;
    /**
     * a k�plet bal fels� sark�nak Y koordin�t�ja
     */
    public double y;
    /**
     * a k�plet sz�less�ge
     */
    public int width;
    /**
     * a k�plet magass�ga
     */
    public int height;
    /**
     * a k�plet f�jlneve el�r�si �ttal egy�tt
     */
    public String fileName;

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    void Calculate() {
    }

    /**
     * A scaleFactor �rt�k�nek megfelel�en nagy�tja/kicsiny�ti a megadott 
     * (originX;originY) k�z�ppontb�l az objektumot, illetve be�ll�tja a
     * c�mk�j�nek a poz�ci�j�t 
     * @param scaleFactor
     * @param originX
     * @param originY
     */
    @Override
    void Scale( double scaleFactor, double originX, double originY) {
    }

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    @Override
    void Translate( double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**    
     * Konstruktor. Be�ll�tja a k�plet f�jlnev�t �s bal fels� sark�nak
     * koordin�t�it.
     * @param x
     * @param y
     * @param fileName
     */
    public Formula(double x, double y, String fileName) {
        super();
        this.x = x;
        this.y = y;
        this.fileName = fileName;
    }
}
