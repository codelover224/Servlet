package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBfunctions {
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet=null;

	public void establishConnection() throws ClassNotFoundException, SQLException {
		final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
		Class.forName(DRIVER_CLASS);
		connection = DriverManager.getConnection("jdbc:mysql://localhost/todoApp?user=krushna&password=password");
		statement = connection.createStatement();
	}

	public void createUser(String name, String email, String password) throws ClassNotFoundException, SQLException {
		establishConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into todoAppUser values " + "(?,?,?" + ")");
		preparedStatement.setString(1, email);
		preparedStatement.setString(2, name);
		preparedStatement.setString(3, password);
		preparedStatement.executeUpdate();
	}

	public boolean checkUser(String userEmail, String userPassword) throws SQLException, ClassNotFoundException {
		establishConnection();
		boolean user_exists = false;
		
		

		 resultSet = statement.executeQuery("Select * from todoAppUser");
		 /*Select * from todoAppUser where email = ? and passsword=?;*/
		if(resultSet!=null) {
			while(resultSet.next() || !user_exists) {
				String email_id = resultSet.getString("email");
				String password_id = resultSet.getString("passsword");
				if(userEmail.equals(email_id) && userPassword.equals(password_id)) {
					user_exists=true;
				}
			}
		}
		
		return user_exists;

	}

	public String[] getTODOforUser(String userEmail) throws ClassNotFoundException, SQLException {

		establishConnection();
		ArrayList<String> todo = new ArrayList<>();
		ResultSet resultSet = statement.executeQuery("Select * from todos where email='" +userEmail +"';");
		//PreparedStatement preparedStatement = connection.prepareStatement("Select * from todos where email=?;");
		//preparedStatement.setString(1, userEmail);
		//preparedStatement.executeUpdate();
		//ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet != null) {
			while (resultSet.next()) {
				String todos = resultSet.getString("todo_desc");
				todo.add(todos);
				System.out.println(todo);
			}
		}
		
		return todo.toArray(new String[todo.size()]);

	}

	public void createTODO(String userEmail, String TODOmessage,int status) throws ClassNotFoundException, SQLException {
		establishConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("insert into todos values " + "(?,?,?,0)");
		preparedStatement.setString(1, userEmail);
		preparedStatement.setString(2, TODOmessage);
		preparedStatement.setInt(3, 0);
		preparedStatement.executeUpdate();
	}

	public void updateTODO(int status,String userEmail,String TODOmessage) throws ClassNotFoundException, SQLException {
		establishConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("update todos set status =? where email=? and todo_desc=?;");
		preparedStatement.setInt(1, status);
		preparedStatement.setString(2, userEmail);
		preparedStatement.setString(3, TODOmessage);
		preparedStatement.executeUpdate();
	}

}
