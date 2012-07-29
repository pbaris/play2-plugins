package pbaris.play.thumbs;

/**
 * Thrown when an application attempts to create a thumbnail
 * of type other than png or jpg
 *
 * @author  Panos Bariamis
 * @since	1.0.0
 */
public class UnsupportedThumbnailException extends RuntimeException {

	private static final long serialVersionUID = -4961292895069070815L;

	/**
     * Constructs a <code>UnsupportedThumbnailException</code> with no detail message.
     */
	public UnsupportedThumbnailException() {
		super();
	}
	
	/**
     * Constructs a <code>UnsupportedThumbnailException</code> with the specified 
     * detail message. 
     *
     * @param   s   the detail message.
     */
    public UnsupportedThumbnailException(String s) {
    	super(s);
    }
}