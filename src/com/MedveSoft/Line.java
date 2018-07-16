package com.MedveSoft;

/**
 * A vonal objektumokat (szakasz, f�legyenes, egyenes, vektor) megtestes�t� oszt�ly.
 */
public class Line extends GraphicalObject {

    /**
     * vonal kezd�pontj�nak X koordin�t�ja
     */
    public double x0;
    /**
     * vonal kezd�pontj�nak Y koordin�t�ja
     */
    public double y0;
    /**
     * vonal v�gpontj�nak X koordin�t�ja
     */
    public double x1;
    /**
     * vonal v�gpontj�nak Y koordin�t�ja
     */
    public double y1;
    /**
     * vonal ir�nysz�ge
     */
    public double alfa;
    /**
     * vonal f�t�pusa
     */
    public int mainType;
    /**
     * egyenes f�t�pus
     */
    public static final int LINE = 1;
    /**
     * szakasz f�t�pus
     */
    public static final int LINESEGMENT = 2;
    /**
     * vektor f�t�pus
     */
    public static final int VECTOR = 3;
    /**
     * f�legyenes f�t�pus
     */
    public static final int HALFLINE = 4;
    /**
     * k�t ponton �thalad� vonal alt�pus
     */
    public static final int TWOPOINTS = 1;
    /**
     * mer�leges egyenes alt�pus
     */
    public static final int PERPENDICULAR = 2;
    /**
     * tetsz�leges vonalra mer�leges egyenes alt�pus
     */
    public static final int PERPENDICULARBYLINE = 3;
    /**
     * r�gz�tett meredeks�g� vonal alt�pus
     */
    public static final int FIXEDALFA = 4;
    /**
     * sz�gfelez� egyenes 3 pontb�l alt�pus
     */
    public static final int BISECTOR = 5;

    /**
     * �tsz�molja az egyenes "v�gpontjainak" koordin�t�it, hogy k�v�l essenek a
     * l�that� tartom�nyon. (a0;b0) - kezd�pont, (a1;b1) - v�gpont
     * A Java be�p�tett v�g� algoritmusa pedig �gyis elint�zi, hogy az
     * egyenes l�that� r�sze jelenjen csak meg.
     * @param a0 
     * @param b0
     * @param a1
     * @param b1
     */
    public void calculateModifiedCoordinates(double a0, double b0, double a1, double b1) {
        this.x0 = a0 - (a1 - a0) * 1000;
        this.y0 = b0 - (b1 - b0) * 1000;
        this.x1 = a1 + (a1 - a0) * 1000;
        this.y1 = b1 + (b1 - b0) * 1000;
    }

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    @Override
    public void Calculate() {
        double a0, b0, a1, b1;
        double x, y;
        double m;

        checkAncestorValidity();
        //csak akkor sz�molunk, ha megszerkeszthet� az objektum minden �se
        if (this.valid) {
            //egyenes
            if (this.mainType == LINE) {
                //k�t ponton �tmen� egyenes
                if (this.creationType == Line.TWOPOINTS) {
                    BasePoint o = (BasePoint) creationObjectList.get(0);
                    a0 = o.x;
                    b0 = o.y;
                    o = (BasePoint) creationObjectList.get(1);
                    a1 = o.x;
                    b1 = o.y;
                    calculateModifiedCoordinates(a0, b0, a1, b1);
                }
                //szakaszra mer�leges egyenes
                if (this.creationType == Line.PERPENDICULAR) {
                    //szakasz kezd�pont
                    BasePoint o = (BasePoint) creationObjectList.get(0);
                    a0 = o.x;
                    b0 = o.y;
                    //szakasz v�gpont
                    o = (BasePoint) creationObjectList.get(1);
                    a1 = o.x;
                    b1 = o.y;
                    //felez�pont vagy m�s pont
                    o = (BasePoint) creationObjectList.get(2);
                    x0 = o.x;
                    y0 = o.y;
                    m = (a1 - a0) / (b1 - b0);
                    if (b1 - b0 != 0) {
                        x1 = x0 + 1;
                        y1 = y0 - m;
                    } else {
                        x1 = x0;
                        y1 = y0 + 1;
                    }
                    calculateModifiedCoordinates(x0, y0, x1, y1);
                }
                //tetsz�leges vonalra mer�leges egyenes
                if (this.creationType == Line.PERPENDICULARBYLINE) {
                    Line l = (Line) creationObjectList.get(0);
                    a0 = l.x0;
                    b0 = l.y0;
                    a1 = l.x1;
                    b1 = l.y1;
                    BasePoint o = (BasePoint) creationObjectList.get(1);
                    x0 = o.x;
                    y0 = o.y;
                    m = (a1 - a0) / (b1 - b0);
                    if (b1 - b0 != 0) {
                        x1 = x0 + 1;
                        y1 = y0 - m;
                    } else {
                        x1 = x0;
                        y1 = y0 + 1;
                    }
                    calculateModifiedCoordinates(x0, y0, x1, y1);
                }
                //sz�gfelelez� egyenes meghat�roz�sa
                if (this.creationType == Line.BISECTOR) {
                    BasePoint p1 = (BasePoint) creationObjectList.get(0);
                    BasePoint p2 = (BasePoint) creationObjectList.get(1);
                    BasePoint p3 = (BasePoint) creationObjectList.get(2);
                    double alfa1, alfa2;
                    alfa1 = Math.atan((p1.y - p2.y) / (p1.x - p2.x));
                    if (p2.x > p1.x) {
                        alfa1 += Math.PI;
                    }
                    if (p2.x == p1.x) {
                        if (p1.y > p2.y) {
                            alfa1 = Math.PI / 2;
                        } else {
                            alfa1 = -Math.PI / 2;
                        }
                    }

                    alfa2 = Math.atan((p3.y - p2.y) / (p3.x - p2.x));
                    if (p2.x > p3.x) {
                        alfa2 += Math.PI;
                    }
                    if (p2.x == p3.x) {
                        if (p3.y > p2.y) {
                            alfa2 = Math.PI / 2;
                        } else {
                            alfa2 = -Math.PI / 2;
                        }
                    }

                    a0 = p2.x + Math.cos(alfa1) * 100;
                    b0 = p2.y + Math.sin(alfa1) * 100;
                    a1 = p2.x + Math.cos(alfa2) * 100;
                    b1 = p2.y + Math.sin(alfa2) * 100;
                    x0 = p2.x;
                    y0 = p2.y;
                    x1 = (a0 + a1) / 2;
                    y1 = (b0 + b1) / 2;
                    calculateModifiedCoordinates(x0, y0, x1, y1);
                }
                //r�gz�tett meredeks�g� egyenes
                if (this.creationType == Line.FIXEDALFA) {
                    BasePoint p1 = (BasePoint) creationObjectList.get(0);
                    x0 = p1.x;
                    y0 = p1.y;
                    x1 = p1.x + 10 * Math.cos(alfa);
                    y1 = p1.y + 10 * Math.sin(alfa);
                    calculateModifiedCoordinates(x0, y0, x1, y1);
                }
            }
            //szakasz
            if (this.mainType == LINESEGMENT) {
                if (this.creationType == Line.TWOPOINTS) {
                    //Kezd�pont
                    BasePoint o = (BasePoint) creationObjectList.get(0);
                    this.x0 = o.x;
                    this.y0 = o.y;
                    //V�gpont
                    o = (BasePoint) creationObjectList.get(1);
                    this.x1 = o.x;
                    this.y1 = o.y;
                }
                if (this.creationType == Line.FIXEDALFA) {
                    //Kezd�pont
                    BasePoint o = (BasePoint) creationObjectList.get(0);
                    this.x0 = o.x;
                    this.y0 = o.y;
                    //V�gpont
                    o = (BasePoint) creationObjectList.get(1);
                    this.x1 = o.x;
                    this.y1 = o.y;
                }
            }
            //vektor
            if (this.mainType == VECTOR) {
                if (this.creationType == Line.TWOPOINTS) {
                    //Kezd�pont
                    BasePoint o = (BasePoint) creationObjectList.get(0);
                    this.x0 = o.x;
                    this.y0 = o.y;
                    //V�gpont
                    o = (BasePoint) creationObjectList.get(1);
                    this.x1 = o.x;
                    this.y1 = o.y;
                }

                alfa = Math.atan((y1 - y0) / (x1 - x0));
                if (x0 > x1) {
                    alfa += Math.PI;
                }
                if (x1 == x0) {
                    if (y1 > y0) {
                        alfa = Math.PI / 2;
                    } else {
                        alfa = -Math.PI / 2;
                    }
                }
            }
            //f�legyenes
            if (this.mainType == Line.HALFLINE) {
                if (this.creationType == Line.TWOPOINTS) {
                    //Kezd�pont
                    BasePoint o = (BasePoint) creationObjectList.get(0);
                    this.x0 = o.x;
                    this.y0 = o.y;
                    //Ir�nypont
                    o = (BasePoint) creationObjectList.get(1);
                    this.x1 = o.x;
                    this.y1 = o.y;
                }

                //Korrekci�
                if (x0 == x1 & y0 == y1) {
                    //ekkor a k�t pont egybeesik, nem egy�rtelm� a megad�s
                } else {
                    this.x1 = x0 + (x1 - x0) * 1000;
                    this.y1 = y0 + (y1 - y0) * 1000;
                }
            }
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
    }

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    @Override
    public void Translate(double dx, double dy) {
        labelX += dx;
        labelY += dy;
    }

    /**
     * A list t�mb �ltal szolg�ltatott objektumokat t�rolja az objektum 
     * creationObjectList vektor�ban, valamint be�ll�tja a grafikus objektum 
     * kre�ci�j�nak f�- �s alt�pus�t. 
     * @param list
     * @param subType
     * @param mainType
     */
    public Line(GraphicalObject[] list, int subType, int mainType) {
        super(list, subType);
        this.mainType = mainType;
        this.Calculate();
    }
}
