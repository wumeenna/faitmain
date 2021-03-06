package com.faitmain.sample;

import static org.assertj.core.api.Assertions.catchException;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

public class MySQLConnectionTest {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/fait_main?userSSL=false&serverTimezone=Asia/Seoul";
	private static final String USER = "root";
	private static final String PASSWORD = "1234";
	
	@Test
	public void testConnection() throws Exception{
		Class.forName(DRIVER);
		try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
			System.out.println(connection);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
