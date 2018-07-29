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

		case "done": {
			String email = request.getParameter("email");
			String task = request.getParameter("task");
			int status = Integer.valueOf(request.getParameter("status"));
			System.out.println(email + "-->" + task + "-->" + status);
			try {
				bfunctions.updateTODO(status, email, task);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		case "task":
			String email = request.getParameter("email");
			String task = request.getParameter("task");

			try {
				bfunctions.createTODO(email, task, 0);
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

					String[] todo_list = bfunctions.getTODOforUser(useremail);
					if (todo_list.length > 0) {
						response.setContentType("text/html");
						PrintWriter out = response.getWriter();
						out.println("<h3>TODO list of " + username + "</h3>");
						for (int i = 0; i < todo_list.length; i++) {
							// out.println(todo_list[i]);

							/*
							 * out.println( "<input type=\"checkbox\" name=\"todo\" value=\"todos\">" +
							 * todo_list[i] + "<br>");
							 */
							out.println("<input type=\"checkbox\" email='" + useremail + "' todo='" + todo_list[i]
									+ "' onclick='handleClick(this);' name=\"todo\" value=\"todos\">" + todo_list[i]
									+ "<br>");

						}
						out.println("<br><br><a href=\"http://localhost:8080/TestPro/Hello/user\">Logout</a>");
						out.println("<a href=\"http://localhost:8080/TestPro/Hello/task\">add task</a>");
						out.println(getFunction());
					}

				} else {
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

	public String getFunction() {

		String function = "<script>%s</script>";

		return String.format(function,
				"function handleClick(checkBoxRefrence){\n" + "var email = checkBoxRefrence.getAttribute(\"email\");\n"
						+ "var todo = checkBoxRefrence.getAttribute(\"todo\");\n" + "var status = 0;\n" + " \n" + "\n"
						+ " if (checkBoxRefrence.checked == true){\n" + "     status = 1;\n" + "  } else {\n"
						+ "     status = 0;\n" + "  } \n" + "\n"
						+ "var data = \"email=\"+email+\"&task=\"+todo+\"&status=\"+status;\n" + "\n"
						+ "var xhr = new XMLHttpRequest();\n" + "xhr.withCredentials = true;\n" + "\n"
						+ "xhr.addEventListener(\"readystatechange\", function () {\n"
						+ "  if (this.readyState === 4) {\n"
						+ "    console.log(this.responseText);\nalert('status updated successfully');" + "  }\n"
						+ "});\n" + "\n" + "xhr.open(\"POST\", \"http://localhost:8080/TestPro/Hello/done\");\n"
						+ "xhr.setRequestHeader(\"content-type\", \"application/x-www-form-urlencoded\");\n"
						+ "xhr.setRequestHeader(\"cache-control\", \"no-cache\");\n"
						+ "xhr.setRequestHeader(\"postman-token\", \"33a647d4-0ebf-17f3-5c80-c00aa9e4f2ea\");\n" + "\n"
						+ "xhr.send(data);\n" + "\n" + "}");
	}

}
