package res;

import java.util.Vector;

public class TableDTO {
    // 从数据库获取表格的数据以及总记录数返回给后端处理
    private Vector<Vector<Object>> data;
    private int totalCount;  // 总记录数，用来判断上一页下一页按钮是否可用

    public Vector<Vector<Object>> getData() {
        return data;
    }

    public void setData(Vector<Vector<Object>> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
