package pbaris.play.thumbs;

import static pbaris.play.thumbs.ThumbsPlugin.getBasePath;
import static pbaris.play.thumbs.ThumbsPlugin.getCacheFolder;
import static pbaris.play.thumbs.ThumbsPlugin.isRelative;

import java.io.File;
import java.net.URL;
import java.util.Map;

import play.mvc.Http.Request;

/**
 * A helper class that gives info about the source image
 * and the thumbnail from the {@link play.mvc.Http.Request}.
 * 
 * <p>It gives info about the source image URL, the file
 * and the type of the thumbnail to be generated. This info 
 * is based on the {@link play.mvc.Http.Request} analysis.
 * 
 * @author	Panos Bariamis
 * @since	0.0.1
 */
public class ImageInfo {
	private String 	
		name = "", 
		regex = "^(?:https?|ftps?|file)://.*$",
		
		sourceURL, type, suffix, filename, src;
	
	private File thumbnailFile, noImageFile;
	
	private Integer size, width, height;
	private Boolean frame;
	
	/**
	 * It takes a {@link play.mvc.Http.Request} and analyzes
	 * the parameters (src, size, width, height, frame, type) 
	 * to to which the generated thumbnail based  
	 * 
	 * @param	request
	 * 			The {@link play.mvc.Http.Request} to analyze
	 * 
	 * @throws	NullPointerException
	 * 			If the src parameter is omitted or if request wants a frame 
	 * 			thumbnail but width or height parameter are omitted.
	 * 
	 * @throws	UnsupportedThumbnailException
	 * 			if type parameter is other than png or jpg
	 */
	public ImageInfo(Request request) {
		super();
		
		Map<String, String[]> qs = request.queryString();
		
		try {
			src = qs.get("src")[0].trim();
			int pos = src.lastIndexOf("/");
			name = pos == -1 ? src : src.substring(pos + 1);
			if (name.startsWith("/")) name = name.substring(1);
			
		} catch (Exception e) {
			throw new NullPointerException("src parameter is required");
		}
			
		try { size = Integer.parseInt(qs.get("size")[0]); } catch (Exception e) {}
		try { width = Integer.parseInt(qs.get("width")[0]); } catch (Exception e) {}
		try { height = Integer.parseInt(qs.get("height")[0]); } catch (Exception e) {}
		
		try {
			frame = new Boolean(qs.get("frame")[0]);
			if (frame != null && frame && (width == null || height == null))
				throw new NullPointerException("frame parameter requires both width and height parameters");
		} catch (Exception e) {}
		
		try { type = qs.get("type")[0].toLowerCase(); } catch (Exception e) {
			type = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
		}
		if (type == null || (!type.equals("jpg") && !type.equals("png"))) {
			throw new UnsupportedThumbnailException("thumbnail can be of type png or jpg only");
		}
	}
	
	/**
	 * Analyzes the src parameter to generate the source image URL.
	 * 
	 * <p>It checks if the src is already a valid URL and if it's not, 
	 * generates a valid URL based on the configuration of the plugin.
	 * 
	 * @return	the URL of the source image	
	 */
	public String getSourceURL() {
		if (sourceURL == null) {
			if (src.matches(regex)) {
				sourceURL = src;
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("file://")
					.append(isRelative() ? getBasePath() : "")
					.append(src.startsWith("/") ? "" : "/")
					.append(src);
				
				sourceURL = sb.toString().trim();
			}
		}
		
		return sourceURL;
	}
	
	/**
	 * Analyzes the src parameter to generate the file in which to store the thumbnail.
	 * 
	 * @return	the thumbnail file
	 */
	public File getThumbnailFile() {
		if (thumbnailFile == null) {
			if (src.matches(regex)) {
				int pos = src.lastIndexOf("/");
				String s = src.substring(0, pos + 1);
				
				pos = s.indexOf("://");
				s = s.substring(pos + 3);
				
				pos = s.indexOf("/");
				s = pos == -1 ? s : s.substring(pos + 1);
				
				File f = new File(getCacheFolder(), s);
				if (!f.exists()) f.mkdirs();
				
				thumbnailFile = new File(f, getFilename());
			
			} else {
				int pos = src.lastIndexOf("/");
				String s = src.substring(0, pos + 1);
				
				File f = new File(getCacheFolder(), s);
				if (!f.exists()) f.mkdirs();
				
				thumbnailFile = new File(f, getFilename());
			}
		}
		
		return thumbnailFile;
	}
	
	/**
	 * It generates a file that corresponds to the no-image file for the given parameters.
	 * If the method been called, the type of the generated (no) image becomes jpg.
	 * 
	 * @return	The no image file
	 */
	public File getNoImageFile() {
		if (noImageFile == null) {
			noImageFile = new File(getCacheFolder(), "noimage" + getSuffix() + ".jpg");
			type = "jpg";
		}
		
		return noImageFile;
	}
	
	/**
	 * Generates a filename for the thumbnail based on the source image file name
	 * and the {@link play.mvc.Http.Request} parameters
	 * 
	 * @return	The thumbnail filename
	 */
	public String getFilename() {
		if (filename == null) {
			filename = name.substring(0, name.lastIndexOf(".")) + getSuffix() + "." + type;
		}
		return filename;
	}
	
	/**
	 * Analyzes all the parameters to generate a suffix for the thumbnail.
	 * 
	 * @return	The thumbnail suffix
	 */
	private String getSuffix() {
		if (suffix == null) {
			suffix = "_";
			if (frame != null && frame) {
				suffix += "f";
			}
			if (size != null) {
				suffix += "s" + size;
			} else if (width != null && height != null) {
				suffix += width + "x" + height;
			} else if (width != null && height == null) {
				suffix += "w" + width;
			} else if (width == null && height != null) {
				suffix += "h" + height;
			} else {
				suffix += "d100";
			}
		}
		
		return suffix;
	}
	
	public boolean needsCreate() {
		File fileDst = getThumbnailFile();
		
		if (!fileDst.exists()) 
			return true;

		if (!getSourceURL().startsWith("file://"))
			return true;
		
		try {
			File fileSrc = new File(new URL(getSourceURL()).toURI());
			return fileSrc.lastModified() > fileDst.lastModified();
		} catch (Exception e) {}
		
		return true;
	}
	
	/**
	 * The type of the thumbnail is based on type parameter or the extension
	 * of the source file (src parameter). Valid type are png and jpg.
	 * 
	 * @return	The type of the thumbnail
	 */
	public String getType() {
		return type;
	}
	
	protected void setType(String type) {
		this.type = type;
	}
	
	public Boolean getFrame() {
		return frame;
	}
	
	public Integer getSize() {
		return size;
	}
	
	/**
	 * @return	The width of the thumbnail or null if not
	 * 			specified in the {@link play.mvc.Http.Request}
	 */
	public Integer getWidth() {
		return width;
	}
	
	/**
	 * @return	The height of the thumbnail or null if not
	 * 			specified in the {@link play.mvc.Http.Request}
	 */
	public Integer getHeight() {
		return height;
	}
}
