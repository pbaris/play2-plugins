package pbaris.play.thumbs;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * Supports only png and jpg
 * 
 * @author Panos Bariamis
 */
public class Thumbnail {
	public static File createThumbnail() {
		
		try {
			URL url = new URL("http://www.paobc.gr/image.ashx?fid=5052");
			File f = new File(url.toURI());
			return f;
		} catch (MalformedURLException e) {
		} catch (URISyntaxException e) {
		}
		
		return null;
	}
}
