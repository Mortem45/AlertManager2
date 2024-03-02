package com.bmonterrozo.alertmanager.jobs.services;

import com.bmonterrozo.alertmanager.entity.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SqlService {

    private static final Logger LOG = LoggerFactory.getLogger(SqlService.class);

    public Map<String, Long> checkOracleAlert(Alert alert) {
        String url = alert.getSourceGroup().getRandomDataSource().getUrl();
        String user = alert.getSourceGroup().getRandomDataSource().getUsername();
        String password = alert.getSourceGroup().getRandomDataSource().getPassword();
        String query = alert.getSearch();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Long> result = new HashMap<>();
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setReadOnly(true);
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();


            while (rs.next()) {
                String name = rs.getString(1);
                Long count = rs.getLong(2);
                result.put(name, count);
            }
        } catch (SQLException e) {
            LOG.error("checkOracleAlert - Error in SQL connection: ", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOG.error("checkOracleAlert - Error in Closing SQL connection: ", e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

}
