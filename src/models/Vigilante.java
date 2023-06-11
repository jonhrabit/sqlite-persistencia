package models;

import java.util.Date;

import annotacions.Coluna;
import annotacions.Tabela;

@Tabela
public class Vigilante {

	@Coluna(AI=true, PK=true)
	private Integer id;
	private Date admissao,desligamento;
	private String inicio, matricula, nome, observacao, status;
	private Integer posto;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getAdmissao() {
		return admissao;
	}
	public void setAdmissao(Date admissao) {
		this.admissao = admissao;
	}
	public Date getDesligamento() {
		return desligamento;
	}
	public void setDesligamento(Date desligamento) {
		this.desligamento = desligamento;
	}
	public String getInicio() {
		return inicio;
	}
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPosto() {
		return posto;
	}
	public void setPosto(Integer posto) {
		this.posto = posto;
	}
	public Vigilante(Integer id, Date admissao, Date desligamento, String inicio, String matricula, String nome,
			String observacao, String status, Integer posto) {
		super();
		this.id = id;
		this.admissao = admissao;
		this.desligamento = desligamento;
		this.inicio = inicio;
		this.matricula = matricula;
		this.nome = nome;
		this.observacao = observacao;
		this.status = status;
		this.posto = posto;
	}
	public Vigilante() {
		super();
	}
	@Override
	public String toString() {
		return "Vigilante [id=" + id + ", admissao=" + admissao + ", desligamento=" + desligamento + ", inicio="
				+ inicio + ", matricula=" + matricula + ", nome=" + nome + ", observacao=" + observacao + ", status="
				+ status + ", posto=" + posto + "]";
	}

}
