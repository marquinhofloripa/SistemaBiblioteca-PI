package br.com.biblioteca.model;

import java.time.LocalDate;

public class Emprestimo {
    private int id;
    private Livro livro;
    private Leitor leitor;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private String situacao;

    public Emprestimo(int id, Livro livro, Leitor leitor, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        if (livro == null || leitor == null || dataEmprestimo == null || dataPrevistaDevolucao == null) {
            throw new IllegalArgumentException("Dados obrigatorios do emprestimo nao informados.");
        }
        if (dataPrevistaDevolucao.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A devolucao prevista nao pode anteceder o emprestimo.");
        }
        if (!livro.isDisponivel()) {
            throw new IllegalStateException("O livro selecionado nao esta disponivel para emprestimo.");
        }
        this.id = id;
        this.livro = livro;
        this.leitor = leitor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.situacao = "EMPRESTADO";
        this.livro.setDisponivel(false);
    }

    /** Reconstroi registro ja persistido sem alterar a disponibilidade do livro. */
    public static Emprestimo doBanco(int id, Livro livro, Leitor leitor, LocalDate data,
                                     LocalDate prevista, String situacao) {
        Emprestimo e = new Emprestimo(id, livro, leitor, data, prevista);
        e.situacao = situacao;
        return e;
    }

    public void registrarDevolucao() {
        if ("DEVOLVIDO".equals(this.situacao)) {
            throw new IllegalStateException("Este emprestimo ja foi devolvido.");
        }
        this.situacao = "DEVOLVIDO";
        this.livro.setDisponivel(true);
    }

    public int getId() { return id; }
    public Livro getLivro() { return livro; }
    public Leitor getLeitor() { return leitor; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public String getSituacao() { return situacao; }

    @Override
    public String toString() {
        return "Emprestimo #" + id + " | Livro: " + livro.getTitulo() + " | Leitor: " + leitor.getNome()
                + " | Data: " + dataEmprestimo + " | Prevista: " + dataPrevistaDevolucao
                + " | Situacao: " + situacao;
    }
}
