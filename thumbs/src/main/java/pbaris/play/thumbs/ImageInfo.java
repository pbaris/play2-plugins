package pbaris.play.thumbs;

/**
 * @author Panos Bariamis
 */
public class ImageInfo {
	// info
	private String name, fileType;
	private Integer size, width, height;
	private Boolean frame;
	private boolean noImage;
	
	//internal
	private String filename;
	
	public ImageInfo(String name, String fileType, Integer size, Integer width, Integer height, Boolean frame, boolean noImage) {
		super();
		this.name = name;
		this.fileType = fileType;
		this.size = size;
		this.width = width;
		this.height = height;
		this.frame = frame;
		this.noImage = noImage;
	}

	public String getFilename() {
		if (filename == null) {
			String suffix = "_";
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
			
			int pos = name.lastIndexOf(".");
			filename = name.substring(0, pos) + suffix;
			if (frame != null && frame && !noImage) {
				filename += fileType.equals("png") ? ".png" : ".jpg";
			} else {
				filename += name.substring(pos);
			}
		}
		return filename;  
	}
}
