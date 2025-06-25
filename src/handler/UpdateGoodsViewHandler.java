package handler;

import entity.GoodsDO;
import service.GoodsService;
import service.impl.GoodsServiceImpl;
import view.AddGoodsView;
import view.MainView;
import view.UpdateGoodsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateGoodsViewHandler implements ActionListener {
    private UpdateGoodsView updateGoodsView;
    private MainView mainView;

    public UpdateGoodsViewHandler(UpdateGoodsView updateGoodsView, MainView mainView) {
        this.updateGoodsView = updateGoodsView;
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton) e.getSource();
        String text = jButton.getText();

        if ("修改".equals(text)) {
            GoodsService goodsService = new GoodsServiceImpl();
            GoodsDO goodsDO = updateGoodsView.buildUpdatedGoodsDO();
            boolean addResult = goodsService.update(goodsDO);
            if (addResult) {
                // 重新加载表格
                mainView.reloadTable();
                updateGoodsView.dispose();
            } else {
                JOptionPane.showMessageDialog(updateGoodsView, "添加失败");
            }
        }
    }
}
