package com.gs.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.gs.bean.Fournisseur;
import com.gs.bean.Produit;
import com.gs.util.Commande;
import com.gs.util.SMTPConnect;

public class SqlProduit {
	
	public Connection con = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	String conString ;
	
	private static final int COMMANDE_LETTRE = 1;
	private static final int COMMANDE_EMAIL = 2;
	
	public SqlProduit(String BDDFilePath) {
		this.conString = BDDFilePath;
	}
	
	public String execDML(String sqlText) {
		int resultCode;	
		try {
	        
	        con = DriverManager.getConnection(
	                conString); 
			stmt = con.createStatement();
			resultCode = stmt.executeUpdate(sqlText);
			return "OK <" + resultCode + "> "+sqlText;
		}catch (Exception e) {
			e.printStackTrace();
			return "***Erreur dans execDML() "+ sqlText;
		}
	}
	
	public ArrayList<Produit> recupereProduits(){
		ArrayList<Produit> produits = new ArrayList<Produit>(); 
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT iD, nomProd, description, prix, uniteEnStock, uniteStockVoulu, "
					+ "niveauReApprovisionnement, uniteACommander, idFournisseur FROM xProds ");
			
			while(rs.next()) {
				int id = rs.getInt("iD");
				String nomProduit = rs.getString("nomProd");
				String description = rs.getString("description");
				float prix = rs.getFloat("prix");
				int uniteEnStock = rs.getInt("uniteEnStock");
				int uniteStockVoulu = rs.getInt("uniteStockVoulu");
				int  niveauReapprovisionnement = rs.getInt("niveauReApprovisionnement");
				int uniteACommander = rs.getInt("uniteACommander");
				int idFournisseur = rs.getInt("idFournisseur");
			
				Produit produit = new Produit();
			
				produit.setiD(id);
				produit.setNomProduit(nomProduit);
				produit.setDescription(description);
				produit.setPrix(prix);
				produit.setUniteEnStock(uniteEnStock);
				produit.setUniteStockVoulu(uniteStockVoulu);
				produit.setNiveauReapprovisionnement(niveauReapprovisionnement);
				produit.setUniteACommander(uniteACommander);
				produit.setIdFournisseur(idFournisseur);
			
				produits.add(produit);
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
		return produits;	
	}
	
	public boolean testerNbProduit(Produit produit, int nbItem) {
		boolean res  = false;
		int iD, commandeType=-1, noFax=-1;
		String nomFournisseur="-1", nomContact="-1", adresse="-1", codePostal="-1",ville="-1", email="-1";
		int uniteEnStock=0, uniteStockVoulu=0, niveauReApprovisonnement=0, uniteACommander=0;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			String sqlTxt = "SELECT uniteEnStock, uniteStockVoulu, niveauReApprovisionnement"+
			" FROM xProds WHERE id = "+ produit.getiD();
			rs = stmt.executeQuery(sqlTxt);
			while (rs.next()) {
				uniteEnStock = rs.getInt("uniteEnStock");
				uniteStockVoulu = rs.getInt("uniteStockVoulu");
				niveauReApprovisonnement = rs.getInt("niveauReApprovisionnement");
			}
			
			uniteEnStock = uniteEnStock- nbItem;
			
			sqlTxt = "UPDATE xprods SET uniteEnStock = " + uniteEnStock +" WHERE id = " + produit.getiD();
			execDML(sqlTxt);
			
			sqlTxt = "SELECT xfournisseurs.nomFournisseur, xfournisseurs.nomContact, xfournisseurs.commandeType, xfournisseurs.adresse,"+
					"xfournisseurs.codePostal, xfournisseurs.ville, xfournisseurs.noFax, xfournisseurs.email"+
					" FROM xprods, xfournisseurs where"
					+ " xprods.idFournisseur = xfournisseurs.id AND xprods.id = "+ produit.getiD();

			rs = stmt.executeQuery(sqlTxt);
			while (rs.next()) {
				nomFournisseur = rs.getString("xfournisseurs.nomFournisseur");
				nomContact = rs.getString("xfournisseurs.nomContact");
				commandeType = rs.getInt("xfournisseurs.commandeType");
				adresse = rs.getString("xfournisseurs.adresse");
				codePostal = rs.getString("xfournisseurs.codePostal");
				ville = rs.getString("xfournisseurs.ville");
				noFax = rs.getInt("xfournisseurs.noFax");
				email = rs.getString("xfournisseurs.email");
			}
			
			Fournisseur fournisseur = new Fournisseur();
			fournisseur.setNomFournisseur(nomFournisseur);
			fournisseur.setNomContact(nomContact);
			fournisseur.setCommandeType(commandeType);
			fournisseur.setAdresse(adresse);
			fournisseur.setCodePostal(codePostal);
			fournisseur.setVille(ville);
			fournisseur.setNoFax(noFax);
			fournisseur.setEmail(email);
			
			if (uniteEnStock <= niveauReApprovisonnement) {
				uniteACommander = uniteStockVoulu - uniteEnStock;
				if (commandeType == COMMANDE_LETTRE) {
					Commande commande = new Commande(fournisseur, produit, uniteACommander);
					commande.fichierCommande();
				}
				if (commandeType == COMMANDE_EMAIL) {
					SMTPConnect mail = new SMTPConnect();
					String sujet = "Commande de "+ uniteACommander + " boîtes de "+ produit.getNomProduit() +"." ;
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
					
					String date = sdf.format(new java.util.Date());
					String txtCommande0 = "Madame, Monsieur,\n\n";
					
					String adrPharmacie = "Pharmacie St-Laurent\n"+
							"Pl. St-Laurent 4\n"+
							"1004 Lausanne\n\n";
					String txtCommande1 = "La pharmacie St-Laurent vous commande du ";
					String txtCommande2 = "au plus vite. Nous vous seront gré de bien vouloir nous livrer ces "+
											"produits à notre\npharmacie.\n"+
											"Veuillez, Madame, Monsieur, agréez nos salutations les plus respectueuses.\n\n\n";
					String txtCommande3 = "\t\t\t\t\t\t\tAvis sans signature";
					String body = "\t\t\t\t\t\t\t\t\t\tLausanne, le "+ date+ "\n\n" +adrPharmacie  +txtCommande0 + txtCommande1 + 
							produit.getNomProduit() +" ("+ uniteACommander+" boîtes).\n"+
							txtCommande2 + txtCommande3;
					mail.connect(email, "jmonacho@ik.me", sujet, body);
				}
				sqlTxt = "UPDATE xprods SET uniteEnStock = "+ uniteStockVoulu+
			    		" WHERE id = "+ produit.getiD();
			    execDML(sqlTxt);		
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
	
	public boolean insertProduit(Produit produit) {
		int id =-1;
		boolean res = false;
		try {
			con = DriverManager.getConnection(conString);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT iD FROM xProds ");
			
			while (rs.next()) {
				id = rs.getInt("iD");
			}
			id++;
			produit.setiD(id);
			String sql = "INSERT INTO xProds (iD, nomProd, description, prix, uniteEnStock, uniteStockVoulu, niveauReApprovisionnement, uniteACommander, "
					+ "idFournisseur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, produit.getNomProduit());
            preparedStatement.setString(3, produit.getDescription());
            preparedStatement.setDouble(4, produit.getPrix());
            preparedStatement.setInt(5, produit.getUniteEnStock());
            preparedStatement.setInt(6, produit.getUniteStockVoulu());
            preparedStatement.setInt(7, produit.getNiveauReapprovisionnement());
            preparedStatement.setInt(8, produit.getUniteACommander());
            preparedStatement.setInt(9, produit.getIdFournisseur());
            
            int row = preparedStatement.executeUpdate();
             
            if (row > 0) {
                System.out.println("Une produit a ete enregistree dans la table xProds.");
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
