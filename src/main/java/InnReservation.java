import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Map;
import java.util.Scanner;


import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/*
Introductory JDBC examples based loosely on the BAKERY dataset from CSC 365 labs.
 */
public class InnReservation {

    private final String JDBC_URL = "jdbc:h2:~/csc365_lab7";
    private final String JDBC_USER = "";
    private final String JDBC_PASSWORD = "";
    
    public static void main(String[] args) {
	try {
	    InnReservation hp = new InnReservation();
            hp.initDb();
	    hp.demo1();
	} catch (SQLException e) {
	    System.err.println("SQLException: " + e.getMessage());
	}
	}
	
	private void initDb() throws SQLException {
		try (Connection conn = DriverManager.getConnection(JDBC_URL,
								   JDBC_USER,
								   JDBC_PASSWORD)) {
			try (Statement stmt = conn.createStatement()) {
					stmt.execute("DROP TABLE IF EXISTS hp_goods");
					stmt.execute("CREATE TABLE hp_goods (GId varchar(15) PRIMARY KEY, Food varchar(100), Flavor varchar(100), Price DECIMAL(5,1), AvailUntil DATE)");
					stmt.execute("INSERT INTO hp_goods (GId, Flavor, Food, Price) VALUES ('L1', 'Lemon', 'Cake', 20.0)");
					stmt.execute("INSERT INTO hp_goods (GId, Flavor, Food, Price) VALUES ('L2', 'Lemon', 'Twist', 3.50)");
					stmt.execute("INSERT INTO hp_goods (GId, Flavor, Food, Price) VALUES ('A3', 'Almond', 'Twist', 4.50)");
					stmt.execute("INSERT INTO hp_goods (GId, Flavor, Food, Price) VALUES ('A4', 'Almond', 'Cookie', 4.50)");
					stmt.execute("INSERT INTO hp_goods (GId, Flavor, Food, Price) VALUES ('L5', 'Lemon', 'Cookie', 1.50)");
					stmt.execute("INSERT INTO hp_goods (GId, Flavor, Food, Price) VALUES ('A6', 'Almond', 'Danish', 2.50)");

					//lab7_reservations
					stmt.execute("DROP TABLE IF EXISTS lab7_reservations");
					stmt.execute("CREATE TABLE lab7_reservations(Code INT(11), Room CHAR(5), CheckIn DATE, Checkout DATE, Rate FLOAT, LastName VARCHAR(15), FirstName VARCHAR(15), Adults INT(3), Kids INT(3),PRIMARY KEY (Code))");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10105, 'HBB', '2010-10-23', '2010-10-25', 100, 'SLEBIG', 'CONRAD', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10183, 'IBD', '2010-09-19', '2010-09-20', 150, 'GABLER', 'DOLLIE', 2, 0)");

					stmt.execute("DROP TABLE IF EXISTS lab7_rooms");
					stmt.execute("CREATE TABLE lab7_rooms(RoomCode CHAR(5), RoomName VARCHAR(30), Beds INT(3), BedType VARCHAR(8), MaxOcc INT(3), BasePrice FLOAT, Decor VARCHAR(20))");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('IBD', 'Immutable before decorum', 2, 'Queen', 4, 150, 'rustic')");
			}
		}
	}
    
    // Demo1 - Establish JDBC connection, execute DDL statement
    private void demo1() throws SQLException {

	// Step 0: Load JDBC Driver
	// No longer required as of JDBC 2.0  / Java 6
	try{
	    Class.forName("org.h2.Driver");
	    System.out.println("H2 JDBC Driver loaded");
	} catch (ClassNotFoundException ex) {
	    System.err.println("Unable to load JDBC Driver");
	    System.exit(-1);
	}

	// Step 1: Establish connection to RDBMS
	try (Connection conn = DriverManager.getConnection(JDBC_URL,
							   JDBC_USER,
							   JDBC_PASSWORD)) {
	    // Step 2: Construct SQL statement
		String sql = "SELECT * FROM lab7_reservations";
		String sql1 = "SELECT * FROM lab7_rooms";

	    // Step 3: (omitted in this example) Start transaction

	    try (Statement stmt = conn.createStatement()) {

		// Step 4: Send SQL statement to DBMS
		ResultSet rs = stmt.executeQuery(sql);
		
		// Step 5: Handle results
		while (rs.next())
		{
			Integer code = rs.getInt("Code");
			String room = rs.getString("Room");
			Date checkIn = rs.getDate("CheckIn");
			Date checkOut = rs.getDate("Checkout");
			Float rate = rs.getFloat("Rate");
			String lastName = rs.getString("LastName");
			String firstName = rs.getString("FirstName");
			Integer adults = rs.getInt("Adults");
			Integer kids = rs.getInt("Kids");
			System.out.format("%d, %s, %s, %s, %.2f, %s, %s, %d, %d %n", code, room, checkIn, checkOut, rate, lastName, firstName, adults, kids);
		}
		
		rs = stmt.executeQuery(sql1);
		while (rs.next())
		{
			String roomCode = rs.getString("RoomCode");
			String roomName = rs.getString("RoomName");
			Integer beds = rs.getInt("Beds");
			String bedType = rs.getString("BedType");
			Integer maxOcc = rs.getInt("MaxOcc");
			Float basePrice = rs.getFloat("BasePrice");
			String decor = rs.getString("Decor");
			
			System.out.format("%s, %s, %d, %s, %d, %.2f, %s", roomCode, roomName, beds, bedType, maxOcc, basePrice, decor);
		}
	}
	    // Step 6: (omitted in this example) Commit or rollback transaction
	}
	// Step 7: Close connection (handled by try-with-resources syntax)
    }
    

    // // Demo2 - Establish JDBC connection, execute SELECT query, read & print result
    // private void demo2() throws SQLException {

	// // Step 1: Establish connection to RDBMS
	// try (Connection conn = DriverManager.getConnection(JDBC_URL,
	// 						   JDBC_USER,
	// 						   JDBC_PASSWORD)) {
	//     // Step 2: Construct SQL statement
	//     String sql = "SELECT * FROM hp_goods";

	//     // Step 3: (omitted in this example) Start transaction

	//     // Step 4: Send SQL statement to DBMS
	//     try (Statement stmt = conn.createStatement();
	// 	 ResultSet rs = stmt.executeQuery(sql)) {

	// 	// Step 5: Receive results
	// 	while (rs.next()) {
	// 	    String flavor = rs.getString("Flavor");
	// 	    String food = rs.getString("Food");
	// 	    float price = rs.getFloat("Price");
	// 	    System.out.format("%s %s ($%.2f) %n", flavor, food, price);
	// 	}
	//     }

	//     // Step 6: (omitted in this example) Commit or rollback transaction
	// }
	// // Step 7: Close connection (handled by try-with-resources syntax)
    // }


    // // Demo3 - Establish JDBC connection, execute DML query (UPDATE)
    // // -------------------------------------------
    // // Never (ever) write database code like this!
    // // -------------------------------------------
    // private void demo3() throws SQLException {

    //     demo2();
        
	// // Step 1: Establish connection to RDBMS
	// try (Connection conn = DriverManager.getConnection(JDBC_URL,
	// 						   JDBC_USER,
	// 						   JDBC_PASSWORD)) {
	//     // Step 2: Construct SQL statement
	//     Scanner scanner = new Scanner(System.in);
	//     System.out.print("Enter a flavor: ");
	//     String flavor = scanner.nextLine();
	//     System.out.format("Until what date will %s be available (YYYY-MM-DD)? ", flavor);
	//     String availUntilDate = scanner.nextLine();

	//     // -------------------------------------------
	//     // Never (ever) write database code like this!
	//     // -------------------------------------------
	//     String updateSql = "UPDATE hp_goods SET AvailUntil = '" + availUntilDate + "' " +
	// 	               "WHERE Flavor = '" + flavor + "'";

	//     // Step 3: (omitted in this example) Start transaction
	    
	//     try (Statement stmt = conn.createStatement()) {
		
	// 	// Step 4: Send SQL statement to DBMS
	// 	int rowCount = stmt.executeUpdate(updateSql);
		
	// 	// Step 5: Handle results
	// 	System.out.format("Updated %d records for %s pastries%n", rowCount, flavor);		
	//     }

	//     // Step 6: (omitted in this example) Commit or rollback transaction
	    
	// }
	// // Step 7: Close connection (handled implcitly by try-with-resources syntax)

    //     demo2();
        
    // }


    // // Demo4 - Establish JDBC connection, execute DML query (UPDATE) using PreparedStatement / transaction    
    // private void demo4() throws SQLException {

	// // Step 1: Establish connection to RDBMS
	// try (Connection conn = DriverManager.getConnection(JDBC_URL,
	// 						   JDBC_USER,
	// 						   JDBC_PASSWORD)) {
	//     // Step 2: Construct SQL statement
	//     Scanner scanner = new Scanner(System.in);
	//     System.out.print("Enter a flavor: ");
	//     String flavor = scanner.nextLine();
	//     System.out.format("Until what date will %s be available (YYYY-MM-DD)? ", flavor);
	//     LocalDate availDt = LocalDate.parse(scanner.nextLine());
	    
	//     String updateSql = "UPDATE hp_goods SET AvailUntil = ? WHERE Flavor = ?";

	//     // Step 3: Start transaction
	//     conn.setAutoCommit(false);
	    
	//     try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
		
	// 	// Step 4: Send SQL statement to DBMS
	// 	pstmt.setDate(1, java.sql.Date.valueOf(availDt));
	// 	pstmt.setString(2, flavor);
	// 	int rowCount = pstmt.executeUpdate();
		
	// 	// Step 5: Handle results
	// 	System.out.format("Updated %d records for %s pastries%n", rowCount, flavor);

	// 	// Step 6: Commit or rollback transaction
	// 	conn.commit();
	//     } catch (SQLException e) {
	// 	conn.rollback();
	//     }

	// }
	// // Step 7: Close connection (handled implcitly by try-with-resources syntax)
    // }



    // // Demo5 - Construct a query using PreparedStatement
    // private void demo5() throws SQLException {

	// // Step 1: Establish connection to RDBMS
	// try (Connection conn = DriverManager.getConnection(JDBC_URL,
	// 						   JDBC_USER,
	// 						   JDBC_PASSWORD)) {
	//     Scanner scanner = new Scanner(System.in);
	//     System.out.print("Find pastries with price <=: ");
	//     Double price = Double.valueOf(scanner.nextLine());
	//     System.out.print("Filter by flavor (or 'Any'): ");
	//     String flavor = scanner.nextLine();

	//     List<Object> params = new ArrayList<Object>();
	//     params.add(price);
	//     StringBuilder sb = new StringBuilder("SELECT * FROM hp_goods WHERE price <= ?");
	//     if (!"any".equalsIgnoreCase(flavor)) {
	// 	sb.append(" AND Flavor = ?");
	// 	params.add(flavor);
	//     }
	    
	//     try (PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {
	// 	int i = 1;
	// 	for (Object p : params) {
	// 	    pstmt.setObject(i++, p);
	// 	}

	// 	try (ResultSet rs = pstmt.executeQuery()) {
	// 	    System.out.println("Matching Pastries:");
	// 	    int matchCount = 0;
	// 	    while (rs.next()) {
	// 		System.out.format("%s %s ($%.2f) %n", rs.getString("Flavor"), rs.getString("Food"), rs.getDouble("price"));
	// 		matchCount++;
	// 	    }
	// 	    System.out.format("----------------------%nFound %d match%s %n", matchCount, matchCount == 1 ? "" : "es");
	// 	}
	//     }

	// }
    // }


   
    

}

