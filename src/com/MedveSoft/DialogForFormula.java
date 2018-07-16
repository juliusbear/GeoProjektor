package com.MedveSoft;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

/**
 * A megfelel� k�plet kiv�laszt�s�t lehet�v� tev� p�rbesz�dablak.
 */
public class DialogForFormula extends JDialog implements TreeSelectionListener {

    /**
     * a fastrukt�ra objektuma
     */
    private JTree tree;
    /**
     * Ok nyom�gomb
     */
    private JButton btOk;
    /**
     * M�gse nyom�gomb
     */
    private JButton btCancel;
    /**
     * a v�lasztott nyom�gomb k�dja
     */
    int option;
    /**
     * az aktu�lisan kiv�lasztott f�jl nev�t t�rol� mez�
     */
    String actSelection = null;
    /**
     * a v�laszt�s �rv�nyess�g�t t�rol� mez�
     */
    boolean validSelection = false;
    /**
     * a fastrukt�ra objektum-hierarchi�j�t t�rol� t�mb
     */
    Object[] Hierarchy = {
        "Matematikai �sszef�gg�sek",
        "Algebra",
        "F�ggv�nytan",
        new Object[]{"Geometria",
            new Object[]{"H�romsz�gek",
                new Object[]{"�ltal�nos h�romsz�gek",
                    "H�romsz�g-egyenl�tlens�g"
                },
                new Object[]{"Der�ksz�g� h�romsz�gek",
                    "Pitagorasz t�tel"
                },},
            new Object[]{"N�gysz�gek",
                "�ltal�nos n�gysz�gek",
                "Trap�z",
                "Paralelogramma",
                "Deltoid",
                "Rombusz",
                "T�glalap",
                "N�gyzet",
                "H�rn�gysz�gek",
                "�rint�n�gysz�gek"
            }
        },
        "Kombinatorika",
        "Koordin�ta-geometria",
        "Statisztika",
        "Sorozatok",
        "Val�sz�n�s�gsz�m�t�s"
    };
    /**
     * a fastrukt�r�hoz tartoz� f�jlok �sszerendel�s�t t�rol� t�mb
     */
    Object[] fileAssociation = {
        "H�romsz�g-egyenl�tlens�g", "formula001",
        "Pitagorasz t�tel", "formula002"
    };

    /**
     * Konstruktor.
     * L�trehozza a p�rbesz�dablakot, felt�lti a tartalompanelj�t, be�ll�tja a
     * c�msor�t (title), majd az ablakot a sz�l� (parent) keret k�zep�re
     * poz�cion�lja, de m�g nem jelen�ti meg.
     * @param parent sz�l� ablak
     * @param title c�msor sz�vegez�se
     */
    public DialogForFormula(JFrame parent, String title) {
        super(parent, title, true);
        setSize(350, 300);
        setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Container content = getContentPane();

        URLClassLoader urlLoader = (URLClassLoader) DialogForErase.class.getClassLoader();
        URL url = urlLoader.findResource("icons/program.jpg");
        ImageIcon icon = new ImageIcon(url);
        this.setIconImage(icon.getImage());

        DefaultMutableTreeNode root = processHierarchy(Hierarchy);
        tree = new JTree(root);
        content.add(new JScrollPane(tree), BorderLayout.CENTER);
        JPanel pnControl = new JPanel();
        content.add(pnControl, "South");
        btOk = new JButton("OK");
        btOk.setMnemonic('O');
        pnControl.add(btOk);
        btOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (validSelection) {
                    option = JOptionPane.YES_OPTION;
                    dispose();
                }
            }
        });

        btCancel = new JButton("M�gse");
        btCancel.setMnemonic('M');
        pnControl.add(btCancel);
        btCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                option = JOptionPane.CANCEL_OPTION;
                actSelection = null;
                dispose();
            }
        });

        expandAll(tree, true);
        tree.addTreeSelectionListener(this);
    }

    /**
     * A p�rbesz�dablak megjelen�t�s�t v�gzi.
     * @return a v�laszt�s eredm�nye
     */
    public String showDialog() {
        setLocationRelativeTo(getParent());
        setVisible(true);
        if (option == JOptionPane.YES_OPTION) {
            return actSelection;
        } else {
            return null;
        }
    }

    /**
     * A f�ggv�ny rekurz�v m�don v�gighalad a megadott objektumhierarchi�n, �s
     * l�trehozza a csom�pontokat, valamint azok gyermek objektumait.
     * @param hierarchy objektum-hierarchi�t t�rol� t�mb
     * @return a hierarchia els� elem�nek csom�pont objektum�t adja vissza.
     */
    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
        DefaultMutableTreeNode child;
        for (int i = 1; i < hierarchy.length; i++) {
            Object actNode = hierarchy[i];
            //csom�pont gyermek objektumokkal
            if (actNode instanceof Object[]) {
                child = processHierarchy((Object[]) actNode);
            //lev�l objektum
            } else {
                child = new DefaultMutableTreeNode(actNode);
            }
            node.add(child);
        }
        return node;
    }

    /**
     * Kibontja/�sszez�rja a megadott fastrukt�r�t.
     * @param tree a fastrukt�ra
     * @param expand �rt�ke TRUE, ha kibont�s, FALSE, ha �sszez�r�s
     */
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        //a gy�k�relemt�l kezdj�k a kibont�st
        expandAll(tree, new TreePath(root), expand);
    }

    /**
     * A fastrukt�ra kibont�s�t v�gz� rekurz�v elj�r�s.
     * @param tree a fastrukt�ra
     * @param parent a sz�l� objektum
     * @param expand �rt�ke TRUE, ha kibont�s, FALSE, ha �sszez�r�s
     */
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        //ha van gyermek objektum
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        //ha kibont�st kell v�gezni
        if (expand) {
            tree.expandPath(parent);
        //ha �sszet�m�r�t�st kell v�gezni
        } else {
            tree.collapsePath(parent);
        }
    }

    /**
     * Amennyiben a kiv�lasztott �rt�k v�ltozik a fastrukt�r�ban, kikeresi a
     * fastrukt�ra aktu�lis elem�hez tartoz� f�jlt.
     * @param e a fastrukt�r�ban val� kiv�laszt�s esem�nye
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        validSelection = false;
        actSelection = tree.getLastSelectedPathComponent().toString();
        for (int i = 0; i <= fileAssociation.length - 1; i++) {
            if (fileAssociation[i].equals(actSelection)) {
                actSelection = (String) fileAssociation[i + 1];
                validSelection = true;
                break;
            }
        }
    }
}

