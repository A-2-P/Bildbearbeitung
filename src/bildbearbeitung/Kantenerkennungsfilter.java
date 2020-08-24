package bildbearbeitung;

import java.awt.Color;

public class Kantenerkennungsfilter extends Filter
{
    private int breite;
    private int hoehe;

    public Kantenerkennungsfilter(String name)
    {
        super(name);
    }

    /**
     * Berechne für jedes Pixel den Durchschnittswert der Rot- Grün- und Blau-Anteile aus diesem
     * und allen direkt benachbarten Pixeln.
     * Setze die Farbe des Pixels auf den Durchschnittswert
     */
    public void anwenden(Farbbild bild)
    {
        Farbbild original = new  Farbbild(bild);
        breite = bild.getWidth();
        hoehe = bild.getHeight();

        for(int x = 0; x < breite - 1; ++x) { 
            for(int y = 0; y < hoehe -1; ++y) {
                bild.setzePunktfarbe(x, y, gibDifferenzFarbe(original, x, y));
            }
        }
    }

    private Color gibDifferenzFarbe(Farbbild bild, int x, int y) {
        //variables for min and max for every color(rgb)
        //set min to 256 -> invalid value
        int r_max = 0,r_min = 256;        
        int g_max = 0,g_min = 256;        
        int b_max = 0,b_min = 256;
        
        
        for(int ix = Math.max(x-1, 0); ix <= Math.min(x+1, breite-1); ++ix){
            for(int iy = Math.max(y-1, 0); iy <= Math.min(y+1, hoehe-1); ++iy){
                Color col = bild.gibPunktfarbe(ix, iy);
                
                //set the max and mins of every color
                r_max = Math.max(r_max, col.getRed());
                r_min = Math.min(r_min, col.getRed());
               
                g_max = Math.max(g_max, col.getGreen());
                g_min = Math.min(g_min, col.getGreen());
                
                b_max = Math.max(b_max, col.getBlue());
                b_min = Math.min(b_min, col.getBlue());
                
               
            }
        }
        
        return new Color(r_max-r_min, g_max-g_min, b_max-b_min);
    }
}