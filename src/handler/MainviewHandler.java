package handler;

import service.GoodsService;
import service.impl.GoodsServiceImpl;
import view.AddGoodsView;
import view.MainView;
import view.UpdateGoodsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainviewHandler implements ActionListener {

    private MainView mainView;

    public MainviewHandler(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jButton = (JButton) e.getSource();
        String text = jButton.getText();

        if ("增加".equals(text)) {
            new AddGoodsView(mainView);
        } else if ("修改".equals(text)) {
            int[] selectedGoodsIds = mainView.getSelectedGoodsId();
            if (selectedGoodsIds.length != 1) {
                JOptionPane.showMessageDialog(mainView, "一次仅能修改一行");
                return;
            }
            new UpdateGoodsView(mainView, selectedGoodsIds[0]);
        } else if ("删除".equals(text)) {
            int[] selectedGoodsIds = mainView.getSelectedGoodsId();
            if (selectedGoodsIds.length == 0) {
                JOptionPane.showMessageDialog(mainView, "请选择要删除的行！");
                return;
            }
            int option = JOptionPane.showConfirmDialog(mainView, "是否确认删除已选择的" +
                    selectedGoodsIds.length + "行？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 确认删除
                GoodsService goodsService = new GoodsServiceImpl();
                boolean deleteResult = goodsService.delete(selectedGoodsIds);
                if (deleteResult) {
                    // 重新加载表格
                    mainView.reloadTable();

                } else {
                    JOptionPane.showConfirmDialog(mainView, "删除失败");
                }
            }

        } else if ("查询".equals(text)) {
            mainView.setPageNow(1);
            mainView.reloadTable();
        } else if ("上一页".equals(text)) {

            mainView.setPageNow(mainView.getPageNow() - 1);
            mainView.reloadTable();
        } else if ("下一页".equals(text)) {
            mainView.setPageNow(mainView.getPageNow() + 1);
            mainView.reloadTable();
        }
    }
}
