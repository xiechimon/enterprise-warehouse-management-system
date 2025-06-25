package view.ext;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

public class MainViewTable extends JTable {
    public MainViewTable() {
        JTableHeader tableHeader = getTableHeader();
        tableHeader.setFont(new Font(null, Font.BOLD, 16));
        tableHeader.setForeground(Color.RED);
        // 设置表格体
        setFont(new Font(null, Font.PLAIN, 14));
        setForeground(Color.BLACK);
        setGridColor(Color.BLACK);
        setRowHeight(30);
        // 设置多行选择
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void renderRule(Vector<String> columns) {
        // 设置表格列的渲染方式
        MainViewCellRender render = new MainViewCellRender();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn column = getColumn(columns.get(i));
            column.setCellRenderer(render);
            if (i == 0) {
                column.setPreferredWidth(120);
                column.setMaxWidth(80);
                column.setResizable(false);
            }
        }
    }
}
