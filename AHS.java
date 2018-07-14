/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahs;

import java.sql.*;
import java.util.Scanner;  
import java.util.Date;
class AHS {
        public static String uid, pswd;
        public static int type;
        public static Connection con;
        static void publicType() throws Exception{
            int choice;
            int r;
            Scanner sc = new Scanner(System.in);
            //Finding the report id => Find last row from the report table and incremenet it
            Statement stmt = con.createStatement();
           
            PreparedStatement pstmt = con.prepareStatement("Insert into report ( rid , rts , rdesc ) values (?,?,?)");
            String rtime, rdate, rdesc;
            do{
            ResultSet rs = stmt.executeQuery("select * from report");
            rs.last();
            r=Integer.parseInt(rs.getString(1))+1;
            String rid = Integer.toString(r);
            System.out.println("\n\n=======PUBLIC INTERFACE OF AHS=======");
            System.out.println("1. Submit a Report");
            System.out.println("2. Quit");
            System.out.print("Enter your choice: ");
            choice=sc.nextInt();
            switch(choice){
                case 1:
                    System.out.print("Enter Report Desc: ");
                    rdesc = sc.next();
                    Date dt = new Date();
                    Timestamp timestamp = new Timestamp(dt.getTime());
                    pstmt.setString(1, rid);
                    pstmt.setTimestamp(2, timestamp);
                    pstmt.setString(3,rdesc);
                    int i = pstmt.executeUpdate();
                    System.out.println("Report with report ID " + rid + " successfully submitted to the System.");
                    break;
                case 2:
                    System.out.println("Quitting...");
                    return;
            }
            }while(true);
        }
        static void police() throws Exception{
            int choice;
           
            Scanner sc = new Scanner(System.in);
  
            Statement stmt = con.createStatement();
            do{
            System.out.println("\n\n=======POLICE INTERFACE OF AHS=======");
            System.out.println("1. Display all reports");
            System.out.println("2. Mark served");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");
            choice=sc.nextInt();
            switch(choice){
                case 1:
                    ResultSet rs = stmt.executeQuery("select * from report");
                    System.out.println("RID    Report TimeStamp    Report Description  Status");
                    while(rs.next())
                        System.out.println(rs.getString(1)+"    "+rs.getString(2)+"    "+rs.getString(3)+"   "+rs.getInt(4));
                    break;
                case 2:
                    String rid;
                    System.out.print("Enter the report ID to be marked serviced: ");
                    rid=sc.next();
                    int i = stmt.executeUpdate("Update report SET pm = 1 where rid LIKE " +"\'"+rid+"\'");
                    System.out.println("Report with report ID " + rid + " successfully marked as serviced");
                    break;
                case 3:
                    System.out.println("Quitting...");
                    return;
            }
            }while(true);
        }
        static void hospital() throws Exception{
            int choice;
           
            Scanner sc = new Scanner(System.in);
  
            Statement stmt = con.createStatement();
            do{
            System.out.println("\n\n=======HOSPITAL INTERFACE OF AHS=======");
            System.out.println("1. Display all reports");
            System.out.println("2. Mark a request serviced");
            System.out.println("3. Quit");
            System.out.print("Enter your choice :");
            choice=sc.nextInt();
            switch(choice){
                case 1:
                    ResultSet rs = stmt.executeQuery("select * from report");
                    System.out.println("RID    Report TimeStamp    Report Description  ");
                    while(rs.next())
                        System.out.println(rs.getString(1)+"    "+rs.getString(2)+"    "+rs.getString(3)+"   "+rs.getInt(5));
                    break;
                case 2:
                    String rid;
                    System.out.print("Enter the report ID to be marked serviced: ");
                    rid=sc.next();
                    int i = stmt.executeUpdate("Update report SET hm = 1 where rid LIKE " +"\'"+rid+"\'");
                    System.out.println("Report with report ID " + rid + " successfully marked served");
                    break;
                case 3:
                    System.out.println("Quitting...");
                    return;
            }
            }while(true);
        }
	public static void main(String args[]) throws Exception{
		//Connection establishment
		
		Class.forName("com.mysql.jdbc.Driver"); 
		con  = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/ACS","root", "");
		Statement stmt=con.createStatement();
                ResultSet rs;
                /* --- FOR DB TESTING PURPOSES 
		rs=stmt.executeQuery("select * from user");  
		while(rs.next())  
			System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getInt(3));  
		-- */
                
                
		//Signup or login procedure
		Boolean flag1, flag2;
		String sql_signup="insert into user values(?,?,?)";  
		Scanner sc = new Scanner(System.in);
               
		System.out.println("\n\n=======ACCIDENT HANDLING SYSTEM=======");
		System.out.println("1. Sign Up");
		System.out.println("2. Log In");
                System.out.println("3. SOS");
                System.out.println("4. Quit");
		System.out.print("Enter your choice: ");
		int choice1 = sc.nextInt();
		switch(choice1){
			case 1:
				//Check if the username already exists
				do{
					flag1=true;
					System.out.print("Enter username: ");
					uid=sc.next();
					rs=stmt.executeQuery("select * from user");  
					while(rs.next()){
						if(rs.getString(1).equals(uid)){
								System.out.println("UserID already exists, try signing up with a different UserID");
								flag1=false;
						}
					}
				}while(flag1==false);
				System.out.print("Enter password: ");
                                pswd=sc.next();
				System.out.println("Enter usertype - \n1 for Public \n2 for Hospital \n3 for Poilice");
				type=sc.nextInt();
                                PreparedStatement pstmt=con.prepareStatement(sql_signup);
				pstmt.setString(1,uid);
				pstmt.setString(2,pswd);
				pstmt.setInt(3,type);
				int i = pstmt.executeUpdate();
				break;
			case 2:
				//Check if username and pswd match
				do{
				flag2=true;
				System.out.print("Enter username: ");
				uid=sc.next();
				System.out.print("Enter password: ");
				pswd=sc.next();
                      
                                
				rs=stmt.executeQuery("select * from user where uid LIKE " +"\'"+uid+"\'" ); 
				rs.first();
                                if(rs.getString(2).equals(pswd)==false){
					flag2=false;
                                        System.out.println("Wrong password");
                                }
				}while(flag2==false);
                                rs.first();
                                type=rs.getInt(3);
                                switch(type){
                                    case 1:
                                        System.out.println("Welcome " + uid + "! You are a user");
                                        break;
                                    case 2:
                                        System.out.println("Welcome " + uid + "! You are a Hospital Manager");
                                        break;
                                    case 3:
                                        System.out.println("Welcome " + uid + "! You are a Policeman");
                                        break;
                                }
				//System.out.println("You are a type " + type + " guy");
                                break;
                        case 3:
                                int r;
                                rs = stmt.executeQuery("select * from report");
                                rs.last();
                                r=Integer.parseInt(rs.getString(1))+1;
                                String rid = Integer.toString(r);
                                pstmt = con.prepareStatement("Insert into report ( rid , rts , rdesc ) values (?,?,?)");
                                String rdesc = "This is an automated report submitted by SOS system";
                      
                                Date dt = new Date();
                                Timestamp timestamp = new Timestamp(dt.getTime());
                                pstmt.setString(1, rid);
                                pstmt.setTimestamp(2, timestamp);
                                pstmt.setString(3, rdesc);
                                int k = pstmt.executeUpdate();
                                System.out.println("An auto-generated report has been submitted.");
                                return;
                        case 4:
                                System.out.println("Quitting...");
                                return;
		}
                //Providing Interface acoording to the user
                //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                switch(type){
                    case 1:
                        publicType();
                        break;
                    case 2:
                        hospital();
                        break;
                    case 3:
                        police();
                        break;
                }  
                
	}
}