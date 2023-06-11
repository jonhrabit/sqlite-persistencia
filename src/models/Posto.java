package models;

import annotacions.Coluna;
import annotacions.Tabela;

@Tabela
public class Posto {

	@Coluna(PK=true,AI=true)
	private Integer id;

	private String nome, jornada , turno;
	private Integer quantidade;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getJornada() {
		return jornada;
	}
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public Posto(Integer id, String nome, String jornada, String turno, Integer quantidade) {
		super();
		this.id = id;
		this.nome = nome;
		this.jornada = jornada;
		this.turno = turno;
		this.quantidade = quantidade;
	}
	public Posto() {
		super();
	}
	@Override
	public String toString() {
		return "Posto [id=" + id + ", nome=" + nome + ", jornada=" + jornada + ", turno=" + turno + ", quantidade="
				+ quantidade + "]";
	}

}
