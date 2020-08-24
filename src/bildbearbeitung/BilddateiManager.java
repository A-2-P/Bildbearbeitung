package bildbearbeitung;

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

/**
 * BilddateiManager - zum Laden und Speichern von Bildern.
 */
public class BilddateiManager
{
    // Zulässige Formate 
    private static final String BILDFORMAT = "jpg";
    
    /**
     * Lies eine Bilddatei ein und liefere sie als ein Bild zur�ck.
     * Diese Methode kann Dateien im JPG- und im PNG-Format lesen.
     * Bei Problemen (etwa, wenn die Datei nicht existiert oder ein nicht
     * lesbares Format hat oder es einen sonstigen Lesefehler gibt)
     * liefert diese Methode null.
     * 
     * @param bilddatei  Die zu ladende Bilddatei.
     * @return           Das Bild-Objekt oder null, falls die Datei nicht
     *                      lesbar ist.
     */
    public static Farbbild ladeBild(File bilddatei)
    {
        try {
            BufferedImage bild = ImageIO.read(bilddatei);
            if(bild == null || (bild.getWidth(null) < 0)) {
                // Bild konnte nicht geladen werden - vermutlich falsches Format
                return null;
            }
            return new Farbbild(bild);
        }
        catch(IOException exc) {
            return null;
        }
    }

    /**
     * Schreibe das gegebene Bild in eine Bilddatei im JPG-Format.
     */
    public static void speichereBild(Farbbild bild, File datei)
    {
        try {
            ImageIO.write(bild, BILDFORMAT, datei);
        }
        catch(IOException exc) {
            return;
        }
    }
}
