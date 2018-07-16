package com.MedveSoft;

/**
 * A k�r objektum oszt�lya
 */
public class Circle extends GraphicalObject {

    /**
     * a k�r befoglal� n�gyzete bal fels� sark�nak X koordin�t�ja
     */
    public double x0;
    /**
     * a k�r befoglal� n�gyzete bal fels� sark�nak Y koordin�t�ja
     */
    public double y0;
    /**
     * a k�r k�z�ppontj�nak X koordin�t�ja
     */
    public double x;
    /**
     * a k�r k�z�ppontj�nak Y koordin�t�ja
     */
    public double y;
    /**
     * a k�r sugara
     */
    public double r;
    /**
     * k�r k�t pontb�l (k�z�ppont + ker�leten elhelyezked� pont)
     */
    public static final int TWOPOINTS = 1;
    /**
     * h�rom ponton �tmen� k�r
     */
    public static final int THREEPOINTS = 2;

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    public void Calculate() {
        checkAncestorValidity();
        //csak akkor sz�molunk, ha megszerkeszthet� az objektum minden �se
        if (this.valid) {
            //k�r k�t pontb�l (k�z�ppont + ker�leten elhelyezked� pont)
            if (this.creationType == Circle.TWOPOINTS) {
                BasePoint o = (BasePoint) creationObjectList.get(0);
                x = o.x;
                y = o.y;
                o = (BasePoint) creationObjectList.get(1);
                r = Math.sqrt(Math.pow(o.x - x, 2) + Math.pow(o.y - y, 2));
                x0 = x - r;
                y0 = y - r;
            }
            //h�rom ponton �tmen� k�r
            if (this.creationType == Circle.THREEPOINTS) {
                double a1, a2, b1, b2, c1, c2, fax, fay, fcx, fcy, a, b, c, d, e, f;
                BasePoint o = (BasePoint) creationObjectList.get(0);
                a1 = o.x;
                a2 = o.y;
                o = (BasePoint) creationObjectList.get(1);
                b1 = o.x;
                b2 = o.y;
                o = (BasePoint) creationObjectList.get(2);
                c1 = o.x;
                c2 = o.y;
                a = b1 - a1;
                b = b2 - a2;
                d = c1 - b1;
                e = c2 - b2;

                fcx = (a1 + b1) / 2;
                fcy = (a2 + b2) / 2;
                c = a * fcx + b * fcy;
                fax = (c1 + b1) / 2;
                fay = (c2 + b2) / 2;
                f = d * fax + e * fay;

                if (a == 0) {
                    if (b == 0) {
                        valid = false;
                    } else if (d == 0) {
                        valid = false;
                    } else if (e == 0) {
                        x = f / d;
                        y = c / b;
                    } else {
                        y = c / b;
                        x = (f - e * y) / d;
                    }
                } else {
                    if (b == 0) {
                        if (e == 0) {
                            valid = false;
                        } else if (d == 0) {
                            x = c / a;
                            y = f / e;
                        } else {
                            x = c / a;
                            y = (f - d * x) / e;
                        }
                    } else {
                        if (d == 0) {
                            if (e == 0) {
                                valid = false;
                            } else {
                                y = f / e;
                                x = (c - b * y) / a;
                            }
                        } else {
                            if (e == 0) {
                                x = f / d;
                                y = (c - a * x) / b;
                            } else {
                                y = (a * f - d * c) / (a * e - d * b);
                                x = (c - b * y) / a;
                            }
                        }
                    }
                }
                r = Math.sqrt(Math.pow(c1 - x, 2) + Math.pow(c2 - y, 2));
                x0 = x - r;
                y0 = y - r;
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

    /**
     * A list t�mb �ltal szolg�ltatott objektumokat t�rolja az objektum 
     * creationObjectList vektor�ban, valamint be�ll�tja a grafikus objektum 
     * kre�ci�j�nak t�pus�t. 
     * @param list
     * @param type
     */
    public Circle(GraphicalObject[] list, int type) {
        super(list, type);
    }
}