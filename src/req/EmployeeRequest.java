package req;

public class EmployeeRequest {
    // 用于从前端获取当前物品表的当前页，查询词
    private int pageNow;
    private int pageSize;
    private int start;

    public int getStart() {
        return (pageNow - 1) * pageSize;
    }

    private String searchKey;

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
