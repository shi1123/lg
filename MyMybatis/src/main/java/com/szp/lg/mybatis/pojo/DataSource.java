package com.szp.lg.mybatis.pojo;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private String driverClass;
    private String jdbcUrl;
    private String username;
    private String password;


    @Override
    public String toString() {
        return "DataSource{" +
                "driverClass='" + driverClass + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", props=" + props +
                '}';
    }

    public List<Property> getProps() {
        return props;
    }

    public void setProps(List<Property> props) {
        this.props = props;
    }
    private List<Property> props = new ArrayList<Property>();

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
