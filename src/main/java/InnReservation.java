import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			String option;
			InnReservation hp = new InnReservation();
			hp.initDb();
			hp.demo1();
			printOption();
			while(!(option = new Scanner(System.in).next()).equals("Q"))
			{
				switch(option) {
					case "FR1":
						hp.fr1();
						break;
					case "FR2":
						hp.parseReservationInput();
						break;
					case "FR3":
						break;
					case "FR4":
						break;
					case "FR5":
						break;
					case "Q":
						System.exit(0);
					default:
						System.out.println("Error: Command not found");
						printOption();
						break;
				}
			}

		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}

	public static void printOption() {
		System.out.println("\nPlease choose one of the following options:");
		System.out.println("FR1: Rooms and Rates\nFR2: Reservations\nFR3: Reservation Change\nFR4: Reservation Cancellation\nFR5: Revenue Summary\nQ: Quit");
		System.out.print("Your option: ");
	}
	
	private void initDb() throws SQLException {
		try (Connection conn = DriverManager.getConnection(JDBC_URL,
								   JDBC_USER,
								   JDBC_PASSWORD)) {
			try (Statement stmt = conn.createStatement()) {
					stmt.execute("DROP TABLE IF EXISTS lab7_reservations");
					stmt.execute("DROP TABLE IF EXISTS lab7_rooms");

					stmt.execute("CREATE TABLE lab7_rooms(RoomCode CHAR(5), RoomName VARCHAR(30), Beds INT(3), BedType VARCHAR(8), MaxOcc INT(3), BasePrice FLOAT, Decor VARCHAR(20), PRIMARY KEY (RoomCode))");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('AOB', 'Abscond or bolster', 2, 'Queen', 4, 175, 'traditional')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('CAS', 'Convoke and sanguine', 2, 'King', 4, 175, 'traditional')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('FNA', 'Frugal not apropos', 2, 'King', 4, 250, 'traditional')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('HBB', 'Harbinger but bequest', 1, 'Queen', 2, 100, 'modern')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('IBD', 'Immutable before decorum', 2, 'Queen', 4, 150, 'rustic')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('IBS', 'Interim but salutary', 1, 'King', 2, 150, 'traditional')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('MWC', 'Mendicant with cryptic', 2, 'Double', 4, 125, 'modern')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('RND', 'Recluse and defiance', 1, 'King', 1, 150, 'modern')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('RTE', 'Riddle to exculpate', 2, 'Queen', 4, 175, 'rustic')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('SAY', 'Stay all year (added May 19th)', 1, 'Queen', 3, 100, 'modern')");
					stmt.execute("INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('TAA', 'Thrift and accolade', 1, 'Double', 2, 75, 'modern')");

					//lab7_reservations
					stmt.execute("DROP TABLE IF EXISTS lab7_reservations");
					stmt.execute("CREATE TABLE lab7_reservations(Code INT(11), Room CHAR(5), CheckIn DATE, Checkout DATE, Rate FLOAT, LastName VARCHAR(15), FirstName VARCHAR(15), Adults INT(3), Kids INT(3),PRIMARY KEY (Code), FOREIGN KEY (Room) REFERENCES lab7_rooms(RoomCode))");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10498, 'AOB', '2010-02-02', '2010-02-05', 218.75, 'CARISTO', 'MARKITA', 2, 1)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10990, 'CAS', '2010-09-21', '2010-09-27', 175, 'TRACHSEL', 'DAMIEN', 1, 3)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10574, 'FNA', '2010-11-26', '2010-12-03', 287.5, 'SWEAZY', 'ROY', 2, 1)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10105, 'HBB', '2010-10-23', '2010-10-25', 100, 'SLEBIG', 'CONRAD', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10183, 'IBD', '2010-09-19', '2010-09-20', 150, 'GABLER', 'DOLLIE', 2, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (11996, 'IBS', '2010-09-14', '2010-09-16', 197.5, 'BURBANK', 'ROBERT', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (14273, 'MWC', '2010-05-28', '2010-05-30', 125, 'STARE', 'ELIJAH', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10449, 'RND', '2010-09-30', '2010-10-01', 150, 'KLESS', 'NELSON', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (12142, 'RTE', '2010-08-13', '2010-08-23', 175, 'JUNOR', 'LENNY', 3, 1)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (99999, 'SAY', '2009-12-12', '2011-01-15', 99, 'ARNN', 'KIP', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (83481, 'TAA', '2010-01-03', '2010-01-07', 67.5, 'ENGELSON', 'MIKKI', 2, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10107, 'HBB', '2020-05-01', '2020-06-11', 100, 'SLEBIG', 'CONRAD', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10188, 'AOB', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10578, 'FNA', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)");

					
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
			
			System.out.format("%s, %s, %d, %s, %d, %.2f, %s %n", roomCode, roomName, beds, bedType, maxOcc, basePrice, decor);
		}
	}
	    // Step 6: (omitted in this example) Commit or rollback transaction
	}
	// Step 7: Close connection (handled by try-with-resources syntax)
    }
    

	private void fr1() throws SQLException 
	{
		String nextAvailableDate = "";
		String nextStartDate = "";
		List<String> codes = new ArrayList<String>();
		try
		{
			Class.forName("org.h2.Driver");
			System.out.println("H2 JDBC Driver loaded");
		}
		catch (ClassNotFoundException ex) 
		{
			System.err.println("Unable to load JDBC Driver");
			System.exit(-1);
		}
	
		try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD))
		{	
			try (Statement stmt = conn.createStatement()) 
			{
				ResultSet rs = stmt.executeQuery("SELECT * FROM lab7_rooms");
				while (rs.next())
				{
					String roomCode = rs.getString("RoomCode");
					String roomName = rs.getString("RoomName");
					Integer beds = rs.getInt("Beds");
					String bedType = rs.getString("BedType");
					Integer maxOcc = rs.getInt("MaxOcc");
					Float basePrice = rs.getFloat("BasePrice");
					String decor = rs.getString("Decor");

					try(Statement stmt1 = conn.createStatement())
					{
						ResultSet temp1 = stmt1.executeQuery("select Room, Checkout " +
														"from lab7_reservations " +
														"where Room = '" + roomCode +"' and CheckIn <= CURDATE() and Checkout > CURDATE()");
						if (temp1.next())
						{
							//if there is an entry => room is occupied, so the checkout date of the current reservation should be the next available date for checking in.
							nextAvailableDate = temp1.getString("Checkout");
							//now look for next start date
							try(Statement stmt2 = conn.createStatement())
							{
								ResultSet temp2 = stmt2.executeQuery("select Room, CheckIn " +
													"from lab7_reservations " + 
													"where Room = '" + roomCode + "' and CheckIn > '" + nextAvailableDate + "' " +
													"order by CheckIn");
								if (temp2.next())
								{
									nextStartDate = temp2.getString("CheckIn");
								}
								else
								{
									nextStartDate = "None";
								}
								temp2.close();
								stmt2.close();
							}
						}
						else
						{
							//if there is no entry => room is not occupied, so the next available date is today
							nextAvailableDate = "Today";
							try(Statement stmt2 = conn.createStatement())
							{
								ResultSet temp2 = stmt2.executeQuery("select Room, CheckIn " +
													"from lab7_reservations " + 
													"where Room = '" + roomCode + "' and CheckIn > CURDATE() " +
													"order by CheckIn");
								if (temp2.next())
								{
									nextStartDate = temp2.getString("CheckIn");
								}
								else
								{
									nextStartDate = "None";
								}
								temp2.close();
								stmt2.close();
							}
						}
						temp1.close();
						stmt1.close();
					}

					System.out.format("%s, %s, %d, %s, %d, %.2f, %s, %s, %s %n", roomCode, roomName, beds, bedType, maxOcc, basePrice, decor, nextAvailableDate, nextStartDate);
					
				}
			}/////
		}
			// Step 6: (omitted in this example) Commit or rollback transaction
	}
		// Step 7: Close connection (handled by try-with-resources syntax)
	

	private String[] parseReservationInput()
	{
		System.out.println("Please input first name, last name, room code, begin date, end date, number of children and number of adult in a comma separated list");
		String[] returnResult;
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String userInput = reader.readLine();
			returnResult = userInput.split("[, ?.@]+");
			for (String string : tokenizedInput) 
			{
				System.out.println(string);
			}
		}
		catch (IOException e)
		{
			System.err.println("SQLException: " + e.getMessage());
		}	
		return returnResult;	
	}

}


	private void fr2() throws SQLException
	{
		
	}

    /* // Demo2 - Establish JDBC connection, execute SELECT query, read & print result
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
    // }*/
    /* // // Demo3 - Establish JDBC connection, execute DML query (UPDATE)
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
        
    // }*/
    /*// // Demo4 - Establish JDBC connection, execute DML query (UPDATE) using PreparedStatement / transaction    
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
    // }*/
    /* // // Demo5 - Construct a query using PreparedStatement
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
    // }*/

