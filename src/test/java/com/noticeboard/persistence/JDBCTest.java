package com.noticeboard.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class JDBCTest {
	static { 
        try { 
        	Class<?> dbDriver = Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("마리아디비 드라이버(" + dbDriver.toString() + ")가 로딩됨");
        } catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 
    
    @Test 
    public void testConnection() { 
        try(Connection con = DriverManager.getConnection( 
                "jdbc:mariadb://localhost:3306/test?serverTimezone=Asia/Seoul", 
                "root", 
                "1234")){ 
            System.out.println(con); 
        } catch (Exception e) { 
            fail(e.getMessage()); 
        } 
    
    }
}
