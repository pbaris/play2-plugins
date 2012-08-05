package pbaris.play.thumbs;

import static pbaris.play.thumbs.ThumbsPlugin.getCacheFolder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.api.libs.MimeTypes;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

/**
 * TODO documentation
 * 
 * @author	Panos Bariamis
 * @since	1.0.0
 */
public class Thumbnail extends Controller {
	
	public static Result thumb() {
		ImageInfo ii = new ImageInfo(request());
		return Results.ok(createThumbnail(ii)).as(MimeTypes.forExtension(ii.getType()).get());
	}
	
	private static InputStream createThumbnail(ImageInfo ii) {
		clearCache();

		InputStream is = null;
		try {
			if (ii.needsCreate()) {
				ImageScaler.scale(ImageIO.read(new URL(ii.getSourceURL())), ii);
			}
			
			is = new FileInputStream(ii.getThumbnailFile());
		} catch (Exception e) {} 
		
		if (is == null) {			
			try {
				File fileDst = ii.getNoImageFile();
				if (!fileDst.exists()) createNoImage(ii);
				is = new FileInputStream(fileDst);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}
		
		return is;
	}
	
	private static void createNoImage(ImageInfo ii) throws IOException {
		Integer width = ii.getWidth(),
				height = ii.getHeight();
		
		width = width != null ? width : (height != null ? height : 100);
		height = height != null ? height : (width != null ? width : 100);
		
		Color c1 = new Color(0xffcccccc),
			  c2 = new Color(0xfff6f6f6);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int cols = Math.round(1f * (width / 8)),
			rows = Math.round(1f * (height / 8));
		
		for (int y=0; y<rows; y++) {
			for (int x=0; x<cols; x++) {			
				g.setColor(((x + y) % 2 == 0) ? c1 : c2);
				g.fillRect(x*8, y*8, 8, 8);		
			}
		}
		g.dispose();
		
		ImageIO.write(image, ii.getType(), ii.getNoImageFile());
	}
	
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
				FileUtils.cleanDirectory(getCacheFolder());
				cacheFile.createNewFile();
			}
		} catch (Exception e) {
			try {
				FileUtils.cleanDirectory(getCacheFolder());
				cacheFile.createNewFile();
			} catch (IOException e1) {}
		}
	}
}
