package com.gs.util;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.openxml4j.opc.internal.*;

import com.gs.bean.Fournisseur;
import com.gs.bean.Produit;


public class Commande {
	public String nomProd, description, nomFournisseur, nomContact, adresse,ville, email, codePostal;
	public String date;
	private final String adrPharmacie = "Pharmacie St-Laurent\n"+
						"Pl. St-Laurent 4\n"+
						"1004 Lausanne\n";
	private final String txtCommande = "La pharmacie St-Laurent vous commande du ";
	private final String txtCommande2 = "au plus vite. Nous vous seront gré de bien vouloir nous livrer ces "+
										"produits à notre pharmacie.\n"+
										"Veuillez, Madame, Monsieur, agréez nos salutations les plus respectueuses.\n";
	private final String txtCommande3 = "\t\t\t\t\t\t\tAvis sans signature";
	private int  uniteACommander;
	public static int noFichier=0;
	public ArrayList<String> lignes;
	
	public Commande(Fournisseur fournisseur, Produit produit, int uniteACommander) {
		this.nomProd = produit.getNomProduit();
		this.description = produit.getDescription();
		this.nomFournisseur = fournisseur.getNomFournisseur();
		this.nomContact = fournisseur.getNomContact();
		this.adresse = fournisseur.getAdresse();
		this.ville = fournisseur.getVille();
		this.email = fournisseur.getVille();
		this.codePostal = fournisseur.getCodePostal();
		this.uniteACommander = uniteACommander;
	}
	
	public void fichierCommande() {
		noFichier++;
		lignes = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		date = sdf.format(new Date());
		lignes.add("\t\t\t\t\t\t\t\t\t\t\tLausanne, le "+date);
		lignes.add(adrPharmacie);
		lignes.add("\t\t\t\t\t\t\t"+nomContact);
		lignes.add("\t\t\t\t\t\t\t"+adresse);
		lignes.add("\t\t\t\t\t\t\t"+codePostal + " "+ville);
		lignes.add("\n\n\n\n\n");
		lignes.add("Commande de "+ uniteACommander+ " boîtes de " +nomProd);
		lignes.add("\n\n\n");
		lignes.add("Madame, Monsieur,\n");
		lignes.add(txtCommande+ nomProd);
		lignes.add(txtCommande2+"\n\n\n");
		lignes.add(txtCommande3);
		
		/*
		XWPFDocument doc = new XWPFDocument();  
		try(OutputStream os = new FileOutputStream("Commande_"+noFichier+".docx")) {  
		    XWPFParagraph paragraph = doc.createParagraph();  
	        paragraph.setAlignment(ParagraphAlignment.RIGHT);   
		    XWPFRun run = paragraph.createRun();
		    String dateStr = "Lausanne, le "+date; 
		    run.setText(dateStr); 
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setText(adrPharmacie);
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setText("");
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setText("");
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    paragraph = doc.createParagraph();
		    run.setText("\t\t\t\t"+nomContact+"\n");
		    run.setText("\t\t\t\t"+adresse+"\n");
		    run.setText("\t\t\t\t"+codePostal + " "+ville+"\n");
		    run.setText("\n\n\n\n\n");
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setBold(true);
		    run.setText("Commande de "+ uniteACommander + " boîtes de "+nomProd);
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setText("\n\n\n");
		    run.setText("Madame, Monsieur,\n");
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setText(txtCommande+ nomProd+"\n");
		    run.setText(txtCommande2+"\n\n\n");
		    paragraph = doc.createParagraph();
		    run = paragraph.createRun();
		    run.setText(txtCommande3);
		    doc.write(os);  
		}catch(Exception e) {  
		    e.printStackTrace();  
		}  */
		String filePath = "Commande_"+noFichier+".txt";
		System.out.println("Ds Commande / filepath : "+ filePath);
		    
		try {
			
		    PrintWriter sortie = new PrintWriter(new BufferedWriter(
				new FileWriter(filePath)));
			
				for (int i=0;i< lignes.size(); i++) {
					sortie.println(lignes.get(i));
				}
				sortie.close();
					
		}catch (IOException e) { e.printStackTrace();}
		
		PrinterJob job = PrinterJob.getPrinterJob();

		job.setPrintable(new Impression(filePath));

		boolean doPrint = job.printDialog();
		if(doPrint) {
			
			try {
				job.print();
			}
			catch (PrinterException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
