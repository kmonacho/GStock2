package com.gs.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.gs.bean.Produit;
import com.gs.bean.Vente;

public class SqlVente {
	
	public Connection con = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	String conString;

	public SqlVente(String BDDFilePath) {
		this.conString = BDDFilePath;
	}
	
	public ArrayList<Vente> recupereVentes(){
		
		ArrayList<Vente> ventes = new ArrayList<Vente>(); 

		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT date, prixTot, nomProd, nbProd, prix FROM xVentes");
			
			while(rs.next()) {
				String date = rs.getString("date");
				double prixTot = rs.getDouble("prixTot");
				String nomProduit = rs.getString("nomProd");
				int nbProd = rs.getInt("nbProd");
				double prix = rs.getDouble("prix");
			
				Vente vente = new Vente();
			
				vente.setDate(date);
				vente.setPrixTot(prixTot);
				vente.setNomProd(nomProduit);
				vente.setNbProd(nbProd);;
				vente.setPrix(prix);
			
				ventes.add(vente);
			}
		}
		catch (SQLException e) {
			printSQLError(e);
			e.printStackTrace();
		}
		finally {
			if (rs != null) 
				try {
					rs.close();
					stmt.close();
					con.close();	
				}catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		return ventes;	
	}
	
	public boolean sauverVentes(String date, double prixTot, String nomProd, int nbProd, double prix) {
		
		boolean res= false;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
		
			String sql = "INSERT INTO xVentes (date, prixTot, nomProd, nbProd, prix) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, date);
            preparedStatement.setDouble(2, prixTot);
            System.out.println("nom produit : "+ nomProd);
            preparedStatement.setString(3, nomProd);
            preparedStatement.setInt(4, nbProd);
            preparedStatement.setDouble(5, prix);
             
            int row = preparedStatement.executeUpdate();
             
            if (row > 0) {
                System.out.println("Une vente a ete enregistree dans la table xVente.");
                res = true;
            }
		}
		catch (SQLException e) {
			printSQLError(e);
			e.printStackTrace();
		}
		finally {
			if (rs != null) 
				try {
					rs.close();
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

}
