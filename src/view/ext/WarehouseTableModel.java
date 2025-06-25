package view.ext;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class WarehouseTableModel extends DefaultTableModel {
    static Vector<String> columns = new Vector<>();

    static {
        columns.addElement("编号");
        columns.addElement("仓库名称");
        columns.addElement("仓库地址");
        columns.addElement("创建时间");
    }

    public WarehouseTableModel() {
        super(null, columns);
    }

    private static WarehouseTableModel warehouseTableModel = new WarehouseTableModel();

    public static WarehouseTableModel assembleModel(Vector<Vector<Object>> data) {
        warehouseTableModel.setDataVector(data, columns);
        return warehouseTableModel;
    }

    public static void updateModel(Vector<Vector<Object>> data) {
        warehouseTableModel.setDataVector(data, columns);
    }

    public static Vector<String> getColumns() {
        return columns;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
