package handler;

import entity.GoodsDO;
import service.GoodsService;
import service.impl.GoodsServiceImpl;
import view.AddGoodsView;
import view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGoodsViewHandler implements ActionListener {
    private AddGoodsView addGoodsView;
    private MainView mainView;

    public AddGoodsViewHandler(AddGoodsView addGoodsView, MainView mainView) {
        this.addGoodsView = addGoodsView;
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton) e.getSource();
        String text = jButton.getText();

        if ("添加".equals(text)) {
            GoodsService goodsService = new GoodsServiceImpl();
            GoodsDO goodsDO = addGoodsView.buildGoodsDO();
            boolean addResult = goodsService.add(goodsDO);
            if (addResult) {
                // 重新加载表格
                mainView.reloadTable();
                addGoodsView.dispose();
            } else {
                JOptionPane.showMessageDialog(addGoodsView, "添加失败");
            }
        }
    }
}
