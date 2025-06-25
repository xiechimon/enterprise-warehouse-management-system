package view;

import entity.GoodsDO;
import handler.AddGoodsViewHandler;

import javax.swing.*;
import java.awt.*;

public class AddGoodsView extends JDialog {
    JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
    JLabel goodsNameLabel = new JLabel("物品名称：", JLabel.RIGHT);
    JTextField goodsNameTxt = new JTextField();
    JLabel stockQuantityLabel = new JLabel("库存数量：", JLabel.RIGHT);
    JTextField stockQuantityTxt = new JTextField();
    JLabel categoryIdLabel = new JLabel("类别：", JLabel.RIGHT);
    JTextField categoryIdTxt = new JTextField();
    JLabel warehouseIdLabel = new JLabel("所属仓库：", JLabel.RIGHT);
    JTextField warehouseIdTxt = new JTextField();
    JButton addBtn = new JButton("添加");

    AddGoodsViewHandler addGoodsViewHandler;

    int LABELWIDTH = 80;
    int FIELDWIDTH = 200;
    int FORMHEIGHT = 30;
    public AddGoodsView(MainView mainView) {
        super(mainView, "添加物品", true);

        addGoodsViewHandler = new AddGoodsViewHandler(this, mainView);

        createFormItem(goodsNameLabel, goodsNameTxt);
        createFormItem(stockQuantityLabel, stockQuantityTxt);
        createFormItem(categoryIdLabel, categoryIdTxt);
        createFormItem(warehouseIdLabel, warehouseIdTxt);

        addBtn.addActionListener(addGoodsViewHandler);
        jPanel.add(addBtn);

        Container contentPane = getContentPane();
        contentPane.add(jPanel);

        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // 只销毁当前窗体
        setResizable(false);
        setVisible(true);
    }

    private void createFormItem(JLabel jLabel, JTextField jTextField) {
        jLabel.setPreferredSize(new Dimension(LABELWIDTH, FORMHEIGHT));
        jPanel.add(jLabel);
        jTextField.setPreferredSize(new Dimension(FIELDWIDTH, FORMHEIGHT));
        jPanel.add(jTextField);
    }

    public GoodsDO buildGoodsDO() {
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setGoodsName(goodsNameTxt.getText());
        goodsDO.setCategoryId(Integer.valueOf(categoryIdTxt.getText()));
        goodsDO.setWarehouseId(Integer.valueOf(warehouseIdTxt.getText()));
        goodsDO.setStockQuantity(Integer.valueOf(stockQuantityTxt.getText()));

        return goodsDO;
    }
}
