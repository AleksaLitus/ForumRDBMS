package com.aleksalitus.forumrdbms.util;

import java.sql.Connection;
import java.sql.SQLException;

public final class JdbcUtils {
	
	private static boolean initialized;
	
	private JdbcUtils(){
		
	}

	public static synchronized void initDriver(String driverClass) {
		if (!initialized) {
				try {
					Class.forName(driverClass).newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			initialized = true;
		}
	}

	public static void closeResourses(AutoCloseable... resources) {
		for (AutoCloseable ac : resources) {
			if (ac != null) {
				try {
					ac.close();
				} catch (Exception e) {
					// NOP
				}
			}
		}
	}

	public static void rollbackQuietly(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				// NOP
			}
		}
	}
}
