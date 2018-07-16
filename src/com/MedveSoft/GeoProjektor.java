package com.MedveSoft;

import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.util.Vector;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * F� oszt�ly, mely felel�s a teljes alkalmaz�s megjelen�t�s��rt �s vez�rl�s��rt. Ez az oszt�ly fogja egybe a 
 * teljes programot, teremti meg a kapcsolatot a t�bbi �bjektummal.
 */
class GeoProjektor extends JFrame {

    /**
     * Rajzv�szon objektuma, melyen a grafikus objektumok, k�pletek, valamint a k�z�r�sok helyezkednek el.
     */
    public PaintPanel paintPanel = new PaintPanel(this);
    /**
     * a szerkeszt�s sor�n l�trehozott grafikai objektumok vektora
     */
    public Vector objectList = new Vector();
    /**
     * a vez�rl�k t�rol�s�ra szolg�l� kont�ner oszt�ly
     */
    public Container cp = this.getContentPane();
    /**
     * fels� eszk�zsor
     */
    public JToolBar upToolBar = new JToolBar();
    /**
     * baloldali eszk�zsor
     */
    public JToolBar leftToolBar = new JToolBar();
    /**
     * als� eszk�zsor
     */
    public JToolBar downToolBar = new JToolBar();
    /**
     * jobboldali eszk�zsor
     */
    public JToolBar rightToolBar = new JToolBar();
    /**
     * st�tuszsor aktu�lis sz�vege 
     */
    public JLabel statusBarText;
    /**
     * kijel�l� t�glalap l�that�s�gi �llapotjelz�je
     */
    public boolean selectionBoxOn = false;
    /**
     * kijel�l� t�glalap objektuma
     */
    public Rectangle selectionBox;
    /**
     * aktu�lis dia sorsz�ma
     */
    public int actDia = 1;
    /**
     * utols� dia sorsz�ma
     */
    public int lastDia;
    /**
     * undo verem a visszavon�shoz
     */
    public Vector undoStack = new Vector();
    /**
     * redo verem a visszavon�s visszavon�s�hoz
     */
    public Vector redoStack = new Vector();
    /**
     * toll rajzol�sz�neinek palett�ja
     */
    public Vector penColorPalette = new Vector();
    /**
     * kijel�l� soksz�g sz�npalett�ja
     */
    public Vector highlightColorPalette = new Vector();
    /**
     * toll rajzol�sz�n�nek ikonja
     */
    public ColorIcon1 penColorIcon;
    /**
     * kijel�l� soksz�g kit�lt�sz�n�nek ikonja
     */
    public ColorIcon2 highlightColorIcon;
    /**
     * A v�szon mozgat�s�nak X ir�ny� eltol�sa
     */
    public double transDX = 0;
    /**
     * A v�szon mozgat�s�nak Y ir�ny� eltol�sa
     */
    public double transDY = 0;
    /**
     * aktu�lis f�lia, melyre �rni lehet
     */
    public Graphics2D actFoil;
    /**
     * f�li�hoz tartoz� k�p objektum
     */
    public BufferedImage foilImage;
    /**
     * k�perny� maxim�lis X koordin�t�ja
     */
    public int screenMaxX = 1024;
    /**
     * k�perny� maxim�lis Y koordin�t�ja
     */
    public int screenMaxY = 768 - 30;
    /**
     * bet�lt�tt k�plet
     */
    public BufferedImage formulaImage;
    /**
     * Az aktu�lis dia be�ll�t�sait tartalmaz� objektum
     */
    public Config config;
    /**
     * a kijelz�s tilt�s�nak �llapotjelz�je
     */
    public boolean displayLock = true;
    /**
     * Eszk�zsorok nyom�gombjai
     */
    public JButton Button1,  Button2,  Button3,  Button4,  Button5,  Button6,  Button7,  Button8,  Button9,  Button10,  Button11,  Button12,  Button13,  Button14,  Button15,  Button16,  Button17,  Button18,  Button19,  Button20,  Button21,  Button22,  Button23,  Button24,  Button25,  Button26,  Button27,  Button28,  Button29,  Button30,  Button31,  Button32,  Button33,  Button34,  Button35,  Button36,  Button37,  Button38,  Button39,  Button40,  Button41,  Button42,  Button43,  Button44,  Button45,  Button46,  Button47,  Button48,  Button49,  Button50,  Button51,  Button52,  Button53,  Button54,  Button55,  Button56,  Button57,  Button58,  Button59,  Button60,  Button61,  Button62,  Button63,  Button64,  Button65,  Button66,  Button67,  Button68,  Button69,  Button70,  Button71,  Button72;
    /**
     * �sszes�tett ToolBarButton eg�resem�nyfigyel�
     */
    public MouseListener buttonWatch;
    /**
     * Legut�bb haszn�lt eg�resem�nyfigyel�
     */
    public MouseListener lastMouseListener = null;
    /**
     * jobb eg�rgomb lenyom�s�t �rz�kel� eg�resem�nyfigyel�
     */
    public MouseListener rightButtonWatch;
    /**
     * aktu�lis el�ad�s f�jlneve el�r�si �ttal egy�tt
     */
    public String actPresentationFileName;
    /**
     * ComboBox a cs�cspontok c�mk�ihez
     */
    JComboBox pointLabelCombo = new JComboBox();
    /**
     * ComboBox a szakaszok c�mk�ihez
     */
    JComboBox lineSegmentLabelCombo = new JComboBox();
    /**
     * a cs�cspontok c�mk�inek t�mbje
     */
    String[] pointLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /**
     * szakaszok c�mk�inek t�mbje
     */
    String[] lineSegmentLabel = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    //Eg�resem�nyfigyel� pont elhelyez�s�hez
    class MouseWatch1 extends MouseAdapter {

        GraphicalObject o;
        BasePoint p;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                BasePoint bp = getPoint(e);
                if ((o = findNearestObject(e)) == null) {
                    //Ellen�rz�s, hogy k�t pont ne ker�lj�n t�l k�zel egym�shoz
                    if (!isNearPoint(bp)) {
                        saveStateOntoUndoStack();
                        objectList.add(bp);
                        nextBpLabel(bp);
                        bp.group = nextGroup();
                        calculateAllLabels();
                    }
                //Ellen�rz�s, ha egy egyeneshez, f�legyeneshez, szakaszhoz vagy k�rh�z
                //k�zel helyezkedik el a pont, akkor r� kell vet�teni a pontot az
                //alakzatra, �gy fut�pont alakul ki.
                } else {
                    //fut�pont egyenesen
                    if (o instanceof Line) {
                        saveStateOntoUndoStack();
                        GraphicalObject[] list = {o};
                        objectList.add(p = new BasePoint(list, BasePoint.RUNPOINT_ON_LINE));
                        p.x = bp.x;
                        p.y = bp.y;
                        p.Calculate();
                        nextBpLabel(p);
                        p.group = o.group;
                        calculateAllLabels();
                    }
                    //fut�pont k�r�n
                    if (o instanceof Circle) {
                        saveStateOntoUndoStack();
                        GraphicalObject[] list = {o};
                        objectList.add(p = new BasePoint(list, BasePoint.RUNPOINT_ON_CIRCLE));
                        p.x = bp.x;
                        p.y = bp.y;
                        p.Calculate();
                        nextBpLabel(p);
                        p.group = o.group;
                        calculateAllLabels();
                    }
                }
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� szakasz elhelyez�s�hez
    class MouseWatch2 extends MouseInputAdapter {

        BasePoint start, end, temp;
        Line ls;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = findNearestPoint(e);
                saveStateOntoUndoStack();
                //ha a kezd�pont m�g nem l�tez� pont
                if (start == null) {
                    start = getPoint(e);
                    objectList.add(start);
                    nextBpLabel(start);
                    start.group = nextGroup();
                    start.creationType = BasePoint.SIMPLE_POINT;
                    start.Calculate();
                    calculateLabels(start.group);
                }
                end = new BasePoint(e.getX(), e.getY());
                end.group = start.group;
                start.Select();
                GraphicalObject[] list = {start, end};
                objectList.add(ls = new Line(list, Line.TWOPOINTS, Line.LINESEGMENT));
                ls.label = getNextLsLabel();
                ls.group = start.group;
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                temp = findNearestPoint(e);
                //ha a v�gpont megegyezik a kezd�ponttal
                if (temp == start) {
                    undo();
                } else {
                    //ha a v�gpont egy kezd�pontt�l k�l�nb�z�, m�r l�tez� pont
                    if (temp != null) {
                        ls.creationObjectList.remove(end);
                        ls.creationObjectList.add(temp);
                        reNumberGroups(start, temp);
                        ls.Calculate();
                    //ha a v�gpont a kezd�pontt�l k�l�nb�z�, �j pont
                    } else {
                        ls.creationObjectList.remove(end);
                        ls.creationObjectList.add(end = getPoint(e));
                        ls.Calculate();
                        objectList.add(end);
                        nextBpLabel(end);
                        end.group = start.group;
                        end.Calculate();
                    }
                }
                calculateLabels(ls.group);
                deselectAll();
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                if (start != null) {
                    end.x = e.getX();
                    end.y = e.getY();
                    ls.Calculate();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� pont, alakzat �s k�plet mozgat�s�hoz
    class MouseWatch3 extends MouseInputAdapter {

        BasePoint bp, start;
        boolean leftbutton;
        GraphicalObject o;
        double transDXo, transDYo;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                bp = findNearestPoint(e);
                //ha tal�lt pontot
                if (bp != null) {
                    if (bp.movable) {
                        saveStateOntoUndoStack();
                    }
                    bp.Select();
                    repaintAll();
                } //ha nem tal�lt pontot
                else {
                    start = new BasePoint(e.getX(), e.getY());
                    o = findNearestObject(e);
                    //ha nem tal�lt alakzatot, akkor k�pletet keres
                    if (o == null) {
                        o = findNearestFormula(e);
                    }
                    //ha van kijel�lt objektum
                    if (o != null) {
                        saveStateOntoUndoStack();
                        selectGroup(o.group);
                    }
                }
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                //ha pontot kell mozgatni
                if (bp != null) {
                    bp.deSelect();
                    if (bp.movable) {
                        Update(e);
                    } else {
                        repaintAll();
                    }
                    bp = null;
                } else {
                    //ha alakzatot vagy k�pletet kell mozgatni
                    if (o != null) {
                        moveSelectedGroup(transDXo, transDYo);
                        deselectAll();
                        calculateAllObjects();
                        calculateAllLabels();
                        repaintAll();
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                //ha mozgathat� pontot jel�ltek ki
                if (bp != null && bp.movable) {
                    Update(e);
                } else {
                    //ha van kijel�lt alakzat vagy k�plet
                    if (o != null) {
                        transDXo = e.getX() - start.x;
                        transDYo = e.getY() - start.y;
                        moveSelectedGroup(transDXo, transDYo);
                        start.x = e.getX();
                        start.y = e.getY();
                        calculateAllObjects();
                        calculateAllLabels();
                        repaintAll();
                    }
                }
            }
        }

        void Update(MouseEvent e) {
            BasePoint p = getPoint(e);
            bp.x = p.x;
            bp.y = p.y;
            calculateAllObjects();
            calculateAllLabels();
            repaintAll();
        }
    }

    //Eg�resem�nyfigyel� egyenes elhelyez�s�hez
    class MouseWatch4 extends MouseInputAdapter {

        BasePoint start, end, temp;
        Line ls;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = findNearestPoint(e);
                saveStateOntoUndoStack();
                if (start == null) {
                    start = getPoint(e);
                    objectList.add(start);
                    nextBpLabel(start);
                    start.group = nextGroup();
                    start.creationType = BasePoint.SIMPLE_POINT;
                    start.Calculate();
                    calculateLabels(start.group);
                }
                end = new BasePoint(e.getX(), e.getY());
                end.group = start.group;
                GraphicalObject[] list = {start, end};
                start.Select();
                objectList.add(ls = new Line(list, Line.TWOPOINTS, Line.LINESEGMENT));
                ls.label = getNextLsLabel();
                ls.group = start.group;
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                temp = findNearestPoint(e);
                //ha a kezd�pont �s a v�gpont megegyezik
                if (temp == start) {
                    undo();
                } //ha a v�gpont m�r l�tez� pont
                else if (temp != null) {
                    ls.creationObjectList.remove(end);
                    ls.creationObjectList.add(temp);
                    config.actLsLabel = new String("" + (char) ((int) config.actLsLabel.charAt(0) + 1));
                    reNumberGroups(start, temp);
                //ha a v�gpont �j pont
                } else {
                    objectList.add(end);
                    nextBpLabel(end);
                    end.group = start.group;
                    end.Calculate();
                }
                ls.mainType = Line.LINE;
                ls.Calculate();
                calculateLabels(ls.group);
                deselectAll();
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                if (start != null) {
                    ls.creationObjectList.remove(end);
                    ls.creationObjectList.add(end = getPoint(e));
                    end.group = start.group;
                    end.creationType = BasePoint.SIMPLE_POINT;
                    ls.Calculate();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� nagy�t�shoz
    class MouseWatch5 extends MouseInputAdapter {

        boolean leftbutton;
        BasePoint start, prev, next;
        double localScaleFactor;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                prev = start;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                next = new BasePoint(e.getX(), e.getY());
                if (prev.y < next.y) {
                    localScaleFactor = 1.02;
                }
                if (prev.y > next.y) {
                    localScaleFactor = 0.985;
                }
                if (prev.y == next.y) {
                    localScaleFactor = 1;
                }
                //ha t�rt�nt nagy�t�s
                if (localScaleFactor != 1) {
                    for (int i = 0; i < objectList.size(); i++) {
                        GraphicalObject o = (GraphicalObject) objectList.get(i);
                        o.Scale(localScaleFactor, 0, 0);
                    }
                    for (int i = 0; i < objectList.size(); i++) {
                        GraphicalObject o = (GraphicalObject) objectList.get(i);
                        o.Calculate();
                    }
                    config.originX = config.originX * localScaleFactor;
                    config.originY = config.originY * localScaleFactor;
                }
                prev = next;
                config.globalScaleFactor = config.globalScaleFactor * localScaleFactor;
                calculateAllLabels();
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� v�szon mozgat�s�hoz
    class MouseWatch7 extends MouseInputAdapter {

        BasePoint start, end;
        double savedOriginX, savedOriginY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                saveStateOntoUndoStack();
                savedOriginX = config.originX;
                savedOriginY = config.originY;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                for (int i = 0; i < objectList.size(); i++) {
                    GraphicalObject o = (GraphicalObject) objectList.get(i);
                    //K�pletet nem mozgatjuk
                    if (!(o instanceof Formula)) {
                        o.Translate(transDX, transDY);
                    }
                }
                for (int i = 0; i < objectList.size(); i++) {
                    GraphicalObject o = (GraphicalObject) objectList.get(i);
                    o.Calculate();
                }
                calculateAllLabels();

                config.originX = savedOriginX + transDX;
                config.originY = savedOriginY + transDY;
                transDX = 0;
                transDY = 0;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                transDX = e.getX() - start.x;
                transDY = e.getY() - start.y;
                config.originX = savedOriginX + transDX;
                config.originY = savedOriginY + transDY;
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� k�r elhelyez�s�hez
    class MouseWatch8 extends MouseInputAdapter {

        BasePoint start, end, temp;
        Circle c;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = findNearestPoint(e);
                saveStateOntoUndoStack();
                //Ha m�g nem l�tezik a kezd�pont
                if (start == null) {
                    start = getPoint(e);
                    objectList.add(start);
                    nextBpLabel(start);
                    start.group = nextGroup();
                    start.creationType = BasePoint.SIMPLE_POINT;
                    start.Calculate();
                    calculateLabels(start.group);
                }
                end = start;
                end.group = start.group;
                calculateLabels(start.group);
                start.Select();
                c = makeCircle(start, end, true);
                c.group = start.group;
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                temp = findNearestPoint(e);
                //ha a v�gpont megegyezik a kezd�ponttal
                if (temp == start) {
                    undo();
                } //ha a v�gpont a kezd�pontt�l k�l�nb�z� pont k�zel�ben van
                else if (temp != null) {
                    c.creationObjectList.remove(end);
                    c.creationObjectList.add(temp);
                    reNumberGroups(start, temp);
                    c.Calculate();
                //ha m�g nem l�tezik a v�gpont
                } else {
                    objectList.add(end);
                    nextBpLabel(end);
                    end.group = start.group;
                    end.Calculate();
                }
                deselectAll();
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                if (start != null) {
                    end = getPoint(e);
                    end.group = start.group;
                    end.creationType = BasePoint.SIMPLE_POINT;
                    c.creationObjectList.removeElementAt(1);
                    c.creationObjectList.add(end);
                    c.Calculate();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� mer�leges szakasz elhelyez�s�hez
    class MouseWatch9 extends MouseInputAdapter {

        BasePoint start;
        Line ls1, ls2;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                if (start == null) {
                    start = findNearestPoint(e);
                    if (start != null) {
                        start.Select();
                        repaintAll();
                    }
                } else if (ls1 == null) {
                    ls1 = findNearestLine(e);
                    if (ls1 != null) {
                        ls1.Select();
                        repaintAll();
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                if (start != null & ls1 != null) {
                    start.deSelect();
                    ls1.deSelect();
                    saveStateOntoUndoStack();
                    GraphicalObject[] list = {start, ls1};
                    BasePoint bp = new BasePoint(list, BasePoint.PROJECTED_POINT);
                    objectList.add(bp);
                    bp.group = ls1.group;
                    reNumberGroups(start, ls1);
                    nextBpLabel(bp);

                    start = null;
                    ls1 = null;
                    list[1] = bp;
                    objectList.add(ls2 = new Line(list, Line.TWOPOINTS, Line.LINESEGMENT));
                    ls2.group = bp.group;
                    ls2.label = getNextLsLabel();
                    calculateAllLabels();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� vektor elhelyez�s�hez
    class MouseWatch10 extends MouseInputAdapter {

        BasePoint start, end, temp;
        Line ls;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = findNearestPoint(e);
                saveStateOntoUndoStack();
                //ha a kezd�pont m�g nem l�tez� pont
                if (start == null) {
                    start = getPoint(e);
                    objectList.add(start);
                    nextBpLabel(start);
                    start.group = nextGroup();
                    calculateLabels(start.group);
                }
                end = new BasePoint(e.getX(), e.getY());
                end.group = start.group;
                GraphicalObject[] list = {start, end};
                start.Select();
                objectList.add(ls = new Line(list, Line.TWOPOINTS, Line.VECTOR));
                ls.label = getNextLsLabel();
                ls.group = start.group;
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                temp = findNearestPoint(e);
                //ha a v�gpont megegyezik a kezd�ponttal
                if (temp == start) {
                    undo();
                } else {
                    //ha a v�gpont egy kezd�pontt�l k�l�nb�z�, m�r l�tez� pont
                    if (temp != null) {
                        ls.creationObjectList.remove(end);
                        ls.creationObjectList.add(temp);
                        reNumberGroups(start, temp);
                        ls.Calculate();
                    //ha a v�gpont a kezd�pontt�l k�l�nb�z� �j pont
                    } else {
                        ls.creationObjectList.remove(end);
                        end = getPoint(e);
                        ls.creationObjectList.add(end);
                        ls.Calculate();
                        objectList.add(end);
                        nextBpLabel(end);
                        end.group = start.group;
                        end.Calculate();
                    }
                }
                calculateLabels(ls.group);
                deselectAll();
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                if (start != null) {
                    end.x = e.getX();
                    end.y = e.getY();
                    ls.Calculate();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� felez�pont elhelyez�s�hez
    class MouseWatch11 extends MouseInputAdapter {

        BasePoint bp;
        boolean leftbutton;

        //megvizsg�lja, hogy l�tezik-e m�r a szakasznak/vektornak l�that� felez�pontja
        boolean existMidPoint(Line ls) {
            int i = 0;
            do {
                GraphicalObject o = (GraphicalObject) objectList.get(i);
                //ha az objektum a szakasz/vektor felez�pontja �s l�that�
                if (o.creationType == BasePoint.MIDPOINT && o.creationObjectList.get(0) == ls && o.visible) {
                    return true;
                }
                i++;
            } while (i < objectList.size());
            return false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                Line ls = findNearestLine(e);
                //Csak szakasz �s vektor felez�pontja �rtelmezhet�
                if (ls != null && (ls.mainType == Line.LINESEGMENT | ls.mainType == Line.VECTOR)) {
                    //csak akkor lehet elhelyezni a felez�pontot, ha az m�g l�that� pontk�nt nem l�tezik
                    if (!existMidPoint(ls)) {
                        saveStateOntoUndoStack();
                        GraphicalObject[] list = {ls};
                        objectList.add(bp = new BasePoint(list, BasePoint.MIDPOINT));
                        bp.group = ls.group;
                        nextBpLabel(bp);
                        bp.Calculate();
                        calculateAllLabels();
                        repaintAll();
                    }
                }
            }
        }
    }

    //Eg�resem�nyfigyel� egyenl�sz�r� h�romsz�g elhelyez�s�hez
    class MouseWatch16 extends MouseInputAdapter {

        BasePoint start, end, bp, A, B, C;
        Line a, b, c;
        Line l, nb;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "B", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "C", true);
                    c = makeLineSegment(A, B, "a", true);
                    nb = makeNormalBisector(c, false);
                    C = makeProjectedPointOnLine(start.x, start.y, nb, "A", true);
                    b = makeLineSegment(A, C, "b", true);
                    a = makeLineSegment(B, C, "b", true);
                    calculateLabels(config.actGroup);
                }

                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� der�ksz�g� h�romsz�g elhelyez�s�hez
    class MouseWatch17 extends MouseInputAdapter {

        BasePoint start, end, bp, A, B, C;
        Line a, b, c;
        Line l, n;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    C = makeProjectedPointOnLine(start.x, start.y + height, l, "C", true);
                    A = makeProjectedPointOnLine(start.x + width, start.y + height, l, "A", true);
                    b = makeLineSegment(C, A, "b", true);
                    n = makeNormal(b, C, false);
                    B = makeProjectedPointOnLine(start.x, start.y, n, "B", true);
                    c = makeLineSegment(A, B, "c", true);
                    a = makeLineSegment(B, C, "a", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� szab�lyos h�romsz�g elhelyez�s�hez
    class MouseWatch18 extends MouseInputAdapter {

        BasePoint start, end, bp, A, B, C;
        Line a, b, c;
        Line l, n;
        Circle c1, c2;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    c1 = makeCircle(A, B, false);
                    c2 = makeCircle(B, A, false);
                    C = makeInterSectionPointOnCircles(c1, c2, "C", true);
                    a = makeLineSegment(B, C, "a", true);
                    b = makeLineSegment(A, C, "a", true);
                    c = makeLineSegment(A, B, "a", true);

                    calculateLabels(config.actGroup);
                }

                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.x - end.x));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� �ltal�nos h�romsz�g elhelyez�s�hez
    class MouseWatch20 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, M;
        Line a, b, c;
        Line l, n;
        Circle k;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    C = makePoint(start.x + width / 4, start.y, "C", true);
                    a = makeLineSegment(B, C, "a", true);
                    b = makeLineSegment(A, C, "b", true);
                    c = makeLineSegment(A, B, "c", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� �ltal�nos trap�z elhelyez�s�hez
    class MouseWatch21 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D;
        Line a, b, c, d;
        Line l;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);

                    l = makeHiddenLine(new BasePoint(start.x, start.y), new BasePoint(start.x + 1, start.y));
                    C = makeProjectedPointOnLine(start.x + width * 3 / 5, start.y, l, "C", true);
                    D = makeProjectedPointOnLine(start.x + width / 5, start.y, l, "D", true);

                    a = makeLineSegment(A, B, "a", true);
                    b = makeLineSegment(B, C, "b", true);
                    c = makeLineSegment(C, D, "c", true);
                    d = makeLineSegment(A, D, "d", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� paralelogramma elhelyez�s�hez
    class MouseWatch22 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, O;
        Line a, b, c, d, f;
        Line l;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    D = makePoint(start.x + width / 5, start.y, "D", true);
                    a = makeLineSegment(A, B, "a", true);
                    f = makeLineSegment(B, D, "", false);
                    O = makeMidPoint(f, "O", false);
                    C = makeMirroredPointByPoint(A, O, "C", true);
                    b = makeLineSegment(B, C, "b", true);
                    c = makeLineSegment(C, D, "a", true);
                    d = makeLineSegment(A, D, "b", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� n�gysz�g alap� g�la elhelyez�s�hez
    class MouseWatch23 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, O;
        Line a, b, c, d, f;
        Line l, n;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    D = makePoint(start.x + width / 3, start.y + height * 0.75, "D", true);
                    a = makeLineSegment(A, B, "", true);
                    f = makeLineSegment(B, D, "", false);
                    O = makeMidPoint(f, "", true);
                    C = makeMirroredPointByPoint(A, O, "C", true);
                    b = makeLineSegment(B, C, "", true);
                    c = makeLineSegment(C, D, "", true);
                    d = makeLineSegment(A, D, "", true);
                    n = makeNormal(a, O, false);
                    E = makeProjectedPointOnLine(start.x + width, start.y + height / 4, n, "E", true);
                    a = makeLineSegment(A, E, "", true); //itt majd el kell rejteni a c�mk�ket!

                    b = makeLineSegment(B, E, "", true);
                    c = makeLineSegment(C, E, "", true);
                    d = makeLineSegment(D, E, "", true);
                    f = makeLineSegment(O, E, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� has�b elhelyez�s�hez
    class MouseWatch24 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H, O;
        Line a, b, c, d, f, g, h, i, j, k;
        Line l, n, v;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    a = makeLineSegment(A, B, "", true);
                    l = makeNormal(a, A, false);
                    E = makeProjectedPointOnLine(start.x, start.y, l, "E", true);
                    f = makeLineSegment(B, E, "l", false);
                    O = makeMidPoint(f, "O", false);
                    F = makeMirroredPointByPoint(A, O, "F", true);
                    makeLineSegment(B, F, "", true);
                    makeLineSegment(E, F, "", true);
                    makeLineSegment(A, E, "", true);
                    C = makePoint(start.x + width * 1.5, start.y + height * 0.75, "C", true);
                    v = makeVector(B, C, "a", false);
                    D = makeTranslatedPointByVector(A, v, "D", true);
                    makeLineSegment(B, C, "", true);
                    makeLineSegment(C, D, "", true);
                    makeLineSegment(A, D, "", true);
                    H = makeTranslatedPointByVector(E, v, "H", true);
                    G = makeTranslatedPointByVector(F, v, "G", true);
                    makeLineSegment(F, G, "", true);
                    makeLineSegment(G, H, "", true);
                    makeLineSegment(H, E, "", true);
                    makeLineSegment(G, C, "", true);
                    makeLineSegment(H, D, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� t�glalap elhelyez�s�hez
    class MouseWatch25 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H, O;
        Line a, b, c, d, f, g, h, i, j, k;
        Line l, n, v;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    a = makeLineSegment(A, B, "a", true);
                    l = makeNormal(a, A, false);
                    D = makeProjectedPointOnLine(start.x, start.y, l, "D", true);
                    f = makeLineSegment(B, D, "l", false);
                    O = makeMidPoint(f, "O", false);
                    C = makeMirroredPointByPoint(A, O, "C", true);
                    b = makeLineSegment(B, C, "b", true);
                    c = makeLineSegment(C, D, "a", true);
                    d = makeLineSegment(A, D, "b", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� deltoid elhelyez�s�hez
    class MouseWatch26 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H, O;
        Line a, b, c, d, f, g, h, i, j, k;
        Line l, nb;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;
                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height / 3), new BasePoint(start.x + 1, start.y + height / 3));
                    A = makeProjectedPointOnLine(start.x, start.y, l, "A", true);
                    C = makeProjectedPointOnLine(start.x + width, start.y, l, "C", true);
                    f = makeLineSegment(A, C, "", false);
                    nb = makeNormalBisector(f, false);
                    B = makeProjectedPointOnLine(start.x, start.y, nb, "B", true);
                    D = makeProjectedPointOnLine(start.x, start.y + height, nb, "D", true);
                    a = makeLineSegment(A, B, "a", true);
                    b = makeLineSegment(B, C, "a", true);
                    a = makeLineSegment(C, D, "b", true);
                    a = makeLineSegment(A, D, "b", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� rombusz elhelyez�s�hez
    class MouseWatch27 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D;
        Line a, b, c, d;
        Line l, v;
        Circle k;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;
                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width * 2 / 3, start.y + height, l, "B", true);
                    k = makeCircle(A, B, false);
                    D = makeProjectedPointOnCircle(start.x + width / 3, start.y, k, "D", true);
                    v = makeVector(A, B, "", false);
                    C = makeTranslatedPointByVector(D, v, "C", true);
                    makeLineSegment(A, B, "a", true);
                    makeLineSegment(B, C, "a", true);
                    makeLineSegment(C, D, "a", true);
                    makeLineSegment(A, D, "a", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� szab�lyos soksz�g elhelyez�s�hez
    class MouseWatch28 extends MouseInputAdapter {

        int n = 5;
        BasePoint start, end, O, P, A, lastPoint;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        Line l;
        Circle c;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 40 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 40) {
                    DialogForPoligon dlg = new DialogForPoligon(null, true);
                    dlg.setVisible(true);
                    if (dlg.getReturnStatus() == DialogForPoligon.RET_OK) {
                        saveStateOntoUndoStack();
                        nextGroup();
                        n = dlg.getReturnValue();
                        r = width / 2;
                        alfa = -180 / n - 90;
                        l = makeHiddenLine(O = new BasePoint(start.x + r, start.y + r),
                                P = new BasePoint(start.x + r + r * Math.cos(Math.toRadians(alfa)), start.y + r - r * Math.sin(Math.toRadians(alfa))));
                        A = makeProjectedPointOnLine(P.x, P.y, l, "A", true);
                        c = makeCircle(O, A, true);
                        lastPoint = A;
                        config.actBpLabel = "A";
                        makeCentrePoint(c, "", true);
                        for (int i = 0; i < n - 1; i++) {
                            alfa += (360 / n);
                            getNextBpLabel();
                            P = makeFixedPointOnCircle(c, alfa, config.actBpLabel, true);
                            makeLineSegment(lastPoint, P, "", true);
                            lastPoint = P;
                        }
                        makeLineSegment(A, lastPoint, "", true);

                        calculateLabels(config.actGroup);
                    }
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.x - end.x));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� �ltal�nos soksz�g elhelyez�s�hez
    class MouseWatch29 extends MouseInputAdapter {

        int n = 0;
        BasePoint start, end, O, P, A, lastPoint;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        Line l;
        Circle c;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 40 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 40) {
                    DialogForPoligon dlg = new DialogForPoligon(null, true);
                    dlg.setVisible(true);
                    if (dlg.getReturnStatus() == DialogForPoligon.RET_OK) {
                        saveStateOntoUndoStack();
                        nextGroup();
                        n = dlg.getReturnValue();
                        r = width / 2;
                        alfa = (double) 180 / n + 90;
                        //a k�r k�z�ppontj�t r�gz�teni kell!
                        O = new BasePoint(start.x + r, start.y + r);
                        A = makePoint(O.x + r * Math.cos(Math.toRadians(alfa)), O.y + r * Math.sin(Math.toRadians(alfa)), "A", true);
                        lastPoint = A;
                        config.actBpLabel = "A";
                        for (int i = 0; i < n - 1; i++) {
                            alfa -= (360 / n);
                            //itt mindenk�ppen kell a k�vetkez� c�mke, ez�rt nem nextBpLabel();
                            config.actBpLabel = new String("" + (char) ((int) config.actBpLabel.charAt(0) + 1));
                            P = makePoint(O.x + r * Math.cos(Math.toRadians(alfa)), O.y + r * Math.sin(Math.toRadians(alfa)), config.actBpLabel, true);
                            makeLineSegment(lastPoint, P, "", true);
                            lastPoint = P;
                        }
                        makeLineSegment(A, lastPoint, "", true);

                        calculateLabels(config.actGroup);
                    }
                }

                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.x - end.x));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� n�gyzet elhelyez�s�hez
    class MouseWatch30 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D;
        Line l, v;
        Circle c;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();

                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    c = makeCircle(A, B, false);
                    D = makeFixedPointOnCircle(c, 90, "D", true);
                    v = makeVector(A, B, "", false);
                    C = makeTranslatedPointByVector(D, v, "C", true);
                    makeLineSegment(A, B, "a", true);
                    makeLineSegment(B, C, "a", true);
                    makeLineSegment(C, D, "a", true);
                    makeLineSegment(A, D, "a", true);
                    calculateLabels(config.actGroup);
                }

                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.x - end.x));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� kocka elhelyez�s�hez
    class MouseWatch31 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H, M;
        Line l, a, v;
        Circle c;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y + height, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y + height, l, "B", true);
                    c = makeCircle(A, B, false);
                    E = makeFixedPointOnCircle(c, 90, "E", true);
                    v = makeVector(A, B, "", false);
                    F = makeTranslatedPointByVector(E, v, "F", true);
                    a = makeLineSegment(A, B, "", true);
                    makeLineSegment(B, F, "", true);
                    makeLineSegment(F, E, "", true);
                    makeLineSegment(A, E, "", true);
                    M = makeMidPoint(a, "", false);
                    c = makeCircle(B, M, false);
                    C = makeFixedPointOnCircle(c, 45, "C", true);
                    makeLineSegment(B, C, "", true);
                    v = makeVector(B, C, "", false);
                    G = makeTranslatedPointByVector(F, v, "G", true);
                    H = makeTranslatedPointByVector(E, v, "H", true);
                    D = makeTranslatedPointByVector(A, v, "D", true);
                    makeLineSegment(C, G, "", true);
                    makeLineSegment(F, G, "", true);
                    makeLineSegment(E, H, "", true);
                    makeLineSegment(A, D, "", true);
                    makeLineSegment(C, D, "", true);
                    makeLineSegment(H, G, "", true);
                    makeLineSegment(H, D, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.x - end.x));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� pontba eltolt vektor elhelyez�s�hez
    class MouseWatch33 extends MouseInputAdapter {

        BasePoint start;
        Line v, v2;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                //ha m�g nem lett kijel�lve a vektor
                if (v == null) {
                    v = findNearestVector(e);
                    //ha siker�lt vektort kijel�lni
                    if (v != null) {
                        v.Select();
                        repaintAll();
                    }
                    //ha m�g nem lett kijel�lve a pont
                } else if (start == null) {
                    start = findNearestPoint(e);
                    //ha van a k�zelben l�tez� pont
                    if (start != null) {
                        start.Select();
                    } else {
                        start = makePoint(e.getX(), e.getY(), getNextBpLabel(), true);
                        start.Select();
                    }
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                if (start != null & v != null) {
                    deselectAll();
                    saveStateOntoUndoStack();
                    GraphicalObject[] list = {start, v};
                    BasePoint bp = new BasePoint(list, BasePoint.TRANSLATEDPOINT_BY_VECTOR);
                    objectList.add(bp);
                    bp.group = v.group;
                    reNumberGroups(start, v);
                    nextBpLabel(bp);

                    start = null;
                    v = null;
                    list[1] = bp;
                    objectList.add(v2 = new Line(list, Line.TWOPOINTS, Line.VECTOR));
                    v2.group = bp.group;
                    v2.label = config.actLsLabel;
                    config.actLsLabel = new String("" + (char) ((int) config.actLsLabel.charAt(0) + 1));
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel�: �r�s a f�li�ra
    class MouseWatch34 extends MouseInputAdapter {

        int lastPointX, lastPointY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                actFoil.setStroke(new BasicStroke(2));
                actFoil.setColor((Color) penColorPalette.get(config.actPenColor));
                lastPointX = e.getX();
                lastPointY = e.getY();
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                Line2D l = new Line2D.Double(lastPointX, lastPointY, e.getX(), e.getY());
                actFoil.draw(l);
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                Line2D l = new Line2D.Double(lastPointX, lastPointY, e.getX(), e.getY());
                actFoil.draw(l);
                lastPointX = e.getX();
                lastPointY = e.getY();
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� k�p elhelyez�s�hez
    class MouseWatch35 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E;
        Line l;
        Line a;
        Ellipse el;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;
                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y, l, "A", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y, l, "B", true);
                    a = makeLineSegment(A, B, "", false);
                    C = makeMidPoint(a, "C", false);
                    l = makeNormal(a, C, false);
                    D = makeProjectedPointOnLine(start.x, start.y + height + width / 10, l, "", false);
                    makeEllipse(C, B, D, true);
                    E = makeProjectedPointOnLine(start.x, start.y, l, "C", true);
                    makeLineSegment(A, E, "", true);
                    makeLineSegment(B, E, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� henger elhelyez�s�hez
    class MouseWatch36 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H;
        Line l, a;
        Ellipse el;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;
                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y, l, "", true);
                    B = makeProjectedPointOnLine(start.x + width, start.y, l, "", true);
                    a = makeLineSegment(A, B, "", false);
                    C = makeMidPoint(a, "C", false);
                    l = makeNormal(a, C, false);
                    D = makeProjectedPointOnLine(start.x + width / 2, start.y + height + width / 10, l, "", false);
                    makeEllipse(C, B, D, true);
                    l = makeNormal(a, A, false);
                    E = makeProjectedPointOnLine(start.x, start.y, l, "", true);
                    F = makeTranslatedPointByVector(E, makeVector(A, C, "", false), "", false);
                    G = makeTranslatedPointByVector(F, makeVector(C, D, "", false), "", false);
                    makeEllipse(F, E, G, true);
                    H = makeMirroredPointByPoint(E, F, "", false);
                    makeLineSegment(A, E, "", true);
                    makeLineSegment(B, H, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� csonkak�p elhelyez�s�hez
    class MouseWatch37 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H, I;
        Line l, l2, a, v;
        Ellipse el;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;
                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    l = makeHiddenLine(new BasePoint(start.x, start.y + height), new BasePoint(start.x + 1, start.y + height));
                    A = makeProjectedPointOnLine(start.x, start.y, l, "", true);
                    C = makeProjectedPointOnLine(start.x + width / 2, start.y, l, "", true);
                    B = makeMirroredPointByPoint(A, C, "", true);
                    a = makeLineSegment(A, B, "", false);
                    l = makeNormal(a, C, false);
                    D = makeProjectedPointOnLine(start.x + width / 2, start.y + height + width / 10, l, "", false);
                    E = makeProjectedPointOnLine(start.x, start.y + width / 10, l, "", true);
                    makeEllipse(C, B, D, true);
                    l2 = makeNormal(l, E, false);
                    G = makeProjectedPointOnLine(start.x + width / 10, start.y + height + width / 10, l2, "", true);
                    H = makeMirroredPointByPoint(G, E, "", true);
                    v = makeVector(C, D, "", false);
                    I = makeTranslatedPointByVector(E, v, "", false);
                    makeEllipse(E, H, I, true);
                    makeLineSegment(A, G, "", true);
                    makeLineSegment(B, H, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Kijel�l� poligon elhelyez�se eg�resem�nyfigyel�
    class MouseWatch39 extends MouseInputAdapter {

        BasePoint bp;
        BasePoint[] node = new BasePoint[100];
        Poligon p;
        int n = -1;
        boolean leftbutton, closed = false;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                bp = findNearestPoint(e);
                //ha pont lett kijel�lve
                if (bp != null) {
                    if (n == -1) {
                        saveStateOntoUndoStack();
                    }
                    ////ha legal�bb 3 k�l�nb�z� pont ki lett m�r jel�lve
                    if (n >= 2) {
                        //ha m�g nincs a list�ban az pont, akkor fel kell venni
                        if (!contains()) {
                            node[++n] = bp;
                            bp.Select();
                            repaintAll();
                        } //k�l�nben a poligon z�rt
                        else {
                            closed = true;
                        }
                    } else {
                        //ha m�g nincs a list�ban az pont, akkor fel kell venni
                        if (!contains()) {
                            node[++n] = bp;
                            bp.Select();
                            repaintAll();
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                //ha a poligon z�rt, akkor r�gz�teni kell �s mindent alaphelyzetbe kell �ll�tani
                if (closed) {
                    p = new Poligon(node, n + 1);
                    n = -1;
                    deselectAll();
                    p.group = node[1].group;
                    for (int i = 0; i < node.length; i++) {
                        node[i] = null;
                    }
                    closed = false;
                    objectList.add(p);
                    p.color = (Color) highlightColorPalette.get(config.actHighLightColor++);
                    //aktu�lis kijel�l� sz�n rot�l�sa 
                    if (config.actHighLightColor == highlightColorPalette.size()) {
                        config.actHighLightColor = 0;
                    }
                    highlightColorIcon.color = (Color) highlightColorPalette.get(config.actHighLightColor);
                    downToolBar.repaint();
                    repaintAll();
                }
            }
        }

        boolean contains() {
            for (int i = 0; i < node.length; i++) {
                if (node[i] == bp) {
                    return true;
                }
            }
            return false;
        }
    }

    //Eg�resem�nyfigyel� p�rhuzamos szakasz elhelyez�s�hez
    class MouseWatch43 extends MouseInputAdapter {

        BasePoint start, end, lsStart, lsEnd, cloneStart, cloneEnd, temp, sp, ep;
        Line ls1, ls2, v;
        double x, y;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                //ha m�g nincs kijel�lve a kezd�pont
                if (start == null) {
                    //ha m�r ki van v�lasztva a szakasz
                    if (ls1 != null) {
                        start = findNearestPoint(e);
                        //ha pont lett kiv�lasztva
                        if (start != null) {
                            lsStart = (BasePoint) ls1.creationObjectList.get(0);
                            lsEnd = (BasePoint) ls1.creationObjectList.get(1);
                            if (start == lsEnd) {
                                temp = lsStart;
                                lsStart = lsEnd;
                                lsEnd = temp;
                            }
                            //ha a szakasz pontja lett kijel�lve
                            if (start == lsStart | start == lsEnd) {
                                cloneStart = new BasePoint(start.x, start.y);
                                cloneStart.group = nextGroup();
                                cloneStart.label = "";
                                v = makeVector(lsStart, lsEnd, "", false);
                                v.group = cloneStart.group;
                                cloneEnd = makeTranslatedPointByVector(cloneStart, v, "", true);
                                cloneEnd.group = cloneStart.group;
                                ls2 = makeLineSegment(cloneStart, cloneEnd, "", true);
                                ls2.group = cloneStart.group;
                                ls2.Calculate();
                                repaintAll();
                            }
                        }
                    } // ha m�g nincs kiv�lasztva a szakasz
                    else {
                        ls1 = findNearestLineSegment(e);
                        if (ls1 != null) {
                            saveStateOntoUndoStack();
                            ls1.Select();
                            repaintAll();
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                start = findNearestPoint(e);
                if (ls2 != null) {
                    deselectAll();
                    //ha a v�gpont �j pont
                    if (start == null) {
                        objectList.add(cloneStart);
                        cloneStart.label = getNextBpLabel();
                    //ha a v�gpont megl�v� pont
                    } else {
                        ls2.creationObjectList.remove(0);
                        ls2.creationObjectList.add(0, start);
                        cloneEnd.creationObjectList.remove(0);
                        cloneEnd.creationObjectList.add(0, start);
                        cloneEnd.Calculate();
                        reNumberGroups(cloneEnd, start);
                    }

                    cloneEnd.label = getNextBpLabel();
                    ls2.label = config.actLsLabel;
                    ls2.Calculate();
                    config.actLsLabel = new String("" + (char) ((int) config.actLsLabel.charAt(0) + 1));
                    calculateAllLabels();
                    start = null;
                    ls1 = null;
                    ls2 = null;
                    cloneStart = null;
                    cloneEnd = null;
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                //ha a kezd�pont ki van jel�lve
                if (cloneStart != null) {
                    cloneStart.x = e.getX();
                    cloneStart.y = e.getY();
                    cloneStart.Calculate();
                    cloneEnd.Calculate();
                    ls2.Calculate();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� f�legyenes elhelyez�s�hez
    class MouseWatch44 extends MouseInputAdapter {

        BasePoint start, end, temp;
        Line ls;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = findNearestPoint(e);
                saveStateOntoUndoStack();
                if (start == null) {
                    start = getPoint(e);
                    objectList.add(start);
                    nextBpLabel(start);
                    start.group = nextGroup();
                    start.creationType = BasePoint.SIMPLE_POINT;
                    start.Calculate();
                    calculateLabels(start.group);
                }
                end = start;
                end.group = start.group;
                GraphicalObject[] list = {start, end};
                start.Select();
                objectList.add(ls = new Line(list, Line.TWOPOINTS, Line.LINESEGMENT));
                ls.label = "";
                ls.group = start.group;
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                temp = findNearestPoint(e);
                //ha a kezd�pont �s a v�gpont megegyezik
                if (temp == start) {
                    undo();
                } //ha a v�gpont m�r l�tez� pont
                else if (temp != null) {
                    ls.creationObjectList.remove(end);
                    ls.creationObjectList.add(temp);
                    config.actLsLabel = new String("" + (char) ((int) config.actLsLabel.charAt(0) + 1));
                    reNumberGroups(start, temp);
                //ha a v�gpont �j pont
                } else {
                    objectList.add(end);
                    nextBpLabel(end);
                    end.group = start.group;
                    end.Calculate();
                }
                ls.mainType = Line.HALFLINE;
                ls.Calculate();
                calculateLabels(ls.group);
                deselectAll();
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                if (start != null) {
                    ls.creationObjectList.remove(end);
                    ls.creationObjectList.add(end = getPoint(e));
                    end.group = start.group;
                    end.creationType = BasePoint.SIMPLE_POINT;
                    ls.Calculate();
                    repaintAll();
                }
            }
        }
    }

    //K�r�l�rt k�r 3 pontb�l
    class MouseWatch46 extends MouseInputAdapter {

        BasePoint bp;
        BasePoint[] node = new BasePoint[3];
        Circle c;
        int n = -1;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                bp = findNearestPoint(e);
                //ha pont lett kijel�lve �s m�g nincs a list�ban, akkor fel kell venni
                if (bp != null & !contains()) {
                    node[++n] = bp;
                    bp.Select();
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                //ha ki lett jel�lve mindh�rom pont
                if (n == 2) {
                    deselectAll();
                    saveStateOntoUndoStack();
                    c = makeOuterCircle(node[0], node[1], node[2], true);
                    c.group = node[0].group;
                    node[1].group = c.group;
                    node[2].group = c.group;
                    n = -1;
                    bp = makeCentrePoint(c, "", true);
                    bp.group = c.group;
                    for (int i = 0; i < node.length; i++) {
                        node[i] = null;
                    }
                    repaintAll();
                }
            }
        }

        boolean contains() {
            for (int i = 0; i < node.length; i++) {
                if (node[i] == bp) {
                    return true;
                }
            }
            return false;
        }
    }

    //Be�rt k�r 3 pontb�l
    class MouseWatch47 extends MouseInputAdapter {

        BasePoint bp;
        BasePoint[] node = new BasePoint[3];
        Circle circle;
        Line a, b, c;
        int n = -1;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                bp = findNearestPoint(e);
                //ha pont lett kijel�lve �s m�g nincs a list�ban, akkor fel kell venni
                if (bp != null & !contains()) {
                    node[++n] = bp;
                    bp.Select();
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                //ha mindh�rom pont ki lett jel�lve 
                if (n == 2) {
                    deselectAll();
                    saveStateOntoUndoStack();

                    a = findLineSegment(node[0], node[1]);
                    b = findLineSegment(node[1], node[2]);
                    c = findLineSegment(node[2], node[0]);
                    //ha nincsenek szakaszok, l�tre kell hozni �ket
                    if (a == null) {
                        a = makeLineSegment(node[0], node[1], "", false);
                    }
                    if (b == null) {
                        b = makeLineSegment(node[1], node[2], "", false);
                    }
                    if (c == null) {
                        c = makeLineSegment(node[2], node[0], "", false);
                    }
                    circle = makeInnerCircle(a, b, c, true);
                    n = -1;
                    for (int i = 0; i < node.length; i++) {
                        node[i] = null;
                    }
                    repaintAll();
                }
            }
        }

        boolean contains() {
            for (int i = 0; i < node.length; i++) {
                if (node[i] == bp) {
                    return true;
                }
            }
            return false;
        }
    }

    //Eg�resem�nyfigyel� alakzat kl�noz�s�hoz
    class MouseWatch49 extends MouseInputAdapter {

        GraphicalObject o;
        BasePoint start;
        double transDXo, transDYo;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                o = findNearestObject(e);
                //ha tal�lt objektumot
                if (o != null) {
                    saveStateOntoUndoStack();
                    cloneGroup(o.group);
                    selectGroup(o.group);
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                if (o != null) {
                    moveSelectedGroup(transDXo, transDYo);
                    deselectAll();
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                //ha van kijel�lt objektum
                if (o != null) {
                    transDXo = e.getX() - start.x;
                    transDYo = e.getY() - start.y;
                    moveSelectedGroup(transDXo, transDYo);
                    start.x = e.getX();
                    start.y = e.getY();
                    calculateLabels(o.group);
                    repaintAll();
                }
            }
        }
    }

    //S�lypont elhelyez�se eg�resem�nyfigyel�
    class MouseWatch51 extends MouseInputAdapter {

        BasePoint bp;
        BasePoint[] node = new BasePoint[100];
        BasePoint wp;
        int n = -1;
        boolean leftbutton, closed = false;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                bp = findNearestPoint(e);
                //ha pont lett kijel�lve
                if (bp != null) {
                    if (n == -1) {
                        saveStateOntoUndoStack();
                    }
                    ////ha legal�bb 3 k�l�nb�z� pont ki lett m�r jel�lve
                    if (n >= 2) {
                        //ha m�g nincs a list�ban a pont, akkor fel kell venni
                        if (!contains()) {
                            node[++n] = bp;
                            bp.Select();
                            repaintAll();
                        } //ha a kezd�pont lett kijel�lve, akkor a poligon z�rt
                        else if (bp == node[0]) {
                            closed = true;
                        }
                    } else {
                        //ha m�g nincs a list�ban a pont, akkor fel kell venni
                        if (!contains()) {
                            node[++n] = bp;
                            bp.Select();
                            repaintAll();
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                //ha a poligon z�rt, akkor r�gz�teni kell �s mindent alaphelyzetbe kell �ll�tani
                if (closed) {
                    wp = new BasePoint(node, BasePoint.WEIGHTPOINT);
                    wp.n = n + 1;
                    wp.group = bp.group;
                    nextBpLabel(wp);
                    wp.Calculate();
                    n = -1;
                    deselectAll();
                    for (int i = 0; i < node.length; i++) {
                        node[i] = null;
                    }
                    closed = false;
                    objectList.add(wp);
                    calculateAllLabels();
                    repaintAll();
                }
            }
        }

        boolean contains() {
            for (int i = 0; i < node.length; i++) {
                if (node[i] == bp) {
                    return true;
                }
            }
            return false;
        }
    }

    //Eg�resem�nyfigyel� h�romsz�g alap� g�la elhelyez�s�hez
    class MouseWatch52 extends MouseInputAdapter {

        BasePoint start, end, A, B, C, D, E, F, G, H, I, S;
        Line l, l2, a, b, c;
        double tempSX, tempSY, tempEX, tempEY, r, alfa;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;
                //legal�bb 20 pixel sz�lesnek kell lennie az alakzatnak
                if (width > 20) {
                    saveStateOntoUndoStack();
                    nextGroup();
                    A = makePoint(start.x, end.y, "A", true);
                    B = makePoint(end.x, start.y + (end.y - start.y) * 2 / 3, "B", true);
                    C = makePoint(start.x + (end.y - start.y) * 3 / 7, start.y + (end.y - start.y) * 3 / 7, "C", true);
                    a = makeLineSegment(B, C, "", true);
                    b = makeLineSegment(A, C, "", true);
                    c = makeLineSegment(A, B, "", true);
                    S = makeWeightPoint(A, B, C, "", true);
                    l = makeFixedAlfaLine(S, Math.PI / 2, false);
                    D = makeProjectedPointOnLine(start.x, start.y, l, "D", true);
                    makeLineSegment(A, D, "", true);
                    makeLineSegment(B, D, "", true);
                    makeLineSegment(C, D, "", true);
                    makeLineSegment(S, D, "", true);

                    calculateLabels(config.actGroup);
                }
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� alakzat t�rl�s�hez
    class MouseWatch53 extends MouseInputAdapter {

        BasePoint start;
        GraphicalObject o;
        double transDXo, transDYo;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                o = findNearestObject(e);
                //ha nincs k�zel objektum, k�p keres�se
                if (o == null) {
                    o = findNearestFormula(e);
                }
                //ha van kijel�lt objektum
                if (o != null) {
                    saveStateOntoUndoStack();
                    selectGroup(o.group);
                }
                repaintAll();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                if (o != null) {
                    if ((e.getX() < 0) | (e.getX() > paintPanel.getBounds().width) |
                            (e.getY() < 0) | (e.getY() > paintPanel.getBounds().height)) {
                        removeSelectedGroup();
                    } else {
                        undo();
                    }
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                //ha van kijel�lt objektum
                if (o != null) {
                    transDXo = e.getX() - start.x;
                    transDYo = e.getY() - start.y;
                    moveSelectedGroup(transDXo, transDYo);
                    start.x = e.getX();
                    start.y = e.getY();
                    calculateLabels(o.group);
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� sz�gfelez� egyenes elhelyez�s�hez (M�g nincs K�SZ!!!) BUG
    class MouseWatch55 extends MouseInputAdapter {

        BasePoint A, B, C, D, start, end, lsStart, lsEnd, cloneStart, cloneEnd, temp, sp, ep;
        Line a, b;
        double x, y;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                //ha m�g nincs kijel�lve az els� szakasz
                if (a == null) {
                    a = findNearestLine(e);
                    //ha siker�lt kijel�lni az els� szakaszt
                    if (a != null) {
                        A = (BasePoint) a.creationObjectList.get(0); //ITT A HIBA: FindPoint elj�r�st kell �rni!!!

                        B = (BasePoint) a.creationObjectList.get(1);
                        a.Select();
                    }
                } //ha m�g nincs kijel�lve a m�sodik szakasz
                else if (b == null) {
                    b = findNearestLine(e);
                    //k�l�nb�z� szakaszoknak kell lenni�k
                    if (b == a) {
                        b = null;
                    }
                    //ha siker�lt kijel�lni a m�sodik szakaszt                    
                    if (b != null) {
                        C = (BasePoint) b.creationObjectList.get(0);
                        D = (BasePoint) b.creationObjectList.get(1);
                        deselectAll();
                        //a h�rom cs�cs meghat�roz�sa
                        if (C == A) {
                            saveStateOntoUndoStack();
                            makeBisector(B, A, D, "", true);
                        } else if (C == B) {
                            saveStateOntoUndoStack();
                            makeBisector(A, B, D, "", true);
                        } else if (D == A) {
                            saveStateOntoUndoStack();
                            makeBisector(B, A, C, "", true);
                        } else if (D == B) {
                            saveStateOntoUndoStack();
                            makeBisector(A, B, C, "", true);
                        }
                        a = null;
                        b = null;
                    }
                }
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� f�lia r�szleges t�rl�s�hez - szivacs funkci�
    class MouseWatch56 extends MouseInputAdapter {

        BasePoint start, end;
        double tempSX, tempSY, tempEX, tempEY;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                start = new BasePoint(e.getX(), e.getY());
                selectionBoxOn = true;
                selectionBox = new Rectangle();
                selectionBox.setBounds(e.getX(), e.getY(), 1, 1);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                double width = selectionBox.width;
                double height = selectionBox.height;
                end = new BasePoint(e.getX(), e.getY());
                tempSX = Math.min(start.x, end.x);
                tempEX = Math.max(start.x, end.x);
                tempSY = Math.min(start.y, end.y);
                tempEY = Math.max(start.y, end.y);
                start.x = tempSX;
                start.y = tempSY;
                end.x = tempEX;
                end.y = tempEY;

                actFoil.setColor(getBackground());
                actFoil.fillRect((int) start.x, (int) start.y, (int) width, (int) height);
                selectionBoxOn = false;
                repaintAll();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                end = new BasePoint(e.getX(), e.getY());
                selectionBox.setBounds((int) Math.min(start.x, end.x), (int) Math.min(start.y, end.y), (int) Math.abs(start.x - end.x), (int) Math.abs(start.y - end.y));
                repaintAll();
            }
        }
    }

    //Eg�resem�nyfigyel� p�rhuzamos f�legyenes elhelyez�s�hez
    class MouseWatch58 extends MouseInputAdapter {

        BasePoint start, end, hlStart, hlEnd, cloneStart, cloneEnd, temp, sp, ep;
        Line selectedHalfLine, newHalfLine, v;
        double x, y;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                //ha m�g nincs kijel�lve a kezd�pont
                if (start == null) {
                    //ha m�r ki van v�lasztva a f�legyenes
                    if (selectedHalfLine != null) {
                        start = findNearestPoint(e);
                        //ha pont lett kiv�lasztva
                        if (start != null) {
                            hlStart = (BasePoint) selectedHalfLine.creationObjectList.get(0);
                            hlEnd = (BasePoint) selectedHalfLine.creationObjectList.get(1);
                            if (start == hlEnd) {
                                temp = hlStart;
                                hlStart = hlEnd;
                                hlEnd = temp;
                            }
                            //ha a f�legyenes pontja lett kijel�lve
                            if (start == hlStart | start == hlEnd) {
                                //kezd�pont "kl�noz�sa"
                                cloneStart = new BasePoint(start.x, start.y);
                                cloneStart.group = nextGroup();
                                v = makeVector(hlStart, hlEnd, "", false);
                                v.group = cloneStart.group;
                                cloneEnd = makeTranslatedPointByVector(cloneStart, v, "", true);
                                newHalfLine = makeHalfLine2(cloneStart, cloneEnd, "", true);
                                newHalfLine.Calculate();
                                repaintAll();
                            }
                        }
                    } // ha m�g nincs kiv�lasztva a f�legyenes
                    else {
                        selectedHalfLine = findNearestHalfLine(e);
                        if (selectedHalfLine != null) {
                            saveStateOntoUndoStack();
                            selectedHalfLine.Select();
                            repaintAll();
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = false;
                start = findNearestPoint(e);
                if (newHalfLine != null) {
                    deselectAll();
                    //ha a v�gpont �j pont
                    if (start == null) {
                        objectList.add(cloneStart);
                        cloneStart.label = getNextBpLabel();
                    //ha a v�gpont megl�v� pont
                    } else {
                        newHalfLine.creationObjectList.remove(0);
                        newHalfLine.creationObjectList.add(0, start);
                        cloneEnd.creationObjectList.remove(0);
                        cloneEnd.creationObjectList.add(0, start);
                        cloneEnd.Calculate();
                        reNumberGroups(cloneEnd, start);
                    }
                    cloneEnd.label = getNextBpLabel();
                    newHalfLine.Calculate();
                    calculateAllLabels();
                    start = null;
                    selectedHalfLine = null;
                    newHalfLine = null;
                    cloneStart = null;
                    cloneEnd = null;
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (leftbutton) {
                //ha a kezd�pont ki van jel�lve
                if (cloneStart != null) {
                    cloneStart.x = e.getX();
                    cloneStart.y = e.getY();
                    cloneStart.Calculate();
                    cloneEnd.Calculate();
                    newHalfLine.Calculate();
                    calculateAllLabels();
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� szakaszfelez� mer�leges elhelyez�s�hez
    class MouseWatch60 extends MouseInputAdapter {

        BasePoint start, end, lsStart, lsEnd, cloneStart, cloneEnd, temp, sp, ep;
        Line a, f;
        double x, y;
        boolean leftbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                leftbutton = true;
                a = findNearestLineSegment(e);
                //ha siker�lt szakaszt kijel�lni
                if (a != null) {
                    saveStateOntoUndoStack();
                    f = makeNormalBisector(a, true);
                    f.group = a.group;
                    repaintAll();
                }
            }
        }
    }

    //Eg�resem�nyfigyel� k�plet elhelyez�s�hez
    class MouseWatch69 extends MouseInputAdapter {

        String fileName;
        Formula f;

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                JFrame parent = new JFrame();
                DialogForFormula dlgTree = new DialogForFormula(parent, "V�lasszon az al�bbi lehet�s�gek k�z�l!");
                fileName = dlgTree.showDialog();
                if (fileName != null) {
                    fileName = "pics/" + fileName + ".bmp";
                    saveStateOntoUndoStack();
                    nextGroup();
                    objectList.add(f = new Formula(e.getX(), e.getY(), fileName));
                    f.group = config.actGroup;
                }
                repaintAll();
            }
        }
    }

    //**************************************************************************

    //�sszes�tett nyom�gomb esem�nyfigyel�
    class ButtonWatch extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            URL url = null;
            URLClassLoader urlLoader = (URLClassLoader) GeoProjektor.class.getClassLoader();
            if (e.getButton() == MouseEvent.BUTTON1) {
                boolean allowChange = true;
                MouseListener mw = null;
                //Pont elhelyez�se
                if (e.getComponent() == Button1) {
                    mw = new MouseWatch1();
                    initDia();
                }
                //Szakasz elhelyez�se
                if (e.getComponent() == Button2) {
                    mw = new MouseWatch2();
                    statusBarText.setText("SZAKASZ: Kattint�ssal �s h�z�ssal k�sse �ssze a szakasz kezd� �s v�gpontj�t!");
                }
                if (e.getComponent() == Button3) {
                    mw = new MouseWatch3();
                    statusBarText.setText("PONT/ALAKZAT/K�PLET MOZGAT�SA: Kattint�ssal jel�lje ki a mozgatni k�v�nt objektumot, majd h�z�ssal helyezze el az �j poz�ci�ban!");
                    paintPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                if (e.getComponent() == Button4) {
                    mw = new MouseWatch4();
                    statusBarText.setText("EGYENES: Kattint�ssal �s h�z�ssal jel�lje ki azt a k�t pontot, melyen az egyenes �thalad!");
                }
                //Nagy�t�s/kicsiny�t�s
                if (e.getComponent() == Button5) {
                    mw = new MouseWatch5();
                    statusBarText.setText("NAGY�T�S/KICSINY�T�S: Kattintson a szerkeszt�v�sznon, majd lassan mozgassa az eg�rmutat�t le �s fel a nagy�t�shoz/kicsiny�t�shez!");
                }
                //R�csok be/ki
                if (e.getComponent() == Button6) {
                    config.gridOn = !config.gridOn;
                    allowChange = false;
                }
                if (e.getComponent() == Button7) {
                    mw = new MouseWatch7();
                    statusBarText.setText("SZERKESZT�V�SZON MOZGAT�SA: A szerkeszt�v�szon tetsz�leges pontj�n, kattint�ssal �s h�z�ssal mozgassa a v�sznat a k�v�nt poz�ci�ba!");
                }
                if (e.getComponent() == Button8) {
                    mw = new MouseWatch8();
                    statusBarText.setText("K�R: Kattint�ssal �s h�z�ssal jel�lje ki a k�r k�z�ppontj�t �s a sug�r nagys�g�t k�t pont �sszek�t�s�vel!");
                }
                if (e.getComponent() == Button9) {
                    mw = new MouseWatch9();
                    statusBarText.setText("MER�LEGES SZAKASZ: Kattint�ssal jel�lje ki a pontot, majd a vonalat (szakaszt, f�legyenest, egyenest, vagy vektort), melyre mer�legest k�v�n szerkeszteni!");
                }
                if (e.getComponent() == Button10) {
                    mw = new MouseWatch10();
                    statusBarText.setText("VEKTOR: Kattint�ssal �s h�z�ssal k�sse �ssze a vektor kezd�- �s v�gpontj�t!");
                }
                if (e.getComponent() == Button11) {
                    mw = new MouseWatch11();
                    statusBarText.setText("FELEZ�PONT: Kattint�ssal jel�lje ki a szakaszt vagy vektort, melyen meg k�v�nja szerkeszteni a felez�pontot!");
                }
                //T�rl�sek
                if (e.getComponent() == Button12) {
                    saveStateOntoUndoStack();
                    JFrame frmParent = new JFrame();
                    int n = 0;
                    DialogForErase ch = new DialogForErase(frmParent, "T�rl�s");
                    if ((n = ch.show("Mit k�v�n t�r�lni?", n)) > 0) {
                        switch (n) {
                            //Szerkeszt�s + k�pletek t�rl�se
                            case 1: {
                                saveStateOntoUndoStack();
                                while (objectList.size() > 1) {
                                    objectList.remove(objectList.size() - 1);
                                }
                                break;
                            }
                            //F�lia t�rl�se
                            case 2: {
                                actFoil.setColor(getBackground());
                                actFoil.fillRect(0, 0, screenMaxX, screenMaxY);
                                break;
                            }
                            //Kijel�l� poligonok t�rl�se
                            case 3: {
                                //Van kijel�l� poligon?
                                boolean isPoligon = false;
                                for (int i = 0; i < objectList.size(); i++) {
                                    GraphicalObject o = (GraphicalObject) objectList.get(i);
                                    if (o instanceof Poligon) {
                                        isPoligon = true;
                                        break;
                                    }
                                }
                                //ha van poligon, akkor ment�s
                                if (isPoligon) {
                                    saveStateOntoUndoStack();
                                }
                                //poligonok t�rl�se
                                for (int i = 0; i < objectList.size(); i++) {
                                    GraphicalObject o = (GraphicalObject) objectList.get(i);
                                    if (o instanceof Poligon) {
                                        objectList.remove(o);
                                        i--;
                                    }
                                }
                                break;
                            }
                            //Teljes dia t�rl�se
                            case 4: {
                                saveDia();
                                //F�jlok t�rl�se                                
                                File fCanvas = new File(getActualDiaFileName() + ".canvas");
                                File fFoil = new File(getActualDiaFileName() + ".foil");
                                fCanvas.delete();
                                fFoil.delete();
                                actDia++;
                                shiftAllDiaBackward();
                                actDia -= 2;
                                //itt m�g lehet BUG - van is BUG!!!!!!!
                                if (actDiaExist()) {
                                    loadDia();
                                } else {
                                    if (actDia > lastDia) {
                                        lastDia++;
                                    }
                                    if (actDia < 1) {
                                        actDia = 1;
                                        newDia();
                                    }
                                }
                                break;
                            }
                        }
                    }
                    frmParent.dispose();
                    allowChange = false;
                }
                if (e.getComponent() == Button16) {
                    mw = new MouseWatch16();
                    statusBarText.setText("EGYENL�SZ�R� H�ROMSZ�G: Kattint�ssal �s h�z�ssal jel�lje ki a h�romsz�g befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button17) {
                    mw = new MouseWatch17();
                    statusBarText.setText("DER�KSZ�G� H�ROMSZ�G: Kattint�ssal �s h�z�ssal jel�lje ki a h�romsz�g befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button18) {
                    mw = new MouseWatch18();
                    statusBarText.setText("SZAB�LYOS H�ROMSZ�G: Kattint�ssal �s h�z�ssal jel�lje ki a h�romsz�g befoglal� n�gyzet�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button20) {
                    mw = new MouseWatch20();
                    statusBarText.setText("�LTAL�NOS H�ROMSZ�G: Kattint�ssal �s h�z�ssal jel�lje ki a h�romsz�g befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button21) {
                    mw = new MouseWatch21();
                    statusBarText.setText("�LTAL�NOS TRAP�Z: Kattint�ssal �s h�z�ssal jel�lje ki a trap�z befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button22) {
                    mw = new MouseWatch22();
                    statusBarText.setText("PARALELOGRAMMA: Kattint�ssal �s h�z�ssal jel�lje ki a paralelogramma befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button23) {
                    mw = new MouseWatch23();
                    statusBarText.setText("N�GYSZ�G ALAP� G�LA: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� t�glalapot!");
                }
                if (e.getComponent() == Button24) {
                    mw = new MouseWatch24();
                    statusBarText.setText("T�GLATEST: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� t�glalapot!");
                }
                if (e.getComponent() == Button25) {
                    mw = new MouseWatch25();
                    statusBarText.setText("T�GLALAP: Kattint�ssal �s h�z�ssal defini�lja a szerkesztend� t�glalap dimenzi�j�t!");
                }
                if (e.getComponent() == Button26) {
                    mw = new MouseWatch26();
                    statusBarText.setText("DELTOID: Kattint�ssal �s h�z�ssal jel�lje ki a deltoid befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button27) {
                    mw = new MouseWatch27();
                    statusBarText.setText("ROMBUSZ: Kattint�ssal �s h�z�ssal jel�lje ki a rombusz befoglal� t�glalapj�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button28) {
                    mw = new MouseWatch28();
                    statusBarText.setText("SZAB�LYOS SOKSZ�G: Kattint�ssal �s h�z�ssal jel�lje ki a szab�lyos soksz�g befoglal� n�gyzet�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button29) {
                    mw = new MouseWatch29();
                    statusBarText.setText("�LTAL�NOS SOKSZ�G: Kattint�ssal �s h�z�ssal jel�lje ki a soksz�g befoglal� n�gyzet�t a szerkeszt�v�sznon!");
                }
                if (e.getComponent() == Button30) {
                    mw = new MouseWatch30();
                    statusBarText.setText("N�GYZET: Kattint�ssal �s h�z�ssal defini�lja a n�gyzet dimenzi�j�t!");
                }
                if (e.getComponent() == Button31) {
                    mw = new MouseWatch31();
                    statusBarText.setText("KOCKA: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� n�gyzetet!");
                }
                if (e.getComponent() == Button32) {
                    undo();
                    allowChange = false;
                }
                if (e.getComponent() == Button33) {
                    mw = new MouseWatch33();
                    statusBarText.setText("PONTBA ELTOLT VEKTOR: Kattint�ssal jel�lje ki a vektort, majd az �j vektor kezd�pontj�t!");
                }
                if (e.getComponent() == Button34) {
                    mw = new MouseWatch34();
                    statusBarText.setText("K�Z�R�S: V�lassza ki a k�v�nt rajzol�sz�nt �s �rjon a szerkeszt�v�szonra!");
                    penColorIcon.color = (Color) penColorPalette.get(0);
                    downToolBar.repaint();
                    config.actPenColor = 0;
                }
                if (e.getComponent() == Button35) {
                    mw = new MouseWatch35();
                    statusBarText.setText("K�P: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� t�glalapot!");
                }
                if (e.getComponent() == Button36) {
                    mw = new MouseWatch36();
                    statusBarText.setText("HENGER: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� t�glalapot!");
                }
                if (e.getComponent() == Button37) {
                    mw = new MouseWatch37();
                    statusBarText.setText("CSONKAK�P: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� t�glalapot!");
                }
                if (e.getComponent() == Button39) {
                    mw = new MouseWatch39();
                    statusBarText.setText("KIJEL�L� SOKSZ�G: Kattint�ssal jel�lje ki a soksz�g cs�csait valamely k�r�lj�r�si ir�ny szerint! A soksz�get a kezd�cs�csra kattint�ssal z�rja le!");
                }
                if (e.getComponent() == Button40) {
                    config.axisOn = !config.axisOn;
                    allowChange = false;
                }
                if (e.getComponent() == Button41) {
                    redo();
                    allowChange = false;
                }
                if (e.getComponent() == Button43) {
                    mw = new MouseWatch43();
                    statusBarText.setText("P�RHUZAMOS SZAKASZ: Kattint�ssal jel�lje ki a szakaszt, majd �jabb kattint�ssal a szakasz kezd�pontj�t, �s h�zza a k�v�nt poz�ci�ba!");
                }
                if (e.getComponent() == Button44) {
                    mw = new MouseWatch44();
                    statusBarText.setText("F�LEGYENES: Kattint�ssal �s h�z�ssal k�ss�n �ssze k�t pontot, melyen a f�legyenes �thalad � a kezd�pontb�l indulva!");
                }
                if (e.getComponent() == Button45) {
                    config.nextLabelOn = !config.nextLabelOn;
                    //r�csraigaz�t�s
                    if (config.nextLabelOn) {
                        url = urlLoader.findResource("icons/icon45a.jpg");
                        Button45.setIcon(new ImageIcon(url));
                    } else {
                        url = urlLoader.findResource("icons/icon45b.jpg");
                        Button45.setIcon(new ImageIcon(url));
                    }
                    allowChange = false;
                }
                if (e.getComponent() == Button46) {
                    mw = new MouseWatch46();
                    statusBarText.setText("K�R�L�RT K�R: Kattint�ssal jel�lje ki azt a 3 pontot, melyen a k�r �thalad!");
                }
                if (e.getComponent() == Button47) {
                    mw = new MouseWatch47();
                    statusBarText.setText("BE�RT K�R: Kattint�ssal jel�lje ki azt a 3 pontot, melyen a k�r �thalad!");
                }
                //r�csraigaz�t�s
                if (e.getComponent() == Button48) {
                    config.snapToGrid = !config.snapToGrid;
                    if (config.snapToGrid) {
                        url = urlLoader.findResource("icons/icon48a.jpg");
                        Button48.setIcon(new ImageIcon(url));
                    } else {
                        url = urlLoader.findResource("icons/icon48b.jpg");
                        Button48.setIcon(new ImageIcon(url));
                    }
                    allowChange = false;
                }
                if (e.getComponent() == Button49) {
                    mw = new MouseWatch49();
                    statusBarText.setText("ALAKZAT KL�NOZ�SA: Kattint�ssal �s h�z�ssal jel�lje ki a m�soland� alakzatot, majd poz�cion�lja a megfelel� helyre!");
                }
                if (e.getComponent() == Button50) {
                    config.nodesOn = !config.nodesOn;
                    if (config.nodesOn) {
                        url = urlLoader.findResource("icons/icon50a.jpg");
                        Button50.setIcon(new ImageIcon(url));
                    } else {
                        url = urlLoader.findResource("icons/icon50b.jpg");
                        Button50.setIcon(new ImageIcon(url));
                    }
                    allowChange = false;
                }
                if (e.getComponent() == Button51) {
                    mw = new MouseWatch51();
                    statusBarText.setText("S�LYPONT: Kattint�ssal jel�lje ki a soksz�g cs�csait valamely k�r�lj�r�si ir�ny szerint! A soksz�get a kezd�cs�csra kattint�ssal z�rja le!");
                }
                if (e.getComponent() == Button52) {
                    mw = new MouseWatch52();
                    statusBarText.setText("H�ROMSZ�G ALAP� G�LA: Kattint�ssal jel�lje ki a test dimenzi�j�t megad� t�glalapot!");
                }
                if (e.getComponent() == Button53) {
                    mw = new MouseWatch53();
                    statusBarText.setText("EGYSZER� VAGY �SSZETETT ALAKZAT T�RL�SE: Kattint�ssal �s h�z�ssal jel�lje ki a t�rlend� objektumot, majd a meger�s�t�shez h�zza a v�sznon k�v�lre!");
                }
                if (e.getComponent() == Button55) {
                    mw = new MouseWatch55();
                    statusBarText.setText("SZ�GFELEZ� EGYENES: Kattint�ssal jel�lje ki azt a k�t k�z�s ponttal rendelkez� vonalat, melynek sz�gfelez� egyenes�t k�v�nja megszerkeszteni!");
                }
                if (e.getComponent() == Button56) {
                    mw = new MouseWatch56();
                    statusBarText.setText("RAD�R: Kattint�ssal �s h�z�ssal jel�lje ki azt a t�glalap alak� tartom�nyt, melyen bel�l elhelyezked� k�z�r�st t�r�lni k�v�n!");
                }
                if (e.getComponent() == Button58) {
                    mw = new MouseWatch58();
                    statusBarText.setText("P�RHUZAMOS F�LEGYENES: Kattint�ssal jel�lje ki a f�legyenest, majd �jabb kattint�ssal a fel�gyenes kezd�pontj�t, �s h�zza a k�v�nt poz�ci�ba!");
                }
                //tintasz�n v�ltoztat�sa
                if (e.getComponent() == Button59) {
                    mw = new MouseWatch34();
                    statusBarText.setText("K�Z�R�S: V�lassza ki a k�v�nt rajzol�sz�nt �s �rjon a t�bl�ra!");
                }
                if (e.getComponent() == Button60) {
                    mw = new MouseWatch60();
                    statusBarText.setText("SZAKASZFELEZ� MER�LEGES: Kattint�ssal jel�lje ki azt a szakaszt, melynek felez�mer�leges�t meg k�v�nja szerkeszteni!");
                }
                //�j dia besz�r�sa
                if (e.getComponent() == Button63) {
                    //aktu�lis dia ment�se
                    saveDia();
                    actDia++;
                    if (actDia > lastDia) {
                        lastDia = actDia;
                    }
                    //ha m�r l�tezik ez a dia, akkor shiftelni kell a t�bbi di�t                    
                    if (actDiaExist()) {
                        shiftAllDiaForward();
                    }
                    newDia();
                    allowChange = false;
                }
                //ugr�s az els� di�ra
                if (e.getComponent() == Button64) {
                    //aktu�lis dia ment�se
                    saveDia();
                    actDia = 1;
                    //ha l�tezik a dia
                    if (actDiaExist()) {
                        loadDia();
                    }
                    allowChange = false;
                }
                //ugr�s az utols� di�ra
                if (e.getComponent() == Button65) {
                    //aktu�lis dia ment�se
                    saveDia();
                    actDia = lastDia;
                    //ha l�tezik a dia
                    if (actDiaExist()) {
                        loadDia();
                    }
                    allowChange = false;
                }
                //lapoz�s vissza
                if (e.getComponent() == Button66) {
                    //aktu�lis dia ment�se
                    saveDia();
                    actDia--;
                    if (actDia > 0) {
                        //ha l�tezik a dia
                        if (actDiaExist()) {
                            loadDia();
                        }
                    } else {
                        actDia++;
                    }
                    allowChange = false;
                }
                //lapoz�s el�re
                if (e.getComponent() == Button67) {
                    //aktu�lis dia ment�se
                    saveDia();
                    actDia++;
                    if (actDia <= lastDia) {
                        //ha l�tezik a dia
                        if (actDiaExist()) {
                            loadDia();
                        }
                    } else {
                        actDia = lastDia;
                    }
                    allowChange = false;
                }
                //�j dia besz�r�sa szerkeszt�s meg�rz�s�vel
                if (e.getComponent() == Button68) {
                    //aktu�lis dia ment�se
                    saveDia();
                    actDia++;
                    if (actDia > lastDia) {
                        lastDia = actDia;
                    }
                    //ha m�r l�tezik ez a dia, akkor shiftelni kell a t�bbi di�t
                    if (actDiaExist()) {
                        shiftAllDiaForward();
                    }
                    newDiaWithEmptyFoil();
                    allowChange = false;
                }
                if (e.getComponent() == Button69) {
                    mw = new MouseWatch69();
                    statusBarText.setText("K�PLET: Kattint�ssal jel�lje ki a k�plet bal fels� sark�t, majd v�lassza ki a megjelen�tend� k�pletet!");
                }
                //v�szon alaphelyzetbe mozgat�sa
                if (e.getComponent() == Button70) {
                    //nagy�t�si alaphelyzetbe �ll�t�sa
                    double localScaleFactor = 1 / config.globalScaleFactor;
                    for (int i = 0; i < objectList.size(); i++) {
                        GraphicalObject o = (GraphicalObject) objectList.get(i);
                        o.Scale(localScaleFactor, 0, 0);
                    }
                    for (int i = 0; i < objectList.size(); i++) {
                        GraphicalObject o = (GraphicalObject) objectList.get(i);
                        o.Calculate();
                    }
                    config.originX = config.originX * localScaleFactor;
                    config.originY = config.originY * localScaleFactor;
                    config.globalScaleFactor = 1;

                    //orig� �s objektumok szerkeszt�v�szon k�zep�re helyez�se
                    double deltaX = (paintPanel.getBounds().getWidth() / 2) - config.originX;
                    double deltaY = (paintPanel.getBounds().getHeight() / 2) - config.originY;
                    for (int i = 0; i < objectList.size(); i++) {
                        GraphicalObject o = (GraphicalObject) objectList.get(i);
                        //K�pletet nem mozgatjuk
                        if (!(o instanceof Formula)) {
                            o.Translate(deltaX, deltaY);
                        }
                    }
                    for (int i = 0; i < objectList.size(); i++) {
                        GraphicalObject o = (GraphicalObject) objectList.get(i);
                        o.Calculate();
                    }
                    config.originX += deltaX;
                    config.originY += deltaY;

                    calculateAllLabels();
                    allowChange = false;
                }

                //n�vjegy ablak megjelen�t�se
                if (e.getComponent() == Button72) {
                    DialogAbout dlg = new DialogAbout(null, true);
                    dlg.setVisible(true);
                    allowChange = false;
                }
                //eg�resem�nyfigyel�k elt�vol�t�sa, ha enged�lyezett
                if (lastMouseListener != null && allowChange) {
                    paintPanel.removeMouseListener(lastMouseListener);
                    if (!(lastMouseListener instanceof MouseWatch1)) {
                        paintPanel.removeMouseMotionListener((MouseMotionListener) lastMouseListener);
                    }
                }

                //ideiglenesen �thelyezve ide
                //toll rajzol�sz�ne
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getComponent() == Button59) {
//                    if (e.getClickCount() == 1) {
                        ++config.actPenColor;
                        if (config.actPenColor == penColorPalette.size()) {
                            config.actPenColor = 0;
                        }
                        penColorIcon.color = (Color) penColorPalette.get(config.actPenColor);
//                    } else if (e.getClickCount() == 2) {
//                        statusBarText.setText("Dupla katt.");
//                    }
                    }
                }
                //kit�lt�si sz�n
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getComponent() == Button61) {
//                    if (e.getClickCount() == 1) {
                        ++config.actHighLightColor;
                        if (config.actHighLightColor == highlightColorPalette.size()) {
                            config.actHighLightColor = 0;
                        }
                        highlightColorIcon.color = (Color) highlightColorPalette.get(config.actHighLightColor);
//                    } else if (e.getClickCount() == 2) {
//                        statusBarText.setText("Dupla katt.");
//                    }
                    }
                }

                //�j eg�resem�nyfigyel� felf�z�se a figyel�l�ncra, ha enged�lyezett
                if (allowChange) {
                    paintPanel.addMouseListener(mw);
                    lastMouseListener = mw;
                    if (e.getComponent() != Button1) {
                        paintPanel.addMouseMotionListener((MouseMotionListener) mw);
                    }
                    deselectAll();
                    //eg�rkurzor be�ll�t�sa
                    if (e.getComponent() == Button3 | e.getComponent() == Button7 |
                            e.getComponent() == Button34 | e.getComponent() == Button59) {
                        //alakzat vagy objektum mozgat�sa
                        if (e.getComponent() == Button3) {
                            paintPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }
                        //v�szon mozgat�sa
                        if (e.getComponent() == Button7) {
                            paintPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        }
                        //�r�s a f�li�ra
                        if (e.getComponent() == Button34 | e.getComponent() == Button59) {
                            paintPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    } else {
                        paintPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    }

                }
                repaintAll();
            }
        }
    }

    //Eg�rg�rg� esem�nyfigyel�je a zoomol�shoz
    class MouseWheelWatch implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double actScale;
            if (e.getWheelRotation() > 0) {
                actScale = 1.015;
            } else {
                actScale = 0.985;
            }
            //saveStateToUndoStack();
            for (int i = 0; i < objectList.size(); i++) {
                GraphicalObject o = (GraphicalObject) objectList.get(i);
                o.Scale(actScale, config.originX, config.originY);
            }
            for (int i = 0; i < objectList.size(); i++) {
                GraphicalObject o = (GraphicalObject) objectList.get(i);
                o.Calculate();
            }
            config.globalScaleFactor *= actScale;
            calculateAllLabels();
            repaintAll();
        }
    }

    //Jobb eg�rgomb esem�nyfigyel�je
    class RightButtonWatch extends MouseInputAdapter {

        GraphicalObject objectOfLabel = null;
        int startX, startY;
        boolean rightbutton;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                rightbutton = true;
                saveStateOntoUndoStack();
                objectOfLabel = findNearestLabel(e);
                if (objectOfLabel != null) {
                    startX = e.getX();
                    startY = e.getY();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (objectOfLabel != null) {
                    rightbutton = false;
                    objectOfLabel.labelDX = e.getX() - objectOfLabel.labelX;
                    objectOfLabel.labelDY = e.getY() - objectOfLabel.labelY;
                    repaintAll();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (rightbutton && objectOfLabel != null) {
                objectOfLabel.labelDX = e.getX() - objectOfLabel.labelX;
                objectOfLabel.labelDY = e.getY() - objectOfLabel.labelY;
                repaintAll();
            }
        }
    }

    //**************************************************************************
    /**
     * K�t �j ponton �thalad�, rejtett egyenes l�trehoz�sa.
     * A p1 �s p2 pontokat is l�trehozza �s felveszi az objectList vektorba.
     * @param p1 az egyenes egyik fixpontja
     * @param p2 az egyenes m�sik fixpontja
     * @return a k�sz egyenes objektuma
     */
    Line makeHiddenLine(BasePoint p1, BasePoint p2) {
        Line l;
        objectList.add(p1);
        p1.group = config.actGroup;
        p1.Hide();
        objectList.add(p2);
        p2.group = config.actGroup;
        p2.Hide();
        objectList.add(l = new Line(new GraphicalObject[]{p1, p2}, Line.TWOPOINTS, Line.LINE));
        l.group = config.actGroup;
        l.Hide();
        return l;
    }

    /**
     * Adott ponton �thalad�, r�gz�tett meredeks�g� egyenes l�trehoz�sa.
     * @param p1 az egyenes egy fixpontja
     * @param alfa az egyenes v�zszintessel bez�rt sz�ge
     * @param visible l�that�-e
     * @return a k�sz egyenes objektuma
     */
    Line makeFixedAlfaLine(BasePoint p1, double alfa, boolean visible) {
        Line l;
        objectList.add(l = new Line(new GraphicalObject[]{p1}, Line.FIXEDALFA, Line.LINE));
        l.group = config.actGroup;
        l.alfa = alfa;
        l.Calculate();
        l.visible = visible;
        return l;
    }

    /** 
     * K�t ponton �thalad� egyenes l�trehoz�sa.
     * @param p1 az egyenes egyik fixpontja
     * @param p2 az egyenes m�sik fixpontja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz egyenes objektuma
     */
    Line makeLine(BasePoint p1, BasePoint p2, String label, boolean visible) {
        Line l;
        objectList.add(l = new Line(new GraphicalObject[]{p1, p2}, Line.TWOPOINTS, Line.LINE));
        l.group = config.actGroup;
        l.visible = visible;
        l.label = label;
        return l;
    }

    /**
     * Egy kezd�- �s v�gpontj�val megadott szakasz l�trehoz�sa.
     * @param p1 kezd�pont
     * @param p2 v�gpont
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz szakasz objektuma
     */
    Line makeLineSegment(BasePoint p1, BasePoint p2, String label, boolean visible) {
        Line ls;
        objectList.add(ls = new Line(new GraphicalObject[]{p1, p2}, Line.TWOPOINTS, Line.LINESEGMENT));
        ls.group = config.actGroup;
        ls.visible = visible;
        ls.label = label;
        return ls;
    }

    /**
     * F�legyenes l�trehoz�sa k�t �j pontb�l.
     * A p1 �s p2 pontokat is l�trehozza �s felveszi az objectList vektorba.
     * @param p1 a f�legyenes kezd� pontja
     * @param p2 a f�legyenes egy m�sik fixpontja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz f�legyenes objektuma
     */
    Line makeHalfLine(BasePoint p1, BasePoint p2, String label, boolean visible) {
        Line hl;
        objectList.add(p1);
        p1.group = config.actGroup;
        p1.label = "";

        objectList.add(p2);
        p2.group = config.actGroup;
        p2.label = "";

        objectList.add(hl = new Line(new GraphicalObject[]{p1, p2}, Line.TWOPOINTS, Line.HALFLINE));
        hl.group = config.actGroup;
        hl.label = label;
        hl.visible = visible;
        return hl;
    }

    /**
     * F�legyenes l�trehoz�sa k�t l�tez� pontb�l.
     * @param p1 a f�legyenes kezd� pontja
     * @param p2 a f�legyenes egy m�sik fixpontja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz f�legyenes objektuma
     */
    Line makeHalfLine2(BasePoint p1, BasePoint p2, String label, boolean visible) {
        Line hl;
        objectList.add(hl = new Line(new GraphicalObject[]{p1, p2}, Line.TWOPOINTS, Line.HALFLINE));
        hl.group = config.actGroup;
        hl.label = label;
        hl.visible = visible;
        return hl;
    }

    /**
     * Sz�gfelez� egyenes l�trehoz�sa 3 pontb�l.
     * @param p1 az egyik sz�gsz�r v�gpontja
     * @param p2 a sz�g cs�cspontja
     * @param p3 a m�sik sz�gsz�r v�gpontja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz egyenes objektuma
     */
    Line makeBisector(BasePoint p1, BasePoint p2, BasePoint p3, String label, boolean visible) {
        Line l;
        objectList.add(l = new Line(new GraphicalObject[]{p1, p2, p3}, Line.BISECTOR, Line.LINE));
        l.visible = visible;
        l.group = config.actGroup;
        l.label = label;
        return l;
    }

    /**
     * K�t koordin�t�j�val adott pont mer�leges vet�lete egyenes vonalon.
     * Az (x;y) koordin�t�j� pont nem j�n l�tre!
     * @param x a vet�tend� pont X koordin�t�ja
     * @param y a vet�tend� pont Y koordin�t�ja
     * @param l a vonal objektum, melyre t�rt�nik a mer�leges vet�t�s
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeProjectedPointOnLine(double x, double y, Line l, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(new GraphicalObject[]{l}, BasePoint.RUNPOINT_ON_LINE));
        p.label = label;
        p.group = config.actGroup;
        p.x = x;
        p.y = y;
        p.Calculate();
        p.visible = visible;
        return p;
    }

    /**
     * L�tez� pont mer�leges vet�lete egyenes vonalon.
     * @param p a vet�tend� pont
     * @param l a vonal objektum, melyre t�rt�nik a mer�leges vet�t�s
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeProjectedPointOnLine(BasePoint p, Line l, String label, boolean visible) {
        BasePoint projPoint;
        objectList.add(projPoint = new BasePoint(new GraphicalObject[]{p, l}, BasePoint.PROJECTED_POINT));
        projPoint.label = label;
        projPoint.group = config.actGroup;
        projPoint.visible = visible;
        return projPoint;
    }

    /**
     * Szakaszfelez� mer�leges l�trehoz�sa.
     * El�sz�r l�trehozza a szakasz rejtett felez�pontj�t, majd az egyenest.
     * @param ls a szakasz, melynek szakaszfelez� mer�leges�t kell megszerkeszteni
     * @param visible l�that�-e
     * @return a k�sz egyenes objektuma
     */
    Line makeNormalBisector(Line ls, boolean visible) {
        BasePoint F;
        Line l;
        GraphicalObject[] list = {ls};
        objectList.add(F = new BasePoint(list, BasePoint.MIDPOINT));
        F.Hide();
        F.group = config.actGroup;

        GraphicalObject[] list2 = {(GraphicalObject) ls.creationObjectList.get(0),
            (GraphicalObject) ls.creationObjectList.get(1), F
        };
        objectList.add(l = new Line(list2, Line.PERPENDICULAR, Line.LINE));
        l.group = config.actGroup;
        l.visible = visible;
        return l;
    }

    /**
     * Vonalra mer�leges egyenes l�trehoz�sa.
     * @param line a vonal, melyre mer�legest kell szerkeszteni
     * @param p a pont, melyen �thalad a mer�leges egyenes
     * @param visible l�that�-e
     * @return a k�sz egyenes objektuma
     */
    Line makeNormal(Line line, BasePoint p, boolean visible) {
        Line l = null;
        switch (line.mainType) {
            case Line.LINESEGMENT: {
                GraphicalObject[] list = {(GraphicalObject) line.creationObjectList.get(0),
                    (GraphicalObject) line.creationObjectList.get(1), p
                };
                objectList.add(l = new Line(list, Line.PERPENDICULAR, Line.LINE));
                break;
            }
            default: {
                objectList.add(l = new Line(new GraphicalObject[]{line, p}, Line.PERPENDICULARBYLINE, Line.LINE));

                break;
            }
        }
        l.group = config.actGroup;
        l.visible = visible;
        return l;
    }

    /**
     * K�r l�trehoz�sa a k�z�ppont �s egy ker�leti pont seg�ts�g�vel.
     * @param p1 a k�r k�z�ppontja
     * @param p2 a k�r egyik ker�leti pontja
     * @param visible l�that�-e
     * @return a k�sz k�r objektuma
     */
    Circle makeCircle(BasePoint p1, BasePoint p2, boolean visible) {
        Circle c;
        objectList.add(c = new Circle(new GraphicalObject[]{p1, p2}, Circle.TWOPOINTS));
        c.group = config.actGroup;
        c.visible = visible;
        return c;
    }

    /**
     * H�romsz�g k�r� �rhat� k�r l�trehoz�sa a cs�cspontokb�l.
     * A cs�cspontok sorrendje tetsz�leges.
     * @param p1 a h�romsz�g 1. cs�cspontja
     * @param p2 a h�romsz�g 2. cs�cspontja
     * @param p3 a h�romsz�g 3. cs�cspontja
     * @param visible l�that�-e
     * @return a k�sz k�r objektuma
     */
    Circle makeOuterCircle(BasePoint p1, BasePoint p2, BasePoint p3, boolean visible) {
        Circle c;
        objectList.add(c = new Circle(new GraphicalObject[]{p1, p2, p3}, Circle.THREEPOINTS));
        c.group = config.actGroup;
        c.visible = visible;
        return c;
    }

    /**
     * H�romsz�gbe �rhat� k�r l�trehoz�sa a h�romsz�g oldalaib�l.
     * Az oldalak megad�s�nak sorrendje tetsz�leges.
     * @param a a h�romsz�g egyik oldala
     * @param b a h�romsz�g m�sik oldala
     * @param c a h�romsz�g harmadik oldala
     * @param visible l�that�-e
     * @return a k�sz k�r objektuma
     */
    Circle makeInnerCircle(Line a, Line b, Line c, boolean visible) {
        Circle ic;
        BasePoint A, B, C, tempC, o, p;
        Line bs1, bs2;
        A = (BasePoint) a.creationObjectList.get(0);
        B = (BasePoint) a.creationObjectList.get(1);
        tempC = (BasePoint) b.creationObjectList.get(0);
        if (tempC == A | tempC == B) {
            C = (BasePoint) b.creationObjectList.get(1);
        } else {
            C = tempC;
        }
        bs1 = makeBisector(A, B, C, "", false);
        bs2 = makeBisector(B, C, A, "", false);
        o = makeInterSectionPointOnLines(bs1, bs2, "", true);
        p = makeProjectedPointOnLine(o, a, "", true);
        objectList.add(ic = new Circle(new GraphicalObject[]{o, p}, Circle.TWOPOINTS));
        ic.group = config.actGroup;
        ic.visible = visible;
        return ic;
    }

    /**
     * K�r k�z�ppontj�nak l�trehoz�sa.
     * @param c a k�r, melynek a k�z�ppontj�t kell megszerkeszteni
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektum
     */
    BasePoint makeCentrePoint(Circle c, String label, boolean visible) {
        BasePoint bp;
        objectList.add(bp = new BasePoint(new GraphicalObject[]{c}, BasePoint.CENTRE));
        bp.group = config.actGroup;
        bp.visible = visible;
        bp.label = label;

        return bp;
    }

    /**
     * K�t k�r metsz�spontj�nak l�trehoz�sa.
     * Ez a funkci� m�g fejleszt�sre szorul. Egyel�re csak a szab�lyos h�romsz�g szerkeszt�s�re haszn�lhat�!
     * @param c1 az egyik k�r
     * @param c2 a m�sik k�r
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeInterSectionPointOnCircles(Circle c1, Circle c2, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(new GraphicalObject[]{c1, c2}, BasePoint.INTERSECTION_ON_CIRCLE));
        p.group = config.actGroup;
        p.visible = visible;
        p.label = label;
        return p;
    }

    /**
     * K�t egyenes metsz�spontj�nak l�trehoz�sa.
     * @param l1 az egyik egyenes
     * @param l2 a m�sik egyenes
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeInterSectionPointOnLines(Line l1, Line l2, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(new GraphicalObject[]{l1, l2}, BasePoint.INTERSECTION_ON_LINES));
        p.group = config.actGroup;
        p.visible = visible;
        p.label = label;
        return p;
    }

    /**
     * Szakaszfelez� pont l�trehoz�sa.
     * @param l a szakasz
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeMidPoint(Line l, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(new GraphicalObject[]{l}, BasePoint.MIDPOINT));
        p.group = config.actGroup;
        p.visible = visible;
        p.label = label;
        return p;
    }

    /**
     * Koordin�t�ival megadott pont l�trehoz�sa.
     * @param x a pont X koordin�t�ja
     * @param y a pont Y koordin�t�ja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makePoint(double x, double y, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(x, y));
        p.group = config.actGroup;
        p.visible = visible;
        p.label = label;
        return p;
    }

    /**
     * Pont vet�let�nek l�trehoz�sa k�r�n.
     * @param x a pont X koordin�t�ja
     * @param y a pont Y koordin�t�ja 
     * @param c a k�r
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeProjectedPointOnCircle(double x, double y, Circle c, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(new GraphicalObject[]{c}, BasePoint.RUNPOINT_ON_CIRCLE));
        p.label = label;
        p.group = config.actGroup;
        p.x = x;
        p.y = y;
        p.Calculate();
        p.visible = visible;
        return p;
    }

    /**
     * K�z�ppontosan t�kr�z�tt pont l�trehoz�sa.
     * @param p a pont, melyet t�kr�zni kell
     * @param o a t�kr�z�s k�z�ppontja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeMirroredPointByPoint(BasePoint p, BasePoint o, String label, boolean visible) {
        BasePoint q;
        objectList.add(q = new BasePoint(new GraphicalObject[]{p, o}, BasePoint.MIRROREDPOINT_BY_POINT));
        q.label = label;
        q.group = config.actGroup;
        q.visible = visible;
        return q;
    }

    /**
     * Vektorral eltolt pont l�trehoz�sa.
     * @param p a pont, melyet el kell tolni
     * @param v a vektor
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeTranslatedPointByVector(BasePoint p, Line v, String label, boolean visible) {
        BasePoint q;
        objectList.add(q = new BasePoint(new GraphicalObject[]{p, v}, BasePoint.TRANSLATEDPOINT_BY_VECTOR));
        q.label = label;
        q.group = config.actGroup;
        q.visible = visible;
        return q;
    }

    /**
     * H�romsz�g s�lypontj�nak l�trehoz�sa.
     * @param p1 a h�romsz�g 1. cs�csa
     * @param p2 a h�romsz�g 2. cs�csa
     * @param p3 a h�romsz�g 3. cs�csa
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeWeightPoint(BasePoint p1, BasePoint p2, BasePoint p3, String label, boolean visible) {
        BasePoint wp;
        objectList.add(wp = new BasePoint(new GraphicalObject[]{p1, p2, p3}, BasePoint.WEIGHTPOINT));
        wp.n = 3;
        wp.Calculate();
        wp.label = label;
        wp.group = config.actGroup;
        wp.visible = visible;
        return wp;
    }

    /**
     * Vektor l�trehoz�sa kezd�- �s v�gpontb�l.
     * @param sp a vektor kezd�pontja
     * @param ep a vektor v�gpontja
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz vektor objektuma
     */
    Line makeVector(BasePoint sp, BasePoint ep, String label, boolean visible) {
        Line v;
        objectList.add(v = new Line(new GraphicalObject[]{sp, ep}, Line.TWOPOINTS, Line.VECTOR));
        v.label = label;
        v.group = config.actGroup;
        v.visible = visible;
        return v;
    }

    /**
     * Adott sz�ggel elforgatott, r�gz�tett pont l�trehoz�sa k�r�n.
     * @param c a k�r
     * @param alfa az adott elforgat�si sz�g radi�nban megadva
     * @param label c�mke
     * @param visible l�that�-e
     * @return a k�sz pont objektuma
     */
    BasePoint makeFixedPointOnCircle(Circle c, double alfa, String label, boolean visible) {
        BasePoint p;
        objectList.add(p = new BasePoint(c, alfa));
        p.label = label;
        p.group = config.actGroup;
        p.visible = visible;
        return p;
    }

    /**
     * Ellipszis l�trehoz�sa h�rom pontb�l.
     * @param p1 az ellipszis k�z�ppontja
     * @param p2 az ellipszis f�l nagytengely�nek egyik v�gpontja
     * @param p3 az ellipszis f�l kistengely�nek egyik v�gpontja
     * @param visible l�thato-e
     * @return a k�sz ellipszis objektuma
     */
    Ellipse makeEllipse(BasePoint p1, BasePoint p2, BasePoint p3, boolean visible) {
        Ellipse e;
        objectList.add(e = new Ellipse(new GraphicalObject[]{p1, p2, p3}, Ellipse.THREEPOINTS));
        e.group = config.actGroup;
        e.visible = visible;
        return e;
    }

    /**
     * K�plet l�trehoz�sa.
     * @param p1 pont objektum, mely tartalmazza a k�plet bal fels� sark�nak koordin�t�it
     * @param fileName a k�plethez tartoz� f�jl neve el�r�si �ttal egy�tt
     * @return a k�sz k�plet objektuma
     */
    Formula makeFormula(BasePoint p1, String fileName) {
        Formula f;
        objectList.add(f = new Formula(p1.x, p1.y, fileName));
        f.group = nextGroup();
        f.visible = true;
        return f;
    }

    //**************************************************************************

    /**
     * Az adott pont k�zel�ben van-e m�sik pont.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param bp a vizsg�land� pont
     * @return ha 20 pixeles k�rnyezeten bel�l van pont, akkor TRUE, egy�bk�nt FALSE
     */
    boolean isNearPoint(BasePoint bp) {
        double min = screenMaxX;
        for (int i = 0; i < objectList.size(); i++) {
            Object o = objectList.get(i);
            if (o instanceof BasePoint) {
                BasePoint oo = (BasePoint) o;
                min = Math.min(distance(bp, oo), min);
            }
        }
        return (min < 20);
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� BasePoint objektumot, ha van ilyen.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return BasePoint objektum vagy null, ha nincs a k�zelben pont.
     */
    BasePoint findNearestPoint(MouseEvent e) {
        double min = screenMaxX, dist;
        BasePoint minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof BasePoint) {
                BasePoint oo = (BasePoint) o;
                dist = distance(bp, oo);
                if (dist < min) {
                    min = dist;
                    minobj = oo;
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * Adott x,y koordin�t�j� pont objektum kikeres�se az objectList vektorban, ha van ilyen.
     * @param x a pont X koordin�t�ja
     * @param y a pont Y kooordin�t�ja
     * @return a BasePoint objektum vagy ha nincs ilyen pont, akkor null
     */
    BasePoint findPoint(double x, double y) {
        BasePoint retBP = null;
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof BasePoint) {
                BasePoint bp = (BasePoint) o;
                if (bp.x == x & bp.y == y) {
                    retBP = bp;
                    break;
                }
            }
        }
        return retBP;
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� szakasz objektumot, ha van ilyen.
     * Az eg�r 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return a szakasz objektum vagy null, ha nincs a k�zelben szakasz.
     */
    Line findNearestLineSegment(MouseEvent e) {
        double min = screenMaxX, dist;
        Line minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof Line) {
                if (((Line) o).mainType == Line.LINESEGMENT) {
                    Line oo = (Line) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� f�legyenes objektumot, ha van ilyen.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return a f�legyenes objektum vagy null, ha nincs a k�zelben f�legyenes.
     */
    Line findNearestHalfLine(MouseEvent e) {
        double min = screenMaxX, dist;
        Line minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof Line) {
                if (((Line) o).mainType == Line.HALFLINE) {
                    Line oo = (Line) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� vonal (szakasz, vektor, egyenes, f�legyenes) objektumot, ha van ilyen.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return a vonal objektum vagy null, ha nincs a k�zelben vonal.
     */
    Line findNearestLine(MouseEvent e) {
        double min = screenMaxX, dist;
        Line minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof Line) {
                Line oo = (Line) o;
                dist = distance(bp, oo);
                if (dist < min) {
                    min = dist;
                    minobj = oo;
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� vektor objektumot, ha van ilyen.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return a vektor objektum vagy null, ha nincs a k�zelben vektor.
     */
    Line findNearestVector(MouseEvent e) {
        double min = screenMaxX, dist;
        Line minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof Line) {
                if (((Line) o).mainType == Line.VECTOR) {
                    Line oo = (Line) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� szerkesztett objektumot (pont, vonal, k�r, ellipszis), ha van ilyen.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return a szerkesztett objektum vagy null, ha nincs a k�zelben szerkesztett objektum.
     */
    GraphicalObject findNearestObject(MouseEvent e) {
        double min = screenMaxX, dist;
        GraphicalObject minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            //ha az objektum l�that�
            if (o.visible) {
                if (o instanceof Line) {
                    Line oo = (Line) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
                if (o instanceof BasePoint) {
                    BasePoint oo = (BasePoint) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
                if (o instanceof Circle) {
                    Circle oo = (Circle) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
                if (o instanceof Ellipse) {
                    Ellipse oo = (Ellipse) o;
                    dist = distance(bp, oo);
                    if (dist < min) {
                        min = dist;
                        minobj = oo;
                    }
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� c�mke objektumot, ha van ilyen a k�zelben.
     * Az eg�rmutat� 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e
     * @return
     */
    GraphicalObject findNearestLabel(MouseEvent e) {
        double min = screenMaxX, dist;
        GraphicalObject minobj = null;
        BasePoint bp = new BasePoint(e.getX(), e.getY());
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & !o.hiddenLabel) {
                BasePoint oo = new BasePoint(o.labelX + o.labelDX, o.labelY + o.labelDY);
                dist = distance(bp, oo);
                if (dist < min) {
                    min = dist;
                    minobj = o;
                }
            }
        }
        if (min < 20) {
            return minobj;
        } else {
            return null;
        }
    }

    /**
     * A megadott k�t ponton �thalad� szakasz objektum�nak kikeres�se az objectList vektorb�l.
     * @param p1 az egyik pont
     * @param p2 a m�sik pont
     * @return a szakasz vagy null, ha nincs ilyen objektum
     */
    Line findLineSegment(BasePoint p1, BasePoint p2) {
        BasePoint a, b;
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o instanceof Line) {
                Line oo = (Line) o;
                if (oo.mainType == Line.LINESEGMENT) {
                    a = (BasePoint) oo.creationObjectList.get(0);
                    b = (BasePoint) oo.creationObjectList.get(1);
                    if ((a == p1 & b == p2) | (b == p1 & a == p2)) {
                        return oo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Megkeresi �s param�terk�nt visszaadja az eg�r aktu�lis poz�ci�j�hoz legk�zelebbi, l�that� k�plet objektumot, ha van ilyen.
     * Az eg�r 20 pixeles k�rnyezet�t vizsg�lja a f�ggv�ny.
     * @param e az eg�resem�ny objektuma
     * @return a k�plet objektum vagy null, ha nincs a k�zelben k�plet.
     */
    Formula findNearestFormula(MouseEvent e) {
        int x = e.getX(), y = e.getY();
        Formula retvalue = null;
        for (int i = objectList.size() - 1; i >= 0; i--) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.visible & o instanceof Formula) {
                Formula oo = (Formula) o;
                if (oo.x <= x & x <= oo.x + oo.width & oo.y <= y & y <= oo.y + oo.height) {
                    retvalue = oo;
                    break;
                }
            }
        }
        return retvalue;
    }

    //**************************************************************************
    /**
     * Pont �s vonal t�vols�g�nak a kisz�m�t�sa.
     * @param p a pont objektum
     * @param l a vonal objektum (szakasz, egyenes, f�legyenes, vektor)
     * @return a t�vols�g
     */
    double distance(BasePoint p, Line l) {
        double retvalue = 1000;
        switch (l.mainType) {
            case Line.LINESEGMENT: {
                BasePoint p2 = projectedPoint(p, l);
                double a = Math.min(l.x0, l.x1), b = Math.min(l.y0, l.y1), c = Math.max(l.x0, l.x1), d = Math.max(l.y0, l.y1);
                if (a <= p2.x & p2.x <= c & b <= p2.y & p2.y <= d) {
                    retvalue = distance(p, p2);
                } else {
                    retvalue = Math.min(distance(new BasePoint(l.x0, l.y0), p), distance(new BasePoint(l.x1, l.y1), p));
                }
                break;
            }
            case Line.LINE: {
                retvalue = distance(p, projectedPoint(p, l));
                break;
            }
            case Line.VECTOR: {
                BasePoint p2 = projectedPoint(p, l);
                double a = Math.min(l.x0, l.x1), b = Math.min(l.y0, l.y1), c = Math.max(l.x0, l.x1), d = Math.max(l.y0, l.y1);
                if (a <= p2.x & p2.x <= c & b <= p2.y & p2.y <= d) {
                    retvalue = distance(p, p2);
                } else {
                    retvalue = Math.min(distance(new BasePoint(l.x0, l.y0), p), distance(new BasePoint(l.x1, l.y1), p));
                }
                break;
            }
            case Line.HALFLINE: {
                BasePoint p2 = projectedPoint(p, l);
                double a = Math.min(l.x0, l.x1), b = Math.min(l.y0, l.y1), c = Math.max(l.x0, l.x1), d = Math.max(l.y0, l.y1);
                if (a <= p2.x & p2.x <= c & b <= p2.y & p2.y <= d) {
                    retvalue = distance(p, p2);
                } else {
                    retvalue = Math.min(distance(new BasePoint(l.x0, l.y0), p), distance(new BasePoint(l.x1, l.y1), p));
                }
                break;
            }
        }
        return retvalue;
    }

    /**
     * Pont �s k�r t�vols�g�nak a kisz�m�t�sa.
     * @param p a pont objektum
     * @param c a k�r objektum
     * @return a t�vols�g
     */
    double distance(BasePoint p, Circle c) {
        return Math.abs(distance(p, new BasePoint(c.x, c.y)) - c.r);
    }

    /**
     * Pont �s ellipszis t�vols�g�nak a kisz�m�t�sa.
     * A pont az ellipszis k�t f�kuszpontj�t�l vett t�vols�g�nak �sszege �s a nagytengely hossz�nak k�l�nbs�ge.
     * @param p a pont objektum
     * @param e az ellipszis objektum
     * @return a t�vols�g
     */
    double distance(BasePoint p, Ellipse e) {
        //
        return Math.abs((distance(p, new BasePoint(e.x - e.c, e.y)) + distance(p, new BasePoint(e.x + e.c, e.y))) - e.a * 2);
    }

    /**
     * K�t pont t�vols�g�nak a kisz�m�t�sa
     * @param p1 az egyik pont objektuma
     * @param p2 a m�sik pont objektuma
     * @return a t�vols�g
     */
    double distance(BasePoint p1, BasePoint p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     * Pont egyenes vonalra es� mer�leges vet�lete.
     * @param p a pont objektuma
     * @param l a vonal objektum
     * @return a vet�tett pont objektuma
     */
    BasePoint projectedPoint(BasePoint p, Line l) {
        double m = (l.y1 - l.y0) / (l.x1 - l.x0);
        double x = (p.x + m * p.y + m * m * l.x0 - m * l.y0) / (m * m + 1);
        double y = m * (x - l.x0) + l.y0;
        if (l.x1 == l.x0) {
            return new BasePoint(l.x1, p.y);
        } else {
            return new BasePoint(x, y);
        }
    }

    //**************************************************************************
    /**
     * az objektumok �sszes kijel�l�s�nek megsz�ntet�se
     */
    void deselectAll() {
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            o.deSelect();
        }
    }

    /**
     * a k�vetkez� m�g nem foglalt csoportsz�m meghat�roz�sa
     * Az �sszef�gg� szerkeszt�sek objektumainak azonos csoportsz�mmal kell rendelkezni�k.
     * @return az �j csoportsz�m
     */
    int nextGroup() {
        boolean newGroup;
        int i;
        do {
            ++config.actGroup;
            newGroup = true;
            i = 0;
            //�j csoportsz�m ellen�rz�se
            do {
                GraphicalObject o = (GraphicalObject) objectList.get(i);
                //ha van m�r ilyen csoport
                if (o.group == config.actGroup) {
                    newGroup = false;
                }
                i++;
            } while (!newGroup & i < objectList.size());
        } while (!newGroup);
        return config.actGroup;
    }

    /**
     * a megadott sz�m� csoport kijel�l�se
     * @param g a csoport sz�ma
     */
    void selectGroup(int g) {
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.group == g) {
                o.Select();
            }
        }
    }

    /**
     * a megadott sz�m� csoport kl�noz�sa
     * @param g a csoport sz�ma
     */
    void cloneGroup(int g) {
        int newGroup = nextGroup();
        ByteArrayOutputStream memoryStream = null;
        Vector clonedObjectList = null;

        //teljes objektumlista ment�se
        try {
            ObjectOutputStream os = new ObjectOutputStream(
                    memoryStream = new ByteArrayOutputStream());
            os.writeObject(objectList);
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        //teljes objektumlista visszat�lt�se/kl�noz�s
        try {
            ObjectInputStream os = new ObjectInputStream(
                    new ByteArrayInputStream(memoryStream.toByteArray()));
            clonedObjectList = (Vector) os.readObject();
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        //a sz�ks�ges elemek kiv�logat�sa �s hozz�ad�sa a list�hoz
        for (int i = 0; i < clonedObjectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) clonedObjectList.get(i);
            if (o.group == g) {
                objectList.add(o);
                o.group = newGroup;
            }
        }
    }

    /**
     * a kijel�lt csoport mozgat�sa megadott koordin�t�kkal
     * @param DX az X ir�ny� eltol�s
     * @param DY az Y ir�ny� eltol�s
     */
    void moveSelectedGroup(double DX, double DY) {
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.selected == true) {
                o.Translate(DX, DY);
            }
        }
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.selected == true) {
                o.Calculate();
            }
        }
    }

    /**
     * a kijel�lt csoport t�rl�se 
     */
    void removeSelectedGroup() {
        for (int i = 0; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.selected == true) {
                objectList.remove(o);
                i--;
            }
        }
    }

    /**
     * csoportok �tsz�moz�sa
     * @param from mir�l
     * @param to mire
     */
    void reNumberGroups(GraphicalObject from, GraphicalObject to) {
        int From = from.group;
        if (from.group != to.group) {
            for (int i = 0; i < objectList.size(); i++) {
                GraphicalObject o = (GraphicalObject) objectList.get(i);
                if (o.group == From) {
                    o.group = to.group;
                }
            }
        }
    }

    /**
     * az �sszes objektum �jrasz�m�t�sa
     */
    void calculateAllObjects() {
        for (int i = 1; i < objectList.size(); i++) {
            GraphicalObject obj = (GraphicalObject) objectList.get(i);
            obj.Calculate();
        }
    }

    /**
     * a c�mk�k poz�ci�inak kisz�m�t�sa a megadott csoportra n�zve 
     * @param g az adott csoport sz�ma
     */
    void calculateLabels(int g) {
        double sumX = 0, sumY = 0;
        int n = 0; //pontok sz�ma

        //s�lypont kisz�m�t�sa
        for (int i = 1; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.group == g & o instanceof BasePoint & o.visible & o.valid & o.creationType != BasePoint.WEIGHTPOINT) {
                BasePoint oo = (BasePoint) o;
                sumX += oo.x;
                sumY += oo.y;
                n++;
            }
        }
        BasePoint wp = new BasePoint(sumX / n, sumY / n);

        Graphics2D g2 = (Graphics2D) paintPanel.getGraphics();
        Font font = new Font("Serif", Font.PLAIN, 20);
        FontRenderContext frc = g2.getFontRenderContext();
        double cx, cy;

        //c�mkepoz�ci�k meghat�roz�sa
        for (int i = 1; i < objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) objectList.get(i);
            if (o.group == g & o instanceof BasePoint & o.visible) {
                BasePoint oo = (BasePoint) o;
                Line v = new Line(new GraphicalObject[]{wp, oo}, Line.TWOPOINTS, Line.VECTOR);

                cx = font.getStringBounds(oo.label, frc).getCenterX();
                cy = font.getStringBounds(oo.label, frc).getCenterY();

                oo.labelX = oo.x + Math.cos(v.alfa) * 30 - cx;
                oo.labelY = oo.y + Math.sin(v.alfa) * 30 - cy;
            }
            if (o.group == g & o instanceof Line) {
                if (((Line) o).mainType == Line.LINESEGMENT |
                        ((Line) o).mainType == Line.VECTOR & o.visible) {
                    Line oo = (Line) o;
                    BasePoint p = new BasePoint((oo.x0 + oo.x1) / 2, (oo.y0 + oo.y1) / 2);
                    Line v = new Line(new GraphicalObject[]{wp, p}, Line.TWOPOINTS, Line.VECTOR);

                    cx = font.getStringBounds(oo.label, frc).getCenterX();
                    cy = font.getStringBounds(oo.label, frc).getCenterY();

                    oo.labelX = p.x + Math.cos(v.alfa) * 15 - cx;
                    oo.labelY = p.y + Math.sin(v.alfa) * 15 - cy;
                }
            }
        }
    }

    /**
     * az �sszes csoport c�mkepoz�ci�inak �jrasz�m�t�sa 
     */
    void calculateAllLabels() {
        Vector groups = new Vector();
        GraphicalObject act;
        for (int i = 1; i < objectList.size(); i++) {
            act = (GraphicalObject) objectList.get(i);
            if (!groups.contains(act.group)) {
                groups.add(act.group);
                calculateLabels(act.group);
            }
        }
    }

    /**
     * visszaadja a k�vetkez� pontc�mk�t
     * @return a pontc�mke �rt�ke vagy �res sztring, ha nincs enged�lyezve a c�mk�z�s
     */
    String getNextBpLabel() {
        String temp = config.actBpLabel;
        if (config.nextLabelOn) {
            if (++config.actBpLabelIDX > pointLabel.length - 1) {
                config.actBpLabelIDX = 0;
            }
            config.actBpLabel = pointLabel[config.actBpLabelIDX];
            pointLabelCombo.setSelectedItem(config.actBpLabel);
            return temp;
        } else {
            return "";
        }
    }

    /**
     * be�ll�tja a megadott grafikus objektum (BasePoint) c�mk�j�t
     * @param o a pont objektum
     */
    void nextBpLabel(GraphicalObject o) {
        if (config.nextLabelOn) {
            o.label = config.actBpLabel;
            if (++config.actBpLabelIDX > pointLabel.length - 1) {
                config.actBpLabelIDX = 0;
            }
            config.actBpLabel = pointLabel[config.actBpLabelIDX];
            pointLabelCombo.setSelectedItem(config.actBpLabel);
        } else {
            o.hiddenLabel = true;
            o.label = "";
        }
    }

    /**
     * visszaadja a k�vetkez� szakaszc�mk�t 
     * @return a szakaszc�mke �rt�ke vagy �res sztring, ha nincs enged�lyezve a c�mk�z�s
     */
    String getNextLsLabel() {
        String temp = config.actLsLabel;
        if (config.nextLabelOn) {
            if (++config.actLsLabelIDX > lineSegmentLabel.length - 1) {
                config.actLsLabelIDX = 0;
            }
            config.actLsLabel = lineSegmentLabel[config.actLsLabelIDX];
            lineSegmentLabelCombo.setSelectedItem(config.actLsLabel);
            return temp;
        } else {
            return "";
        }
    }

    /**
     * Visszadja az eg�r koordin�t�j�hoz legk�zelebbi pontot, vagy r�csraigaz�t�s eset�n
     * a legk�zelebbi r�cspontot.
     * @param e az eg�resem�ny objetuma
     * @return pont objektum
     */
    BasePoint getPoint(MouseEvent e) {
        double x = e.getX(), y = e.getY();
        BasePoint bp = null;
        //ha r�csra kell igaz�tani
        if (config.snapToGrid) {
            double sf = 20 * config.globalScaleFactor;
            double cy = config.originY % sf, cx = config.originX % sf;
            x = Math.round(((x - cx) / sf)) * sf + cx;
            y = Math.round(((y - cy) / sf)) * sf + cy;
            bp = new BasePoint(x, y);
        } else {
            bp = new BasePoint(e.getX(), e.getY());
        }
        return bp;
    }

    /**
     * Mindent aktualiz�l, ami v�ltozhat az ablakon bel�l.
     */
    void repaintAll() {
        paintPanel.repaint();
        //k�vetkez� cs�cspont c�mk�j�nek be�ll�t�sa
        pointLabelCombo.setSelectedItem(config.actBpLabel);
        //k�vetkez� szakasz c�mk�j�nek be�ll�t�sa
        lineSegmentLabelCombo.setSelectedItem(config.actLsLabel);
        downToolBar.repaint();
    }
    //*********************************************************************************************

    /**
     * Az aktu�lis dia f�jlnev�t adja vissza el�r�si �ttal egy�tt.
     * @return f�jln�v el�r�si �ttal sztring alakban
     */
    String getActualDiaFileName() {
        String fileName = "WORK/dia";
        DecimalFormat leadingZeroes = new DecimalFormat("000");
        fileName += leadingZeroes.format(actDia);
        return fileName;
    }

    /**
     * A legutols� l�tez� dia sorsz�m�nak meghat�roz�sa.
     * Erre az�rt van sz�ks�g, mert lehet hogy t�r�ltek a di�k k�z�l.
     */
    void searchLastDia() {
        int maxN = 1;
        File f = new File("WORK");
        String files[] = f.list();
        for (int i = 0; i < files.length; i++) {
            int n = Integer.valueOf(files[i].substring(3, 6));
            if (maxN < n) {
                maxN = n;
            }
        }
        lastDia = maxN;
    }

    /**
     * eggyel n�vekv�en �tsz�mozza a di�kat az aktu�lis elemt�l kezdve
     */
    void shiftAllDiaForward() {
        File dir = new File("work");
        File files[] = dir.listFiles();
        Vector fileVector = new Vector();
        for (int i = 0; i <= files.length - 1; i++) {
            fileVector.add(files[i]);
        }
        DecimalFormat leadingZeroes = new DecimalFormat("000");
        String numberSTR = null;
        String s = null;
        File actFile = null;

        for (int i = lastDia; i >= actDia; i--) {
            for (int j = 0; j <= fileVector.size() - 1; j++) {
                actFile = (File) fileVector.get(j);
                numberSTR = leadingZeroes.format(i);
                s = "dia" + numberSTR + ".canvas";
                //ha megvan a canvas f�jl
                if (actFile.getName().equals(s)) {
                    numberSTR = leadingZeroes.format(i + 1);
                    s = "dia" + numberSTR + ".canvas";
                    actFile.renameTo(new File("WORK/" + s));
                }

                s = "dia" + numberSTR + ".foil";
                //ha megvan a foil f�jl
                if (actFile.getName().equals(s)) {
                    numberSTR = leadingZeroes.format(i + 1);
                    s = "dia" + numberSTR + ".foil";
                    actFile.renameTo(new File("WORK/" + s));
                }
            }
        }
        lastDia++;
    }

    /**
     * eggyel cs�kken�en �tsz�mozza a di�kat az aktu�lis elemt�l kezdve
     */
    void shiftAllDiaBackward() {
        File dir = new File("work");
        File files[] = dir.listFiles();
        Vector fileVector = new Vector();
        for (int i = 0; i <= files.length - 1; i++) {
            fileVector.add(files[i]);
        }
        DecimalFormat leadingZeroes = new DecimalFormat("000");
        String numberSTR = null;
        String s = null;
        File actFile = null;

        for (int i = actDia; i <= lastDia; i++) {
            for (int j = 0; j <= fileVector.size() - 1; j++) {
                actFile = (File) fileVector.get(j);
                numberSTR = leadingZeroes.format(i);
                s = "dia" + numberSTR + ".canvas";
                //ha megvan a canvas f�jl
                if (actFile.getName().equals(s)) {
                    numberSTR = leadingZeroes.format(i - 1);
                    s = "dia" + numberSTR + ".canvas";
                    actFile.renameTo(new File("WORK/" + s));
                }

                s = "dia" + numberSTR + ".foil";
                //ha megvan a foil f�jl
                if (actFile.getName().equals(s)) {
                    numberSTR = leadingZeroes.format(i - 1);
                    s = "dia" + numberSTR + ".foil";
                    actFile.renameTo(new File("WORK/" + s));
                }
            }
        }
        lastDia--;
        if (lastDia == 0) {
            lastDia = 1;
        }
    }

    /**
     * �j dia l�trehoz�sa
     */
    void newDia() {
        displayLock = true;
        foilImage = new BufferedImage(screenMaxX, screenMaxY, BufferedImage.TYPE_INT_RGB);
        actFoil = foilImage.createGraphics();
        actFoil.setColor(getBackground());
        actFoil.fillRect(0, 0, screenMaxX, screenMaxY);
        actFoil.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        objectList.clear();
        objectList.add(config = new Config());
        config.originX = paintPanel.getBounds().getWidth() / 2;
        config.originY = paintPanel.getBounds().getHeight() / 2;

        undoStack.clear();
        redoStack.clear();
        saveStateOntoUndoStack();
        config.globalScaleFactor = 1;
        displayLock = false;
        repaintAll();
        initDia();
    }

    /**
     * �j dia l�trehoz�sa aktu�lis szerkeszt�ssel, �res f�li�val, k�pletek n�lk�l 
     */
    void newDiaWithEmptyFoil() {
        GraphicalObject o;
        displayLock = true;
        foilImage = new BufferedImage(screenMaxX, screenMaxY, BufferedImage.TYPE_INT_RGB);
        actFoil = foilImage.createGraphics();
        actFoil.setColor(getBackground());
        actFoil.fillRect(0, 0, screenMaxX, screenMaxY);
        actFoil.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        undoStack.clear();
        redoStack.clear();
        saveStateOntoUndoStack();
        //K�pletek t�rl�se
        for (int i = 0; i <= objectList.size() - 1; i++) {
            if ((o = (GraphicalObject) objectList.get(i)) instanceof Formula) {
                objectList.remove(o);
                i--;
            }
        }
        displayLock = false;
        repaintAll();
        penColorIcon.color = (Color) penColorPalette.get(config.actPenColor);
        highlightColorIcon.color = (Color) highlightColorPalette.get(config.actHighLightColor);
        statusBarText.setText("PONT: Adja meg a pont helyzet�t!");
        downToolBar.repaint();
    }

    /**
     * aktu�lis dia bet�lt�se
     */
    void loadDia() {
        displayLock = true;
        //szerkeszt�s bet�lt�se
        try {
            ObjectInputStream os = new ObjectInputStream(
                    new FileInputStream(getActualDiaFileName() + ".canvas"));
            objectList = (Vector) os.readObject();
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        //dia be�ll�t�sainak egyszer�s�tett el�r�se
        config = (Config) objectList.get(0);
        //undo/redo verm�nek ki�r�t�se
        undoStack.clear();
        redoStack.clear();
        //f�lia bet�lt�se
        File f = new File(getActualDiaFileName() + ".foil");
        try {
            foilImage = ImageIO.read(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        actFoil = (Graphics2D) foilImage.getGraphics();
        actFoil.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        displayLock = false;
        repaintAll();
        initDia();
    }

    /**
     * aktu�lis dia ment�se .canvas �s .foil kiterjeszt�s� f�jlokba
     */
    void saveDia() {
        //szerkeszt�s ment�se
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getActualDiaFileName() + ".canvas"));
            os.writeObject(objectList);
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        //undo/redo verm�nek ki�r�t�se
        undoStack.clear();
        redoStack.clear();
        //f�lia ment�se min�s�groml�s n�lk�li BMP form�tumba
        File f = new File(getActualDiaFileName() + ".foil");
        try {
            ImageIO.write(foilImage, "bmp", f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * aktu�lis dia ment�se JPG form�tumban a TEMP k�nyvt�rba
     */
    void saveDiaToJPG() {
        String fileName = "TEMP" + getActualDiaFileName().substring(4) + ".jpg";
        File f = new File(fileName);
        try {
            ImageIO.write(paintPanel.makeImage(), "jpg", f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Megkeresi a soron k�vetkez� el�ad�s f�jlnev�t, mely a k�vetkez� form�tum�:
     * "GeoProjektor��HHNN_E.zip", ahol �- �v, H - h�nap, N - nap �s az
     * E - el�ad�s sorsz�ma
     * Az aktu�lis d�tum ut�n mindig az el�ad�s sorsz�ma tal�lhat� egyes�vel n�vekv�
     * sz�moz�ssal.
     * @return az el�ad�s File objektumk�nt el�r�si �ttal egy�tt
     */
    File getNewPresentationFile() {
        File ZIPFile;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String dateSTR = dateFormat.format(date);
        int i = 0;
        do {
            ZIPFile = new File("PRESENTATIONS/GeoProjektor" + dateSTR + "_" + String.valueOf(++i) + ".zip");
        } while (ZIPFile.exists());
        return ZIPFile;
    }

    /**
     * di�k archiv�l�sa zip f�jlba csomagolva a .canvas �s .foil f�jlok mind p�rban
     */
    void archiveAllDia() {
        String tempZIPFile = actPresentationFileName;
        tempZIPFile = tempZIPFile.replaceAll("PRESENTATIONS", "ARCHIVE");
        File ZIPFile = new File(tempZIPFile);

        int BUFFER = 2048;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(ZIPFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            File f = new File("WORK");
            String files[] = f.list();
            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream("WORK/" + files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } //ha siker�lt az archiv�l�s, t�r�lhet�k a di�k
        finally {
            eraseDir(new File("WORK"));
        }
    }

    /**
     * az �sszes elk�sz�tett dia "lefot�z�sa" jpg f�jlokba + t�m�r�t�s ZIP f�jlba
     */
    void captureAllDia() {
        //TEMP k�nyvt�r l�trehoz�sa, ha sz�ks�ges
        File dir = new File("TEMP");
        if (!dir.exists()) {
            dir.mkdir();
        } //ha l�tezik, el�bb a mappa �r�t�se
        else {
            eraseDir(dir);
        }
        //di�k egym�s ut�n t�rt�n� bet�lt�se �s jpg f�jlba ment�se
        for (int i = 1; i <= lastDia; i++) {
            actDia = i;
            if (actDiaExist()) {
                loadDia();
                saveDiaToJPG();
            }
        }
        //a k�sz fot�k t�m�r�t�se egy "GeoProjektor��HHNN_E.zip" f�jlba
        File ZIPFile = getNewPresentationFile();

        int BUFFER = 2048;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(ZIPFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            //out.setMethod(ZipOutputStream.STORED); -t�m�r�t�s n�lk�l
            byte data[] = new byte[BUFFER];
            File f = new File("TEMP");
            String files[] = f.list();
            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream("TEMP/" + files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        actPresentationFileName = ZIPFile.toString();
    }

    /**
     * dia �llapot�nak ment�se az Undo verembe a visszavon�shoz
     */
    void saveStateOntoUndoStack() {
        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(memoryStream);
            os.writeObject(objectList);
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        //visszavon�sok maxim�lis sz�ma: 100
        if (undoStack.size() == 100) {
            undoStack.remove(0);
        }
        undoStack.add(undoStack.size(), memoryStream);
    }

    /**
     * dia �llapot�nak ment�se az Redo verembe az ism�tl�shez
     */
    void saveStateOntoRedoStack() {
        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(memoryStream);
            os.writeObject(objectList);
            os.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        //ism�tl�sek maxim�lis sz�ma: 100
        if (redoStack.size() == 100) {
            redoStack.remove(0);
        }
        redoStack.add(redoStack.size(), memoryStream);
    }

    /**
     * a legutols� szerkeszt�si m�velet visszavon�sa
     */
    void undo() {
        //ha van mit visszavonni
        if (undoStack.size() != 0) {
            saveStateOntoRedoStack();
            ByteArrayOutputStream memoryStream = (ByteArrayOutputStream) undoStack.lastElement();
            try {
                ObjectInputStream os = new ObjectInputStream(
                        new ByteArrayInputStream(memoryStream.toByteArray()));
                objectList = (Vector) os.readObject();
                os.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            undoStack.removeElement(memoryStream);
        }
        config = (Config) (GraphicalObject) objectList.get(0);
    }

    /**
     * a legutols� visszavon�s visszavon�sa (ism�tl�s)
     */
    void redo() {
        //ha van mit ism�telni
        if (redoStack.size() != 0) {
            saveStateOntoUndoStack();
            ByteArrayOutputStream memoryStream = (ByteArrayOutputStream) redoStack.lastElement();
            try {
                ObjectInputStream os = new ObjectInputStream(
                        new ByteArrayInputStream(memoryStream.toByteArray()));
                objectList = (Vector) os.readObject();
                os.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            redoStack.removeElement(memoryStream);
        }
        config = (Config) (GraphicalObject) objectList.get(0);
    }

    /**
     * palett�k kezd��rt�keinek be�ll�t�sa 
     */
    void initPalette() {
        penColorPalette.add(Color.BLACK);
        penColorPalette.add(Color.RED);
        penColorPalette.add(Color.GREEN);
        penColorPalette.add(Color.BLUE);
        penColorPalette.add(new Color(151, 69, 120));
        penColorPalette.add(Color.MAGENTA);

        highlightColorPalette.add(Color.GREEN);
        highlightColorPalette.add(Color.BLUE);
        highlightColorPalette.add(Color.RED);
        highlightColorPalette.add(Color.YELLOW);
        highlightColorPalette.add(Color.MAGENTA);
        highlightColorPalette.add(Color.CYAN);
        highlightColorPalette.add(Color.BLACK);
    }

    /**
     * f�ablak tulajdons�gainak be�ll�t�sa
     */
    void initFrame() {
        // C�m, poz�ci� �s m�ret megad�sa:
        this.setTitle("MedveSoft GeoProjektor");
        this.setSize(screenMaxX, screenMaxY);
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //program ikonj�nak be�ll�t�sa
        URLClassLoader urlLoader = (URLClassLoader) GeoProjektor.class.getClassLoader();
        URL url = urlLoader.findResource("icons/program.jpg");
        ImageIcon icon = new ImageIcon(url);
        this.setIconImage(icon.getImage());
    }

    /**
     * Dia inicializ�l�sa
     */
    void initDia() {
        URL url = null;
        statusBarText.setText("PONT: Kattint�ssal helyezze el a pontot a szerkeszt�v�sznon!");
        URLClassLoader urlLoader = (URLClassLoader) GeoProjektor.class.getClassLoader();
        //cs�cspontok
        if (config.nodesOn) {
            url = urlLoader.findResource("icons/icon50a.jpg");
            Button50.setIcon(new ImageIcon(url));
        } else {
            url = urlLoader.findResource("icons/icon50b.jpg");
            Button50.setIcon(new ImageIcon(url));
        }
        //r�csraigaz�t�s
        if (config.snapToGrid) {
            url = urlLoader.findResource("icons/icon48a.jpg");
            Button48.setIcon(new ImageIcon(url));
        } else {
            url = urlLoader.findResource("icons/icon48b.jpg");
            Button48.setIcon(new ImageIcon(url));
        }
        //k�vetkez� c�mke bekapcsolva
        if (config.nextLabelOn) {
            url = urlLoader.findResource("icons/icon45a.jpg");
            Button45.setIcon(new ImageIcon(url));
        } else {
            url = urlLoader.findResource("icons/icon45b.jpg");
            Button45.setIcon(new ImageIcon(url));
        }

        penColorIcon.color = (Color) penColorPalette.get(config.actPenColor);
        highlightColorIcon.color = (Color) highlightColorPalette.get(config.actHighLightColor);
        downToolBar.repaint();
    }

    /**
     * ikonok l�trehoz�sa �s hozz�ad�sa az eszk�zt�rakhoz
     */
    void initButtons() {
        upToolBar.addSeparator();

        URLClassLoader urlLoader = (URLClassLoader) GeoProjektor.class.getClassLoader();
        URL url = urlLoader.findResource("icons/icon01.jpg");
        upToolBar.add(Button1 = new ToolBarButton(url)); //pont 

        url = urlLoader.findResource("icons/icon11.jpg");
        upToolBar.add(Button11 = new ToolBarButton(url)); //szakaszfelez� pont

        url = urlLoader.findResource("icons/icon51.jpg");
        upToolBar.add(Button51 = new ToolBarButton(url)); //s�lypont

        upToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon02.jpg");
        upToolBar.add(Button2 = new ToolBarButton(url));  //szakasz 

        url = urlLoader.findResource("icons/icon43.jpg");
        upToolBar.add(Button43 = new ToolBarButton(url)); //p�rhuzamos szakasz

        url = urlLoader.findResource("icons/icon09.jpg");
        upToolBar.add(Button9 = new ToolBarButton(url));  //mer�leges szakasz

        upToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon44.jpg");
        upToolBar.add(Button44 = new ToolBarButton(url)); //f�legyenes 

        url = urlLoader.findResource("icons/icon58.jpg");
        upToolBar.add(Button58 = new ToolBarButton(url)); //p�rhuzamos f�legyenes

        upToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon04.jpg");
        upToolBar.add(Button4 = new ToolBarButton(url));  //egyenes 

        url = urlLoader.findResource("icons/icon60.jpg");
        upToolBar.add(Button60 = new ToolBarButton(url)); //szakaszfelez� mer�leges          

        url = urlLoader.findResource("icons/icon55.jpg");
        upToolBar.add(Button55 = new ToolBarButton(url)); //sz�gfelez� egyenes        

        upToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon10.jpg");
        upToolBar.add(Button10 = new ToolBarButton(url)); //vektor 

        url = urlLoader.findResource("icons/icon33.jpg");
        upToolBar.add(Button33 = new ToolBarButton(url)); //vektor eltol�sa egy kezd�pontba

        upToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon08.jpg");
        upToolBar.add(Button8 = new ToolBarButton(url));  //k�r 

        url = urlLoader.findResource("icons/icon46.jpg");
        upToolBar.add(Button46 = new ToolBarButton(url)); //k�r h�rom ponton �t - k�r�l�rt k�r      

        url = urlLoader.findResource("icons/icon47.jpg");
        upToolBar.add(Button47 = new ToolBarButton(url)); //be�rt k�r              

        upToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon31.jpg");
        upToolBar.add(Button31 = new ToolBarButton(url)); //kocka

        url = urlLoader.findResource("icons/icon24.jpg");
        upToolBar.add(Button24 = new ToolBarButton(url)); //t�glatest

        url = urlLoader.findResource("icons/icon23.jpg");
        upToolBar.add(Button23 = new ToolBarButton(url)); //n�gysz�g alap� g�la

        url = urlLoader.findResource("icons/icon36.jpg");
        upToolBar.add(Button36 = new ToolBarButton(url)); //henger

        url = urlLoader.findResource("icons/icon35.jpg");
        upToolBar.add(Button35 = new ToolBarButton(url)); //k�p

        url = urlLoader.findResource("icons/icon37.jpg");
        upToolBar.add(Button37 = new ToolBarButton(url)); //csonkak�p

        url = urlLoader.findResource("icons/icon52.jpg");
        upToolBar.add(Button52 = new ToolBarButton(url)); //h�romsz�g alap� g�la        


        rightToolBar.addSeparator();

        url = urlLoader.findResource("icons/icon20.jpg");
        rightToolBar.add(Button20 = new ToolBarButton(url)); //�ltal�nos h�romsz�g

        url = urlLoader.findResource("icons/icon17.jpg");
        rightToolBar.add(Button17 = new ToolBarButton(url)); //der�ksz�g� h�romsz�g

        url = urlLoader.findResource("icons/icon16.jpg");
        rightToolBar.add(Button16 = new ToolBarButton(url)); //egyenl�sz�r� h�romsz�g

        url = urlLoader.findResource("icons/icon18.jpg");
        rightToolBar.add(Button18 = new ToolBarButton(url)); //szab�lyos h�romsz�g

        rightToolBar.addSeparator();

        url = urlLoader.findResource("icons/icon21.jpg");
        rightToolBar.add(Button21 = new ToolBarButton(url)); //�ltal�nos trap�z

        url = urlLoader.findResource("icons/icon22.jpg");
        rightToolBar.add(Button22 = new ToolBarButton(url)); //paralelogramma

        url = urlLoader.findResource("icons/icon26.jpg");
        rightToolBar.add(Button26 = new ToolBarButton(url)); //deltoid

        url = urlLoader.findResource("icons/icon27.jpg");
        rightToolBar.add(Button27 = new ToolBarButton(url)); //rombusz

        url = urlLoader.findResource("icons/icon25.jpg");
        rightToolBar.add(Button25 = new ToolBarButton(url)); //t�glalap

        url = urlLoader.findResource("icons/icon30.jpg");
        rightToolBar.add(Button30 = new ToolBarButton(url)); //n�gyzet

        rightToolBar.addSeparator();

        url = urlLoader.findResource("icons/icon29.jpg");
        rightToolBar.add(Button29 = new ToolBarButton(url)); //�ltal�nos soksz�g

        url = urlLoader.findResource("icons/icon28.jpg");
        rightToolBar.add(Button28 = new ToolBarButton(url)); //szab�lyos soksz�g      

        rightToolBar.setOrientation(JToolBar.VERTICAL);


        leftToolBar.addSeparator();

        url = urlLoader.findResource("icons/icon03.jpg");
        leftToolBar.add(Button3 = new ToolBarButton(url));   //pont mozgat�sa

        url = urlLoader.findResource("icons/icon07.jpg");
        leftToolBar.add(Button7 = new ToolBarButton(url));   //szerkeszt�s mozgat�sa

        leftToolBar.addSeparator();

        url = urlLoader.findResource("icons/icon06.jpg");
        leftToolBar.add(Button6 = new ToolBarButton(url));   //cellar�cs ki/be

        url = urlLoader.findResource("icons/icon40.jpg");
        leftToolBar.add(Button40 = new ToolBarButton(url));  //koordin�ta-rendszer ki/be

        url = urlLoader.findResource("icons/icon45a.jpg");
        leftToolBar.add(Button45 = new ToolBarButton(url));  //k�vetkez� c�mke ki/be

        url = urlLoader.findResource("icons/icon48a.jpg");
        leftToolBar.add(Button48 = new ToolBarButton(url));  //r�csraigaz�t�s ki/be

        url = urlLoader.findResource("icons/icon50a.jpg");
        leftToolBar.add(Button50 = new ToolBarButton(url));  //cs�csok megjelen�t�se ki/be

        leftToolBar.addSeparator();

//        url = urlLoader.findResource("icons/icon71.jpg");
//        leftToolBar.add(Button71 = new ToolBarButton(url)); //s�g�

        url = urlLoader.findResource("icons/icon72.jpg");
        leftToolBar.add(Button72 = new ToolBarButton(url)); //n�vjegy ablak        

        leftToolBar.setOrientation(JToolBar.VERTICAL);

        downToolBar.addSeparator();

        url = urlLoader.findResource("icons/icon69.jpg");
        downToolBar.add(Button69 = new ToolBarButton(url)); //�j k�plet besz�r�sa        

        downToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon34.jpg");
        downToolBar.add(Button34 = new ToolBarButton(url)); //�r�s a f�li�ra

        downToolBar.add(Button59 = new ToolBarButton(penColorIcon = new ColorIcon1((Color) penColorPalette.get(0)))); //tintasz�n

        url = urlLoader.findResource("icons/icon56.jpg");
        downToolBar.add(Button56 = new ToolBarButton(url)); //t�rl�s a f�li�r�l - szivacs

        downToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon39.jpg");
        downToolBar.add(Button39 = new ToolBarButton(url)); //poligon kijel�l�se

        downToolBar.add(Button61 = new ToolBarButton(highlightColorIcon = new ColorIcon2((Color) highlightColorPalette.get(0)))); //kijel�l�sz�n

        url = urlLoader.findResource("icons/icon49.jpg");
        downToolBar.add(Button49 = new ToolBarButton(url)); //alakzat kl�noz�sa        

        url = urlLoader.findResource("icons/icon53.jpg");
        downToolBar.add(Button53 = new ToolBarButton(url)); //alakzat t�rl�se

        downToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon32.jpg");
        downToolBar.add(Button32 = new ToolBarButton(url)); //undo

        url = urlLoader.findResource("icons/icon41.jpg");
        downToolBar.add(Button41 = new ToolBarButton(url)); //redo   

        downToolBar.addSeparator();
        url = urlLoader.findResource("icons/icon12.jpg");
        downToolBar.add(Button12 = new ToolBarButton(url));  //t�bla t�rl�sek        

        url = urlLoader.findResource("icons/icon63.jpg");
        downToolBar.add(Button63 = new ToolBarButton(url)); //�j dia besz�r�sa

        url = urlLoader.findResource("icons/icon68.jpg");
        downToolBar.add(Button68 = new ToolBarButton(url)); //�j dia szerkeszt�ssel        

        url = urlLoader.findResource("icons/icon64.jpg");
        downToolBar.add(Button64 = new ToolBarButton(url)); //ugr�s az els� di�ra

        url = urlLoader.findResource("icons/icon66.jpg");
        downToolBar.add(Button66 = new ToolBarButton(url)); //lapoz�s vissza

        url = urlLoader.findResource("icons/icon67.jpg");
        downToolBar.add(Button67 = new ToolBarButton(url)); //lapoz�s el�re

        url = urlLoader.findResource("icons/icon65.jpg");
        downToolBar.add(Button65 = new ToolBarButton(url)); //ugr�s az utols� di�ra

        url = urlLoader.findResource("icons/icon05.jpg");
        downToolBar.add(Button5 = new ToolBarButton(url)); //nagy�t�s

        url = urlLoader.findResource("icons/icon70.jpg");
        downToolBar.add(Button70 = new ToolBarButton(url)); //szerkeszt�v�szon alaphelyzetbe �ll�t�sa        


        downToolBar.addSeparator();
        downToolBar.add(pointLabelCombo);                   //pont c�mk�j�nek a kiv�laszt�s�t lehet�v� tev� combobox
        pointLabelCombo.setMaximumSize(new Dimension(50, 36));
        for (int i = 0; i < pointLabel.length; i++) {
            String string = pointLabel[i];
            pointLabelCombo.addItem(string);
        }

        //a cs�cspont c�mk�j�nek kiv�laszt�s�t figyel� esem�nykezel�
        pointLabelCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.actBpLabel = (String) pointLabelCombo.getSelectedItem();
                config.actBpLabelIDX = pointLabelCombo.getSelectedIndex();
            }
        });

        downToolBar.addSeparator();
        downToolBar.add(lineSegmentLabelCombo);                   //szakasz c�mk�j�nek a kiv�laszt�s�t lehet�v� tev� combobox
        lineSegmentLabelCombo.setMaximumSize(new Dimension(50, 36));
        for (int i = 0; i < lineSegmentLabel.length; i++) {
            String string = lineSegmentLabel[i];
            lineSegmentLabelCombo.addItem(string);
        }
        downToolBar.validate();

        //a szakasz c�mk�j�nek kiv�laszt�s�t figyel� esem�nykezel�
        lineSegmentLabelCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.actLsLabel = (String) lineSegmentLabelCombo.getSelectedItem();
                config.actLsLabelIDX = lineSegmentLabelCombo.getSelectedIndex();
            }
        });


        upToolBar.setFloatable(false);
        downToolBar.setFloatable(false);
        rightToolBar.setFloatable(false);
        leftToolBar.setFloatable(false);

        buttonWatch = new ButtonWatch();
        Button1.addMouseListener(buttonWatch);
        Button2.addMouseListener(buttonWatch);
        Button3.addMouseListener(buttonWatch);
        Button4.addMouseListener(buttonWatch);
        Button5.addMouseListener(buttonWatch);
        Button6.addMouseListener(buttonWatch);
        Button7.addMouseListener(buttonWatch);
        Button8.addMouseListener(buttonWatch);
        Button9.addMouseListener(buttonWatch);

        Button10.addMouseListener(buttonWatch);
        Button11.addMouseListener(buttonWatch);
        Button12.addMouseListener(buttonWatch);
        Button16.addMouseListener(buttonWatch);
        Button17.addMouseListener(buttonWatch);
        Button18.addMouseListener(buttonWatch);

        Button20.addMouseListener(buttonWatch);
        Button21.addMouseListener(buttonWatch);
        Button22.addMouseListener(buttonWatch);
        Button23.addMouseListener(buttonWatch);
        Button24.addMouseListener(buttonWatch);
        Button25.addMouseListener(buttonWatch);
        Button26.addMouseListener(buttonWatch);
        Button27.addMouseListener(buttonWatch);
        Button28.addMouseListener(buttonWatch);
        Button29.addMouseListener(buttonWatch);

        Button30.addMouseListener(buttonWatch);
        Button31.addMouseListener(buttonWatch);
        Button32.addMouseListener(buttonWatch);
        Button33.addMouseListener(buttonWatch);
        Button34.addMouseListener(buttonWatch);
        Button35.addMouseListener(buttonWatch);
        Button36.addMouseListener(buttonWatch);
        Button37.addMouseListener(buttonWatch);
        Button39.addMouseListener(buttonWatch);

        Button40.addMouseListener(buttonWatch);
        Button41.addMouseListener(buttonWatch);
        Button43.addMouseListener(buttonWatch);
        Button44.addMouseListener(buttonWatch);
        Button45.addMouseListener(buttonWatch);
        Button46.addMouseListener(buttonWatch);
        Button47.addMouseListener(buttonWatch);
        Button48.addMouseListener(buttonWatch);
        Button49.addMouseListener(buttonWatch);

        Button50.addMouseListener(buttonWatch);
        Button51.addMouseListener(buttonWatch);
        Button52.addMouseListener(buttonWatch);
        Button53.addMouseListener(buttonWatch);
        Button55.addMouseListener(buttonWatch);
        Button56.addMouseListener(buttonWatch);
        Button58.addMouseListener(buttonWatch);
        Button59.addMouseListener(buttonWatch);

        Button60.addMouseListener(buttonWatch);
        Button61.addMouseListener(buttonWatch);
        Button63.addMouseListener(buttonWatch);
        Button64.addMouseListener(buttonWatch);
        Button65.addMouseListener(buttonWatch);
        Button66.addMouseListener(buttonWatch);
        Button67.addMouseListener(buttonWatch);
        Button68.addMouseListener(buttonWatch);
        Button69.addMouseListener(buttonWatch);

        Button70.addMouseListener(buttonWatch);
//        Button71.addMouseListener(buttonWatch);
        Button72.addMouseListener(buttonWatch);
    }

    /**
     * Az aktu�lis dia l�tez�s�nek vizsg�lata. Mindk�t f�jl, a .canvas �s 
     * a .foil f�jlok megl�t�t is ellen�rzi.
     */
    boolean actDiaExist() {
        File fDia = new File(getActualDiaFileName() + ".canvas");
        File fFoil = new File(getActualDiaFileName() + ".foil");
        return (fDia.exists() && fFoil.exists() ? true : false);
    }

    /**
     * k�nyvt�r l�trehoz�sa, ha nem l�tezik
     */
    void makeDir(File f) {
        if (!f.exists() | !f.isDirectory()) {
            f.mkdir();
        }
    }

    /**
     * k�nyvt�r t�rl�se, ha l�tezik
     */
    void eraseDir(File dir) {
        if (dir.exists() & dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * k�nyvt�rszerkezet megl�t�nek ellen�rz�se, �s sz�ks�g eset�n jav�t�sa
     */
    void checkDirectories() {
        makeDir(new File("WORK"));
        makeDir(new File("TEMP"));
        makeDir(new File("PRESENTATIONS"));
        makeDir(new File("ARCHIVE"));
    }

    /**
     * email postafi�k bejelentkez�si adatainak hiteles�t�se
     */
    class SMTPAuthenticator extends Authenticator {

        /**
         * Felhaszn�l� emailc�me
         */
        private String userEmail;
        /**
         * Felhaszn�l� jelszava
         */
        private String userPassword;

        /**
         * Konstruktor. L�trehozza az objektumot a megadott param�terekkel.
         * @param userEmail felhaszn�l� emailc�me
         * @param userPassword felhaszn�l� jelszava
         */
        public SMTPAuthenticator(String userEmal, String userPassword) {
            super();
            this.userEmail = userEmal;
            this.userPassword = userPassword;
        }

        /**
         * Hiteles�t� objektum l�trehoz�sa.
         * @return a hiteles�t� objektum
         */
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userEmail, userPassword);
        }
    }

    /**
     * Email k�ld�se a megadott adatok alapj�n
     * @param data a p�rbesz�dablakban be�ll�t�tott adatokat tartalmaz� vektor objektum
     * @return hibak�d 
     * 0 - sikeres k�ld�s
     * 1 - sikertelen k�ld�s
     * 2 - sikertelen hiteles�t�s
     */
    int sendEmail(Vector data) {
        int errorCode = 0;
        String from = (String) data.get(0);
        String password = (String) data.get(1);
        String to = (String) data.get(2);
        String subject = (String) data.get(3);
        String body = (String) data.get(4);
        Boolean supportDevelopment = (Boolean) data.get(5);
        String fileAttachment = actPresentationFileName;

        Properties props = new Properties();
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");

        try {
            Authenticator auth = new SMTPAuthenticator(from, password);
            Session session = Session.getInstance(props, auth);

            MimeMessage message = new MimeMessage(session);
            message.setText(body);
            message.setSubject(subject);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));
            message.setHeader("X-Mailer", "JAVA");
            message.setSentDate(new Date());

            //m�solat k�ld�se a fejleszt�nek
            if (supportDevelopment) {
                message.setRecipients(Message.RecipientType.BCC, "GeoProjektor@gmail.com");
            }

            //�zenet r�sz
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            //csatolt f�jl r�sz
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileAttachment);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileAttachment);
            multipart.addBodyPart(messageBodyPart);

            //a r�szek �sszerak�sa
            message.setContent(multipart);
            Transport.send(message);
        } catch (AuthenticationFailedException mex) {
            errorCode = 1;
        } catch (Exception ex) {
            errorCode = 2;
        }
        return errorCode;
    }

    /**
     * A f� oszt�ly konstruktora. L�trehozza az alkalmaz�s f� ablak�t, �s
     * bet�lti az el�ad�s els� di�j�t.
     */
    public GeoProjektor() {
        checkDirectories();
        initPalette();
        initFrame();
        searchLastDia();

        //esem�nyfigyel� az ablak bez�r�s�hoz
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Vector data = null;
                int errorCode = 0;

                //Kil�p�s meger�s�t�se p�rbesz�dablak                    
                DialogOnExit exitFrm = new DialogOnExit(null, true);
                do {
                    exitFrm.setVisible(true);

                    //adatok �tv�tele a formt�l
                    data = exitFrm.getReturnValues();

                    //ha nem a M�gse gombot nyomt�k, akkor di�k fot�z�sa
                    if (exitFrm.getReturnStatus() != DialogOnExit.RET_CANCEL) {
                        saveDia();
                        captureAllDia();
                        //Ha archiv�lhat� az el�ad�s
                        if ((Boolean) data.get(6)) {
                            archiveAllDia();
                        }
                    } else {
                        errorCode = 0;
                    }

                    //Ha a kil�p�s k�ld�s n�lk�l gombot nyomt�k
                    if (exitFrm.getReturnStatus() == DialogOnExit.RET_EXIT_WITHOUT_SEND) {
                        errorCode = 0;
                    }

                    //ha a k�ld�s gombot nyomt�k, email k�ld�se
                    if (exitFrm.getReturnStatus() == DialogOnExit.RET_EXIT_AND_SEND) {
                        if ((errorCode = sendEmail(data)) == 1) {
                            JOptionPane.showMessageDialog(null, "Hiba t�rt�nt a k�ld� emailc�m �s jelsz� hiteles�t�sekor!", "Hiba!", JOptionPane.ERROR_MESSAGE);
                        }
                        if (errorCode == 2) {
                            JOptionPane.showMessageDialog(null, "Hiba t�rt�nt az email k�ld�sekor!", "Hiba!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } while (errorCode != 0);

                //ha nem a M�gse gombot nyomt�k, akkor kil�p�s
                if (exitFrm.getReturnStatus() != DialogOnExit.RET_CANCEL) {
                    System.exit(0);
                }
            }
        });

        //Ikonok l�trehoz�sa �s elhelyez�se az eszk�zsorokon
        initButtons();

        //Statuszsor
        JPanel StatusBar = new JPanel();
        StatusBar.setLayout(new BorderLayout());
        StatusBar.add(statusBarText = new JLabel(), BorderLayout.CENTER);
        StatusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JPanel SouthPanel = new JPanel();
        SouthPanel.setLayout(new GridLayout(2, 1));
        SouthPanel.add(downToolBar);
        SouthPanel.add(StatusBar);

        //eszk�zsorok elrendez�se
        cp.setLayout(new BorderLayout(1, 1));
        cp.add(upToolBar, "North");
        cp.add(leftToolBar, "West");
        cp.add(rightToolBar, "East");
        cp.add(SouthPanel, "South");
        cp.add(paintPanel, "Center");

        //Eg�resem�nyfigyel�k felf�z�se a figyel�l�ncra
        paintPanel.addMouseListener(lastMouseListener = new MouseWatch1());
        paintPanel.addMouseListener(rightButtonWatch = new RightButtonWatch());
        paintPanel.addMouseMotionListener((MouseMotionListener) rightButtonWatch);
        paintPanel.addMouseWheelListener(new MouseWheelWatch());

        this.setVisible(true);

        //ha l�tezik az aktu�lis dia
        if (actDiaExist()) {
            loadDia();
        } else {
            newDia();
        }

        paintPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        //a GUI alaphelyzetbe �ll�t�sa
        initDia();
    }

    /**
     * A program bel�p�si pontja.
     * @param args
     */
    public static void main(String[] args) {
        //F� k�perny�ablak l�trehoz�sa
        new GeoProjektor();
    }
}