package utils;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ResourceLoader {
 
    static ResourceLoader rl = new ResourceLoader();
     
    public static Image loadImage(String imageName){
    	return new Image(rl.getClass().getClassLoader().getResourceAsStream("images/"+imageName));
    }

    
    public static OutputStream loadBundleOutput(String bundleName){
    	try {
        	File f = new File(System.getProperty("user.home")+"/.upmt/"+bundleName);
	        return new FileOutputStream( f );
    	}catch(Exception e) {
    		return null;
    	}
    	
    }
}