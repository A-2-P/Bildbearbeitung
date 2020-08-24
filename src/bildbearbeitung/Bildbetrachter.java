package bildbearbeitung;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Bildbetrachter
{
    // static DF
    private static final String VERSION = "Version 2.0";
    private static JFileChooser dateiauswahldialog = new JFileChooser(System.getProperty("user.dir"));

    // Datenfelder
    private JFrame fenster;
    private Bildflaeche bildflaeche;
    private JLabel dateinameLabel;
    private JLabel statusLabel;
    private Farbbild aktuellesBild;
    ArrayList<Farbbild> undoBildListe,reDoBildListe;
    int sizeOfArrayList;

    private List<Filter> filterliste;

    private ArrayList<JComponent> deaktivierbareKomponenten;
    
    
    public Bildbetrachter()
    {
        deaktivierbareKomponenten = new ArrayList<>();
        undoBildListe = new ArrayList<>();              //implizit-diamond notation
        reDoBildListe = new ArrayList<Farbbild>(); 
        aktuellesBild = null;
        filterliste = filterlisteErzeugen();
        fensterErzeugen();
        anzeigeElementeDeaktivieren();
        
    }

    // ---- Implementierung der Menü-Funktionen ----

    private void dateiOeffnen()
    {
        int ergebnis = dateiauswahldialog.showOpenDialog(fenster);

        if(ergebnis != JFileChooser.APPROVE_OPTION) { // abgebrochen
            return;  
        }
        File selektierteDatei = dateiauswahldialog.getSelectedFile();
        aktuellesBild = BilddateiManager.ladeBild(selektierteDatei);

        if(aktuellesBild == null) {   // Bilddatei nicht im gültigen Format
            JOptionPane.showMessageDialog(fenster,
                "Die Datei hat keines der unterstützten Formate.",
                "Fehler beim Bildladen",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        bildflaeche.setzeBild(aktuellesBild);
        dateinameAnzeigen(selektierteDatei.getPath());
        statusAnzeigen("Datei geladen.");
        anzeigeElementeAktivieren();
        fenster.pack();
        makeBackup();
    }

    /**
     * 'Schliessen'-Funktion: Schließt das aktuelle Bild.
     */
    private void schliessen()
    {
        aktuellesBild = null;
        bildflaeche.loeschen();
        dateinameAnzeigen(null);
        anzeigeElementeDeaktivieren();
    }

    private void beenden()
    {
        System.exit(0);
    }
    //Optionen sind nicht verfügbar, wenn kein Bild geladen
    private void anzeigeElementeDeaktivieren()
    {
        for(JComponent c : deaktivierbareKomponenten){
            c.setEnabled(false);
        }
    }

    private void anzeigeElementeAktivieren()
    {
        for(JComponent c : deaktivierbareKomponenten){
            c.setEnabled(true);
        }
    }

    /**
     * Wende den gegebenen Filter auf das aktuelle Bild an.
     */
    private void filterAnwenden(Filter filter)
    {
        if(aktuellesBild != null) {
            makeBackup();
            filter.anwenden(aktuellesBild);
            fenster.repaint();
            statusAnzeigen("Angewendet: " + filter.gibName());
        }
        else {
            statusAnzeigen("Kein Bild geladen.");
        }
    }

    /**
     * 'Info'-Funktion: Zeige Informationen zur Anwendung.
     */
    private void zeigeInfo()
    {
        JOptionPane.showMessageDialog(fenster, 
            "Bildbearbeitung\n" + VERSION + "\nAuthor: Anel Pjanic",
            "Allgemeine Infos.",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ---- Hilfsmethoden ----
    /**
     * Zeigt den Dateinamen des aktuellen Bildes auf dem Label für den
     * Dateinamen.
     */
    private void dateinameAnzeigen(String dateiname)
    {
        if(dateiname == null) {
            dateinameLabel.setText("Keine Datei angezeigt.");
        }
        else {
            dateinameLabel.setText("Datei: " + dateiname);
        }
    }

    /**
     * Zeige den gegebenen Text in der Statuszeile am unteren
     * Rand des Fensters.
     */
    private void statusAnzeigen(String text)
    {
        statusLabel.setText(text);
    }

    /**
     * Erzeuge und liefere eine Liste mit allen bekannten Filtern.
     * @return die Liste der Filter.
     */
    private List<Filter> filterlisteErzeugen()
    {
        List<Filter> filterliste = new ArrayList<Filter>();
        filterliste.add(new Abdunkelfilter("Dunkler"));
        filterliste.add(new Aufhellfilter("Heller"));
        filterliste.add(new Schwellwertfilter("Schwellwert"));
        filterliste.add(new Graustufenfilter("Graustufe"));
        filterliste.add(new Negativfilter("Negativ"));
        filterliste.add(new BildSpiegeln("Bild spiegeln"));
        filterliste.add(new Weichzeichenfilter("Weichzeichnen"));
        filterliste.add(new Solarisationsfilter("Solarisationsfilter"));
        filterliste.add(new Kantenerkennungsfilter("Kantenerkennung"));
        return filterliste;
    }

    // ---- Swing-Anteil zum Erzeugen des Fensters mit allen Komponenten ----

    private void fensterErzeugen()
    {
        fenster = new JFrame("Bildbetrachter");
        Container contentPane = fenster.getContentPane();

        menuezeileErzeugen(fenster);

        // Ein Layout mit geeigneten Abständen definieren
        contentPane.setLayout(new BorderLayout(6, 6));

        // Die Bildfläche in der Mitte erzeugen
        bildflaeche = new Bildflaeche();
        contentPane.add(bildflaeche, BorderLayout.CENTER);

        // Zwei Label oben und unten für den Dateinamen und Statusmeldungen
        dateinameLabel = new JLabel();
        contentPane.add(dateinameLabel, BorderLayout.NORTH);

        statusLabel = new JLabel(VERSION);
        contentPane.add(statusLabel, BorderLayout.SOUTH);

        // werkzeugleiste
        JPanel werkzeugleiste = new JPanel();    // deault: flow-layout
        JPanel werkzeugPanel = new JPanel();
        werkzeugPanel.setLayout(new GridLayout(0,1));

        JButton buttonVergroessern = new JButton("+");        
        buttonVergroessern.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    bildVergroessern();
                }
            });
        werkzeugPanel.add(buttonVergroessern);
        deaktivierbareKomponenten.add(buttonVergroessern);

        JButton buttonVerkleinern = new JButton("-");
        buttonVerkleinern.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    bildVerkleinern();
                }
            });
        werkzeugPanel.add(buttonVerkleinern);
        deaktivierbareKomponenten.add(buttonVerkleinern);

        JButton buttonUndo = new JButton("<<");
        buttonUndo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    undo();
                }
            });
        werkzeugPanel.add(buttonUndo);
        deaktivierbareKomponenten.add(buttonUndo);
        
        JButton buttonSpeichern = new JButton("Speichern");
        buttonSpeichern.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    speichern();
                }

            private void speichern() {
                File outputfile = new File("image.jpg");
                    try {
                        ImageIO.write(aktuellesBild, "jpg", outputfile);
                        fenster.pack();
                    } catch (IOException ex) {
                        throw new UnsupportedOperationException("Not supported yet."); 
                    }
            }
            });
        werkzeugPanel.add(buttonSpeichern);
        deaktivierbareKomponenten.add(buttonSpeichern);

        werkzeugleiste.add(werkzeugPanel);  
        contentPane.add(werkzeugleiste, BorderLayout.WEST);

        // Aufbau abgeschlossen - Komponenten arrangieren lassen
        dateinameAnzeigen(null);
        fenster.pack();
        fenster.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        // Das Fenster in der Mitte des Bildschirms platzieren und anzeigen
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        //fenster.setLocation(d.width/2 - fenster.getWidth()/2, d.height/2 - fenster.getHeight()/2);
        fenster.setLocation(0, 0);
        fenster.setVisible(true);
        fenster.setResizable(true);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void bildVerkleinern()
    {
        //System.out.println("verkleinern");
        makeBackup();
        int faktor = 2;
        int breite = aktuellesBild.getWidth();
        int hoehe = aktuellesBild.getHeight();
        Farbbild neuesBild = new Farbbild(breite / faktor, hoehe / faktor);

        for(int y = 0; y < hoehe -1; y += 2){           // -1 wegen Problemenen mit ungerader Breite
            for(int x = 0; x < breite -1; x += 2) {
                Color c = aktuellesBild.gibPunktfarbe(x, y);
                neuesBild.setzePunktfarbe(x / faktor, y / faktor, c);
            }
        }

        aktuellesBild = neuesBild;
        bildflaeche.setzeBild(aktuellesBild);
        fenster.pack();
    }

    public void bildVergroessern()
    {
        //System.out.println("vergrößern");
        makeBackup();
        int faktor = 2;
        int breite = aktuellesBild.getWidth() * faktor;
        int hoehe = aktuellesBild.getHeight() * faktor;
        Farbbild neuesBild = new Farbbild(breite, hoehe);

        for(int y = 0; y < hoehe; ++y){
            for(int x = 0; x < breite; ++x) {
                Color c = aktuellesBild.gibPunktfarbe(x / faktor, y / faktor);
                neuesBild.setzePunktfarbe(x, y, c);
            }
        }

        aktuellesBild = neuesBild;
        bildflaeche.setzeBild(aktuellesBild);
        fenster.pack();
    }

    /**
     * Die Menüzeile des Hauptfensters erzeugen.
     */
    private void menuezeileErzeugen(JFrame fenster)
    {
        final int SHORTCUT_MASK =
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuBar menuezeile = new JMenuBar();
        fenster.setJMenuBar(menuezeile);

        JMenu menue;
        JMenuItem eintrag;

        //Das Datei-Menü erzeugen
        menue = new JMenu("Datei");
        menuezeile.add(menue);

        eintrag = new JMenuItem("Bild öffnen...");
        eintrag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
        eintrag.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { dateiOeffnen(); }
            });
        menue.add(eintrag);

        eintrag = new JMenuItem("Bild schließen...");
        eintrag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
        eintrag.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { schliessen(); }
            });
        menue.add(eintrag);
        menue.addSeparator();
        deaktivierbareKomponenten.add(eintrag);

        eintrag = new JMenuItem("Beenden");
        eintrag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        eintrag.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { beenden(); }
            });
        menue.add(eintrag);

        // Das Filter-Menü erzeugen
        menue = new JMenu("Filter");
        menuezeile.add(menue);

        for(final Filter filter : filterliste) {
            eintrag = new JMenuItem(filter.gibName());
            eintrag.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) { 
                        filterAnwenden(filter);
                    }
                });
            menue.add(eintrag);
            deaktivierbareKomponenten.add(eintrag);
        }

        // Das Hilfe-Menü erzeugen
        menue = new JMenu("Hilfe");
        menuezeile.add(menue);

        eintrag = new JMenuItem("Info");
        eintrag.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { zeigeInfo(); }
            });
        menue.add(eintrag);

    }
    
    private void makeBackup(){
        undoBildListe.add(new Farbbild(aktuellesBild));
        sizeOfArrayList = undoBildListe.size();
    }
    
    private void undo(){
        if(sizeOfArrayList>0){
            sizeOfArrayList -= 1;
            aktuellesBild = undoBildListe.get(sizeOfArrayList);
            bildflaeche.setzeBild(aktuellesBild);
            //Entfernen der Elemente aus der Liste und hinzufügen in eine andere Liste,
            //für einen weiteren Vorwärtsbutton - ist jedoch nicht implementiert.
            reDoBildListe.add(undoBildListe.remove(sizeOfArrayList));

        }else{
            aktuellesBild = undoBildListe.get(0);
            bildflaeche.setzeBild(aktuellesBild);
        }
        fenster.pack();
        
    }
    
}
