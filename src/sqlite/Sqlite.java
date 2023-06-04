package sqlite;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Sqlite {

	List<Tabela> dados;

	public Sqlite() {
		super();
		this.dados = new ArrayList<Tabela>();
	}

	public Connection conn() {
		try {
			System.out.println("Iniciando conecção.....");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:dados2.db");
			return connection;
		} catch (SQLException e) {
			System.out.println("ERRO conn: " + e.getErrorCode());
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public void setClassModel(Class<?> c) {
		Tabela tabela = new Tabela();
		tabela.setNome(c.getName());
		tabela.setClasse(c);
		System.out.println("READ Classe " + c.getName());
		for (Field field : c.getDeclaredFields()) {
			tabela.getAtributos().add(new Atributo(field.getName(), field.getGenericType()));
		}
		dados.add(tabela);
	}

	public Tabela getTabela(Class<?> c) {
		Tabela tabela = new Tabela();
		tabela.setNome(c.getName().replace("models.", "").toLowerCase());
		tabela.setClasse(c);
		System.out.println("READ Classe " + tabela.getNome());
		for (Field field : c.getDeclaredFields()) {
			Atributo atributo = null;
			System.out.println(field.isAnnotationPresent(SQLITE.class));
			
			if (field.isAnnotationPresent(SQLITE.class)) {
				SQLITE annotacion = field.getAnnotation(SQLITE.class);
				atributo = new Atributo(field.getName(), field.getType(), annotacion.PK(), annotacion.AI(),
						annotacion.notNull());

			} else {
				atributo = new Atributo(field.getName(), field.getType(), false, false, false);
			}
			tabela.getAtributos().add(atributo);
		}

		return tabela;
	}

	public void criarTabela(Tabela tabela) throws SQLException {
		Connection conn = conn();
		Statement statement = conn.createStatement();

		String sql = "CREATE TABLE IF NOT EXISTS " + tabela.getNome();
		sql = sql.concat("(");
		String primaria = "";
		for (int i = 0; i < tabela.getAtributos().size(); i++) {
			if (tabela.getAtributos().get(i).isPK()) {
				if (tabela.getAtributos().get(i).isAI()) {
					primaria = ", PRIMARY KEY(\"id\" AUTOINCREMENT)";

				} else {
					primaria = ", PRIMARY KEY(\"id\")";
				}
			}
			String notnull = "";
			if (tabela.getAtributos().get(i).isNN())
				notnull = " NOT NULL";

			if (sql.substring(sql.length() - 1, sql.length()).equals("(")) {
				sql = sql.concat(tabela.getAtributos().get(i).getNome() + " "
						+ Tipos.getTipo(tabela.getAtributos().get(i).getTipo()).name() + notnull);
			} else {
				sql = sql.concat(", " + tabela.getAtributos().get(i).getNome() + " "
						+ Tipos.getTipo(tabela.getAtributos().get(i).getTipo()).name() + notnull);
			}
		}
		sql = sql.concat(primaria + ")");
		statement.execute(sql);
		System.out.println("SQL:" + sql);
		conn.close();

	}

	public static ResultSet query(String sql) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:dados.db");
		PreparedStatement stmt = connection.prepareStatement(sql);
		return stmt.executeQuery();

	}

	public static ResultSet finAll(String table) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:dados.db");
		PreparedStatement stmt = connection.prepareStatement("select * from " + table);
		return stmt.executeQuery();
	}

	public static ResultSet get(String tabela, Integer id) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:dados.db");
		PreparedStatement stmt = connection.prepareStatement("select * from " + tabela + " where id=" + id);
		return stmt.executeQuery();
	}

}
