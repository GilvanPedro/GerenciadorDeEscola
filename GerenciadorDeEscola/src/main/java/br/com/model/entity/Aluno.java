package br.com.model.entity;

import br.com.model.enums.SituacaoAluno;

import java.util.List;

public class Aluno extends Pessoa{
    private int matricula;
    private String serie;
    private int turmaId;
    private SituacaoAluno situacao;
    private List<Integer> responsavelId;


    public Aluno(int matricula, String nome, String cpf, String dataNascimentoTexto, String serie, int turmaId, SituacaoAluno situacao, List<Integer> responsavelId) {
        super(nome, cpf, dataNascimentoTexto);
        this.matricula = matricula;
        this.serie = serie;
        this.turmaId = turmaId;
        this.situacao = situacao;
        this.responsavelId = responsavelId;
    }

    public int getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(int turmaId) {
        this.turmaId = turmaId;
    }

    public SituacaoAluno getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoAluno situacao) {
        this.situacao = situacao;
    }

    public List<Integer> getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavel(List<Integer> responsavelId) {
        this.responsavelId = responsavelId;
    }

    public int getMatricula() {
        return matricula;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {

        String responsaveis = responsavelId.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        return matricula + ";" +
                getNome() + ";" +
                getCpf() + ";" +
                getDataNascimentoFormatada() + ";" +
                serie + ";" +
                turmaId + ";" +
                situacao + ";" +
                responsaveis;
    }
}
