package persistency.Export;

public interface CSVTableModel {
        public String getValueAt(int row, int column);

        public int getColumnCount();

        public int getRowCount();

        public String getColumnName(int column);

        public boolean areColumnsVisible();

}
