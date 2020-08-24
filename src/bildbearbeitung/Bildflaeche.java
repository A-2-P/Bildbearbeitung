package bildbearbeitung;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class Bildflaeche extends JComponent
{
    private static final long serialVersionUID = 20060330L;

    private int breite, hoehe;
    private Farbbild bild;

    public Bildflaeche()
    {
        breite = 480;    // beliebige Größe
        hoehe = 380;
        bild = null;
    }

    public void setzeBild(Farbbild bild)
    {
        if(bild != null) {
            breite = bild.getWidth();
            hoehe = bild.getHeight();
            this.bild = bild;
            repaint();
        }
    }
    
    public void loeschen()
    {
        Graphics bildGraphics = bild.getGraphics();
        bildGraphics.setColor(Color.LIGHT_GRAY);
        bildGraphics.fillRect(0, 0, breite, hoehe);
        repaint();
    }
    
    public Dimension getPreferredSize()
    {
        return new Dimension(breite, hoehe);
    }

    public void paintComponent(Graphics g)
    {
        Dimension size = getSize();
        g.clearRect(0, 0, size.width, size.height);
        if(bild != null) {
            g.drawImage(bild, 0, 0, null);
        }
    }
}
