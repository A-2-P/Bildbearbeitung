package bildbearbeitung;

import java.awt.Color;

public class Weichzeichenfilter extends Filter
{
    private int breite;
    private int hoehe;

    public Weichzeichenfilter(String name)
    {
        super(name);
    }
    public void anwenden(Farbbild bild)
    {
        Farbbild original = new  Farbbild(bild);
        breite = bild.getWidth();
        hoehe = bild.getHeight();

        for(int x = 0; x < breite - 1; ++x) { 
            for(int y = 0; y < hoehe -1; ++y) {
                bild.setzePunktfarbe(x, y, gibDurchschnittPixel(original, x, y));
            }
        }
    }

    private Color gibDurchschnittPixel(Farbbild bild, int x, int y) {
        short r = 0;
        short g = 0;
        short b = 0;
        short zaehler = 0;

        for(int ix = Math.max(x-1, 0); ix <= Math.min(x+1, breite-1); ++ix){
            for(int iy = Math.max(y-1, 0); iy <= Math.min(y+1, hoehe-1); ++iy){
                Color col = bild.gibPunktfarbe(ix, iy);
                r += col.getRed();
                g += col.getGreen();
                b += col.getBlue();
                zaehler++;
            }
        }
        return new Color( r/zaehler, g/zaehler, b/zaehler);
    }
}