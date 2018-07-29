package com.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Hello extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DBfunctions bfunctions = new DBfunctions();

	public Hello() {
		// TODO Auto-generated constructor stub
		System.out.println("constr");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.err.println(request.getPathInfo());
		response.setContentType("text/html");

		switch (request.getPathInfo().replace("/", "")) {
		case "user":
			response.getWriter().append(
					"<b>Save User</<b><form name='loginForm' method='post' action='http://localhost:8080/TestPro/Hello/user'> Username: <input type='text' name='username'/> <br/> Email: <input type='text' name='email'/> <br/>Password: <input type='password' name='password'/> <br/> <input type='submit' value='Login'/></form>");
			break;

		case "task":
			response.getWriter().append(
					"<b>Save task</<b><form name='taskForm' method='post' action='http://localhost:8080/TestPro/Hello/task'> Email: <input type='text' name='email'/> <br/>Task: <input type='text' name='task'/> <br/> <input type='submit' value='Submit'/></form>");
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		switch (request.getPathInfo().replace("/", "")) {
		case "task":
			String email = request.getParameter("email");
			String task = request.getParameter("task");

			try {
				bfunctions.createTODO(email, task);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			response.getWriter().append(
					"<b>Save task</<b><form name='taskForm' method='post' action='http://localhost:8080/TestPro/Hello/task'> Email: <input type='text' name='email'/> <br/>Task: <input type='text' name='task'/> <br/> <input type='submit' value='Submit'/></form>");

			System.out.println("Task Info");
			System.out.println(email);
			System.out.println(task);

			break;

		case "user":
			String username = request.getParameter("username");
			String useremail = request.getParameter("email");
			String password = request.getParameter("password");
			
			try {
				if (bfunctions.checkUser(useremail, password)) {

					String[] todo_list=bfunctions.getTODOforUser(useremail);
					if(todo_list.length>0) {
						response.setContentType("text/html");
						PrintWriter out = response.getWriter();
						out.println("<h3>TODO list of " + username + "</h3>");
						for (int i = 0; i < todo_list.length; i++) {
							//out.println(todo_list[i]);
							
							out.println("<input type=\"checkbox\" name=\"todo\" value=\"todos\">"  +todo_list[i]+"<br>");
							
						}
						out.println("<br><br><a href=\"http://localhost:8080/TestPro/Hello/user\">Logout</a>");
						out.println("<a href=\"http://localhost:8080/TestPro/Hello/task\">add task</a>");
					}

				}else {
					bfunctions.createUser(username, useremail, password);
					response.getWriter().append(
							"<b>Save User</<b><form name='loginForm' method='post' action='http://localhost:8080/TestPro/Hello/user'> Username: <input type='text' name='username'/> <br/> Email: <input type='text' name='email'/> <br/>Password: <input type='password' name='password'/> <br/> <input type='submit' value='Login'/></form>");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.err.println("user Info");
			System.err.println(username);
			System.err.println(useremail);
			System.err.println(password);
			break;

		}
	}
}
