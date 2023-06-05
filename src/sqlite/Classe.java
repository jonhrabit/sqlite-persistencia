package sqlite;

import java.util.ArrayList;
import java.util.List;

public class Classe {
	
	private String nome;
	private Class<?> classe;
	private List<Atributo> colunas;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Atributo> getAtributos() {
		return colunas;
	}
	public void setAtributos(List<Atributo> atributos) {
		this.colunas = atributos;
	}
	
	public Class<?> getClasse() {
		return classe;
	}
	public void setClasse(Class<?> classe) {
		this.classe = classe;
	}
	public Classe(String nome, Class<?> classe, List<Atributo> atributos) {
		super();
		this.nome = nome;
		this.classe = classe;
		this.colunas = atributos;
	}
	public Classe() {
		super();
		this.colunas=new ArrayList<Atributo>();
	}
	@Override
	public String toString() {
		return "Tabela [nome=" + nome + ", classe=" + classe + ", atributos=" + colunas + "]";
	}
	

}
