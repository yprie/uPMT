package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.imageio.ImageIO;

import com.sun.javafx.tk.Toolkit;

import application.Main;
import javafx.scene.image.Image;
import model.Projet;

public class ResourceLoader {
 
    static ResourceLoader rl = new ResourceLoader();
     
    public static Image loadImage(String imageName){
    	return new Image(rl.getClass().getClassLoader().getResourceAsStream("images/"+imageName));
    }
    
    public static InputStream loadBundleInput(String bundleName){
    	InputStream ret = null;
    	File f = new File("./data/"+bundleName);
    	try{
	    	if(!f.exists()) {
	    		new File("./data/").mkdir();
    			f.createNewFile();
    			PrintWriter writer = new PrintWriter("./data/"+bundleName, "UTF-8");
    			writer.println("locale=en");
        		writer.close();
	    	}
	    	ret = new FileInputStream(new File("./data/"+bundleName));
    	}catch(Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
    
    public static OutputStream loadBundleOutput(String bundleName){
    	try {
        	File f = new File("./data/"+bundleName);
	        return new FileOutputStream( f );
    	}catch(Exception e) {
    		return null;
    	}
    	
    }
}