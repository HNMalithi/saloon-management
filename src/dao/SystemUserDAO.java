/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.SystemUser;

/**
 *
 * @author User
 */
public class SystemUserDAO {

    public boolean addSystemUser(SystemUser systemUser) throws Exception {
        return CrudUtil.executeUpdate("INSERT INTO systemUser VALUES(?,?,?,?,?,?,?,?) ",
                systemUser.getUserId(),
                systemUser.getFirstName(),
                systemUser.getLastName(),
                systemUser.getContactNo(),
                systemUser.getUserName(),
                systemUser.getPassword(),
                systemUser.getUserPost(),
                systemUser.getUserRegDate()
        ) > 0;
    }

    public boolean updateSystemUser(SystemUser systemUser) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE systemUser SET firstName=?, lastName=?, contactNo=?, userName=?, password=?, userPost=?, userRegDate=? WHERE userId=? ",
                systemUser.getFirstName(),
                systemUser.getLastName(),
                systemUser.getContactNo(),
                systemUser.getUserName(),
                systemUser.getPassword(),
                systemUser.getUserPost(),
                systemUser.getUserRegDate(),
                systemUser.getUserId()
        ) > 0;
    }

    public boolean deleteSystemUser(String userId) throws Exception {
        return CrudUtil.executeUpdate("DELETE FROM systemUser WHERE userId=? ", userId) > 0;
    }

    public SystemUser searchById(String id) throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM systemUser WHERE userId=? ", id);
        if (rst.next()) {
            return new SystemUser(
                    rst.getString("userId"),
                    rst.getString("firstName"),
                    rst.getString("lastName"),
                    rst.getString("contactNo"),
                    rst.getString("userName"),
                    rst.getString("password"),
                    rst.getString("userPost"),
                    rst.getString("userRegDate")
            );
        } else {
            return null;
        }
    }
    
    public SystemUser searchByUsername(String username) throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM systemUser WHERE username=? ", username);
        if (rst.next()) {
            return new SystemUser(
                    rst.getString("userId"),
                    rst.getString("firstName"),
                    rst.getString("lastName"),
                    rst.getString("contactNo"),
                    rst.getString("userName"),
                    rst.getString("password"),
                    rst.getString("userPost"),
                    rst.getString("userRegDate")
            );
        } else {
            return null;
        }
    }

    public SystemUser loginByUsername(String username, String password) throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM systemUser WHERE username=? AND password=? ", username, password);
        if (rst.next()) {
            return new SystemUser(
                    rst.getString("userId"),
                    rst.getString("firstName"),
                    rst.getString("lastName"),
                    rst.getString("contactNo"),
                    rst.getString("userName"),
                    rst.getString("password"),
                    rst.getString("userPost"),
                    rst.getString("userRegDate")
            );
        } else {
            return null;
        }
    }
    
    public ArrayList<SystemUser> getAllUsers() throws Exception {

        ArrayList<SystemUser> users = new ArrayList<>();
        ResultSet resultSet = CrudUtil.executeQuery("SELECT * FROM systemUser ");
        while (resultSet.next()) {
            users.add(new SystemUser(
                    resultSet.getString("userId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("contactNo"),
                    resultSet.getString("userName"),
                    resultSet.getString("password"),
                    resultSet.getString("userPost"),
                    resultSet.getString("userregDate")));
        }
        return users;
    }

}
