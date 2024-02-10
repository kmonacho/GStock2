package com.gs.bean;

public class Vente {
	private String date, nomProd;
	private double prixTot, prix;
	private int nbProd;
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the nomProd
	 */
	public String getNomProd() {
		return nomProd;
	}
	/**
	 * @param nomProd the nomProd to set
	 */
	public void setNomProd(String nomProd) {
		this.nomProd = nomProd;
	}
	/**
	 * @return the prixTot
	 */
	public double getPrixTot() {
		return prixTot;
	}
	/**
	 * @param prixTot the prixTot to set
	 */
	public void setPrixTot(double prixTot) {
		this.prixTot = prixTot;
	}
	/**
	 * @return the prix
	 */
	public double getPrix() {
		return prix;
	}
	/**
	 * @param prix the prix to set
	 */
	public void setPrix(double prix) {
		this.prix = prix;
	}
	/**
	 * @return the nbProd
	 */
	public int getNbProd() {
		return nbProd;
	}
	/**
	 * @param nbProd the nbProd to set
	 */
	public void setNbProd(int nbProd) {
		this.nbProd = nbProd;
	}
	
}
