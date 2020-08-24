package bildbearbeitung;

import java.awt.*;

public class Negativfilter extends Pixelfilter
{
    
    public Negativfilter(String name){
     super(name);   
    }
    
    public Color spezifischerFarbfilter(Color farbe){
        Color negativ = new Color(255-farbe.getRed(), 255 - farbe.getGreen(), 255-farbe.getBlue());
        return negativ;
    }
  
    
}
