package com.gs.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.gs.sql.*;
import com.gs.bean.*;



public class GUI extends JFrame implements ActionListener, ItemListener{
	
	private int largeur, hauteur;
	public static final String BDD = "@BDD:";
	public static final String USERNAME = "@USERNAME:";
	public static final String PASSWORD = "@PASSWORD:";
	public static Map<String, String> initParams = new HashMap<String, String>();
	public static final Color GREEN = new Color(0,200,0);
	public static final Color RED = new Color(200,0,0);
	private int nbLogin =0;
	private boolean login= false;
	
	private JMenuBar  menuBar;
	private JMenu jmFichier, jmAction;
	private JMenuItem jmiLogin, jmiQuitter, jmiVendre,jmiCreerProduit, jmiCreerFournisseur, jmiCreerCompte, jmiImprimerVemtes;
	private JDialog jdLogin, jdCC, jdCP, jdCF;
	private JPanel jpLogin;
	private JLabel jlLogin, jlCC, jlCC2, jlCC3, jlCCUsername, jlCCPassEgaux, jlCC8Car, jlCC1Maj, jlCC1Chiffre, jlCC1CarZetta;
	private JTextField jtfUsername, jtfCCUsername, jtfCPNProd,jtfCPPrix,jtfCPUES, jtfCPUSV, jtfCPNR,
		jtfCPUAC, jtfCPIDF, jtfCFNomFournissseur, jtfCFNomContact, jtfCFComandeType, jtfCFAdresse, jtfCFCP, jtfCFVille,jtfCFnoFax, jtfCFEmail  ;
	private JPasswordField jpfLPass, jpfCCPassword, jpfCCPassword2;
	private JButton jbOk, jbCCOk, jbCPEnregistrer, jbCFEnregister;
	private JTextArea jtaCPDescription;
	private JScrollPane jspCP;
	private ArrayList<Produit> produits;
	private ArrayList<String> lNomProduit, lDescription, lPrix; 
	private JComboBox nomProduit, nbDeProduits;
	private JLabel etiquette, prix, parUnite, prix2, montant  ;

	private JTextArea description;
	private JScrollPane jspDesc;
	private JButton annuler, vendre;
	private String[] nbDeProduitsStr = {"1","2","3","4","5"};
	private int noItem=0, nbItem=1; ;
	
	
	
	
	public GUI(String titre) {
		
		boolean ini = this.initialise();
		if (ini) affiche("Initialisation du programmes");
		menuBar = new JMenuBar();
		menuBar.setEnabled(false);
		
		jmAction = new JMenu("Action");
		jmAction.setMnemonic('A');
		jmFichier = new JMenu("Fichier");
		jmFichier.setMnemonic('F');
		menuBar.add(jmFichier);
		menuBar.add(jmAction);
		
		jmiLogin = new JMenuItem("Login");
		jmiLogin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		jmiLogin.addActionListener(this);
		jmFichier.add(jmiLogin);
		
		jmiQuitter = new JMenuItem("Quitter");
		jmiQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		jmiQuitter.addActionListener(this);
		jmFichier.add(jmiQuitter);
		
		jmiCreerProduit = new JMenuItem("Ajouter nouvel article");
		jmiCreerProduit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		jmiCreerProduit.addActionListener(this);
		jmAction.add(jmiCreerProduit);
		
		jmiCreerFournisseur = new JMenuItem("Ajouter nouveau Fournisseur");
		jmiCreerFournisseur.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
		jmiCreerFournisseur.addActionListener(this);
		jmAction.add(jmiCreerFournisseur);
		
		jmiVendre = new JMenuItem("Vendre");
		jmiVendre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		jmiVendre.addActionListener(this);
		jmAction.add(jmiVendre);
		
		jmiCreerCompte = new JMenuItem("Créer un compte");
		jmiCreerCompte.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		//if (logiin) jmiCreerCompte.addActionListener(this);
		jmAction.add(jmiCreerCompte) ;
		
		jmiImprimerVemtes = new JMenuItem("Imprimer Ventes");
		jmiImprimerVemtes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
		jmiImprimerVemtes.addActionListener(this);
		jmAction.add(jmiImprimerVemtes);
		
		this.setJMenuBar(menuBar);

		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		largeur = 700; 
		hauteur =  800; 
		
		jdLogin = new JDialog(this, "Indentification");
		jdLogin.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jdLogin.setSize(400,300);
		jdLogin.getContentPane().setBackground(Color.white);
		//jdLogin.setLayout(new BorderLayout(30,20));
		jdLogin.setLayout(null);
		jdLogin.setBounds(largeur/2-200, hauteur/2-125, 400, 250);
		jdLogin.setModal(true);
		
		jpLogin = new JPanel();
		jpLogin.setBackground(Color.white);
		jlLogin = new JLabel("Entrez vos informations d'utilisateur : ");
		
		jtfUsername = new JTextField(20);
		Border titreBordure;
		//Border bordureAbaissee = BorderFactory.createLoweredBevelBorder();
		Border bordure = BorderFactory.createLineBorder(Color.black);
		titreBordure = BorderFactory.createTitledBorder(bordure, "Nom d'utilisateur",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
		jtfUsername.setBorder(titreBordure);
		jpfLPass = new JPasswordField(20);
		titreBordure = BorderFactory.createTitledBorder(bordure, "Mot de passe",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
		jpfLPass.setBorder(titreBordure);
		
		jbOk = new JButton("OK");
		//jbOk.setBorder(null);
		jbOk.setBackground(Color.white);
		jbOk.setPreferredSize(new Dimension(20,25));
		jbOk.addActionListener(this);
		
		jlLogin.setBounds(10, 10, 350, 30);
		jpLogin.setBounds(10,50,350,100);
		jbOk.setBounds(240,160,60,30);
		
		jpLogin.add(jtfUsername);
		jpLogin.add(jpfLPass);
		
		jdLogin.add(jlLogin);
		jdLogin.add(jpLogin);
		jdLogin.add(jbOk);
		
		jdLogin.setVisible(true);

		lNomProduit = new ArrayList<String>();
		lDescription = new ArrayList<String>();
		lPrix = new ArrayList<String>();
		
		SqlProduit SQLProduits = new SqlProduit(initParams.get(BDD));
		produits = SQLProduits.recupereProduits();
		
		for (int i=0; i<produits.size(); i++) {
			lNomProduit.add(produits.get(i).getNomProduit());
			lDescription.add(produits.get(i).getDescription());
			
			double montantDb = produits.get(i).getPrix();
			BigDecimal bd = new BigDecimal(montantDb).setScale(2,RoundingMode.HALF_UP);
			double montantDb2 = bd.doubleValue();
			lPrix.add(Double.toString(montantDb2));
		}

		this.setLayout(new GridBagLayout());
		
		etiquette = new JLabel("Choix des articles à vendre : ");
		Border raisedBevelBorder = BorderFactory.createRaisedBevelBorder();
		etiquette.setFont(new Font("Arail",Font.BOLD, 16));
		nomProduit = new JComboBox(lNomProduit.toArray());
		nomProduit.setMaximumRowCount(5);
		nomProduit.setPreferredSize(new Dimension(300,25));
		nomProduit.setSize(new Dimension(300,25));
		nomProduit.addItemListener(this);
		
		description = new JTextArea();
		description.setText(lDescription.get(noItem).toString());
		jspDesc = new JScrollPane(description);
		jspDesc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspDesc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		Border loweredBevelBorder0 = BorderFactory.createLoweredBevelBorder();
		Border etchedBorderRaised = BorderFactory.createTitledBorder(loweredBevelBorder0, "Description",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
		jspDesc.setBorder(etchedBorderRaised);
				
		prix = new JLabel(lPrix.get(noItem).toString());
		Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();
		prix.setBorder(loweredBevelBorder);
		
		parUnite = new JLabel(" CHF  par article");
		Border loweredBevelBorder2 = BorderFactory.createLoweredBevelBorder();
		parUnite.setBorder(loweredBevelBorder2);
		
		nbDeProduits = new JComboBox(nbDeProduitsStr);
		nbDeProduits.addItemListener(this);
			
		prix2 = new JLabel("Total : ");
		Border loweredBevelBorder3 = BorderFactory.createLoweredBevelBorder();
		prix2.setBorder(loweredBevelBorder3);
		
		montant = new JLabel(nbItem *
				Double.parseDouble(lPrix.get(noItem).toString())+" CHF");
		Border loweredBevelBorder4 = BorderFactory.createLoweredBevelBorder();
		montant.setBorder(loweredBevelBorder4);
		
		this.annuler = new JButton("Annuler");
		annuler.addActionListener(this);
		this.vendre = new JButton("Vendre");
		vendre.addActionListener(this);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// etiquette
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(15,15,30,60);
		this.add(etiquette, gbc);
		
		// nomProduit
		gbc.gridx =0;
		gbc.gridy =1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(nomProduit, gbc);
		
		// jspDesc
		jspDesc.setPreferredSize(new Dimension(120,300));
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(jspDesc, gbc);
		
		// Prix
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;	
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		this.add(prix, gbc);
		
		// parUnite
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		this.add(parUnite, gbc);
		
		// label place 
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		JLabel place = new JLabel("\t\t");
		this.add(place, gbc);
		
		// nbDeProduits
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth =1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		this.add(nbDeProduits, gbc);
		
		// Prix2
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		this.add(prix2, gbc);
		
		// montant
		
		gbc.gridx= 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		this.add(montant, gbc);
		
		// annuler
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight =1;
		gbc.anchor = GridBagConstraints.LINE_END;
		this.add(annuler, gbc);
		
		// vendre
		
		gbc.gridx = 3;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		this.add(vendre, gbc);
		
		this.setTitle(titre);
		this.setBounds(40,60, largeur, hauteur);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private boolean initialise() {
		String filePath = "xGS.ini";
		String ligne;
		//String paramValue;
		boolean res;
		BufferedReader entree;
		try {
			entree = new BufferedReader(new FileReader(filePath));
			while ((ligne = entree.readLine()) != null) {
				if (ligne.startsWith(GUI.BDD)) {
					 String paramValue  = ligne.substring(BDD.length());
					 initParams.put(BDD, paramValue);
				} 
				if(ligne.startsWith(GUI.USERNAME)) {
					String paramValue = ligne.substring(USERNAME.length());
					initParams.put(USERNAME, paramValue);
				}
				if(ligne.startsWith(GUI.PASSWORD)) {
					String paramValue =ligne.substring(PASSWORD.length());
					initParams.put(PASSWORD, paramValue);
				}
			} 
			entree.close();
			res = true;
		}
		catch (IOException e) {
			res = false;
			e.printStackTrace();
		}
		return res;
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		int uniteEnStock, uniteStockVoulu, niveauReapprovisionnement, uniteACommander, idFournisseur,  commandeType, noFax;
		String nomProduitSt,  description,  nomFournisseur, nomContact, adresse, codePostal,ville, email;
		double prix;
		
		if (obj == jmiQuitter) {
			System.exit(0);
		}
		
		if (obj == jbOk) {
			String username = jtfUsername.getText().trim();
			//String password = jtfPassword.getText().trim();
			String password = new String(jpfLPass.getPassword());
			SqlLogin SQLL = new SqlLogin(initParams.get(BDD));
			
			this.nbLogin++;
			
			if(SQLL.verifieLogin(username, password)) {
				this.jdLogin.setVisible(false);	
				if (SQLL.isAdmin(username))
					this.jmiCreerCompte.addActionListener(this);
				this.setVisible(true);
			}
			else {
				this.jdLogin.setVisible(true);
				if (nbLogin > 2)System.exit(0);
			}
		}
		if (obj== jbCCOk) {
			String username = jtfCCUsername.getText().trim();
			String password1 = new String(jpfCCPassword.getPassword());
			String password2 = new String(jpfCCPassword2.getPassword());
			SqlLogin SQLL = new SqlLogin(initParams.get(BDD));
			
			if (SQLL.usernameExist(username)) {
				if (jlCCUsername!=null) jlCCUsername.setForeground(RED);
			}
			else {
				jlCCUsername.setForeground(GREEN);
			}	
			if (!login(username, password1, password2)) jdCC.setVisible(true);
			else {
				jdCC.setVisible(false);	
				SQLL.creeCompte(username, password1, false);
			}	
		}
		if (obj == jmiCreerProduit) {
			jdCP = new JDialog(this, "Insérer un nouvel article dans la BDD");
			jdCP.setLayout(new FlowLayout());
			jdCP.setBounds(200, 100, 800, 200);
			
			jtfCPNProd = new JTextField(20);
			Border bordureAbaissee = BorderFactory.createLoweredBevelBorder();
			Border titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Nom de l'Article",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPNProd.setBorder(titreBordure);
			jdCP.add(jtfCPNProd);
			
			jtaCPDescription = new JTextArea(1,35);
			jspCP = new JScrollPane(jtaCPDescription);
			jspCP.setPreferredSize(new Dimension(400,60));
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Description de l'article",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jspCP.setBorder(titreBordure);
			jdCP.add(jspCP);
			
			jtfCPPrix = new JTextField(8);
			bordureAbaissee = BorderFactory.createLoweredBevelBorder();
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Prix",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPPrix.setBorder(titreBordure);
			jdCP.add(jtfCPPrix);
			
			jtfCPUES = new JTextField(9);
			bordureAbaissee = BorderFactory.createLoweredBevelBorder();
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Unité en Stock",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPUES.setBorder(titreBordure);
			jdCP.add(jtfCPUES);
			
			jtfCPUSV = new JTextField(19);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Maximum d'unité de stock voulu",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPUSV.setBorder(titreBordure);
			jdCP.add(jtfCPUSV);
			
			jtfCPNR = new JTextField(11);
			 titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Minimun d'unité en stock",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPNR.setBorder(titreBordure);
			jdCP.add(jtfCPNR);
			
			jtfCPUAC = new JTextField(11);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Unité a commander",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPUAC.setBorder(titreBordure);
			jdCP.add(jtfCPUAC);
			
			jtfCPIDF = new JTextField(11);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "ID du Fournisseur",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCPIDF.setBorder(titreBordure);
			jdCP.add(jtfCPIDF);
			
			jbCPEnregistrer = new JButton("Enregistrer");
			jbCPEnregistrer.addActionListener(this);
			jdCP.add(jbCPEnregistrer);
			
			jdCP.setVisible(true);	
		}
		if (obj == jbCPEnregistrer) { 
			try {
				nomProduitSt = jtfCPNProd.getText().trim();
				description = jtaCPDescription.getText().trim();
				prix = Double.parseDouble(jtfCPPrix.getText().trim());
				uniteEnStock = Integer.parseInt(jtfCPUES.getText().trim());
				uniteStockVoulu = Integer.parseInt(jtfCPUSV.getText().trim());
				niveauReapprovisionnement =	Integer.parseInt(jtfCPNR.getText().trim());
				uniteACommander = Integer.parseInt(jtfCPUAC.getText().trim());
				idFournisseur = Integer.parseInt(jtfCPIDF.getText().trim());
				Produit produit = new Produit();
				produit.setNomProduit(nomProduitSt);
				produit.setDescription(description);
				produit.setPrix(prix);
				produit.setUniteEnStock(uniteEnStock);
				produit.setNiveauReapprovisionnement(niveauReapprovisionnement);
				produit.setUniteACommander(uniteACommander);
				produit.setIdFournisseur(idFournisseur);
				SqlProduit sqlProduit = new SqlProduit(initParams.get(BDD));
				sqlProduit.insertProduit(produit);	
				jdCP.setVisible(false);
			}catch (NumberFormatException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Veuillez entrer des nombre dans les champs de texte !", "Message d'erreur", JOptionPane.WARNING_MESSAGE );
			}
		}
		if (obj == jmiCreerFournisseur) {
			jdCF = new JDialog(this, "Insérer un nouveau fournisseur dans la BDD");
			jdCF.setLayout(new FlowLayout());
			jdCF.setBounds(200, 100, 800, 200);
			jtfCFNomFournissseur = new JTextField(20);
			Border bordureAbaissee = BorderFactory.createLoweredBevelBorder();
			Border titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Nom du fournisseur",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFNomFournissseur.setBorder(titreBordure);
			jdCF.add(jtfCFNomFournissseur);
			
			jtfCFNomContact = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Nom de contact du fournisseur",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFNomContact.setBorder(titreBordure);
			jdCF.add(jtfCFNomContact);
			
			jtfCFComandeType = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "N° du type de la commande",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFComandeType.setBorder(titreBordure);
			jdCF.add(jtfCFComandeType);
			
			jtfCFAdresse = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Adresse",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFAdresse.setBorder(titreBordure);
			jdCF.add(jtfCFAdresse);
			
			jtfCFCP = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "N° postale",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFCP.setBorder(titreBordure);
			jdCF.add(jtfCFCP);
			
			jtfCFVille = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Ville",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFVille.setBorder(titreBordure);
			jdCF.add(jtfCFVille);
			
			jtfCFnoFax = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "N° de Fax",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFnoFax.setBorder(titreBordure);
			jdCF.add(jtfCFnoFax);
			
			jtfCFEmail = new JTextField(20);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Email",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCFEmail.setBorder(titreBordure);
			jdCF.add(jtfCFEmail);
			
			jbCFEnregister = new JButton("Enregistrer");
			jbCFEnregister.addActionListener(this);
			jdCF.add(jbCFEnregister);
			
			jdCF.setVisible(true);
		}
		if (obj == jbCFEnregister) {
			try {
				nomFournisseur = this.jtfCFNomFournissseur.getText().trim();
				nomContact = this.jtfCFNomContact.getText().trim();
				commandeType = Integer.parseInt(this.jtfCFComandeType.getText().trim());
				adresse =this.jtfCFAdresse.getText().trim();
				codePostal = this.jtfCFCP.getText();
				ville =	this.jtfCFVille.getText().trim();
				noFax = Integer.parseInt(this.jtfCFnoFax.getText().trim());
				email = this.jtfCFEmail.getText().trim();
				Fournisseur fournisseur = new Fournisseur();
				fournisseur.setNomFournisseur(nomFournisseur);
				fournisseur.setNomContact(nomContact);
				fournisseur.setCommandeType(commandeType);
				fournisseur.setAdresse(adresse);
				fournisseur.setCodePostal(codePostal);
				fournisseur.setVille(ville);
				fournisseur.setNoFax(noFax);
				fournisseur.setEmail(email);
				SqlFournisseur sqlFournisseur = new SqlFournisseur(initParams.get(BDD));
				sqlFournisseur.insertFournisseur(fournisseur);	
				jdCF.setVisible(false);
			}catch (NumberFormatException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Veuillez entrer des nombre entier dans les champs de texte !", "Message d'erreur", JOptionPane.WARNING_MESSAGE );
			}	
		}
		if (obj == annuler) {
			nomProduit.setSelectedIndex(0);
			nbDeProduits.setSelectedIndex(0);
			montant.setText(nbItem *
				Double.parseDouble(lPrix.get(noItem).toString())+" CHF");
		}
		if (obj == this.jmiCreerCompte ) {
			jdCC = new JDialog(this, "Créer un noveau compte");
			jdCC.setLayout(new FlowLayout());
			jdCC.setBounds(200, 100, 350,500);
		
			jlCC = new JLabel("Entrez les informations demandées");
			jlCC.setFont(new Font("Arail",Font.BOLD, 12));
			jlCC.setPreferredSize(new Dimension(300,25));
			jlCCUsername = new JLabel("Nom d'utilisateur (pas) employé !");
			jlCCUsername.setPreferredSize(new Dimension(300,25));
			jlCCUsername.setForeground(GREEN);
			jlCC2= new JLabel("Les mots de passe doivent respecter les");
			jlCC2.setPreferredSize(new Dimension(300,15));
			jlCC3= new JLabel("contraintes suivantes :");
			jlCC3.setPreferredSize(new Dimension(300,25));
			jlCCPassEgaux = new JLabel("Les 2 mots de passe sont égaux/inégaux");
			jlCCPassEgaux.setPreferredSize(new Dimension(300,25));
			jlCC8Car = new JLabel("contenir au moins 8 caractères");
			jlCC8Car.setPreferredSize(new Dimension(300,25));
			jlCC1Maj = new JLabel("contenir au moins une majuscule");
			jlCC1Maj.setPreferredSize(new Dimension(300,25));
			jlCC1Chiffre = new JLabel("contenir au moins un chiffre");
			jlCC1Chiffre.setPreferredSize(new Dimension(300,25));
			jlCC1CarZetta = new JLabel("contenir au moins un caractère spécial (+,&,$,...)");
			jlCC1CarZetta.setPreferredSize(new Dimension(300,25));
			
			jtfCCUsername = new JTextField(25);
			Border titreBordure;
			Border bordureAbaissee = BorderFactory.createLoweredBevelBorder();
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Nom d'utilisateur",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jtfCCUsername.setBorder(titreBordure);

			jpfCCPassword = new JPasswordField(25);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Mot de passe",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jpfCCPassword.setBorder(titreBordure);
			
			jpfCCPassword2 = new JPasswordField(25);
			titreBordure = BorderFactory.createTitledBorder(bordureAbaissee, "Mot de passe",
					TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial",Font.PLAIN, 13), Color.black);
			jpfCCPassword2.setBorder(titreBordure);
			
			jbCCOk = new JButton("OK");
			jbCCOk.addActionListener(this);
			
			jdCC.add(jlCC);
			jdCC.add(jtfCCUsername);
			jdCC.add(jpfCCPassword);
			jdCC.add(jpfCCPassword2);
			jdCC.add(jbCCOk);
			JPanel jpCC = new JPanel();
			jpCC.setPreferredSize(new Dimension(350, 250));
			jpCC.add(jlCCUsername);
			jpCC.add(this.jlCC2);
			jpCC.add(this.jlCC3);
			jpCC.add(this.jlCCPassEgaux);
			jpCC.add(jlCC8Car);
			jpCC.add(this.jlCC1Maj);
			jpCC.add(this.jlCC1Chiffre);
			jpCC.add(this.jlCC1CarZetta);
			jdCC.add(jpCC);

			jdCC.setVisible(true);	
		}
		if (obj == vendre) {
			vendreU();	
		}
		if (obj == this.jmiVendre) {
			vendreU();
		}
		if (obj == this.jmiImprimerVemtes) {
			String filePath = "Ventes.txt";
			SqlVente sqlVente = new SqlVente(initParams.get(BDD));
			ArrayList<Vente> ventes = sqlVente.recupereVentes();
			double total = 0;
			
			try {		
			    PrintWriter sortie = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath)));
				
					for (int i=0;i< ventes.size(); i++) {
						String ligne = "Le "+ ventes.get(i).getDate()+ " : "+
							ventes.get(i).getNbProd() +" x "+ ventes.get(i).getPrix()+
							" / total de : "+
							ventes.get(i).getPrixTot()+" CHF.";
							total += ventes.get(i).getPrixTot();
							sortie.println(ligne);
					}
					BigDecimal bd = new BigDecimal(total).setScale(2,RoundingMode.HALF_UP);
					double total2 = bd.doubleValue();
					sortie.println("------------------------------------------------"+
					"-------------------");
					sortie.println("Total :                                          "+
					"           "+total2+ " CHF.");	
					sortie.close();
			}catch (IOException ex) { 
				ex.printStackTrace();
			}
		}
		if (obj == annuler) {
			nomProduit.setSelectedIndex(0);
			nbDeProduits.setSelectedIndex(0);
			montant.setText(nbItem *
				Double.parseDouble(lPrix.get(noItem).toString())+" CHF");
		}
	}
	
	public void itemStateChanged(ItemEvent ev) {
		Object obj = ev.getSource();
		if (obj == nomProduit) {
			Object itemChoisi = nomProduit.getSelectedItem();
			noItem = nomProduit.getSelectedIndex();
			System.out.println("noItem "+ noItem);
			System.out.println("Item choisi "+ (String)itemChoisi);
			description.setText(lDescription.get(noItem).toString());
			prix.setText(lPrix.get(noItem).toString());
			double montantDb = (nbItem *
					Double.parseDouble(lPrix.get(noItem).toString()));
			BigDecimal bd = new BigDecimal(montantDb).setScale(2,RoundingMode.HALF_UP);
			double montantDb2 = bd.doubleValue();	
			montant.setText(montantDb2+" CHF");
		}
		if (obj == nbDeProduits){
			nbItem = nbDeProduits.getSelectedIndex()+1;
			double montantDb = (nbItem *
					Double.parseDouble(lPrix.get(noItem).toString()));
			BigDecimal bd = new BigDecimal(montantDb).setScale(2,RoundingMode.HALF_UP);
			double montantDb2 = bd.doubleValue();
			montant.setText(montantDb2+" CHF");
		}
	}
	
	private void vendreU() {
		noItem = nomProduit.getSelectedIndex();
		nbItem = nbDeProduits.getSelectedIndex()+1;
		SqlProduit sQLProduits = new SqlProduit(initParams.get(BDD));
		boolean res = sQLProduits.testerNbProduit(produits.get(noItem), nbItem);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		String date = sdf.format(new Date());
		double prixTot = (nbItem *
				Double.parseDouble(lPrix.get(noItem).toString()));
		BigDecimal bd = new BigDecimal(prixTot).setScale(2,RoundingMode.HALF_UP);
		prixTot = bd.doubleValue();
		Object itemChoisi = nomProduit.getSelectedItem();
		String nomProduit = (String)itemChoisi;
		double prixU =  Double.parseDouble(lPrix.get(noItem).toString());
		SqlVente SQLVentes = new SqlVente(initParams.get(BDD));
		SQLVentes.sauverVentes(date, prixTot, nomProduit, nbItem, prixU);
	}
	
	private boolean login(String username, String password1, String password2) {
		boolean res = false;
		boolean passEgaux= false, minHuit = false, majuscule = false, chiffre = false, carZetta = false, chiffresEtZettaB = false;
		if (!username.equals(null)) {
				if (!username.equals(null) && !password1.equals(null) && !password2.equals(null)) {
					if (password1.equals(password2)) {
						passEgaux = true;
						if (password1.length() >= 8) {
							minHuit = true;
						}
						String pass1 = password1.toUpperCase();
						char[] chiffresEtZetta = {'0','1','2','3','4','5','6','7','8','9','+','\"','*','ç','%','&','/','(',')','=','?',
								'~','è','!','é','à','$','-','_'};
						for (int i=0; i< pass1.length(); i++) {
							chiffresEtZettaB = false;
							char c1 = pass1.charAt(i);
							char c2 = password1.charAt(i);
							for (int j=0;j<chiffresEtZetta.length;j++) {
								if (c2 == chiffresEtZetta[j]) {
									chiffresEtZettaB = true;
								}
								
							}
							if (!chiffresEtZettaB && c1 == c2) {
								majuscule = true;
								break;
							}
						}
						for (int i=0; i< password1.length(); i++) {
							char c = password1.charAt(i);
							char[] chiffres = {'0','1','2','3','4','5','6','7','8','9'};
							for (int j=0;j<chiffres.length; j++) {
								if (c == chiffres[j]) {
									chiffre = true;
									break;	
								}
								if (chiffre) break;
							}
						}
						for (int i=0; i<password1.length(); i++) {
							char c = password1.charAt(i);
							char[] chiffres = {'+','*','ç','%','&','=','?','!','$','£'};
							for (int j=0;j<chiffres.length; j++) {
								if (c == chiffres[j]) {
									carZetta = true;
									break;
								}
								if (carZetta) break;
							}
						}
						if (minHuit && majuscule && chiffre && carZetta) {
							affiche ("les 4 critères de mot de pass sont respectés");
							res = true;
						}
						else {
							if (!passEgaux) {
								jlCCPassEgaux.setForeground(RED);
							}
							else {
								jlCCPassEgaux.setForeground(GREEN);
							}
							if (!minHuit) {
								jlCC8Car.setForeground(RED);
							}
							else {
								jlCC8Car.setForeground(GREEN);
							}
							if (!majuscule) {
								jlCC1Maj.setForeground(RED);
							}
							else {
								jlCC1Maj.setForeground(GREEN);
							}
							if (!chiffre) {
								jlCC1Chiffre.setForeground(RED);
							}
							else {
								jlCC1Chiffre.setForeground(GREEN);
							}
							if (!carZetta) {
								jlCC1CarZetta.setForeground(RED);
							}
							else {
								jlCC1CarZetta.setForeground(GREEN);	
							}
						}
					}else {
						if (!passEgaux) {
							jlCCPassEgaux.setForeground(RED);
							res = false;
						}else {
							jlCCPassEgaux.setForeground(GREEN);
						}
					}
				}
		}
		else {
			res = true;
		}
		return res;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GUI fen = new GUI("Gestion des stocks");

	}
	
	private void affiche(String str) {
		System.out.println(str);
	}

}
