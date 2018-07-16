package com.MedveSoft;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Szerkeszt�v�szon implement�ci�ja
 *
 * Feladata, hogy megjelen�tse az objectList Vector �ltal szolg�ltatott grafikus objektumokat,
 * illetve a k�z�r�ssal �rt f�li�t, azaz a prezent�ci� di�it.
 * Ezen a vez�rl�n t�rt�nik a dinamikus szerkeszt�s.
 */
class PaintPanel extends JPanel {

    /**
     * kapcsol�mez� a f� vez�rl�objektum el�r�s�hez
     */
    public GeoProjektor outer;

    /**
     * Konstruktor. L�trehozza a szerkeszt�v�sznat �s �sszekapcsolja a f�
     * vez�rl�objektummal.
     * @param outer
     */
    public PaintPanel(GeoProjektor outer) {
        super();
        this.outer = outer;
    }

    /**
     * a raszteres grafik�j� k�pleteket jelen�ti meg
     * @param gr
     */
    private void drawFormulas(Graphics2D gr) {
        gr.setStroke(new BasicStroke(2));
        gr.setColor(Color.BLACK);
        URLClassLoader urlLoader = (URLClassLoader) GeoProjektor.class.getClassLoader();
        for (int i = 0; i < outer.objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) outer.objectList.get(i);
            if (o.visible) {
                if (o instanceof Formula) {
                    Formula oo = (Formula) o;
                    outer.formulaImage = null;
                    URL url = urlLoader.findResource(oo.fileName);
                    if (url != null) {
                        try {
                            outer.formulaImage = ImageIO.read(url);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        if (outer.formulaImage != null) {
                            gr.drawImage(outer.formulaImage, (int) oo.x, (int) oo.y, this);
                            oo.height = outer.formulaImage.getHeight();
                            oo.width = outer.formulaImage.getWidth();
                            gr.drawRoundRect((int) oo.x, (int) oo.y, (int) oo.width, (int) oo.height, 5, 5);
                        }
                    }
                }
            }
        }
    }

    /**
     * a Descartes f�le (dinamikus) der�ksz�g� koordin�tarendszer tengelyeit jelen�t meg
     * @param gr
     */
    private void drawCoordinateSystem(Graphics2D gr) {
        double sf = 20 * outer.config.globalScaleFactor;
        double cy = outer.config.originY % sf;
        double cx = outer.config.originX % sf;
        if (outer.config.gridOn) {
            gr.setColor(new Color(0, 150, 255));
            gr.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1, 2}, 0));
            for (int i = 0; i <= (int) outer.paintPanel.getBounds().getHeight() / sf; i++) {
                Line2D l = new Line2D.Double(0, i * sf + cy, outer.paintPanel.getBounds().getMaxX(), i * sf + cy);
                gr.draw(l);
            }
            for (int i = 0; i <= (int) outer.paintPanel.getBounds().getWidth() / sf; i++) {
                Line2D l = new Line2D.Double(i * sf + cx, 0, i * sf + cx, outer.paintPanel.getBounds().getMaxY());
                gr.draw(l);
            }
        }
        if (outer.config.axisOn) {
            gr.setColor(Color.BLACK);
            gr.setStroke(new BasicStroke(1));
            double x = outer.config.originX;
            double y = outer.config.originY;
            Line2D xAxis = new Line2D.Double(0, y, outer.screenMaxX, y);
            gr.draw(xAxis);
            gr.translate(outer.screenMaxX - 99, y);
            drawArrow(gr, Color.BLACK);
            gr.translate(-outer.screenMaxX + 99, -y);
            Line2D yAxis = new Line2D.Double(x, 0, x, outer.screenMaxY);
            gr.draw(yAxis);
            gr.translate(x, 1);
            gr.rotate(-Math.PI / 2);
            drawArrow(gr, Color.BLACK);
            gr.rotate(Math.PI / 2);
            gr.translate(-x, -1);
            for (int i = 1; (i * sf + cy) <= (int) outer.paintPanel.getBounds().getHeight(); i++) {
                Line2D l = new Line2D.Double(outer.config.originX - 2, i * sf + cy, outer.config.originX + 2, i * sf + cy);
                gr.draw(l);
            }
            for (int i = 0; (i * sf + cx) <= (int) outer.paintPanel.getBounds().getWidth() - 10; i++) {
                Line2D l = new Line2D.Double(i * sf + cx, outer.config.originY - 2, i * sf + cx, outer.config.originY + 2);
                gr.draw(l);
            }
            gr.setStroke(new BasicStroke(2));
            for (int i = 1; (outer.config.originX + i * sf * 5) <= (int) outer.paintPanel.getBounds().getWidth() - 10; i++) {
                Line2D l = new Line2D.Double(outer.config.originX + i * sf * 5, outer.config.originY - 4, outer.config.originX + i * sf * 5, outer.config.originY + 4);
                gr.draw(l);
                Integer number = new Integer(i * 5);
                gr.drawString(number.toString(), (float) (outer.config.originX + i * sf * 5), (float) outer.config.originY + 20);
            }
            for (int i = 1; (outer.config.originX - i * sf * 5) >= 0; i++) {
                Line2D l = new Line2D.Double(outer.config.originX - i * sf * 5, outer.config.originY - 4, outer.config.originX - i * sf * 5, outer.config.originY + 4);
                gr.draw(l);
                Integer number = new Integer(-i * 5);
                gr.drawString(number.toString(), (float) (outer.config.originX - i * sf * 5 - 3), (float) outer.config.originY + 20);
            }
            for (int i = 1; (outer.config.originY + i * sf * 5) <= (int) outer.paintPanel.getBounds().getHeight(); i++) {
                Line2D l = new Line2D.Double(outer.config.originX - 4, outer.config.originY + i * sf * 5, outer.config.originX + 4, outer.config.originY + i * sf * 5);
                gr.draw(l);
                Integer number = new Integer(-i * 5);
                gr.drawString(number.toString(), (float) outer.config.originX - 25, (float) (outer.config.originY + i * sf * 5) + 10);
            }
            for (int i = 1; (outer.config.originY - i * sf * 5) >= 0; i++) {
                Line2D l = new Line2D.Double(outer.config.originX - 4, outer.config.originY - i * sf * 5, outer.config.originX + 4, outer.config.originY - i * sf * 5);
                gr.draw(l);
                Integer number = new Integer(i * 5);
                gr.drawString(number.toString(), (float) outer.config.originX - 25, (float) (outer.config.originY - i * sf * 5) + 10);
            }
        }
    }

    /**
     * ny�lv�get jelen�t meg
     * @param gr
     * @param c
     */
    private void drawArrow(Graphics2D gr, Color c) {
        gr.setColor(c);
        gr.setStroke(new BasicStroke(1));
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        path.moveTo(-20, -5);
        path.lineTo(0, 0);
        path.lineTo(-20, 5);
        path.closePath();
        gr.fill(path);
        gr.draw(path);
    }

    /**
     * a megadott objektum sz�veges c�mk�j�t, felirat�t jelen�ti meg
     * @param gr
     * @param o
     */
    private void drawLabel(Graphics2D gr, GraphicalObject o) {
        Font font = new Font("Serif", Font.PLAIN, 20);
        gr.setFont(font);
        Color saveColor = gr.getColor();
        if (o instanceof Line) {
            gr.setColor(Color.BLACK);
        } else {
            gr.setColor(new Color(40, 22, 111));
        }
        gr.drawString(o.label, (int) o.labelX + (int) o.labelDX, (int) o.labelY + (int) o.labelDY);
        gr.setColor(saveColor);
    }

    /**
     * a kijel�l� poligonokat jelen�ti meg
     * @param gr
     */
    private void drawPoligons(Graphics2D gr) {

        for (int i = 0; i < outer.objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) outer.objectList.get(i);
            if (o instanceof Poligon) {
                Poligon p = (Poligon) o;
                gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
                gr.setColor(p.color);
                gr.setStroke(new BasicStroke(1));
                GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
                path.moveTo(p.getNodeX(0), p.getNodeY(0));
                for (int j = 1; j < p.creationType; j++) {
                    path.lineTo(p.getNodeX(j), p.getNodeY(j));
                }
                path.closePath();
                gr.fill(path);
                gr.draw(path);
            }
        }
    }

    /**
     * a kijel�l� t�glalapot jelen�ti meg
     * @param gr
     */
    private void drawSelectionBox(Graphics2D gr) {
        if (outer.selectionBoxOn) {
            gr.setStroke(new BasicStroke(2));
            gr.setColor(Color.CYAN);
            gr.draw(outer.selectionBox);
            gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
            gr.setPaint(Color.CYAN);
            gr.fill(outer.selectionBox);
        }
    }

    /**
     * megjelen�ti a Vektor objektumokat
     * @param gr
     * @param oo
     */
    private void drawVectors(Graphics2D gr) {
        for (int i = 0; i < outer.objectList.size(); i++) {
            GraphicalObject o = (GraphicalObject) outer.objectList.get(i);
            if (o instanceof Line & o.visible) {
                if (((Line) o).mainType == Line.VECTOR) {
                    Line oo = (Line) o;
                    if (o.selected == true) {
                        gr.setColor(Color.GREEN);
                    } else {
                        gr.setColor(Color.BLACK);
                    }
                    gr.setStroke(new BasicStroke(4));
                    Line2D l = new Line2D.Double(oo.x0, oo.y0, oo.x1, oo.y1);
                    gr.draw(l);

                    gr.setColor(Color.RED);
                    gr.setStroke(new BasicStroke(4));
                    gr.translate(oo.x0, oo.y0);
                    gr.rotate(oo.alfa);
                    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
                    path.moveTo(5, -5);
                    path.lineTo(5, 5);
                    gr.draw(path);
                    gr.rotate(-oo.alfa);
                    gr.translate(-oo.x0, -oo.y0);

                    gr.translate(oo.x1, oo.y1);
                    gr.rotate(oo.alfa);
                    drawArrow(gr, Color.RED);
                    gr.rotate(-oo.alfa);
                    gr.translate(-oo.x1, -oo.y1);
                    drawLabel(gr, oo);
                }
            }
        }
    }

    /**    
     * Elhelyezi a v�sznon az �sszes l�that� grafikus objektumot a JPanel saj�t
     * kirajzol� met�dus�nak fel�l�r�s�val.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        //Antialiasing be�ll�t�sa
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //ha nem z�rolt a kijelz�s
        if (!outer.displayLock) {
            //f�lia h�tt�rbe vet�t�se
            gr.drawImage(outer.foilImage, 0, 0, this);

            //Koordin�tatengelyek kirajzol�sa
            drawCoordinateSystem(gr);

            //K�pletek megjelen�t�se            
            drawFormulas(gr);

            //Keret kirajzol�sa a v�szon k�r�
            gr.setColor(Color.LIGHT_GRAY);
            gr.drawRect(0, 0, outer.paintPanel.getWidth() - 1, getHeight() - 1);

            //Dia szorsz�m�nak megjelen�t�se
            gr.setColor(Color.BLACK);
            Font font = new Font("Serif", Font.PLAIN, 20);
            gr.setFont(font);
            gr.drawString("Dia: " + String.valueOf(outer.actDia) + "/" + String.valueOf(outer.lastDia), outer.paintPanel.getWidth() - 100, outer.paintPanel.getHeight() - 30);

            //az al�bbi objektumok eltolva ker�lnek kirajzol�sra
            gr.translate(outer.transDX, outer.transDY);
            gr.setStroke(new BasicStroke(2));
            for (int i = 0; i < outer.objectList.size(); i++) {
                GraphicalObject o = (GraphicalObject) outer.objectList.get(i);
                if (o.visible) {
                    if (o.selected == true) {
                        gr.setColor(Color.GREEN);
                    } else {
                        gr.setColor(Color.BLACK);
                    }
                    if (o instanceof BasePoint) {
                        BasePoint oo = (BasePoint) o;
                        if (oo.creationType == BasePoint.RUNPOINT_ON_LINE) {
                        }
                        drawLabel(gr, oo);
                        if (outer.config.nodesOn) {
                            Ellipse2D e = new Ellipse2D.Double(oo.x - 5, oo.y - 5, 10, 10);
                            gr.fill(e);
                        }
                    }
                    if (o instanceof Line) {
                        if (((Line) o).mainType == Line.LINESEGMENT) {
                            Line oo = (Line) o;
                            Line2D l = new Line2D.Double(oo.x0, oo.y0, oo.x1, oo.y1);
                            gr.draw(l);
                            drawLabel(gr, oo);
                        }
                        if (((Line) o).mainType == Line.LINE) {
                            Line oo = (Line) o;
                            Line2D l = new Line2D.Double(oo.x0, oo.y0, oo.x1, oo.y1);
                            gr.draw(l);
                        }
                        if (((Line) o).mainType == Line.HALFLINE) {
                            Line oo = (Line) o;
                            Line2D l = new Line2D.Double(oo.x0, oo.y0, oo.x1, oo.y1);
                            gr.draw(l);
                            drawLabel(gr, oo);
                        }
                    }
                    if (o instanceof Circle) {
                        Circle oo = (Circle) o;
                        Ellipse2D e = new Ellipse2D.Double(oo.x0, oo.y0, 2 * oo.r, 2 * oo.r);
                        gr.draw(e);
                    }
                    if (o instanceof Ellipse) {
                        Ellipse oo = (Ellipse) o;
                        Ellipse2D e = new Ellipse2D.Double(oo.x0, oo.y0, 2 * oo.a, 2 * oo.b);
                        gr.draw(e);
                    }
                }
            }

            //vektorok megjelen�t�se
            drawVectors(gr);

            //kijel�l� poligonok megjelen�t�se
            drawPoligons(gr);

            //kijel�l� t�glalap megjelen�t�se
            drawSelectionBox(gr);
        }
    }

    /**
     * l�trehoz egy BufferedImage objektumot az aktu�lis di�b�l
     * @return
     */
    public BufferedImage makeImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        g2.dispose();
        return image;
    }
}
