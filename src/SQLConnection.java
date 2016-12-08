import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class SQLConnection {
	private HashMap<String, String> conMap;
	private ArrayList<String> tables;
	private Connection conn;
	private int id;

	/**
	 * Creates the iSQL Connection and the map which contains the references for
	 * the various bits of information needed
	 * 
	 * @see Map Keys: Address, Username, Password, Database, Tables
	 * @param conMap
	 * 
	 */
	public SQLConnection(HashMap<String, String> conMap) {
		this.conMap = conMap;
		this.tables = new ArrayList<String>();
		for (String s : conMap.get("Tables").split("/"))
			if (s.length() > 0)
				tables.add(s);
		try {
			getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", conMap.get("Username"));
		connectionProps.put("password", conMap.get("Password"));
		conn = DriverManager.getConnection(conMap.get("Address"), connectionProps);
		System.out.println("Connected to Server");
	}

	public boolean login(String username, String password) {
		Statement stmt = null;
		String query = "select * from " + conMap.get("Database") + "." + tables.get(0);
		try {
			stmt = conn.createStatement();
			ResultSet data = stmt.executeQuery(query);
			while (data.next()) {
				String dbUserName = data.getString("username");
				System.out.println("dbUsername: " + dbUserName);
				String dbPassword = data.getString("password");
				System.out.println("dbPassword: " + dbPassword);
				if (username.equals(dbUserName) && password.equals(dbPassword)) {
					id = data.getInt("id");
					stmt.close();
					return true;
				}
			}
			stmt.close();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean createAccount(String username, String password) {
		Statement stmt = null;
		String update = "insert into " + conMap.get("Database") + "." + tables.get(0) + "(username, password) values(\""
				+ username + "\", \"" + password + "\");";
		// select id from final_project.user_accounts where username = "admin";
		String query = "select id from " + conMap.get("Database") + "." + tables.get(0) + " where username = \""
				+ username + "\"";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(update);
			return login(username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void viewTable(Connection con, int tableNum) throws SQLException {

		Statement stmt = null;
		String query = "select * from " + conMap.get("Database") + "." + tables.get(tableNum);
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String userName = rs.getString("username");
				System.out.println(userName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public String runCmd(String cmd) {
		try {
			Statement stmt = conn.createStatement();
			if (stmt.execute(cmd)) {
				String toReturn = "";
				ResultSet rs = stmt.getResultSet();
				ResultSetMetaData metaData = rs.getMetaData();
				String[] cols = new String[metaData.getColumnCount() - 1];
				if (metaData.getColumnCount() > 1) {
					for (int c1 = 1; c1 < metaData.getColumnCount(); c1++) {
						System.out.println(metaData.getColumnLabel(c1));
						cols[c1 - 1] = metaData.getColumnLabel(c1);
						System.out.println(cols[c1 - 1]);
					}
					for (String s : cols)
						System.out.println(s);
					while (rs.next()) {
						String lineTemp = "";
						for (int c1 = 0; c1 < cols.length; c1++) {
							lineTemp += rs.getObject(cols[c1]) + "/";
						}
						toReturn += lineTemp.replaceAll("(.{80})", "$1\n\t") + "\n";
					}
				}else{
					while(rs.next()){
						toReturn += rs.getObject(1) + "\n";
					}
				}

				System.out.println("Returning:\n" + toReturn);
				stmt.close();
				return toReturn;
			}else{
				return "Command Executed Successfully";
			}
		} catch (SQLException e) {
			System.out.println("SQL error");
			return e.toString();
		}
	}

	public void test() {
		try {
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery("select * from final_project.user_accounts;");
			ResultSetMetaData meta = rs.getMetaData();
			for (int c1 = 1; c1 < meta.getColumnCount(); c1++) {
				System.out.println(meta.getColumnTypeName(c1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<Integer, String[]> getScoreboard() {
		HashMap<Integer, String[]> board = new HashMap<Integer, String[]>();
		String query = "select * from " + conMap.get("Database") + "." + tables.get(1);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				board.put(rs.getInt("id"), new String[] { rs.getString("username"), "" + rs.getFloat("highscore") });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return board;
	}
	
	public void setHighscore(int score, int id){
		try{
		String update = "update " + conMap.get("Database") + "." + tables.get(1) + " set highscore = " + score +
				"where id = " + id;
		}catch(Exception e){
			System.out.println(":(");
		}
	}

	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
}