package bildbearbeitung;

import java.awt.*;
/**
 * Ein Bildfilter zum leichten abdunkeln eines Bildes.
 */
public class Abdunkelfilter extends Pixelfilter
{

    public Abdunkelfilter(String name)
    {
        super(name);
    }

   public Color spezifischerFarbfilter(Color farbe)
    {
       return farbe.darker();
    }
}
