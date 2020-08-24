package bildbearbeitung;

import java.awt.*;

public class Solarisationsfilter extends Pixelfilter
{ 

    public Solarisationsfilter(String name)
    {
        super(name);
    }

    public Color spezifischerFarbfilter(Color farbe)
    {
       //array for the red, green and blue values
       int[] rgb = {farbe.getRed(), farbe.getGreen(), farbe.getBlue()};
 
       //loop over the array
       for(int i=0; i < rgb.length; i++){
           
           //if the value at position i lower than 128 -> change the value
           if(rgb[i]<128){
               rgb[i] = 255- rgb[i];
            }
           
        }
       
        //return new color
       return new Color(rgb[0], rgb[1], rgb[2]);
    }
}
