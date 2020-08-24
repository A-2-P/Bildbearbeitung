package bildbearbeitung;

import java.awt.Color;

/**
 * Ein dreistufiger (Schwarz-Grau-Weiß) Schwellwertfilter.
 * 
 */
public class Schwellwertfilter extends Pixelfilter
{

    public Schwellwertfilter(String name)
    {
        super(name);
    }

    public Color spezifischerFarbfilter(Color farbe)
    {
      
                int helligkeit = (farbe.getRed() + farbe.getBlue() + farbe.getGreen()) / 3;
                if(helligkeit <= 85) {
                    return Color.BLACK;
                }
                else if(helligkeit <= 170) {
                    return Color.GRAY;
                }
                else {
                    return Color.WHITE;
                }
            }
        }
   
