package sqlite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Classe {

	private String nome;
	private Class<?> classe;
	private List<Atributo> colunas;
	private Method construtor;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Class<?> getClasse() {
		return classe;
	}

	public void setClasse(Class<?> classe) {
		this.classe = classe;
	}

	public List<Atributo> getColunas() {
		return colunas;
	}

	public void setColunas(List<Atributo> colunas) {
		this.colunas = colunas;
	}

	public Method getConstrutor() {
		return construtor;
	}

	public void setConstrutor(Method construtor) {
		this.construtor = construtor;
	}


	public Classe(String nome, Class<?> classe, List<Atributo> colunas, Method construtor) {
		super();
		this.nome = nome;
		this.classe = classe;
		this.colunas = colunas;
		this.construtor = construtor;
	}

	public Classe() {
		super();
		this.colunas = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "Tabela [nome=" + nome + ", classe=" + classe + ", atributos=" + colunas + "]";
	}

}
