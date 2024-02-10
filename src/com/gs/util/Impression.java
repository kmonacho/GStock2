package com.gs.util;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Impression implements Printable {

	public List<String> lignes;

	public Impression(String path){
		lignes = new ArrayList<String>();
		BufferedReader fluxEntree=null;
		String ligneLue;
		try {
			fluxEntree = new BufferedReader(new FileReader(path));

			ligneLue = fluxEntree.readLine();
			while(ligneLue!=null){
				lignes.add(ligneLue);
				ligneLue = fluxEntree.readLine();
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
	throws PrinterException {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
		for(int i=0; i<lignes.size(); i++){
			graphics.drawString(lignes.get(i), 50, 30+i*15);
		}
		return PAGE_EXISTS;
	}
	
}

