package com.MedveSoft;

/**
 * A dia konfigur�ci�s be�ll�t�sait tartalmaz� objektum, melynek nincs grafikus
 * reprezent�ci�ja a szerkeszt�v�sznon, csup�n csak a be�ll�t�sok r�gz�t�s�re
 * szolg�l.
 */
public class Config extends GraphicalObject {

    /**
     * n�gyzetr�csos megjelen�t�s bekapcsolt-e
     */
    public boolean gridOn;
    /**
     * koordin�tarendszer megjelen�t�se bekapcsolt-e
     */
    public boolean axisOn;
    /**
     * k�vetkez� c�mke megjelen�t�s bekapcsolt-e
     */
    public boolean nextLabelOn;
    /**
     * r�csra igaz�t�s bekapcsolt-e
     */
    public boolean snapToGrid;
    /**
     * cs�csok megjelen�t�se bekapcsolt-e
     */
    public boolean nodesOn;
    /**
     * koordin�tarendszer k�z�ppontj�nak X koordin�t�ja
     */
    public double originX;
    /**
     * koordin�tarendszer k�z�ppontj�nak Y koordin�t�ja
     */
    public double originY;
    /**
     * a dia nagy�t�si t�nyez�je
     */
    public double globalScaleFactor;
    /**
     * aktu�lis pont c�mk�je
     */
    public String actBpLabel;
    /**
     * aktu�lis szakasz c�mk�je
     */
    public String actLsLabel;
    /**
     * aktu�lis toll tintasz�n�nek sz�ma
     */
    public int actPenColor;
    /**
     * aktu�lis kijel�l�sz�n sz�ma
     */
    public int actHighLightColor;
    /**
     * aktu�lis csoportsz�m
     */
    public int actGroup;
    /**
     * aktu�lis cs�cspont c�mk�j�nek indexe
     */
    public int actBpLabelIDX;
    /**
     * aktu�lis szakasz c�mk�j�nek indexe
     */
    public int actLsLabelIDX;

    /**
     * konstruktor
     */
    public Config() {
        gridOn = false;
        axisOn = false;
        nextLabelOn = true;
        snapToGrid = false;
        nodesOn = true;
        originX = 0;
        originY = 0;
        actBpLabel = "A";
        actBpLabelIDX = 0;
        actLsLabel = "a";
        actLsLabelIDX = 0;
        actPenColor = 0;
        actHighLightColor = 0;
        actGroup = 0;
    }

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    public void Calculate() {
    }

    /**
     * a scaleFactor �rt�k�nek megfelel�en nagy�tja/kicsiny�ti a megadott 
     * (originX;originY) k�z�ppontb�l az objektumot, illetve be�ll�tja a
     * c�mk�j�nek a poz�ci�j�t 
     * @param scaleFactor
     * @param originX
     * @param originY
     */
    @Override
    public void Scale(double scaleFactor, double originX, double originY) {
    }

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    @Override
    public void Translate(double dx, double dy) {
    }
}
