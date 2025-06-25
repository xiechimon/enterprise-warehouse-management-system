package view;

import entity.GoodsDO;
import handler.UpdateGoodsViewHandler;
import service.GoodsService;
import service.impl.GoodsServiceImpl;

import javax.swing.*;
import java.awt.*;

public class UpdateGoodsView extends JDialog {
    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));

    JLabel goodsIdLabel = new JLabel("编号：", JLabel.RIGHT);
    JTextField goodsIdTxt = new JTextField();

    JLabel goodsNameLabel = new JLabel("物品名称：", JLabel.RIGHT);
    JTextField goodsNameTxt = new JTextField();
    JLabel stockQuantityLabel = new JLabel("库存数量：", JLabel.RIGHT);
    JTextField stockQuantityTxt = new JTextField();
    JLabel categoryIdLabel = new JLabel("类别：", JLabel.RIGHT);
    JTextField categoryIdTxt = new JTextField();
    JLabel warehouseIdLabel = new JLabel("所属仓库：", JLabel.RIGHT);
    JTextField warehouseIdTxt = new JTextField();
    JButton updateBtn = new JButton("修改");

    int LABELWIDTH = 80;
    int FIELDWIDTH = 200;
    int FORMHEIGHT = 30;
    UpdateGoodsViewHandler updateGoodsViewHandler;
    public UpdateGoodsView(MainView mainView, int selectedGoodsId) {
        super(mainView, "修改物品", true);

        updateGoodsViewHandler = new UpdateGoodsViewHandler(this, mainView);
        // 数据查询选中id对应的记录并回显
        GoodsService goodsService = new GoodsServiceImpl();
        GoodsDO selectedGoods = goodsService.getById(selectedGoodsId);
        if (selectedGoods == null) {
            JOptionPane.showMessageDialog(null, "请先选中一条商品数据！");
            return;
        }

        //createFormItem(goodsIdLabel, goodsIdTxt, String.valueOf(selectedGoods.getGoodsId()));
        goodsIdLabel.setPreferredSize(new Dimension(LABELWIDTH, FORMHEIGHT));
        jPanel.add(goodsIdLabel);
        goodsIdTxt.setPreferredSize(new Dimension(FIELDWIDTH, FORMHEIGHT));
        goodsIdTxt.setText(String.valueOf(selectedGoods.getGoodsId()));
        goodsIdTxt.setEnabled(false);
        jPanel.add(goodsIdTxt);

        createFormItem(goodsNameLabel, goodsNameTxt, selectedGoods.getGoodsName());
        createFormItem(stockQuantityLabel, stockQuantityTxt, String.valueOf(selectedGoods.getStockQuantity()));
        createFormItem(categoryIdLabel, categoryIdTxt, String.valueOf(selectedGoods.getCategoryId()));
        createFormItem(warehouseIdLabel, warehouseIdTxt, String.valueOf(selectedGoods.getWarehouseId()));

        updateBtn.addActionListener(updateGoodsViewHandler);
        jPanel.add(updateBtn);

        Container contentPane = getContentPane();
        contentPane.add(jPanel);

        setSize(350, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // 只销毁当前窗体
        setResizable(false);
        setVisible(true);
    }

    private void createFormItem(JLabel jLabel, JTextField jTextField, String text) {
        jLabel.setPreferredSize(new Dimension(LABELWIDTH, FORMHEIGHT));
        jPanel.add(jLabel);
        jTextField.setPreferredSize(new Dimension(FIELDWIDTH, FORMHEIGHT));
        jTextField.setText(text);
        jPanel.add(jTextField);
    }

    // 获取修改后的学生对象
    public GoodsDO buildUpdatedGoodsDO() {
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setGoodsId(Integer.valueOf(goodsIdTxt.getText()));
        goodsDO.setGoodsName(goodsNameTxt.getText());
        goodsDO.setCategoryId(Integer.valueOf(categoryIdTxt.getText()));
        goodsDO.setWarehouseId(Integer.valueOf(warehouseIdTxt.getText()));
        goodsDO.setStockQuantity(Integer.valueOf(stockQuantityTxt.getText()));

        return goodsDO;
    }
}
