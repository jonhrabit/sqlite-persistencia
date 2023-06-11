package sqlite;

import java.lang.reflect.Type;

public class Atributo {
	private String nome;
	private Type tipo;
	private boolean PK;
	private boolean AI;
	private boolean NN;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Type getTipo() {
		return tipo;
	}

	public void setTipo(Type tipo) {
		if (tipo.equals(int.class))
			this.tipo = Integer.class;
		else
			this.tipo = tipo;
	}

	public boolean isPK() {
		return PK;
	}

	public void setPK(boolean pK) {
		PK = pK;
	}

	public boolean isAI() {
		return AI;
	}

	public void setAI(boolean aI) {
		AI = aI;
	}

	public boolean isNN() {
		return NN;
	}

	public void setNN(boolean nN) {
		NN = nN;
	}

	public Atributo(String nome, Type tipo, boolean pK, boolean aI, boolean nN) {
		super();
		this.nome = nome;
		if (tipo.equals(int.class))
			tipo=Integer.class;
		this.tipo = tipo;
		PK = pK;
		AI = aI;
		NN = nN;
	}

	public Atributo(String nome, Type tipo) {
		super();
		this.nome = nome;
		this.tipo = tipo;
	}

	public Atributo() {
		super();
	}

	@Override
	public String toString() {
		return "Atributo [nome=" + nome + ", tipo=" + tipo + ", PK=" + PK + ", AI=" + AI + ", NN=" + NN + "]";
	}

}
