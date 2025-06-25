package view.ext;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class EmployeeTableModel extends DefaultTableModel {
    static Vector<String> columns = new Vector<>();

    static {
        columns.addElement("编号");
        columns.addElement("用户名");
        columns.addElement("密码");
        columns.addElement("用户角色");
        columns.addElement("工作仓库");
        columns.addElement("注册时间");
    }

    public EmployeeTableModel() {
        super(null, columns);
    }

    private static EmployeeTableModel employeeTableModel = new EmployeeTableModel();

    public static EmployeeTableModel assembleModel(Vector<Vector<Object>> data) {
        employeeTableModel.setDataVector(data, columns);
        return employeeTableModel;
    }

    public static void updateModel(Vector<Vector<Object>> data) {
        employeeTableModel.setDataVector(data, columns);
    }

    public static Vector<String> getColumns() {
        return columns;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
