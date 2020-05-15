package persistency.Export;

import application.configuration.Configuration;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ProjectExporter {
    public static final char COMMA_SEPARATOR = 0x2C;
    public static final char COLON_SEPARATOR = 0x3A;
    public static final char SEMICOLON_SEPARATOR = 0x3B;
    public static final char TABULATOR_SEPARATOR = 0x09;

    public static void write(File file, exportInterface model, char separator) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {

            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            bw = new BufferedWriter(osw);

            String value;

            /* write columns */
            if (model.areColumnsVisible()) {
                for (int column = 0; column < model.getColumnCount(); column++) {
                    value = encodeValue(model.getColumnName(column));
                    bw.write(value);
                    if (column < model.getColumnCount() - 1) {
                        bw.write(separator);
                    } else {
                        bw.newLine();
                    }
                }
            }

            /* write data */
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int column = 0; column < model.getColumnCount(); column++) {
                    value = encodeValue(model.getValueAt(row, column));
                    bw.write(value);
                    if (column < model.getColumnCount() - 1) {
                        bw.write(separator);
                    } else {
                        bw.newLine();
                    }
                }
            }
            showExportResult(true);
        } catch (IOException e) {
            e.printStackTrace();
            showExportResult(false);

        } finally {
            try {
                if (bw != null) { bw.close(); }
                if (fos != null) { fos.close(); }
                if (osw != null) { osw.close(); }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    protected static String encodeValue(String value) {
        return  value==null? "" : value;
    }
    private static void showExportResult(boolean succeed){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Configuration.langBundle.getString("export_to_csv"));
        alert.setHeaderText(null);
        if (succeed) {
            alert.setContentText(Configuration.langBundle.getString("export_success"));
        }
        else{
            alert.setContentText(Configuration.langBundle.getString("export_failure"));

        }
        alert.showAndWait();
    }
}
