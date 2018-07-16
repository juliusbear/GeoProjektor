package com.MedveSoft;

/**
 * Defini�lja a pont t�pus� objektumokat, melyek minden szerkeszt�s alapj�t k�pezik.
 */
public class BasePoint extends GraphicalObject {

    /**
     * a pont X koordin�t�ja
     */
    public double x;
    /**
     * a pont Y koordin�t�ja
     */
    public double y;
    /**
     * r�gz�tett pont ir�nysz�ge k�r�n
     */
    public double alfa;
    /**
     * cs�cspontok sz�ma a s�lypont sz�m�t�s�hoz
     */
    public int n;
    /**
     * egyszer� pont, nincs sz�l� objektum
     */
    public static final int SIMPLE_POINT = 1;
    /**
     * egyenes vonalra mer�legesen vet�tett pont
     */
    public static final int PROJECTED_POINT = 2;
    /**
     * szakasz vagy vektor felez�pontja
     */
    public static final int MIDPOINT = 3;
    /**
     * fut�pont egyenes vonalon
     */
    public static final int RUNPOINT_ON_LINE = 4;
    /**
     * fut�pont k�r�n
     */
    public static final int RUNPOINT_ON_CIRCLE = 5;
    /**
     * k�t k�r metsz�spontja
     */
    public static final int INTERSECTION_ON_CIRCLE = 6;
    /**
     * pont k�z�ppontos t�kr�z�s�vel kapott pont
     */
    public static final int MIRROREDPOINT_BY_POINT = 7;
    /**
     * vektorral eltolt pont
     */
    public static final int TRANSLATEDPOINT_BY_VECTOR = 8;
    /**
     * r�gz�tett pont k�r�n
     */
    public static final int FIXEDPOINT_ON_CIRCLE = 9;
    /**
     * k�t egyenes metsz�spontja
     */
    public static final int INTERSECTION_ON_LINES = 10;
    /**
     * s�lypont
     */
    public static final int WEIGHTPOINT = 11;
    /**
     * k�r k�z�ppontja
     */
    public static final int CENTRE = 12;

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    public void Calculate() {
        double m, xf, yf, tempX, tempY;
        double u1, u2, v1, v2, r1, r2, C;
        
        checkAncestorValidity();
        //csak akkor sz�molunk, ha megszerkeszthet� az objektum minden �se
        if (this.valid) {
            //vonalra mer�legesen vet�tett pont
            if (this.creationType == PROJECTED_POINT) {
                BasePoint o = (BasePoint) creationObjectList.get(0);
                xf = o.x;
                yf = o.y;
                if (creationObjectList.get(1) instanceof Line) {
                    Line l = (Line) creationObjectList.get(1);
                    if (l.x1 - l.x0 != 0) {
                        m = (l.y1 - l.y0) / (l.x1 - l.x0);
                        this.x = (xf + m * yf + m * m * l.x0 - m * l.y0) / (m * m + 1);
                        this.y = m * (this.x - l.x0) + l.y0;
                    } else {
                        this.x = l.x0;
                        this.y = yf;
                    }
                }
            }
            //szakasz vagy vektor felez�pontja
            if (this.creationType == MIDPOINT) {
                Line l = (Line) creationObjectList.get(0);
                this.x = (l.x0 + l.x1) / 2;
                this.y = (l.y0 + l.y1) / 2;
            }
            //fut�pont vonalon
            if (this.creationType == RUNPOINT_ON_LINE) {
                Line l = (Line) creationObjectList.get(0);
                if (l.x1 != l.x0) {
                    m = (l.y1 - l.y0) / (l.x1 - l.x0);
                    tempX = (x + m * y + m * m * l.x0 - m * l.y0) / (m * m + 1);
                    tempY = m * (tempX - l.x0) + l.y0;
                    this.x = tempX;
                    this.y = tempY;
                } else {
                    this.x = l.x0;
                }
            }

            //k�t k�r metsz�spontja(i)
            if (this.creationType == INTERSECTION_ON_CIRCLE) {
                // a k�r�knek "balr�l jobbra" kell elhelyezkedni�k
                Circle c = (Circle) creationObjectList.get(0);
                u1 = c.x;
                v1 = c.y;
                r1 = c.r;
                c = (Circle) creationObjectList.get(1);
                u2 = c.x;
                v2 = c.y;
                r2 = c.r;
                if (v2 == v1 & u2 == u1) {
                    //v�gtelen sok k�z�s pont van, mert a k�r�k egybeesnek
                }
                if (v2 == v1 & u2 != u1) {
                    C = (r1 * r1 - r2 * r2 + u2 * u2 - u1 * u1 + v2 * v2 - v1 * v1) / 2;
                    this.x = C / (u2 - u1);
                    this.y = -Math.sqrt(r1 * r1 - (x - u1) * (x - u1)) + v1;
                }
            //itt m�g van t�bb esetet kell vizsg�lni! Ez a r�sz m�g fejleszt�sre szorul!
            }
            //fut�pont k�r�n
            if (this.creationType == RUNPOINT_ON_CIRCLE) {
                Circle c = (Circle) creationObjectList.get(0);
                alfa = Math.atan((c.y - y) / (c.x - x));
                if (c.x > x) {
                    alfa += Math.PI;
                }
                if (c.x == x) {
                    if (c.y > y) {
                        alfa = -Math.PI / 2;
                    } else {
                        alfa = +Math.PI / 2;
                    }
                }
                x = c.x + c.r * Math.cos(alfa);
                y = c.y + c.r * Math.sin(alfa);
            }
            //pont k�z�ppontos t�kr�z�s�vel kapott pont
            if (this.creationType == MIRROREDPOINT_BY_POINT) {
                BasePoint p = (BasePoint) creationObjectList.get(0);
                BasePoint o = (BasePoint) creationObjectList.get(1);
                this.x = 2 * o.x - p.x;
                this.y = 2 * o.y - p.y;
            }
            //vektorral eltolt pont
            if (this.creationType == TRANSLATEDPOINT_BY_VECTOR) {
                BasePoint p = (BasePoint) creationObjectList.get(0);
                Line v = (Line) creationObjectList.get(1);
                this.x = p.x + v.x1 - v.x0;
                this.y = p.y + v.y1 - v.y0;
            }
            //r�gz�tett pont k�r�n
            if (this.creationType == FIXEDPOINT_ON_CIRCLE) {
                Circle c = (Circle) creationObjectList.get(0);
                this.x = c.x + Math.cos(this.alfa) * c.r;
                this.y = c.y - Math.sin(this.alfa) * c.r;
            }

            //K�t egyenes metsz�spontja
            if (this.creationType == INTERSECTION_ON_LINES) {
                double a1, a2, b1, b2, a, b, c, d, e, f;
                Line o = (Line) creationObjectList.get(0);
                a1 = o.x0;
                a2 = o.y0;
                b1 = o.x1;
                b2 = o.y1;
                a = a2 - b2;
                b = b1 - a1;
                c = a * a1 + b * a2;

                o = (Line) creationObjectList.get(1);
                a1 = o.x0;
                a2 = o.y0;
                b1 = o.x1;
                b2 = o.y1;
                d = a2 - b2;
                e = b1 - a1;
                f = d * a1 + e * a2;

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
            }
            //s�lypont
            if (this.creationType == WEIGHTPOINT) {
                BasePoint p = null;
                x = 0;
                y = 0;
                for (int i = 0; i < n; i++) {
                    p = (BasePoint) creationObjectList.get(i);
                    x += p.x;
                    y += p.y;
                }
                x /= n;
                y /= n;
            }
            //k�r k�z�ppontja
            if (this.creationType == CENTRE) {
                Circle c = (Circle) creationObjectList.get(0);
                this.x = c.x;
                this.y = c.y;
            }
            //c�mke poz�ci�j�nak kisz�m�t�sa
            calculateLabel();
        }
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
    public void Scale(double scaleFactor, double originX, double originY) {
        x = originX + (this.x - originX) * scaleFactor;
        y = originY + (this.y - originY) * scaleFactor;
        labelX = originX + (labelX - originX) * scaleFactor;
        labelY = originY + (labelY - originY) * scaleFactor;
    }

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    @Override
    public void Translate(double dx, double dy) {
        x += dx;
        y += dy;
        labelX += dx;
        labelY += dy;
    }

    /**
     * be�ll�tja a pont c�mk�j�nek poz�ci�j�t
     */
    public void calculateLabel() {
        this.labelX = this.x - 10;
        this.labelY = this.y - 20;
    }

    /**
     * l�trehoz egy r�gz�tett pontot a c k�r�n, alfa ir�nysz�ggel
     * @param c
     * @param alfa
     */
    public BasePoint(Circle c, double alfa) {
        super();
        this.alfa = Math.toRadians(alfa);
        this.creationObjectList.add(c);
        creationType = FIXEDPOINT_ON_CIRCLE;
        Calculate();
        calculateLabel();
    }

    /**
     * A list t�mb �ltal szolg�ltatott objektumokat t�rolja az objektum 
     * creationObjectList vektor�ban, valamint be�ll�tja a grafikus objektum 
     * kre�ci�j�nak t�pus�t. 
     * @param list
     * @param type
     */
    public BasePoint(GraphicalObject[] list, int type) {
        super(list, type);
        calculateLabel();
    }

    /**
     * L�trehoz egy egyszer�, (initX;initY) koordin�t�j� pontot.
     * @param initX
     * @param initY
     */
    BasePoint(double initX, double initY) {
        super();
        this.x = initX;
        this.y = initY;
        creationType = SIMPLE_POINT;
        Calculate();
        calculateLabel();
    }
}
