package sqlite;

import java.util.ArrayList;
import java.util.List;

public class Tabela {
	private String nome;
	private Class<?> classe;

	private List<Atributo> atributos;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Atributo> getAtributos() {
		return atributos;
	}
	public void setAtributos(List<Atributo> atributos) {
		this.atributos = atributos;
	}
	
	public Class<?> getClasse() {
		return classe;
	}
	public void setClasse(Class<?> classe) {
		this.classe = classe;
	}
	public Tabela(String nome, Class<?> classe, List<Atributo> atributos) {
		super();
		this.nome = nome;
		this.classe = classe;
		this.atributos = atributos;
	}
	public Tabela() {
		super();
		this.atributos=new ArrayList<Atributo>();
	}
	@Override
	public String toString() {
		return "Tabela [nome=" + nome + ", classe=" + classe + ", atributos=" + atributos + "]";
	}
	

}
