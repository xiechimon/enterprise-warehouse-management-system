package service.impl;

import entity.EmployeeDO;
import req.EmployeeRequest;
import res.TableDTO;
import service.EmployeeService;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Vector;

public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public boolean validateEmployee(EmployeeDO employeeDO) {
        String userName = employeeDO.getUsername();
        String pwdParam = employeeDO.getPassword();

        String sql = "select password from employee where username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            if (conn == null) {
                System.out.println("数据库连接失败");
                return false;
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            rs = ps.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                // 直接比较输入密码是否相等
                return dbPassword.equals(pwdParam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeRs(rs);
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return false;
    }

    @Override
    public TableDTO retrieveEmployee(EmployeeRequest employeeRequest) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from employee ");
        if (employeeRequest.getSearchKey() != null && !employeeRequest.getSearchKey().trim().isEmpty()) {
            sql.append(" where username like '%").append(employeeRequest.getSearchKey().trim()).append("%' ");
        }
        sql.append(" order by employee_id limit ").append(employeeRequest.getStart()).append(" , ")
                .append(employeeRequest.getPageSize());

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TableDTO returnDTO = new TableDTO();
        try {
            conn = DBUtil.getConn();
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            // 查询记录
            returnDTO.setData(fillData(rs));

            sql.setLength(0);
            sql.append("select count(*) from employee");
            if (employeeRequest.getSearchKey() != null && !employeeRequest.getSearchKey().trim().isEmpty()) {
                sql.append(" where username like '%").append(employeeRequest.getSearchKey().trim()).append("%' ");
            }
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                int count = rs.getInt(1);
                returnDTO.setTotalCount(count);
            }
            //System.out.println("查到的记录数：" + returnDTO.getData().size());
            return returnDTO;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeRs(rs);
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return null;
    }

    @Override
    public EmployeeDO getEmployeeInfo(String username) {
        String sql = "select employee_id, username, password, role, warehouse_id, register_time from employee where username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            if (conn == null) {
                System.out.println("数据库连接失败");
                return null;
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                EmployeeDO employeeDO = new EmployeeDO();
                int employeeId = rs.getInt("employee_id");
                //String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                int warehouseId = rs.getInt("warehouse_id");
                LocalDateTime registerTime = rs.getTimestamp("register_time").toLocalDateTime();
                employeeDO.setEmployeeId(employeeId);
                employeeDO.setUsername(username);
                employeeDO.setPassword(password);
                employeeDO.setRole(role);
                employeeDO.setWarehouseId(warehouseId);
                employeeDO.setRegisterTime(registerTime);
                return employeeDO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeRs(rs);
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return null;
    }

    private static Vector<Vector<Object>> fillData(ResultSet rs) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();

        while (rs.next()) {
            Vector<Object> oneRecord = new Vector<>();
            int employeeId = rs.getInt("employee_id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            int warehouseId = rs.getInt("warehouse_id");
            String registerTime = String.valueOf(rs.getTimestamp("register_time"));
            oneRecord.addElement(employeeId);
            oneRecord.addElement(username);
            oneRecord.addElement(password);
            oneRecord.addElement(role);
            oneRecord.addElement(warehouseId);
            oneRecord.addElement(registerTime);
            data.addElement(oneRecord);
        }
        return data;
    }
}
