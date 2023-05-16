package persistency.Export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    public static void exportToExcel(List<List<List<String>>> tableData, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Comparison Data");

            CellStyle headerStyle = createHeaderStyle(workbook);

            int rowNum = 0;
            for (List<List<String>> table : tableData) {
                boolean headerRow = true;
                for (List<String> rowData : table) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (String cellData : rowData) {
                        Cell cell = row.createCell(colNum++);
                        cell.setCellValue(cellData);
                        if (headerRow) { // Apply style to the header row
                            cell.setCellStyle(headerStyle);
                        }
                    }
                    headerRow = false;
                }
            }

            // Write the workbook data to a file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Table data exported to Excel successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        return style;
    }
}
