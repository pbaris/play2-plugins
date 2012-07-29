package pbaris.play.thumbs;

import static pbaris.play.thumbs.ThumbsPlugin.getCacheFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import play.Logger;
import play.api.libs.MimeTypes;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import com.google.common.io.Files;

/**
 * TODO documentation
 * 
 * @author	Panos Bariamis
 * @since	1.0.0
 */
public class Thumbnail {
	
	public static Result ok() {
		ImageInfo ii = new ImageInfo(Http.Context.current().request());
		return Results.ok(createThumbnail(ii)).as(MimeTypes.forExtension(ii.getType()).get());
	}
	
	private static InputStream createThumbnail(ImageInfo ii) {
		InputStream is = null;
		try {
			clearCache();
			File fileDst = ii.getThumbnailFile();
			if (!fileDst.exists()) { //TODO check the modified date
				ImageScaler.scale(ImageIO.read(new URL(ii.getSourceURL())), ii);
			}
			
			is = new FileInputStream(fileDst);
			
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		} 
		
		//TODO create no image
		
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
