package bildbearbeitung;

import java.awt.Color;

public class Graustufenfilter extends Pixelfilter
{

    public Graustufenfilter(String name)
    {
        super(name);
    }
    
    public Color spezifischerFarbfilter(Color col){
        //Berechnung des Durchschnittswertes für die Graustufe auf Pixelebene
        int graustufe= (col.getRed() + col.getBlue() + col.getGreen())/3;
        Color grau = new Color(graustufe, graustufe, graustufe);
        return grau;
    }
}
