package com.MedveSoft;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

/**
 * Absztrakt oszt�ly, mely a szerkeszt�v�sznon megjelen� grafikus objektumok k�z�s 
 * tulajdons�gait �s viselked�sm�djait defini�lja. 
 */
abstract public class GraphicalObject implements Serializable {

    /**
     * l�that�-e az objektum
     */
    public boolean visible;
    /**
     * rejtett-e az objektum c�mk�je
     */
    public boolean hiddenLabel;
    /**
     * mozgathat�-e az objektum
     */
    public boolean movable;
    /**
     * kijel�lt-e az objektum
     */
    public boolean selected;
    /**
     * megszerkeszthet�-e az objektum
     */
    public boolean valid;
    /**
     * objektum c�mk�je, felirata
     */
    public String label;
    /**
     * c�mke bal fels� sark�nak X koordin�t�ja
     */
    public double labelX;
    /**
     * c�mke bal fels� sark�nak Y koordin�t�ja
     */
    public double labelY;
    /**
     * c�mke X ir�ny� eltol�sa
     */
    public double labelDX;
    /**
     * c�mke Y ir�ny� eltol�sa
     */
    public double labelDY;
    /**
     * a kre�ci� t�pusa (pl. felez�pont, s�lyont, stb.)
     */
    public int creationType;
    /**
     * csoport sz�ma, amihez tartozik az objektum
     */
    public int group;
    /**
     * sz�l� objektumok list�ja, melyb�l k�sz�l az objektum
     */
    public Vector creationObjectList = new Vector();

    /**
     * kisz�molja az objektum megjelen�t�s�hez sz�ks�ges param�tereket
     */
    abstract void Calculate();

    /**
     * a scaleFactor �rt�k�nek megfelel�en nagy�tja/kicsiny�ti a megadott 
     * (originX;originY) k�z�ppontb�l az objektumot, illetve be�ll�tja a
     * c�mk�j�nek a poz�ci�j�t
     * @param scaleFactor
     * @param originX
     * @param originY
     */
    abstract void Scale(double scaleFactor, double originX, double originY);

    /**
     * a megadott (dx;dy) vektorral eltolja az objektumot �s a c�mk�j�t
     * @param dx
     * @param dy
     */
    abstract void Translate(double dx, double dy);

    /**
     * objektum kijel�l�se
     */
    public void Select() {
        this.selected = true;
    }

    /**
     * objektum kijel�l�s�nek t�rl�se
     */
    public void deSelect() {
        this.selected = false;
    }

    /**
     * objektum elrejt�se
     */
    public void Hide() {
        this.visible = false;
    }

    /**
     * objektum felfed�se
     */
    public void unHide() {
        this.visible = true;
    }

    /**
     * kezdeti �rt�kek be�ll�t�sa
     */
    public void initValues() {
        //alap�rtelmezett �rt�kek
        visible = true;
        hiddenLabel = false;
        movable = true;
        selected = false;
        valid = true;
        group = -1;
        labelDX = 0;
        labelDY = 0;
    }

    /**
     * �rv�nyess�g ellen�rz�se a sz�l� objektumok alapj�n.
     */
    public void checkAncestorValidity() {
        this.valid = true;
        int i = 0;
        while (i < creationObjectList.size()) {
            GraphicalObject object = (GraphicalObject) creationObjectList.get(i);
            //Ha a sz�l� objektumok k�z�tt van nem megszerkeszthet�, akkor ez az objektum sem szerkeszthet� meg
            if (object != null && !object.valid) {
                this.valid = false;
            }
            i++;
        }
    }

    /**
     * L�trehozza az objektumot �s inicializ�lja az objektum attrib�tumait.
     */
    GraphicalObject() {
        initValues();
    }

    /**
     * A list t�mb �ltal szolg�ltatott objektumokat t�rolja az objektum 
     * creationObjecList vektor�ban, valamint be�ll�tja a grafikus objektum 
     * kre�ci�j�nak t�pus�t. P�ld�ul ha egy felez�pontot akarunk l�trehozni, 
     * akkor �t kell adni a list t�mbben a szakasz vagy vektor k�t v�gpontj�nak 
     * objektum�t, valamint a felez�pont t�pusazonos�t� konstans�t, melyet 
     * mindig a konkr�t objektumt�pusban defini�lunk.
     * @param list 
     * @param type
     */
    public GraphicalObject(GraphicalObject[] list, int type) {
        initValues();
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                this.creationObjectList.add(list[i]);
            }
        }
        this.creationType = type;
        this.Calculate();
    }
}
