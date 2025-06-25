package service;

import entity.WarehouseDO;
import req.GoodsRequest;
import req.WarehouseRequest;
import res.TableDTO;

import java.util.List;

public interface WarehouseService {
    List<WarehouseDO> getAllWarehouses();

    // 从数据库获取总记录数和数据
    TableDTO retrieveWarehouse(WarehouseRequest warehouseRequest);
}
