package view.ext;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class GoodsTableModel extends DefaultTableModel {
    static Vector<String> columns = new Vector<>();

    static {
        columns.addElement("编号");
        columns.addElement("物品名称");
        columns.addElement("库存数量");
        columns.addElement("类别");
        columns.addElement("所属仓库");
        columns.addElement("最近更新时间");
    }

    public GoodsTableModel() {
        super(null, columns);
    }

    private static GoodsTableModel goodsTableModel = new GoodsTableModel();

    public static GoodsTableModel assembleModel(Vector<Vector<Object>> data) {
        goodsTableModel.setDataVector(data, columns);
        return goodsTableModel;
    }

    public static void updateModel(Vector<Vector<Object>> data) {
        goodsTableModel.setDataVector(data, columns);
    }

    public static Vector<String> getColumns() {
        return columns;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
