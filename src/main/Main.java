package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Alteracao;
import models.Posto;
import sqlite.Sqlite;

public class Main {

	public static void main(String[] args) throws SQLException {
		Sqlite sqlite = new Sqlite();
		sqlite.addPackagescan("models");
		if(!sqlite.init()) {
			System.out.println("Não foi possivel inicializar.");
		}

		Posto posto = (Posto) sqlite.get(Posto.class, 1);
		System.out.println(posto.toString());

		List<Posto> lista = new ArrayList<>();
		lista = (List<Posto>) sqlite.all(Posto.class);
		System.out.println(lista.size());
		lista.forEach(o ->{
			System.out.println(o.getNome());
		});



		Alteracao alteracao = new Alteracao(1,new Date(),"Teste2", "aDASDASDSAD");
		sqlite.save(alteracao);

		alteracao = (Alteracao) sqlite.get(Alteracao.class,1);
		System.out.println(alteracao);

	}

}
