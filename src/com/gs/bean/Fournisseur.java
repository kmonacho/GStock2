package com.gs.bean;

public class Fournisseur {
	private int iD, commandeType, noFax;
	private String nomFournisseur, nomContact, adresse, codePostal,ville, email;
	/**
	 * @return the iD
	 */
	public int getiD() {
		return iD;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setiD(int iD) {
		this.iD = iD;
	}
	/**
	 * @return the commandeType
	 */
	public int getCommandeType() {
		return commandeType;
	}
	/**
	 * @param commandeType the commandeType to set
	 */
	public void setCommandeType(int commandeType) {
		this.commandeType = commandeType;
	}
	/**
	 * @return the noFax
	 */
	public int getNoFax() {
		return noFax;
	}
	/**
	 * @param noFax the noFax to set
	 */
	public void setNoFax(int noFax) {
		this.noFax = noFax;
	}
	/**
	 * @return the nomFournisseur
	 */
	public String getNomFournisseur() {
		return nomFournisseur;
	}
	/**
	 * @param nomFournisseur the nomFournisseur to set
	 */
	public void setNomFournisseur(String nomFournisseur) {
		this.nomFournisseur = nomFournisseur;
	}
	/**
	 * @return the nomContact
	 */
	public String getNomContact() {
		return nomContact;
	}
	/**
	 * @param nomContact the nomContact to set
	 */
	public void setNomContact(String nomContact) {
		this.nomContact = nomContact;
	}
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}
	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	/**
	 * @return the codePostal
	 */
	public String getCodePostal() {
		return codePostal;
	}
	/**
	 * @param codePostal the codePostal to set
	 */
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}
	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	} 
	
}
