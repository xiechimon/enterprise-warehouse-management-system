package view;

import com.formdev.flatlaf.FlatLightLaf;
import entity.EmployeeDO;
import entity.WarehouseDO;
import handler.MainviewHandler;
import req.EmployeeRequest;
import req.GoodsRequest;
import req.WarehouseRequest;
import res.TableDTO;
import service.EmployeeService;
import service.GoodsService;
import service.WarehouseService;
import service.impl.EmployeeServiceImpl;
import service.impl.GoodsServiceImpl;
import service.impl.WarehouseServiceImpl;
import util.DimensionUtil;
import view.ext.EmployeeTableModel;
import view.ext.MainViewTable;
import view.ext.GoodsTableModel;
import view.ext.WarehouseTableModel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class MainView extends JFrame {
    // 菜单栏
    JMenuBar menuBar = new JMenuBar();

    // 菜单
    JMenu userMenu = new JMenu("用户");
    JMenu viewMenu = new JMenu("数据视图");
    JMenu helpMenu = new JMenu("Help");
    //JMenu warehouseMenu;

    // 菜单项
    JMenuItem viewUserItem = new JMenuItem("查看我的信息");
    JMenuItem modifyUserItem = new JMenuItem("修改信息");
    JMenuItem goodsViewItem = new JMenuItem("商品表");
    JMenuItem warehouseViewItem = new JMenuItem("仓库表");
    JMenuItem employeeViewItem = new JMenuItem("员工表");
    JMenuItem exitItem = new JMenuItem("Exit");
    JMenuItem aboutItem = new JMenuItem("About");

    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton addBtn = new JButton("增加");
    JButton updateBtn = new JButton("修改");
    JButton delBtn = new JButton("删除");
    JTextField searchField = new JTextField(15);
    JButton searchBtn = new JButton("查询");

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton preBtn = new JButton("上一页");
    JButton nextBtn = new JButton("下一页");

    MainViewTable mainViewTable = new MainViewTable();

    private int pageNow = 1;    // 当前是第几页
    private int pageSize = 10;    // 一页显示多少条数据库记录
    // 切换仓库
    WarehouseDO currentWarehouse;
    MainviewHandler mainviewHandler;

    enum ViewType {GOODS, WAREHOUSE, EMPLOYEE}

    ViewType currentView = ViewType.GOODS; // 默认是商品表
    private EmployeeDO currentUser;


    public MainView(EmployeeDO currentUser) {
        super("SIMS");
        this.currentUser = currentUser;
        Container contentPane = getContentPane();
        Rectangle bounds = DimensionUtil.getBounds();
        pageSize = Math.floorDiv(bounds.height, 35);

        mainviewHandler = new MainviewHandler(this);

        //// 初始化仓库数据
        //WarehouseService warehouseService = new WarehouseServiceImpl();
        //List<WarehouseDO> warehouseList = warehouseService.getAllWarehouses();
        //
        //if (warehouseList == null || warehouseList.isEmpty()) {
        //    JOptionPane.showMessageDialog(this, "未获取到仓库！待添加功能：添加仓库");
        //    System.exit(0);
        //}
        //
        //// 设置当前仓库和菜单
        //currentWarehouse = warehouseList.getFirst(); // 默认第一个为当前仓库
        //warehouseMenu = new JMenu();             // 初始化菜单
        //updateWarehouseMenu(warehouseList);     // 加载仓库菜单

        // 布置组件
        layoutMenu();
        layoutNorth(contentPane);
        layoutCenter(contentPane);
        layoutSouth(contentPane);

        // 设置ICON
        URL icoUrl = MainView.class.getClassLoader().getResource("whms-logo.png");
        setIconImage(new ImageIcon(Objects.requireNonNull(icoUrl)).getImage());

        setSize(600, 400);

        // 根据屏幕大小设置主界面大小
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);    // 居中
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        // 显示第一页数据并设置按钮状态
        reloadTable();
    }

    private void layoutMenu() {
        // 用户
        viewUserItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "用户名: " + currentUser.getUsername() + "\n" +
                            "角色: " + (isAdmin() ? "管理员" : "普通员工") + "\n" +
                            "工作仓库："  + currentUser.getWarehouseId(),
                    "我的信息", JOptionPane.INFORMATION_MESSAGE);
        });
        modifyUserItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "功能待完善"));

        // 数据视图
        goodsViewItem.addActionListener(e -> {
            currentView = ViewType.GOODS;
            reloadTable();
        });
        warehouseViewItem.addActionListener(e -> {
            currentView = ViewType.WAREHOUSE;
            reloadTable();
        });
        employeeViewItem.addActionListener(e -> {
            if (isAdmin()) {
                currentView = ViewType.EMPLOYEE;
                reloadTable();
            } else {
                JOptionPane.showMessageDialog(this, "您的权限不足！");
            }
        });

        // 帮助
        exitItem.addActionListener(e -> System.exit(0));
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "作者：Xiechimon"));


        // 将菜单添加到菜单栏中
        userMenu.add(viewUserItem);
        userMenu.add(modifyUserItem);

        viewMenu.add(goodsViewItem);
        viewMenu.add(warehouseViewItem);
        viewMenu.add(employeeViewItem);
        helpMenu.add(exitItem);
        helpMenu.add(aboutItem);

        //menuBar.add(warehouseMenu);
        menuBar.add(userMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // 把菜单栏设置到窗口
        setJMenuBar(menuBar);
    }

    private boolean isAdmin() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "未获取到用户");
            return false;
        }
        String role = currentUser.getRole();
        if (role == null) {
            JOptionPane.showMessageDialog(this, "未获取到用户角色");
            return false;
        }
        System.out.println(currentUser.getUsername());
        System.out.println(currentUser.getPassword());
        System.out.println(currentUser.getRole());
        return role.equals("admin");
    }


    private void layoutCenter(Container contentPane) {
        Vector<Vector<Object>> data = getVectors();

        GoodsTableModel goodsTableModel = GoodsTableModel.assembleModel(data);
        Vector<String> columns = GoodsTableModel.getColumns();
        mainViewTable.setModel(goodsTableModel);
        mainViewTable.renderRule(columns);
        JScrollPane jScrollPane = new JScrollPane(mainViewTable);
        contentPane.add(jScrollPane, BorderLayout.CENTER);
    }

    private void layoutNorth(Container contentPane) {
        // 增加事件监听
        addBtn.addActionListener(mainviewHandler);
        updateBtn.addActionListener(mainviewHandler);
        delBtn.addActionListener(mainviewHandler);
        searchBtn.addActionListener(mainviewHandler);

        northPanel.add(addBtn);
        northPanel.add(updateBtn);
        northPanel.add(delBtn);
        northPanel.add(searchField);
        northPanel.add(searchBtn);
        contentPane.add(northPanel, BorderLayout.NORTH);
    }

    private void layoutSouth(Container contentPane) {
        preBtn.addActionListener(mainviewHandler);
        nextBtn.addActionListener(mainviewHandler);

        southPanel.add(preBtn);
        southPanel.add(nextBtn);
        contentPane.add(southPanel, BorderLayout.SOUTH);
    }

    private Vector<Vector<Object>> getVectors() {
        GoodsService goodsService = new GoodsServiceImpl();
        GoodsRequest goodsRequest = new GoodsRequest();
        goodsRequest.setPageNow(pageNow);
        goodsRequest.setPageSize(pageSize);
        goodsRequest.setSearchKey(searchField.getText().trim());
        TableDTO tableDTO = goodsService.retrieveGoods(goodsRequest);
        Vector<Vector<Object>> data = tableDTO.getData();
        return data;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    private void updateWarehouseMenu(List<WarehouseDO> warehouseList) {
        //warehouseMenu.removeAll();

        // 设置菜单标题为当前仓库
        //warehouseMenu.setText("当前仓库：" + currentWarehouse.getWarehouseName());

        // 添加其他仓库作为下拉菜单项
        for (WarehouseDO w : warehouseList) {
            if (w.getWarehouseId().equals(currentWarehouse.getWarehouseId())) {
                continue; // 跳过当前仓库
            }

            JMenuItem item = new JMenuItem(w.getWarehouseName());
            item.addActionListener(e -> {
                currentWarehouse = w;
                updateWarehouseMenu(warehouseList); // 重新设置标题和列表
            });

            //warehouseMenu.add(item);
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        //new MainView();
    }

    public void reloadTable() {
        Vector<String> columns = null;
        if (currentView == ViewType.GOODS) {
            TableDTO goodsDto = getGoodsTableDTO();

            GoodsTableModel.updateModel(goodsDto.getData());
            mainViewTable.setModel(GoodsTableModel.assembleModel(goodsDto.getData())); // 设置表格模型
            columns = GoodsTableModel.getColumns();

            showPreNext(goodsDto.getTotalCount());
        } else if (currentView == ViewType.WAREHOUSE) {
            TableDTO warehouseDto = getWarehouseTableDTO();

            WarehouseTableModel.updateModel(warehouseDto.getData());
            mainViewTable.setModel(WarehouseTableModel.assembleModel(warehouseDto.getData()));
            columns = WarehouseTableModel.getColumns();

            showPreNext(warehouseDto.getTotalCount());
        } else if (currentView == ViewType.EMPLOYEE) {
            TableDTO employeeDto = getEmployeeTableDTO();

            EmployeeTableModel.updateModel(employeeDto.getData());
            mainViewTable.setModel(EmployeeTableModel.assembleModel(employeeDto.getData()));
            columns = EmployeeTableModel.getColumns();

            showPreNext(employeeDto.getTotalCount());
        }
        assert columns != null;
        mainViewTable.renderRule(columns);
    }

    private void showPreNext(int totalCount) {
        if (pageNow == 1) {
            preBtn.setEnabled(false);
            preBtn.setToolTipText("已是第一页");
        } else {
            preBtn.setEnabled(true);
        }
        int pageCount = 0;
        if (totalCount % pageSize == 0) {
            pageCount = totalCount / pageSize;
        } else {
            pageCount = totalCount / pageSize + 1;
        }
        if (pageNow == pageCount) {
            nextBtn.setToolTipText("已是最后一页");
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }
    }

    private TableDTO getGoodsTableDTO() {
        GoodsService goodsService = new GoodsServiceImpl();
        GoodsRequest goodsRequest = new GoodsRequest();
        goodsRequest.setPageNow(pageNow);
        goodsRequest.setPageSize(pageSize);
        goodsRequest.setSearchKey(searchField.getText().trim());
        TableDTO tableDTO;
        tableDTO = goodsService.retrieveGoods(goodsRequest);
        return tableDTO;
    }

    private TableDTO getWarehouseTableDTO() {
        WarehouseService warehouseService = new WarehouseServiceImpl();
        WarehouseRequest warehouseRequest = new WarehouseRequest();
        warehouseRequest.setPageNow(pageNow);
        warehouseRequest.setPageSize(pageSize);
        warehouseRequest.setSearchKey(searchField.getText().trim());
        TableDTO tableDTO;
        tableDTO = warehouseService.retrieveWarehouse(warehouseRequest);
        return tableDTO;
    }

    private TableDTO getEmployeeTableDTO() {
        EmployeeService employeeService = new EmployeeServiceImpl();
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setPageNow(pageNow);
        employeeRequest.setPageSize(pageSize);
        employeeRequest.setSearchKey(searchField.getText().trim());
        TableDTO tableDTO;
        tableDTO = employeeService.retrieveEmployee(employeeRequest);
        return tableDTO;
    }

    // 获取选中的行
    public int[] getSelectedGoodsId() {
        int[] selectedRows = mainViewTable.getSelectedRows();
        int[] ids = new int[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            int rowIndex = selectedRows[i];
            Object idObj = mainViewTable.getValueAt(rowIndex, 0);
            ids[i] = Integer.parseInt(idObj.toString());
        }
        return ids;
    }
}
