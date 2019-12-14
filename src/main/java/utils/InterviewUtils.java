package utils;

import java.io.*;

public class InterviewUtils {
    public static String getExtract(File file) {
        String extract = "";
        BufferedReader br = null;
        FileReader fr = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF8"));
            String sCurrentLine;
            int i=0;
            while ((sCurrentLine = br.readLine()) != null && i<3) {
                if(i!=0)extract+=" ";
                extract += sCurrentLine;
                i++;
            }
        } catch (IOException e) {}
        finally {
            try {
                if (br != null)br.close();
                if (fr != null)fr.close();
            } catch (IOException ex) {}
        }
        return extract;
    }
}
