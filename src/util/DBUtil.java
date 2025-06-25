package util;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/whms?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PWD = "401025";

    // 加载驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    // 获取连接方法
    public static Connection getConn() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PWD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //关闭资源方法
    public static void closeConn(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closePs(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
