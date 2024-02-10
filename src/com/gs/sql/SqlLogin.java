package com.gs.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SqlLogin {
	
	String conString; 
	public Connection con;
	public Statement stmt;
	public ResultSet results;
	
	public SqlLogin(String BDDFilePath) {
		this.conString = BDDFilePath;
	}
	
	public String execDML(String sqlText) {
		int resultCode;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			resultCode = stmt.executeUpdate(sqlText);
			return "OK <" + resultCode + "> "+sqlText;
		}catch (Exception e) {
			e.printStackTrace();
			return "***Erreur dans execDML() "+ sqlText;
		}
	}
	
	public boolean verifieLogin(String username, String password) {
		String passwordV=null;
			try {	         
		        con = DriverManager.getConnection(
		                 conString) ;
		        stmt = con.createStatement();    
		        String sqlText = "SELECT * FROM xLogins WHERE username = '"+username+"'";
		      
		        results = stmt.executeQuery(sqlText);
		        //boolean pasFini = results.next();
		        if (results.next()) {
		        	passwordV = results.getString("password");
		        }
		        stmt.close();
		        con.close();
		        if ((passwordV != null) && password.equals(passwordV)) return true;
		        else return false;
				
			}catch(SQLException e) {
				printSQLError(e);
				e.printStackTrace();
				return false;
			}
	}
	
	public boolean usernameExist(String username) {
		boolean res = false;
		try {
	        con = DriverManager.getConnection(conString) ;
	        stmt = con.createStatement();    
	        
	        String sqlTxt = "SELECT * FROM xLogins WHERE username = '"+username+"'";

	        results = stmt.executeQuery(sqlTxt);
	        if (results.next()) {
	        	res = true;
	        }
	        else res = false;
		}catch(SQLException e) {
			printSQLError(e);
			e.printStackTrace();
			res =  false;	
		}
		finally {
			if (stmt != null) 
				try {
					stmt.close();
					con.close();	
				}catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		return res;
	}
	
	public boolean creeCompte(String username, String password, boolean isAdmin) {
		boolean res = false;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			String sql = "INSERT INTO xLogins (username, password, admin) VALUES (?, ?, ?)";
            
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBoolean(3, isAdmin);

            int row = preparedStatement.executeUpdate();
             
            if (row > 0) {
                System.out.println("Un compte a ete enregistree dans la table xLogins.");
                res = true;
            }
		}
		catch (SQLException e) {
			printSQLError(e);
			e.printStackTrace();
		}
		finally {
			if (stmt != null) 
				try {
					stmt.close();
					con.close();	
				}catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		return res;
	}
		
	public boolean isAdmin(String username) {
		boolean res = false;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			String sql = "SELECT * FROM xLogins WHERE username = '"+ username+"'";
			results = stmt.executeQuery(sql);
			
            while (results.next()) {
            	res = results.getBoolean("admin");	
            }
		}
		catch (SQLException e) {
			printSQLError(e);
			e.printStackTrace();
		}
		finally {
			if (stmt != null) 
				try {
					stmt.close();
					con.close();	
				}catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		return res;
	}
	
	
	
	public void printSQLError(SQLException e ) {	
		System.out.println ("Etat : " + e.getSQLState());
		System.out.println ("Message : " + e.getMessage());
		System.out.println ("Erreur code fourni : "+ e.getErrorCode());	
	}
	
	private void affiche(String str) {
		System.out.println(str);
	}

}
