package com.MedveSoft;

/**
 *Az ellipszis objektum oszt�lya.
 */
public class Ellipse extends GraphicalObject {

    /**
     * az ellipszist befoglal� t�glalap bal fels� sark�nak X koordin�t�ja
     */
    public double x0;
    /**
     * az ellipszist befoglal� t�glalap bal fels� sark�nak Y koordin�t�ja
     */
    public double y0;
    /**
     * az ellipszis k�z�ppontj�nak X koordin�t�ja
     */
    public double x;
    /**
     * az ellipszis k�z�ppontj�nak Y koordin�t�ja
     */
    public double y;
    /**
     * az ellipszis f�l nagytengelye (v�zszintes sugara)
     */
    public double a;
    /**
     * az ellipszis f�l kistengelye (f�gg�leges sugara)
     */
    public double b;
    /**
     * az ellipszis "c" param�tere (k�z�ppont �s f�kuszpont t�vols�ga)
     */
    public double c;
    /**
     * h�rom ponton �thalad� ellipszis (k�z�ppont + 2 ker�leti pont)
     */
    public static final int THREEPOINTS = 1;

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    public void Calculate() {
        checkAncestorValidity();
        //csak akkor sz�molunk, ha megszerkeszthet� az objektum minden �se
        if (this.valid) {
            //h�rom ponton �thalad� ellipszis
            if (this.creationType == Ellipse.THREEPOINTS) {
                BasePoint o = (BasePoint) creationObjectList.get(0);
                BasePoint o2 = (BasePoint) creationObjectList.get(1);
                BasePoint o3 = (BasePoint) creationObjectList.get(2);
                x = o.x;
                y = o.y;
                a = Math.sqrt(Math.pow(o2.x - x, 2) + Math.pow(o2.y - y, 2));
                b = Math.sqrt(Math.pow(o3.x - x, 2) + Math.pow(o3.y - y, 2));
                c = Math.sqrt(Math.pow(a, 2) - Math.pow(b, 2));
                x0 = x - a;
                y0 = y - b;
            }
        }
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
    public void Scale(double scaleFactor, double originX, double OriginY) {
    }

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    @Override
    public void Translate(double dx, double dy) {
    }

    /**
     * A list t�mb �ltal szolg�ltatott objektumokat t�rolja az objektum 
     * creationObjectList vektor�ban, valamint be�ll�tja a grafikus objektum 
     * kre�ci�j�nak t�pus�t. 
     * @param list
     * @param type
     */
    public Ellipse(GraphicalObject[] list, int type) {
        super(list, type);
    }
}
