import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

/**
 * 
 */

/**
 * @author ANITA Jun 6, 2017
 */
public class TestBnfGrp {
	static final String DB_URL = "jdbc:mysql://192.168.13.163:3306/compas_tbc";

	// Database credentials
	static final String USER = "anita";
	static final String PASS = "anita";

	public static void main(String[] args) {

		Connection conn = null;
		java.sql.PreparedStatement stmt = null;
		java.sql.PreparedStatement stmt1 = null;
		java.sql.PreparedStatement stmt2 = null;
		ResultSet resultset = null;
		ResultSet resultset1 = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql;
			String sql1;
			String sql2;
			System.out.println("Connecting to database...");
			conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
			sql1 = "select * from m_membermaster";

			stmt1 = conn.prepareStatement(sql1);
			resultset = stmt1.executeQuery();
			while (resultset.next()) {
				sql2 = "select * from mst_agegroup";
				stmt2 = conn.prepareStatement(sql2);
				//stmt2.setInt(1, resultset.getInt("emp_mst_id"));
				resultset1 = stmt2.executeQuery();
				sql = "insert into dtl_bnf_household (beneficiary_id,bnfgrp_id,agegrp_id,household_no,created_by,created_on) values (?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				//stmt.setInt(1, resultset.getInt("emp_id"));
				while (resultset1.next()) {
					stmt.setInt(1, resultset.getInt("id"));
					stmt.setInt(2, resultset.getInt("bnfgrpid"));
					stmt.setInt(3,  resultset1.getInt("id"));
					stmt.setInt(4, 0);
					stmt.setInt(5, 3);
					
					stmt.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					stmt.executeUpdate();
					System.out.println("insertted###"
							+ resultset.getInt("id"));
				}
			}

			// stmt.setString(1, );

			/*for (int i = 0; i < 20000; i++) {
				String cardno = "804400" + generateNumber();
				stmt.setString(1, cardno);
				stmt.executeUpdate();
				System.out.println(cardno); // Retrieve by column name

				// Display values

			}*/
			// rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {

			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");

	}
}
