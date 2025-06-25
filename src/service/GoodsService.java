package service;

import entity.GoodsDO;
import req.GoodsRequest;
import res.TableDTO;

public interface GoodsService {
    // 从数据库获取总记录数和数据
    TableDTO retrieveGoods(GoodsRequest goodsRequest);

    boolean add(GoodsDO goodsDO);

    GoodsDO getById(int selectedGoodsId);

    boolean update(GoodsDO goodsDO);

    boolean delete(int[] selectedGoodsIds);
}
