package service;

import entity.EmployeeDO;
import req.EmployeeRequest;
import req.GoodsRequest;
import res.TableDTO;

public interface EmployeeService {
    boolean validateEmployee(EmployeeDO employeeDO);

    TableDTO retrieveEmployee(EmployeeRequest goodsRequest);

    // 通过用户名查询用户信息
    EmployeeDO getEmployeeInfo(String username);
}
