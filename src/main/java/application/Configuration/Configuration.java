package application.Configuration;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Configuration {

    private static String HOME_DIRECTORY = System.getProperty("user.home")+"/.upmt/";
    private static String properties_file = "upmt.properties";
    private static String projects_paths_file = "projects_paths";


    private static RecentFirstList<String> projects_paths;
    public static Locale locale;
    public static ResourceBundle langBundle;

    public static boolean loadAppConfiguration() throws IOException {
        if(createRequiredFiles()){
            loadUPMTInformation();
            return true;
        }
        return false;
    }

    public static void addToProjects(String project_path) throws IOException {
        projects_paths.add(project_path);
        saveProjectsPath();
    }

    public static void removeFromProjects(String project_path) throws IOException {
        projects_paths.removeIf(s -> s.equals(project_path));
        saveProjectsPath();
    }

    public static String[] getProjectsPath(int nFirsts) {
        return convertProjectPathsToArray(projects_paths.getFirsts(nFirsts));
    }
    public static String[] getProjectsPath() { return convertProjectPathsToArray(projects_paths); }






    //Loading data from files ------------------------------------------------------------

    private static void loadUPMTInformation() throws IOException {
        loadProperties();
        loadProjectsPath();
    }

    private static void loadProperties() throws IOException {
        File upmtProperties = new File(HOME_DIRECTORY + properties_file);
        Properties properties = new Properties();
        properties.load(new FileInputStream(upmtProperties));


        locale = new Locale(properties.getProperty("locale"));
        Locale.setDefault(locale);

        langBundle = ResourceBundle.getBundle("bundles.Lang", locale);
    }

    private static void loadProjectsPath() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(HOME_DIRECTORY + projects_paths_file)));
        projects_paths = new RecentFirstList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if(new File(line).exists())
                projects_paths.add(line);
        }
        //Removing of old unexisting files
        saveProjectsPath();
        bufferedReader.close();
    }



    //Configuration file creation ----------------------------------------------------------------------------

    private static boolean createRequiredFiles() throws IOException {
        return createUPMTFolder() && createPropertiesFile() && createProjectPathsFile();
    }

    private static boolean createUPMTFolder() {
        File upmtFolder = new File(HOME_DIRECTORY);
        if(!upmtFolder.exists())
            return upmtFolder.mkdir();
        return true;
    }

    private static boolean createPropertiesFile() throws IOException {
        File upmtProperties = new File(HOME_DIRECTORY + properties_file);
        //Create property file if not exists.
        if(!upmtProperties.exists()){
            if(!upmtProperties.createNewFile())
                return false;

            Properties props = new Properties();
            props.setProperty("locale", Locale.ENGLISH.toString());
            props.store(new FileOutputStream(upmtProperties), null);
        }
        return true;
    }

    private static boolean createProjectPathsFile() throws IOException {
        File projectList = new File(HOME_DIRECTORY + projects_paths_file);
        if(!projectList.exists())
            return projectList.createNewFile();
        return true;
    }



    //--------------- Utils -------------------------------------

    private static void saveProjectsPath() throws IOException {
        PrintWriter writer = new PrintWriter(new File(HOME_DIRECTORY + projects_paths_file));
        for(String path: projects_paths) {
            writer.println(path);
        }
        writer.close();
    }

    private static String[] convertProjectPathsToArray(RecentFirstList<String> projects_paths) {
        String[] paths = new String[projects_paths.size()];
        for(int i = 0; i < projects_paths.size(); i++) {
            paths[i] = String.valueOf(projects_paths.get(i));
        }
        return paths;
    }
}
