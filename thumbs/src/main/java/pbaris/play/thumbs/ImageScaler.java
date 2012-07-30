package pbaris.play.thumbs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.BooleanUtils;

/**
 * @author	Panos Bariamis
 * @since	0.0.1
 */
public class ImageScaler {
	
	private ImageInfo ii;
	private BufferedImage image;
	
	private ImageScaler(BufferedImage image, ImageInfo ii) {
		this.image = image;
		this.ii = ii;
	}
	
	public static void scale(BufferedImage image, ImageInfo ii) {
		ImageScaler scaler = new ImageScaler(image, ii);
		scaler.scale();
	}

	private void scale() {
		double 	iw = image.getWidth(),
				ih = image.getHeight(),
				sw = 0,
				sh = 0;
		
		boolean proceed = false;
		
		Integer width = ii.getWidth(), 
				height = ii.getHeight(), 
				size;
		
		if (BooleanUtils.isTrue(ii.getFrame())) {
			size = Math.min(ii.getWidth(), ii.getHeight());
			
			if (size == ii.getHeight()) {
				proceed = true;
				sh = size;
				sw = (sh / ih) * iw;
				
				if (sw > ii.getWidth()) {
					sw = ii.getWidth();
					sh = (sw / iw) * ih;
				}
				
			} else {
				proceed = true;
				sw = size;
				sh = (sw / iw) * ih;
				
				if (sh > ii.getHeight()) {
					sh = ii.getHeight();
					sw = (sh / ih) * iw;
				}
			}
			
			if (proceed) image = getScaledImage((int)sw, (int)sh);
			
			int x = (width - image.getWidth()) / 2;
			int y = (height - image.getHeight()) / 2;
			
			boolean isPNG = ii.getType().equals("png");
			
			BufferedImage frameImage = new BufferedImage(width, height, isPNG ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
			Graphics2D g = frameImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
			g.dispose();
			
			try {
				ImageIO.write(frameImage, ii.getType(), ii.getThumbnailFile());
			} catch (IOException e) {}
			
		} else { 
			size = ii.getSize();
			
			//Size specified. Auto scale
			if (size != null) {
				if (iw > ih && size < iw) {
					proceed = true;
					sw = size;
					sh = (sw / iw) * ih;
				} else if (size < ih) {
					proceed = true;
					sh = size;
					sw = (sh / ih) * iw;
				}
				
			//Both width and height specified from user
			} else if (width != null && height != null) { 
				if (width >= iw && height < ih) { 			//Only height are smaller than original image, scale height
					proceed = true;
					sh = height;
					sw = (sh / ih) * iw;
				} else if (width < iw && height >= ih) { 	//Only width are smaller than original image, scale width
					proceed = true;
					sw = width;
					sh = (sw / iw) * ih;
				} else if (width < iw && height < ih) {		//Both width and height scale
					proceed = true;
					sw = width;
					sh = height;
				}
				
			//Only width specified	
			} else if (width != null && height == null) {
				if (width < iw) {							//width is bigger than original image, no scale need
					proceed = true;
					sw = width;
					sh = (sw / iw) * ih;
				}
			
			//Only height specified	
			} else if (width == null && height != null) {
				if (height < ih) {							//height is bigger than original image, no scale need
					proceed = true;
					sh = height;
					sw = (sh / ih) * iw;
				}
			}
			
			//Neither width or height specified (scale width 100px)
			else {
				proceed = true;
				sw = 100;
				sh = (sw / iw) * ih;
			}
			
			if (proceed) {
				image = getScaledImage((int)sw, (int)sh);
			}
			
			try {
				ImageIO.write(image, ii.getType(), ii.getThumbnailFile());
			} catch (IOException e) {}
		}
		
	}
	
	private BufferedImage getScaledImage(int width, int height) {
		int type = (image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledImage = new BufferedImage(width, height, type);
		Graphics2D g = scaledImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		return scaledImage;
	}
}
