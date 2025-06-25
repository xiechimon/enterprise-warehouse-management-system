package service.impl;

import entity.GoodsDO;
import req.GoodsRequest;
import res.TableDTO;
import service.GoodsService;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Vector;

public class GoodsServiceImpl implements GoodsService {

    @Override
    public TableDTO retrieveGoods(GoodsRequest goodsRequest) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
            .append("g.goods_id, g.goods_name, g.stock_quantity, ")
            .append("c.category_name, w.warehouse_name, g.last_update ")
            .append("FROM goods g ")
            .append("JOIN goods_category c ON g.category_id = c.category_id ")
            .append("JOIN warehouse w ON g.warehouse_id = w.warehouse_id ");
        if (goodsRequest.getSearchKey() != null && !goodsRequest.getSearchKey().trim().isEmpty()) {
            sql.append(" where goods_name like '%" + goodsRequest.getSearchKey().trim() + "%' ");
        }
        sql.append(" order by goods_id limit ").append(goodsRequest.getStart()).append(" , ")
                .append(goodsRequest.getPageSize());

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
            sql.append("select count(*) from goods");
            if (goodsRequest.getSearchKey() != null && !goodsRequest.getSearchKey().trim().isEmpty()) {
                sql.append(" where goods_name like '%" + goodsRequest.getSearchKey().trim() + "%' ");
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
    public boolean add(GoodsDO goodsDO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" insert into goods(goods_name, stock_quantity, category_id, warehouse_id) ");
        sql.append(" values (?, ?, ?, ?) ");

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            ps = conn.prepareStatement(sql.toString());

            ps.setString(1, goodsDO.getGoodsName());
            ps.setInt(2, goodsDO.getStockQuantity());
            ps.setInt(3, goodsDO.getCategoryId());
            ps.setInt(4, goodsDO.getWarehouseId());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return false;
    }

    @Override
    public GoodsDO getById(int selectedGoodsId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from goods where goods_id = ? ");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        GoodsDO goodsDO = new GoodsDO();
        try {
            conn = DBUtil.getConn();
            ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, selectedGoodsId);
            rs = ps.executeQuery();

            while (rs.next()) {
                int goodsId = rs.getInt("goods_id");
                String goodsName = rs.getString("goods_name");
                int stockQuantity = rs.getInt("stock_quantity");
                int categoryId = rs.getInt("category_id");
                int warehouseId = rs.getInt("warehouse_id");

                goodsDO.setGoodsId(goodsId);
                goodsDO.setGoodsName(goodsName);
                goodsDO.setStockQuantity(stockQuantity);
                goodsDO.setCategoryId(categoryId);
                goodsDO.setWarehouseId(warehouseId);
                goodsDO.setLastUpdate(rs.getTimestamp("last_update").toLocalDateTime());
            }

            return goodsDO;

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
    public boolean update(GoodsDO goodsDO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update goods set goods_name = ?, stock_quantity = ?, category_id = ?, warehouse_id = ? ");
        sql.append(" where goods_id = ? ");

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            ps = conn.prepareStatement(sql.toString());

            ps.setString(1, goodsDO.getGoodsName());
            ps.setInt(2, goodsDO.getStockQuantity());
            ps.setInt(3, goodsDO.getCategoryId());
            ps.setInt(4, goodsDO.getWarehouseId());
            ps.setInt(5, goodsDO.getGoodsId());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return false;
    }

    @Override
    public boolean delete(int[] selectedGoodsIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from goods where goods_id in ( ");
        int length = selectedGoodsIds.length;
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                sql.append(" ? ");
            } else {
                sql.append(" ?, ");
            }
        }
        sql.append(" ) ");

        Connection conn = null;
        PreparedStatement ps = null;
        GoodsDO goodsDO = new GoodsDO();
        try {
            conn = DBUtil.getConn();
            ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < length; i++) {
                ps.setInt(i + 1, selectedGoodsIds[i]);
            }
            return ps.executeUpdate() == length;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePs(ps);
            DBUtil.closeConn(conn);
        }

        return false;
    }

    private static Vector<Vector<Object>> fillData(ResultSet rs) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();

        while (rs.next()) {
            Vector<Object> oneRecord = new Vector<>();
            int goodsId = rs.getInt("goods_id");
            String goodsName = rs.getString("goods_name");
            int stockQuantity = rs.getInt("stock_quantity");
            String categoryName = rs.getString("category_name");
            String warehouseName = rs.getString("warehouse_name");
            String lastUpdate = String.valueOf(rs.getTimestamp("last_update"));
            oneRecord.addElement(goodsId);
            oneRecord.addElement(goodsName);
            oneRecord.addElement(stockQuantity);
            oneRecord.addElement(categoryName);
            oneRecord.addElement(warehouseName);
            oneRecord.addElement(lastUpdate);
            data.addElement(oneRecord);
        }
        return data;
    }
}
