package sqlite;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import annotacions.Coluna;

public class Sqlite {

	private final boolean DEBUG = true;

	String file;
	List<Classe> classes;
	List<String> packageScan;

	public Sqlite() {
		super();
		this.file = "teste.db";
		this.packageScan = new ArrayList<>();
		this.classes = new ArrayList<>();
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public void addClasses(Classe classe) {
		this.classes.add(classe);
	}

	public List<String> getPackageScan() {
		return packageScan;
	}

	public void setPackageScan(List<String> packageScan) {
		this.packageScan = packageScan;
	}

	public void addPackagescan(String pack) {
		this.packageScan.add(pack);
	}

	public boolean init() {
		if (this.getPackageScan().size() < 1)
			return false;
		this.scan();
		if (this.getClasses().size() < 1)
			return false;

		this.gerarTabelas();

		return true;
	}

	public Connection conn() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			return connection;
		} catch (SQLException e) {
			System.out.println("ERRO conn: " + e.getErrorCode());
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public Classe gerarClasse(Class<?> c) {
		Classe classe = new Classe();

		String[] n = c.getName().split("\\.");
		classe.setNome(n[n.length - 1]);
		classe.setClasse(c);
		for (Field field : c.getDeclaredFields()) {
			Atributo atributo = null;
			if (field.isAnnotationPresent(Coluna.class)) {
				Coluna annotacion = field.getAnnotation(Coluna.class);
				if (annotacion.nome().equals("$")) {
					atributo = new Atributo(field.getName(), field.getType(), annotacion.PK(), annotacion.AI(),
							annotacion.notNull());

				} else {
					atributo = new Atributo(annotacion.nome(), field.getType(), annotacion.PK(), annotacion.AI(),
							annotacion.notNull());
				}
			} else {
				atributo = new Atributo(field.getName(), field.getType(), false, false, false);
			}
			if (DEBUG)
				System.out.println(atributo);
			classe.getColunas().add(atributo);
		}

		return classe;
	}

	public boolean scan() {

		System.out.println("Scan PACKAGE...(" + this.packageScan.size() + ")");
		for (int i = 0; i < this.packageScan.size(); i++) {

			String packageName = this.packageScan.get(i);
			ClassLoader classLoader = ClassLoader.getSystemClassLoader();
			URL packageURL;

			packageURL = classLoader.getResource(packageName);

			if (packageURL != null) {
				String packagePath = packageURL.getPath();
				if (packagePath != null) {
					File packageDir = new File(packagePath);
					if (packageDir.isDirectory()) {
						File[] files = packageDir.listFiles();
						for (File file : files) {
							String className = file.getName();
							if (className.endsWith(".class")) {
								className = packageName + "." + className.substring(0, className.length() - 6);
								try {
									Class<?> clazz = classLoader.loadClass(className);
									classes.add(gerarClasse(clazz));

								} catch (ClassNotFoundException e) {
									System.out.println(e.getMessage());
									e.printStackTrace();
								}
								// do something with the class
							}
						}
					}
				}
			}

		}
		return true;
	}

	public boolean gerarTabelas() {
		for (Classe element : classes) {
			try {
				gerarTabelaSql(element);
			} catch (SQLException e) {
				System.out.println("ERRO: GERAR TABELAS");
				System.out.println("TABELA: " + element.getNome());
				System.out.println(e.getMessage());

				e.printStackTrace();
			}
		}
		return true;
	}

	public void gerarTabelaSql(Classe classe) throws SQLException {

		Connection conn = conn();
		Statement statement = conn.createStatement();

		String sql = "CREATE TABLE IF NOT EXISTS " + classe.getNome();
		sql = sql.concat("(");
		String primaria = "";
		for (Atributo element : classe.getColunas()) {
			if (element.isPK()) {
				if (element.isAI()) {
					primaria = ", PRIMARY KEY(\"" + element.getNome() + "\" AUTOINCREMENT)";

				} else {
					primaria = ", PRIMARY KEY(\"" + element.getNome() + "\")";
				}
			}
			String notnull = "";
			if (element.isNN())
				notnull = " NOT NULL";

			if (sql.substring(sql.length() - 1, sql.length()).equals("(")) {
				sql = sql.concat(element.getNome() + " "
						+ Tipos.getTipo(element.getTipo()).name() + notnull);
			} else {
				sql = sql.concat(", " + element.getNome() + " "
						+ Tipos.getTipo(element.getTipo()).name() + notnull);
			}
		}
		sql = sql.concat(primaria + ")");
		System.out.println("SQL:" + sql);
		statement.execute(sql);
		conn.close();

	}

	private Integer classePresent(Class<? extends Object> c) {
		for (int i = 0; i < this.classes.size(); i++) {
			if (this.classes.get(i).getClasse().equals(c)) {
				return i;
			}
		}
		return -1;
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

	public boolean save(Object obj) {
		Integer i = this.classePresent(obj.getClass());
		if (i == -1)
			return false;
		Classe classe = this.classes.get(i);
		String sql = "INSERT INTO " + classe.getNome() + "(";
		String colunas = "", valores = "";
		for (int z = 0; z < classe.getColunas().size(); z++) {
			Atributo at = classe.getColunas().get(z);
			if (!at.isAI()) {
				colunas = colunas.concat(at.getNome() + ",");
				if (z == classe.getColunas().size() - 1)
					colunas = colunas.substring(0, colunas.length() - 1);

				try {

					Field f = obj.getClass().getDeclaredField(at.getNome());
					f.setAccessible(true);
					switch (at.getTipo().getTypeName()) {
					case "java.lang.String":
						valores = valores.concat("\"" + f.get(obj) + "\",");
						break;
					case "java.util.Date":


						valores = valores.concat("\"" + f.get(obj) + "\",");
						break;
					default:
						valores = valores.concat(f.get(obj) + ",");

					}

					if (z == classe.getColunas().size() - 1)
						valores = valores.substring(0, valores.length() - 1);

				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {

					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}

		}
		sql = sql.concat(colunas + ") VALUES (" + valores + ")");
		if (DEBUG)
			System.out.println(sql);
		Connection conn = this.conn();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	public boolean update(Object obj, Integer key) {
		Integer i = this.classePresent(obj.getClass());
		if (i == -1)
			return false;
		Classe classe = this.classes.get(i);
		String sql = "UPDATE " + classe.getNome() + " SET ";
		String valores = "", where = " WHERE ";
		for (int z = 0; z < classe.getColunas().size(); z++) {
			Atributo at = classe.getColunas().get(z);

			try {
				Field f = obj.getClass().getDeclaredField(at.getNome());
				f.setAccessible(true);
				if (!at.isAI()) {
					if (at.getTipo().equals(String.class)) {
						valores = valores.concat(at.getNome() + "=\"" + f.get(obj) + "\",");
					} else {
						valores = valores.concat(at.getNome() + "=" + f.get(obj) + ",");
					}
					if (z == classe.getColunas().size() - 1)
						valores = valores.substring(0, valores.length() - 1);
				} else {
					where = where.concat(at.getNome() + "=" + key);
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
		sql = sql.concat(valores + where);
		if (DEBUG)
			System.out.println(sql);
		Connection conn = this.conn();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	public boolean delete(Object obj) {
		Integer i = this.classePresent(obj.getClass());
		if (i == -1)
			return false;
		Classe classe = this.classes.get(i);

		String sql = "DELETE FROM " + classe.getNome() + " WHERE ";

		for (Atributo at : classe.getColunas()) {
			try {
				Field f = obj.getClass().getDeclaredField(at.getNome());
				f.setAccessible(true);
				if (at.isPK()) {
					sql = sql.concat(at.getNome() + "=" + f.get(obj));

				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
		if (DEBUG)
			System.out.println(sql);
		try {
			Connection conn = this.conn();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	public Classe getClasse(Class<? extends Object> c) {
		Integer i = this.classePresent(c);
		if (i == -1)
			return null;
		return this.classes.get(i);
	}

	public Object newClasse(Class<? extends Object> c, ResultSet rs) {
		Classe classe = this.getClasse(c);
		Object[] obj = new Object[classe.getColunas().size()];
		try {
			rs.next();

			for (int z = 0; z < classe.getColunas().size(); z++) {
				Atributo at = classe.getColunas().get(z);
				obj[z] = null;
				switch (at.getTipo().getTypeName()) {
				case "java.lang.Integer":
					obj[z] = rs.getInt(at.getNome());
					break;
				case "java.lang.String":
					obj[z] = rs.getString(at.getNome());
					break;
				case "java.util.Date":
					obj[z] = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(rs.getString(at.getNome()));
					break;
				default:
					obj[z] = rs.getString(at.getNome());
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("DATA: "+ e.getMessage());
			e.printStackTrace();
		}

		Integer indexContrutor = 0;
		for (int x = 0; x < c.getDeclaredConstructors().length; x++) {
			if (c.getDeclaredConstructors()[x].getParameterCount() == classe.getColunas().size()) {
				indexContrutor = x;
			}
		}
		try {
			return c.getDeclaredConstructors()[indexContrutor].newInstance(obj);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public Object get(Class<? extends Object> c, Integer key) {
		Classe classe = this.getClasse(c);

		String sql = "SELECT * FROM " + classe.getNome() + " WHERE ";
		for (Atributo at : classe.getColunas()) {
			try {
				if (at.isPK()) {
					sql = sql.concat(at.getNome() + "=" + key);
				}
			} catch (SecurityException | IllegalArgumentException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}

		if (DEBUG)
			System.out.println("SQL: " + sql);

		try {
			Connection conn = this.conn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			return this.newClasse(c, rs);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	public List<? extends Object> all(Class<? extends Object> c) {
		Classe classe = this.getClasse(c);
		List<Object> lista = new ArrayList<>();
		Object[] obj = new Object[classe.getColunas().size()];
		String sql = "SELECT * FROM " + classe.getNome();

		if (DEBUG)
			System.out.println("SQL: " + sql);

		try {
			Connection conn = this.conn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			Integer indexContrutor = 0;
			for (int x = 0; x < c.getDeclaredConstructors().length; x++) {
				if (c.getDeclaredConstructors()[x].getParameterCount() == classe.getColunas().size()) {
					indexContrutor = x;
				}
			}
			while (rs.next()) {

				for (int z = 0; z < classe.getColunas().size(); z++) {
					Atributo at = classe.getColunas().get(z);
					obj[z] = null;
					switch (at.getTipo().getTypeName()) {
					case "java.lang.Integer":
						obj[z] = rs.getInt(at.getNome());
						break;
					case "java.lang.String":
						obj[z] = rs.getString(at.getNome());
						break;
					default:
						obj[z] = rs.getString(at.getNome());
					}
				}
				try {
					lista.add(c.getDeclaredConstructors()[indexContrutor].newInstance(obj));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | SecurityException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}

			return lista;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public List<? extends Object> query(Class<? extends Object> c, String where) {
		Classe classe = this.getClasse(c);
		List<Object> lista = new ArrayList<>();
		Object[] obj = new Object[classe.getColunas().size()];
		String sql = "SELECT * FROM " + classe.getNome() + " where " + where;

		if (DEBUG)
			System.out.println(sql);

		try {
			Connection conn = this.conn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			Integer indexContrutor = 0;
			for (int x = 0; x < c.getDeclaredConstructors().length; x++) {
				if (c.getDeclaredConstructors()[x].getParameterCount() == classe.getColunas().size()) {
					indexContrutor = x;
				}
			}
			while (rs.next()) {

				for (int z = 0; z < classe.getColunas().size(); z++) {
					Atributo at = classe.getColunas().get(z);
					obj[z] = null;
					switch (at.getTipo().getTypeName()) {
					case "java.lang.Integer":
						obj[z] = rs.getInt(at.getNome());
						break;
					case "java.lang.String":
						obj[z] = rs.getString(at.getNome());
						break;
					default:
						obj[z] = rs.getString(at.getNome());
					}
				}
				try {
					lista.add(c.getDeclaredConstructors()[indexContrutor].newInstance(obj));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | SecurityException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}

			return lista;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
