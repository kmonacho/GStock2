package com.gs.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gs.bean.Fournisseur;
import com.gs.bean.Produit;

public class SqlFournisseur {
		
	public Connection con = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	String conString;
	
	
	public SqlFournisseur(String BDDFilePath) {
		this.conString = BDDFilePath;
	}
	
	public boolean insertFournisseur(Fournisseur fournisseur) {
		
		int id =-1;
		boolean res = false;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT iD FROM xFournisseurs ");
			
			while (rs.next()) {
				id = rs.getInt("iD");
			}
			id++;
			fournisseur.setiD(id);
			String sql = "INSERT INTO xFournisseurs (id, nomFournisseur, nomContact, commandeType, adresse, codePostal, ville, noFax, email)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
			System.out.println("nom fournisseur : "+ fournisseur.getNomFournisseur());
			PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, fournisseur.getNomFournisseur());
            preparedStatement.setString(3, fournisseur.getNomContact());
            preparedStatement.setInt(4, fournisseur.getCommandeType());
            preparedStatement.setString(5, fournisseur.getAdresse());
            preparedStatement.setString(6, fournisseur.getCodePostal());
            preparedStatement.setString(7, fournisseur.getVille());
            preparedStatement.setInt(8, fournisseur.getNoFax());
            preparedStatement.setString(9, fournisseur.getEmail());
            
            int row = preparedStatement.executeUpdate();   
            if (row > 0) {
                System.out.println("Une fournisseur a ete enregistree dans la table xFournisseurs.");
                res = true;
            }
		}
		catch (SQLException e) {
			System.out.println("Problème dE SQL");
			System.out.println ("Etat : " + e.getSQLState());
			System.out.println ("Message : " + e.getMessage());
			System.out.println ("Erreur code fourni : "+ e.getErrorCode());
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
}
