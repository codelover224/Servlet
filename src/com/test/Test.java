package com.test;

import java.sql.SQLException;

public class Test {

		public static void main(String[] args) throws ClassNotFoundException, SQLException {
			DBfunctions db = new DBfunctions();
			db.getTODOforUser("gaurav@gmail.com';delete from todos values ('gaurav','trick');'");
		}
}
