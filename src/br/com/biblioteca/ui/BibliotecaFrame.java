package br.com.biblioteca.ui;

import br.com.biblioteca.db.Banco;
import java.sql.SQLException;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Leitor;
import br.com.biblioteca.model.Livro;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

/** Interface desktop com persistencia MySQL via JDBC. */
public class BibliotecaFrame extends JFrame {
    private static final Color NAVY = new Color(23,43,77);
    private static final Color BLUE = new Color(36,88,166);
    private static final Color BG = new Color(244,247,251);
    private final List<Livro> livros = new ArrayList<>();
    private final List<Leitor> leitores = new ArrayList<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private Banco banco;
    private final CardLayout cards = new CardLayout();
    private final JPanel conteudo = new JPanel(cards);
    private final JLabel resumo = new JLabel();
    private final DefaultTableModel tabelaLivros = model("ID", "Titulo", "Autor", "Categoria", "Disponibilidade");
    private final DefaultTableModel tabelaLeitores = model("ID", "Nome", "Telefone", "E-mail");
    private final DefaultTableModel tabelaEmprestimos = model("ID", "Livro", "Leitor", "Emprestimo", "Prevista", "Situacao");
    private final JTable gridLivros = grid(tabelaLivros), gridLeitores = grid(tabelaLeitores), gridEmprestimos = grid(tabelaEmprestimos);
    private final JTextField titulo = new JTextField(), autor = new JTextField(), categoria = new JTextField();
    private final JTextField nome = new JTextField(), telefone = new JTextField(), email = new JTextField();
    private final JComboBox<Item<Livro>> comboLivro = new JComboBox<>();
    private final JComboBox<Item<Leitor>> comboLeitor = new JComboBox<>();
    private final JTextField dataEmprestimo = new JTextField(LocalDate.now().toString());
    private final JTextField dataPrevista = new JTextField(LocalDate.now().plusDays(7).toString());

    public BibliotecaFrame() {
        super("Sistema Biblioteca - Projeto Integrador Etapa 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 740);
        setMinimumSize(new Dimension(850, 570));
        setLocationRelativeTo(null);
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(BG);
        raiz.add(menu(), BorderLayout.WEST);
        conteudo.setBackground(BG);
        conteudo.add(inicio(), "Inicio");
        conteudo.add(livros(), "Livros");
        conteudo.add(leitores(), "Leitores");
        conteudo.add(emprestimos(), "Emprestimos");
        raiz.add(conteudo, BorderLayout.CENTER);
        setContentPane(raiz);
        try {
            banco = new Banco();
            atualizarTudo();
        } catch (Exception ex) {
            erro("Falha na conexao inicial: " + ex.getMessage() + "\nConsulte o README_ETAPA4.txt.");
        }
        cards.show(conteudo,"Inicio");
    }

    private JPanel menu() {
        JPanel lateral = new JPanel();
        lateral.setBackground(NAVY);
        lateral.setPreferredSize(new Dimension(215, 0));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new EmptyBorder(28, 16, 20, 16));
        JLabel marca = new JLabel("SISTEMA BIBLIOTECA");
        marca.setForeground(Color.WHITE);
        marca.setFont(new Font("SansSerif", Font.BOLD, 16));
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(marca);
        lateral.add(Box.createVerticalStrut(34));
        for (String[] item : new String[][]{{"Inicio", "Inicio"},{"Livros", "Livros"},{"Leitores", "Leitores"},{"Emprestimos", "Emprestimos"}}) {
            JButton b = new JButton(item[0]);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(190, 43));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setUI(new BasicButtonUI());
            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setBackground(NAVY);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(true);
            b.setBorder(new EmptyBorder(8, 12, 8, 8));
            b.setFont(new Font("SansSerif", Font.BOLD, 15));
            b.addActionListener(e -> navegar(item[1]));
            lateral.add(b);
            lateral.add(Box.createVerticalStrut(8));
        }
        lateral.add(Box.createVerticalGlue());
        JLabel aviso = new JLabel("Etapa 4 | MySQL");
        aviso.setForeground(new Color(215,225,241));
        lateral.add(aviso);
        return lateral;
    }

    private JPanel pagina(String nome) {
        JPanel p = new JPanel(new BorderLayout(12, 16));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(28, 30, 28, 30));
        JLabel h = new JLabel(nome);
        h.setFont(new Font("SansSerif", Font.BOLD, 26));
        h.setForeground(NAVY);
        p.add(h, BorderLayout.NORTH);
        return p;
    }

    private JPanel inicio() {
        JPanel p = pagina("Painel inicial");
        JPanel corpo = new JPanel();
        corpo.setBackground(Color.WHITE);
        corpo.setBorder(new EmptyBorder(24, 24, 24, 24));
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        JLabel boasVindas = new JLabel("Bem-vindo ao Sistema Biblioteca");
        boasVindas.setFont(new Font("SansSerif", Font.BOLD, 21));
        boasVindas.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(boasVindas);
        corpo.add(Box.createVerticalStrut(22));
        resumo.setFont(new Font("SansSerif", Font.PLAIN, 17));
        resumo.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(resumo);
        corpo.add(Box.createVerticalStrut(30));
        JLabel texto = new JLabel("Acesso rapido aos cadastros e emprestimos:");
        texto.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(texto);
        corpo.add(Box.createVerticalStrut(12));
        JPanel atalhos = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        atalhos.setBackground(Color.WHITE);
        for (String[] a : new String[][]{{"Cadastrar livro", "Livros"},{"Cadastrar leitor", "Leitores"},{"Registrar emprestimo", "Emprestimos"}}) {
            JButton b = botao(a[0]);
            b.addActionListener(e -> navegar(a[1]));
            atalhos.add(b);
        }
        atalhos.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(atalhos);
        corpo.add(Box.createVerticalGlue());
        JLabel rodape = new JLabel("Sistema com persistencia MySQL: os registros permanecem apos fechar o programa.");
        rodape.setForeground(new Color(82,100,122));
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(rodape);
        p.add(corpo, BorderLayout.CENTER);
        return p;
    }

    private JPanel livros() {
        JPanel p = pagina("Cadastro e consulta de livros");
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 9));
        formulario.setBackground(Color.WHITE);
        formulario.add(new JLabel("Titulo *")); formulario.add(titulo);
        formulario.add(new JLabel("Autor *")); formulario.add(autor);
        formulario.add(new JLabel("Categoria *")); formulario.add(categoria);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.setBackground(Color.WHITE);
        JButton salvar = botao("Salvar livro"), editar = botao("Editar selecionado"), excluir = botao("Excluir selecionado"), limpar = botao("Limpar");
        salvar.addActionListener(e -> salvarLivro());
        editar.addActionListener(e -> editarLivro());
        excluir.addActionListener(e -> excluirLivro());
        limpar.addActionListener(e -> {titulo.setText(""); autor.setText(""); categoria.setText(""); gridLivros.clearSelection();});
        botoes.add(salvar); botoes.add(editar); botoes.add(excluir); botoes.add(limpar);
        p.add(area(formulario, botoes), BorderLayout.CENTER);
        p.add(tabela(gridLivros), BorderLayout.SOUTH);
        return p;
    }

    private JPanel leitores() {
        JPanel p = pagina("Cadastro e consulta de leitores");
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 9));
        formulario.setBackground(Color.WHITE);
        formulario.add(new JLabel("Nome *")); formulario.add(nome);
        formulario.add(new JLabel("Telefone")); formulario.add(telefone);
        formulario.add(new JLabel("E-mail *")); formulario.add(email);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.setBackground(Color.WHITE);
        JButton salvar = botao("Salvar leitor"), editar = botao("Editar selecionado"), excluir = botao("Excluir selecionado"), limpar = botao("Limpar");
        salvar.addActionListener(e -> salvarLeitor());
        editar.addActionListener(e -> editarLeitor());
        excluir.addActionListener(e -> excluirLeitor());
        limpar.addActionListener(e -> {nome.setText(""); telefone.setText(""); email.setText(""); gridLeitores.clearSelection();});
        botoes.add(salvar); botoes.add(editar); botoes.add(excluir); botoes.add(limpar);
        p.add(area(formulario, botoes), BorderLayout.CENTER);
        p.add(tabela(gridLeitores), BorderLayout.SOUTH);
        return p;
    }

    private JPanel emprestimos() {
        JPanel p = pagina("Emprestimos e devolucoes");
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 9));
        formulario.setBackground(Color.WHITE);
        formulario.add(new JLabel("Livro disponivel *")); formulario.add(comboLivro);
        formulario.add(new JLabel("Leitor *")); formulario.add(comboLeitor);
        formulario.add(new JLabel("Data do emprestimo (AAAA-MM-DD) *")); formulario.add(dataEmprestimo);
        formulario.add(new JLabel("Devolucao prevista (AAAA-MM-DD) *")); formulario.add(dataPrevista);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.setBackground(Color.WHITE);
        JButton registrar = botao("Registrar emprestimo"), devolver = botao("Registrar devolucao");
        registrar.addActionListener(e -> registrarEmprestimo());
        devolver.addActionListener(e -> devolverEmprestimo());
        botoes.add(registrar); botoes.add(devolver);
        p.add(area(formulario, botoes), BorderLayout.CENTER);
        p.add(tabela(gridEmprestimos), BorderLayout.SOUTH);
        return p;
    }

    private JPanel area(JPanel campos, JPanel botoes) {
        JPanel area = new JPanel(new BorderLayout(8, 12));
        area.setBackground(Color.WHITE);
        area.setBorder(new EmptyBorder(16, 18, 14, 18));
        area.add(campos, BorderLayout.CENTER);
        area.add(botoes, BorderLayout.SOUTH);
        return area;
    }
    private JScrollPane tabela(JTable grid) {
        JScrollPane s = new JScrollPane(grid);
        s.setPreferredSize(new Dimension(0, 255));
        return s;
    }
    private static DefaultTableModel model(String... colunas) {
        return new DefaultTableModel(colunas, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    }
    private static JTable grid(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setAutoCreateRowSorter(true);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        return t;
    }
    private static JButton botao(String texto) {
        JButton b = new JButton(texto);
        b.setUI(new BasicButtonUI());
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBackground(BLUE);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(true);
        b.setMargin(new Insets(9, 12, 9, 12));
        return b;
    }
    private void navegar(String tela) { atualizarTudo(); cards.show(conteudo, tela); }
    private void erro(String msg) { JOptionPane.showMessageDialog(this, msg, "Atencao", JOptionPane.WARNING_MESSAGE); }
    private void sucesso(String msg) { JOptionPane.showMessageDialog(this, msg, "Operacao concluida", JOptionPane.INFORMATION_MESSAGE); }
    private boolean vazio(String valor) { return valor == null || valor.trim().isEmpty(); }
    private int idSelecionado(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { erro("Selecione um registro na tabela."); return -1; }
        return (Integer) tabela.getValueAt(linha, 0);
    }
    private Livro livroPorId(int id) { for (Livro x : livros) if (x.getId() == id) return x; return null; }
    private Leitor leitorPorId(int id) { for (Leitor x : leitores) if (x.getId() == id) return x; return null; }
    private Emprestimo emprestimoPorId(int id) { for (Emprestimo x : emprestimos) if (x.getId() == id) return x; return null; }
    private boolean livroEmHistorico(Livro l) { for (Emprestimo e : emprestimos) if (e.getLivro() == l) return true; return false; }
    private boolean leitorEmHistorico(Leitor l) { for (Emprestimo e : emprestimos) if (e.getLeitor() == l) return true; return false; }

    private interface AcaoBanco { void executar() throws SQLException; }
    private void acao(AcaoBanco a, String mensagem) {
        if (banco == null) { erro("Conexao nao configurada. Verifique config/banco.properties e o driver."); return; }
        try { a.executar(); atualizarTudo(); sucesso(mensagem); }
        catch (SQLException | IllegalStateException | IllegalArgumentException ex) { erro("Operacao nao concluida: " + ex.getMessage()); }
    }
    private void salvarLivro() {
        if(vazio(titulo.getText())||vazio(autor.getText())||vazio(categoria.getText())){erro("Preencha titulo, autor e categoria.");return;}
        acao(()->banco.criarLivro(titulo.getText().trim(),autor.getText().trim(),categoria.getText().trim()),"Livro cadastrado.");
        titulo.setText("");autor.setText("");categoria.setText("");
    }
    private void editarLivro() {
        int id=idSelecionado(gridLivros);if(id<0)return;
        Livro l=livroPorId(id);if(l==null){erro("Livro nao encontrado.");return;}
        JTextField t=new JTextField(l.getTitulo()),a=new JTextField(l.getAutor()),c=new JTextField(l.getCategoria());
        JPanel campos=new JPanel(new GridLayout(3,2,8,8));
        campos.add(new JLabel("Titulo:"));campos.add(t);campos.add(new JLabel("Autor:"));campos.add(a);campos.add(new JLabel("Categoria:"));campos.add(c);
        if(JOptionPane.showConfirmDialog(this,campos,"Editar livro",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        if(vazio(t.getText())||vazio(a.getText())||vazio(c.getText())){erro("Preencha todos os campos.");return;}
        acao(()->banco.editarLivro(id,t.getText().trim(),a.getText().trim(),c.getText().trim()),"Livro atualizado.");
    }
    private void excluirLivro() {
        int id=idSelecionado(gridLivros);if(id<0)return;
        Livro l=livroPorId(id);if(l==null)return;
        if(livroEmHistorico(l)){erro("Este livro possui historico de emprestimos e nao pode ser excluido.");return;}
        if(JOptionPane.showConfirmDialog(this,"Excluir o livro selecionado?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            acao(()->banco.excluirLivro(id),"Exclusao solicitada.");
    }
    private boolean emailValido(String e) {return e!=null&&e.trim().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");}
    private void salvarLeitor() {
        if(vazio(nome.getText())||!emailValido(email.getText())){erro("Informe nome e e-mail valido.");return;}
        acao(()->banco.criarLeitor(nome.getText().trim(),telefone.getText().trim(),email.getText().trim()),"Leitor cadastrado.");
        nome.setText("");telefone.setText("");email.setText("");
    }
    private void editarLeitor() {
        int id=idSelecionado(gridLeitores);if(id<0)return;
        Leitor l=leitorPorId(id);if(l==null)return;
        JTextField n=new JTextField(l.getNome()),t=new JTextField(l.getTelefone()),e=new JTextField(l.getEmail());
        JPanel campos=new JPanel(new GridLayout(3,2,8,8));
        campos.add(new JLabel("Nome:"));campos.add(n);campos.add(new JLabel("Telefone:"));campos.add(t);campos.add(new JLabel("E-mail:"));campos.add(e);
        if(JOptionPane.showConfirmDialog(this,campos,"Editar leitor",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        if(vazio(n.getText())||!emailValido(e.getText())){erro("Informe nome e e-mail valido.");return;}
        acao(()->banco.editarLeitor(id,n.getText().trim(),t.getText().trim(),e.getText().trim()),"Leitor atualizado.");
    }
    private void excluirLeitor() {
        int id=idSelecionado(gridLeitores);if(id<0)return;
        Leitor l=leitorPorId(id);if(l==null)return;
        if(leitorEmHistorico(l)){erro("Este leitor possui historico de emprestimos e nao pode ser excluido.");return;}
        if(JOptionPane.showConfirmDialog(this,"Excluir o leitor selecionado?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            acao(()->banco.excluirLeitor(id),"Exclusao solicitada.");
    }
    @SuppressWarnings("unchecked")
    private void registrarEmprestimo() {
        Item<Livro> il=(Item<Livro>)comboLivro.getSelectedItem();
        Item<Leitor> ir=(Item<Leitor>)comboLeitor.getSelectedItem();
        if(il==null||ir==null){erro("Cadastre um leitor e um livro disponivel.");return;}
        try {
            LocalDate data=LocalDate.parse(dataEmprestimo.getText().trim()),prevista=LocalDate.parse(dataPrevista.getText().trim());
            if(prevista.isBefore(data)){erro("Data prevista anterior ao emprestimo.");return;}
            acao(()->banco.registrarEmprestimo(il.valor.getId(),ir.valor.getId(),data,prevista),"Emprestimo registrado.");
        }catch(DateTimeParseException ex){erro("Data invalida. Use AAAA-MM-DD.");}
    }
    private void devolverEmprestimo() {
        int id=idSelecionado(gridEmprestimos);if(id<0)return;
        Emprestimo e=emprestimoPorId(id);if(e==null)return;
        if(JOptionPane.showConfirmDialog(this,"Registrar devolucao de '"+e.getLivro().getTitulo()+"'?","Confirmar",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        acao(()->banco.devolverEmprestimo(id),"Devolucao registrada.");
    }
    private void atualizarTudo() {
        if(banco==null)return;
        try {
            List<Livro> novosLivros=banco.livros();
            List<Leitor> novosLeitores=banco.leitores();
            List<Emprestimo> novosEmprestimos=banco.emprestimos(novosLivros,novosLeitores);
            livros.clear();livros.addAll(novosLivros);
            leitores.clear();leitores.addAll(novosLeitores);
            emprestimos.clear();emprestimos.addAll(novosEmprestimos);
            tabelaLivros.setRowCount(0);
            for(Livro l:livros)tabelaLivros.addRow(new Object[]{l.getId(),l.getTitulo(),l.getAutor(),l.getCategoria(),l.isDisponivel()?"Disponivel":"Emprestado"});
            tabelaLeitores.setRowCount(0);
            for(Leitor l:leitores)tabelaLeitores.addRow(new Object[]{l.getId(),l.getNome(),l.getTelefone(),l.getEmail()});
            tabelaEmprestimos.setRowCount(0);
            for(Emprestimo e:emprestimos)tabelaEmprestimos.addRow(new Object[]{e.getId(),e.getLivro().getTitulo(),e.getLeitor().getNome(),e.getDataEmprestimo(),e.getDataPrevistaDevolucao(),e.getSituacao()});
            comboLivro.removeAllItems();for(Livro l:livros)if(l.isDisponivel())comboLivro.addItem(new Item<>(l,l.getId()+" - "+l.getTitulo()));
            comboLeitor.removeAllItems();for(Leitor l:leitores)comboLeitor.addItem(new Item<>(l,l.getId()+" - "+l.getNome()));
            int abertos=0;for(Emprestimo e:emprestimos)if("EMPRESTADO".equals(e.getSituacao()))abertos++;
            resumo.setText("Livros cadastrados: "+livros.size()+"     |     Leitores ativos: "+leitores.size()+"     |     Emprestimos abertos: "+abertos);
        }catch(SQLException ex){erro("Falha ao consultar MySQL: "+ex.getMessage());}
    }
    private static class Item<T> {
        final T valor; final String texto;
        Item(T valor, String texto) { this.valor = valor; this.texto = texto; }
        @Override public String toString() { return texto; }
    }
}
