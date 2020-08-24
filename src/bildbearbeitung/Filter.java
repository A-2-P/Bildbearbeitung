package bildbearbeitung;

/*
 * Abstrakte Superklasse für alle Bildfilter dieser Anwendung. 
 */
public abstract class Filter
{
    private String name;

    /*
     * Erzeuge einen neuen Filter mit dem angegebenen Namen.
     */
    public Filter(String name)
    {
        this.name = name;
    }
    
    /*
     * Liefere den Namen dieses Filters.
     */
    public String gibName()
    {
        return name;
    }
    
    public abstract void anwenden(Farbbild bild);
}
