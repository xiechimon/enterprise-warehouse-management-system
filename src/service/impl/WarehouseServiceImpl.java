package service.impl;

import entity.WarehouseDO;
import req.WarehouseRequest;
import res.TableDTO;
import service.WarehouseService;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class WarehouseServiceImpl implements WarehouseService {
    @Override
    public List<WarehouseDO> getAllWarehouses() {
        List<WarehouseDO> warehouseDOList = new ArrayList<>();
        String sql = "select warehouse_id, warehouse_name, warehouse_address, created_time from warehouse";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // 获取所有仓库并存入List
            conn = DBUtil.getConn();
            if (conn == null) {
                System.out.println("数据库连接失败");
                return List.of();
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                WarehouseDO warehouse = new WarehouseDO();
                warehouse.setWarehouseId(rs.getInt("warehouse_id"));
                warehouse.setWarehouseName(rs.getString("warehouse_name"));
                warehouse.setWarehouseAddress(rs.getString("warehouse_address"));
                warehouse.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());

                warehouseDOList.add(warehouse);
            }
            return warehouseDOList;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeRs(rs);
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return List.of();
    }

    @Override
    public TableDTO retrieveWarehouse(WarehouseRequest warehouseRequest) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from warehouse ");
        if (warehouseRequest.getSearchKey() != null && !warehouseRequest.getSearchKey().trim().isEmpty()) {
            sql.append(" where warehouse_name like '%" + warehouseRequest.getSearchKey().trim() + "%' ");
        }
        sql.append(" order by warehouse_id limit ").append(warehouseRequest.getStart()).append(" , ")
                .append(warehouseRequest.getPageSize());

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
            sql.append("select count(*) from warehouse");
            if (warehouseRequest.getSearchKey() != null && !warehouseRequest.getSearchKey().trim().isEmpty()) {
                sql.append(" where warehouse_name like '%" + warehouseRequest.getSearchKey().trim() + "%' ");
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

    private static Vector<Vector<Object>> fillData(ResultSet rs) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();

        while (rs.next()) {
            Vector<Object> oneRecord = new Vector<>();
            int warehouseId = rs.getInt("warehouse_id");
            String warehouseName = rs.getString("warehouse_name");
            String warehouseAddress = rs.getString("warehouse_address");
            String createdTime = String.valueOf(rs.getTimestamp("created_time"));
            oneRecord.addElement(warehouseId);
            oneRecord.addElement(warehouseName);
            oneRecord.addElement(warehouseAddress);
            oneRecord.addElement(createdTime);
            data.addElement(oneRecord);
        }
        return data;
    }
}
