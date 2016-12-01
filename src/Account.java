public class Account{
	private int id;
	private String user;
	private String pass;
	
	/**
	 * Default constructor
	 */
	public Account() {
		id = 0;
		user = "";
		pass = "";
	}
	/**
	 * Constructor which passes in account values, a "logged in" account
	 * @param id
	 * @param user
	 * @param pass
	 */
	public Account(int id, String user, String pass) {
		this.id = id;
		this.user = user;
		this.pass = pass;
	}
	/**
	 * Simple to String
	 * @return String
	 */
	public String toString(){
		return "ID: " + id + "[User: " + user + " / Pass: " + pass + "]";
	}

	//Standard getters and setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
}