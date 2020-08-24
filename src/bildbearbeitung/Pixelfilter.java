package bildbearbeitung;

import java.awt.*;

public abstract class Pixelfilter extends Filter
{

    public Pixelfilter(String name)
    {
        super(name);
    }

    public void anwenden(Farbbild bild){
        int hoehe = bild.getHeight();
        int breite = bild.getWidth();

        for(int y = 0; y < hoehe; y++) {
            for(int x = 0; x < breite; x++) {
                bild.setzePunktfarbe(x, y, spezifischerFarbfilter(bild.gibPunktfarbe(x,y)));
            }
        }
    }

    public abstract Color spezifischerFarbfilter(Color farbe);
}
