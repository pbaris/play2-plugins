package pbaris.play.thumbs;

import static pbaris.play.thumbs.ThumbsPlugin.getCacheFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import play.mvc.Http.Request;

import com.google.common.io.Files;

/**
 * TODO documentation
 * 
 * @author	Panos Bariamis
 * @since	1.0.0
 */
//http://pbaris.files.wordpress.com/2012/05/tux_pao.jpg
//http://creteotel.gr/logo.jpg
//http://www.bookvillasingreece.com/villas_files/cyclades/santorini/001/20082251011.jpg
public class Thumbnail {
	public static InputStream createThumbnail(Request request) {
		InputStream is = null;
		
		ImageInfo ii = new ImageInfo(request);
		
		try {
			clearCache();
			
			File fileDst = ii.getThumbnailFile();
			if (!fileDst.exists()) { //TODO check the modified date
				ImageScaler.scale(ImageIO.read(new URL(ii.getSourceURL())), ii);
				is = new FileInputStream(fileDst);
			}
			
		} catch (MalformedURLException e) {
		} catch (IOException e) {} 
		
		return is;
	}
	
	@SuppressWarnings("deprecation")
	private static void clearCache() {
		File cacheFile = new File(getCacheFolder(), ".__play2_clear_thumbs_cache__");
		try {
			Date today = new Date();
			Date date = new Date(cacheFile.lastModified());
				
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			date = cal.getTime();
				
			if (today.after(date) || today.equals(date)) {
				Files.deleteDirectoryContents(getCacheFolder());
				cacheFile.createNewFile();
			}
		} catch (Exception e) {
			try {
				Files.deleteDirectoryContents(getCacheFolder());
				cacheFile.createNewFile();
			} catch (IOException e1) {}
		}
	}
}
