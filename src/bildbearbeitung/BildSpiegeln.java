package bildbearbeitung;

import java.awt.*;
/**
 * Ein Bildfilter zum spiegeln eines Bildes.
 */
public class BildSpiegeln extends Filter
{
    public BildSpiegeln(String name){
        super(name);
    }

    public void anwenden(Farbbild bild){
        int hoehe = bild.getHeight();
        int breite = bild.getWidth();
        for(int y = 0; y < hoehe; y++) {
            for(int x = 0; x < breite/2; x++) {
                Color colLast = bild.gibPunktfarbe(breite-x-1, y);
                bild.setzePunktfarbe(breite-x-1,y, bild.gibPunktfarbe(x, y));
                bild.setzePunktfarbe(x,y, colLast);
            }
        }
    }

}
