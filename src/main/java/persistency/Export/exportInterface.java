package persistency.Export;

public interface exportInterface {
        public String getValueAt(int row, int column);

        public int getColumnCount();

        public int getRowCount();

        public String getColumnName(int column);

        public boolean areColumnsVisible();

}
