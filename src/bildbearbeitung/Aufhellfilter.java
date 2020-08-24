package bildbearbeitung;

import java.awt.*;
/**
 * Ein Bildfilter zum leichten aufhellen eines Bildes.
 */
public class Aufhellfilter extends Pixelfilter
{

    public Aufhellfilter(String name)
    {
        super(name);
    }

    public Color spezifischerFarbfilter(Color farbe)
    {
       return farbe.brighter();
    }

}
