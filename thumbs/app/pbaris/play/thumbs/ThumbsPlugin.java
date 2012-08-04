package pbaris.play.thumbs;

import static play.api.Play.unsafeApplication;
import static play.libs.Scala.orNull;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import play.Application;
import play.Configuration;
import play.Plugin;

/**
 * @author	Panos Bariamis
 * @since	0.0.1
 */
public class ThumbsPlugin extends Plugin {
	
	private static final String THUMBS_RELATIVE_ENABLED		= "thumbs.relative.enabled";
	private static final String THUMBS_RELATIVE_BASEPATH 	= "thumbs.relative.basePath";
	private static final String THUMBS_CACHE 				= "thumbs.cache";
	
	private static ThumbsPlugin plugin;
	private static final String CACHE_FOLDER_NAME = "cache";
	
	private final Application application;
	
	private boolean relative = false;
	private String basePath = "";
	private File baseFolder;
	private File cacheFolder;
	
	public ThumbsPlugin(Application application) {
		this.application = application;
	}
	
	/**
     * Reads the configuration file and initialize the plugin
     */
    public void onStart() {
    	Configuration config = application.configuration();
    	
    	//relative
    	if (config.keys().contains(THUMBS_RELATIVE_ENABLED)) {
    		relative = config.getBoolean(THUMBS_RELATIVE_ENABLED);
    		
    		if (relative && (!config.keys().contains(THUMBS_RELATIVE_BASEPATH) 
    				|| StringUtils.isEmpty(config.getString(THUMBS_RELATIVE_BASEPATH)))) {
    			
    			throw config.reportError(THUMBS_RELATIVE_BASEPATH, 
    					"Thumbs plugin is configured as relative, so it needs a basePath", null);
    		
    		} else if (relative) {
    			basePath = config.getString(THUMBS_RELATIVE_BASEPATH).trim();
    			basePath = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
    		}
    	}
    	
    	//cache
    	if (!config.keys().contains(THUMBS_CACHE) || StringUtils.isEmpty(config.getString(THUMBS_CACHE))) {
    		cacheFolder = application.getFile(CACHE_FOLDER_NAME);
    	} else {
    		String cachePath = config.getString(THUMBS_CACHE).trim();
    		cacheFolder = new File(cachePath, CACHE_FOLDER_NAME);
    	}
    	
    	if (!cacheFolder.exists()) cacheFolder.mkdirs();
    }
    
    public static ThumbsPlugin getPlugin() throws Exception {
    	if (plugin == null) {
    		plugin = orNull(unsafeApplication().plugin(ThumbsPlugin.class));
    		if (plugin == null) {
    			throw new Exception("The Thumbs plugin was not registered, or is disabled. Please check your conf/play.plugins file.");
    		}    		
    	}
        return plugin;
    }
    
    public static boolean isRelative() {
    	try {
			ThumbsPlugin p = getPlugin();
			return (p != null ? p.relative : false);
		} catch (Exception e) {}
    	
    	return false;
	}
    
    public static String getBasePath() {
    	try {
			ThumbsPlugin p = getPlugin();
			return (p != null ? p.basePath : "");
		} catch (Exception e) {}
    	
    	return "";
	}
    
    public static File getBaseFolder() {
    	try {
			ThumbsPlugin p = getPlugin();
			return (p != null ? p.baseFolder : null);
		} catch (Exception e) {}
    	
    	return null;
	}
    
    public static File getCacheFolder() {
    	try {
			ThumbsPlugin p = getPlugin();
			return (p != null ? p.cacheFolder : null);
		} catch (Exception e) {}
    	
    	return null;
	}
}
