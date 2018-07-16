package com.MedveSoft;

import java.awt.Color;

/**
 * Defini�lja a kijel�l� soksz�g t�pus� grafikus objektumot, melynek az a c�lja, 
 * hogy tetsz�leges soksz�get � kiv�lasztott kit�lt� sz�nnel � �ttetsz� 
 * kit�lt�ssel kiemeljen a szerkeszt�sen.
 */
public class Poligon extends GraphicalObject {

    /**
     * a kijel�l� soksz�g sz�ne
     */
    public Color color;

    /**
     * a kijel�l� poligon megadott index� cs�cspontj�nak X koordin�t�j�t adja vissza
     * @param i
     * @return
     */
    public double getNodeX(int i) {
        BasePoint o = (BasePoint) creationObjectList.get(i);
        return o.x;
    }

    /**
     * a kijel�l� poligon megadott index� cs�cspontj�nak Y koordin�t�j�t adja vissza
     * @param i
     * @return
     */
    public double getNodeY(int i) {
        BasePoint o = (BasePoint) creationObjectList.get(i);
        return o.y;
    }

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    void Calculate() {
    }

    /**
     * A scaleFactor �rt�k�nek megfelel�en nagy�tja/kicsiny�ti a megadott 
     * (originX;originY) k�z�ppontb�l az objektumot, illetve be�ll�tja a
     * c�mk�j�nek a poz�ci�j�t.
     * @param scaleFactor
     * @param originX
     * @param originY
     */
    @Override
    void Scale(double scaleFactor, double originX, double originY) {
    }

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    @Override
    void Translate(double dx, double dy) {
    }

    /**
     * Konstruktor.
     * A list t�mb �ltal szolg�ltatott objektumokat t�rolja az objektum 
     * creationObjectList vektor�ban, valamint be�ll�tja a grafikus objektum 
     * kre�ci�j�nak t�pus�t. 
     * @param list
     * @param type
     */
    public Poligon(GraphicalObject[] list, int type) {
        super(list, type);
    }
}
