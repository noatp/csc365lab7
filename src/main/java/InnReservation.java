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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

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
	private int nextAvailableReservationCode = 10015;
    
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
					case "demo":
						hp.demo1();
						break;
					case "FR1":
						hp.fr1();
						break;
					case "FR2":
						hp.fr2(hp.FR2ReservationInput());
						break;
					case "FR3":
						hp.fr3(hp.FR3ReservationInput());
						break;
					case "FR4":
						hp.fr4(hp.FR4ReservationInput());
						break;
					case "FR5":
						break;
					case "Q":
						System.exit(0);
					default:
						System.out.println("Error: Command not found");
						break;
				}
				printOption();
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
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10001, 'AOB', '2020-02-02', '2020-02-05', 218.75, 'CARISTO', 'MARKITA', 2, 1)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10002, 'CAS', '2020-09-21', '2020-09-27', 175, 'TRACHSEL', 'DAMIEN', 1, 3)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10003, 'FNA', '2020-11-26', '2020-12-03', 287.5, 'SWEAZY', 'ROY', 2, 1)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10004, 'HBB', '2020-10-23', '2020-10-25', 100, 'SLEBIG', 'CONRAD', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10005, 'IBD', '2020-09-19', '2020-09-20', 150, 'GABLER', 'DOLLIE', 2, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10006, 'IBS', '2020-09-14', '2020-09-16', 197.5, 'BURBANK', 'ROBERT', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10007, 'MWC', '2020-05-28', '2020-05-30', 125, 'STARE', 'ELIJAH', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10008, 'RND', '2020-09-30', '2020-10-01', 150, 'KLESS', 'NELSON', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10009, 'RTE', '2020-08-13', '2020-08-23', 175, 'JUNOR', 'LENNY', 3, 1)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10010, 'SAY', '2020-12-12', '2020-01-15', 99, 'ARNN', 'KIP', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10011, 'TAA', '2020-01-03', '2020-01-07', 67.5, 'ENGELSON', 'MIKKI', 2, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10012, 'HBB', '2020-05-01', '2020-06-11', 100, 'SLEBIG', 'CONRAD', 1, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10013, 'AOB', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)");
					stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10014, 'FNA', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)");

					
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
				rs.close();
				stmt.close();
			}
			conn.close();
		}
	}
	

	private String[] FR2ReservationInput()
	{
		try
		{
			String[] stringArray = new String[7];
			System.out.println("Please input these information:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("First name: ");
			stringArray[0] = reader.readLine();  // Read user input
			System.out.print("Last name: ");
			stringArray[1] = reader.readLine();
			System.out.print("Room code: ");
			stringArray[2] = reader.readLine();
			System.out.print("Begin date (YYYY-MM-DD): ");
			stringArray[3] = reader.readLine();
			System.out.print("End date (YYYY-MM-DD): ");
			stringArray[4] = reader.readLine();
			System.out.print("Number of children: ");
			stringArray[5] = reader.readLine();
			System.out.print("Number of adult: ");
			stringArray[6] = reader.readLine();
			//System.out.println(stringArray);
			return stringArray;
		}
		catch (IOException e)
		{
			System.err.println("SQLException: " + e.getMessage());
		}	
		return null;
	}

	private String[] FR3ReservationInput()
	{
		try
		{
			String[] stringArray = new String[7];
			System.out.println("Please input these information, enter NC if no change is needed:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Reservation code: ");
			stringArray[0] = reader.readLine();  // Read user input
			System.out.print("First name: ");
			stringArray[1] = reader.readLine();
			System.out.print("Last name: ");
			stringArray[2] = reader.readLine();
			System.out.print("Begin date (YYYY-MM-DD): ");
			stringArray[3] = reader.readLine();
			System.out.print("End date (YYYY-MM-DD): ");
			stringArray[4] = reader.readLine();
			System.out.print("Number of children: ");
			stringArray[5] = reader.readLine();
			System.out.print("Number of adult: ");
			stringArray[6] = reader.readLine();
			//System.out.println(stringArray);
			return stringArray;
		}
		catch (IOException e)
		{
			System.err.println("SQLException: " + e.getMessage());
		}	
		return null;
	}

	private String FR4ReservationInput()
	{
		try
		{
			String reservationCode = "";
			System.out.println("Please input these information:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Reservation code: ");
			reservationCode = reader.readLine();  // Read user input
			
			return reservationCode;
		}
		catch (IOException e)
		{
			System.err.println("SQLException: " + e.getMessage());
		}	
		return null;
	}

	private void fr2(String[] userInput) throws SQLException
	{
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
			try (PreparedStatement pstmt = conn.prepareStatement("SELECT * " +
																"FROM lab7_reservations " +
																"where Room = ? and (CheckIn <= ? and CheckOut > ?) or (CheckIn < ? and CheckIn > ?)"))
			{
				pstmt.setString(1, userInput[2]);
				pstmt.setString(2, userInput[3]);
				pstmt.setString(3, userInput[3]);
				pstmt.setString(4, userInput[4]);
				pstmt.setString(5, userInput[3]);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					System.out.println("Sorry, there is a conflict with another reservation in the system.");
				}
				else
				{
					try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * "+
																		"from lab7_rooms " +
																		" where RoomCode = ? and MaxOcc >= ?"))
					{
						pstmt1.setString(1, userInput[2]);
						pstmt1.setInt(2, Integer.parseInt(userInput[5]) + Integer.parseInt(userInput[6]));
						ResultSet rs1 = pstmt1.executeQuery();
						if (rs1.next())
						{
							System.out.println("New reservation has been added.");
							try (Statement stmt = conn.createStatement()) {
								stmt.execute("INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES ("
											+ Integer.toString(nextAvailableReservationCode) + ", '" 
											+ userInput[2] + "', '"
											+ userInput[3] + "', '"
											+ userInput[4] + "', 218.75, '"
											+ userInput[1] + "', '"
											+ userInput[0] + "', "
											+ userInput[6] + ", "
											+ userInput[5] + ")");
								stmt.close();			
							}
						}
						else
						{
							System.out.println("Sorry, the requested person count exceeds the maximum capacity of the selected room.");
						}
						rs1.close();
						pstmt1.close();
					}
				}
				rs.close();
				pstmt.close();
			}
			conn.close();
		}
	}

	private void fr3(String[] userInput) throws SQLException
	{
		String Room = "";
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

			try (PreparedStatement lookUp = conn.prepareStatement("SELECT * " +
																"from lab7_reservations " +
																"where Code = ?"))
			{
				lookUp.setInt(1, Integer.parseInt(userInput[0]));
				ResultSet lookUpResult = lookUp.executeQuery();
				if (lookUpResult.next())
				{
					System.out.println("Found a record");
					if (userInput[1].equals("NC"))
					{
						userInput[1] = lookUpResult.getString("FirstName");
					}
					if (userInput[2].equals("NC"))
					{
						userInput[2] = lookUpResult.getString("LastName");
					}
					if (userInput[3].equals("NC"))
					{
						userInput[3] = lookUpResult.getString("CheckIn");
					}
					if (userInput[4].equals("NC"))
					{
						userInput[4] = lookUpResult.getString("Checkout");
					}
					if (userInput[5].equals("NC"))
					{
						userInput[5] = Integer.toString(lookUpResult.getInt("Kids"));
					}
					if (userInput[6].equals("NC"))
					{
						userInput[6] = Integer.toString(lookUpResult.getInt("Adults"));
					}
					Room = lookUpResult.getString("Room");
					System.out.println("This is user input reservation code: " + userInput[0]);
					System.out.println("Which is of this room: " + Room);
				}
				else {
					System.out.println("There is no record found.");
					return;
				}
				lookUpResult.close();
				lookUp.close();
			}

			try (PreparedStatement pstmt = conn.prepareStatement("SELECT * " +
																"from lab7_reservations R1, (select Room from lab7_reservations where Code = ?) as R2 " +
																"where R1.Room = R2.Room and R1.Code <> ? and (CheckIn <= ? and CheckOut > ?) or (CheckIn < ? and CheckIn > ?)"))
			{
				pstmt.setInt(1, Integer.parseInt(userInput[0]));
				pstmt.setInt(2, Integer.parseInt(userInput[0]));
				pstmt.setString(3, userInput[3]);
				pstmt.setString(4, userInput[3]);
				pstmt.setString(5, userInput[4]);
				pstmt.setString(6, userInput[3]);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					//try input the same data in 10107
					System.out.println(rs.getString("Code"));
					System.out.println("Sorry, there is a conflict with another reservation in the system.");
				}
				else
				{
					try (PreparedStatement pstmt3 = conn.prepareStatement("SELECT * "+
																		"from lab7_rooms " +
																		" where RoomCode = ? and MaxOcc >= ?"))
					{
						pstmt3.setString(1, Room);
						pstmt3.setInt(2, Integer.parseInt(userInput[5]) + Integer.parseInt(userInput[6]));
						ResultSet rs1 = pstmt3.executeQuery();
						if (rs1.next())
						{
							//update	
							try (PreparedStatement pstmt1 = conn.prepareStatement("UPDATE lab7_reservations "+
												"SET FirstName = ?,  LastName = ?, CheckIn = ?, CheckOut = ?, Adults = ?, Kids = ? "+"WHERE Code = ?"))
							{
								pstmt1.setString(1, userInput[1]);
								pstmt1.setString(2, userInput[2]);
								pstmt1.setString(3, userInput[3]);
								pstmt1.setString(4, userInput[4]);
								pstmt1.setInt(5, Integer.parseInt(userInput[5]));
								pstmt1.setInt(6, Integer.parseInt(userInput[6]));
								pstmt1.setInt(7, Integer.parseInt(userInput[0]));
								pstmt1.executeUpdate();
								System.out.println("The reservation has been updated");
							}
						}
						else
						{
							System.out.println("Sorry, the requested person count exceeds the maximum capacity of the selected room.");
						}
					}
					
				}
			}
		}
	}


	private void fr4(String userInput) throws SQLException
	{
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

			try (PreparedStatement lookUp = conn.prepareStatement("SELECT * " +
																"from lab7_reservations " +
																"where Code = ?"))
			{
				lookUp.setInt(1, Integer.parseInt(userInput));
				ResultSet lookUpResult = lookUp.executeQuery();
				if (lookUpResult.next())
				{
					try (Statement stmt = conn.createStatement()) {
						System.out.println("Remove entry");
						stmt.execute("delete from lab7_reservations where Code = '" + userInput + "'");
						stmt.close();
					}
				}
				else
				{
					System.out.println("Could not find this reservation code");
				}
				lookUpResult.close();
				lookUp.close();
			}
			conn.close();
		}
	}

	private void fr5() throws SQLException 
	{
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

		String sql = "with sameMonth as (select Room, Monthname(CheckIn) as month, Month(Checkin) as Monthorder, sum(rate*(datediff(checkout, checkin))) as revenue" +
					"from lab7_reservations" +
					"where Monthname(CheckIn) = Monthname(Checkout)" +
					"group by Room, month, Monthorder" +
					"order by Room)," +
					"listDifferentMonth as (select * from lab7_reservations where Monthname(CheckIn) <> Monthname(Checkout))," +
					"combine as(" +
						"(select Room, Monthname(CheckIn) as month, Month(Checkin) as Monthorder, sum(rate*(datediff(last_day(checkin), checkin)+1)) as revenue" +
						"from listDifferentMonth" +
						"group by Room, month, Monthorder)" +
						"union" +
						"(select Room, Monthname(Checkout) as month, Month(Checkout) as Monthorder, sum(rate*(datediff(checkout, date_add(checkout, INTERVAL(1 - DAYOFMonth(checkout)) day))+1)) as revenue" +
						"from listDifferentMonth" +
						"group by Room, month, Monthorder)" +
						"union" +
						"(select * from sameMonth))" +
					"select Room, Roomname, month, sum(revenue)" +
					"from combine C, lab7_rooms R" +
					"where C.Room = R.RoomCode" +
					"group by Room, Roomname, month, Monthorder" +
					"order by Room, Roomname, Monthorder";
	
		try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD))
		{	
			try (Statement stmt = conn.createStatement()) 
			{
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next())
				{
					//do something
				}
				rs.close();
				stmt.close();
			}
			conn.close();
		}
	}
}

