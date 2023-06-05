package main;

import java.sql.SQLException;

import models.Posto;
import sqlite.Sqlite;

public class Main {

	public static void main(String[] args) throws SQLException {
		Sqlite sqlite = new Sqlite();
		sqlite.addPackagescan("models");
		if(!sqlite.init()) {
			System.out.println("Não foi possivel inicializar.");
		}
		Posto posto = new Posto(1,"Casa","12x36","DIURNO",1);
		//sqlite.save(posto);
		Posto posto2 = new Posto(1,"Terreno","8:48","DIURNO",1);
		sqlite.update(posto2, 2);
		
	}

}
