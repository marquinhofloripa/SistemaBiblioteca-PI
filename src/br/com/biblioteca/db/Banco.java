package br.com.biblioteca.db;

import br.com.biblioteca.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/** Acesso JDBC definitivo: nenhuma gravacao depende de listas em memoria. */
public class Banco {
    private final String url, usuario, senha;
    public Banco() {
        Properties p = new Properties();
        try (java.io.InputStream in = new java.io.FileInputStream("config/banco.properties")) {
            p.load(in);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Nao foi possivel ler config/banco.properties. " + e.getMessage(), e);
        }
        url = p.getProperty("db.url", ""); usuario = p.getProperty("db.user", ""); senha = p.getProperty("db.password", "");
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new IllegalStateException("Driver MySQL ausente. Execute BAIXAR_DRIVER.ps1 ou coloque o JAR em lib/.", e); }
    }
    private Connection abrir() throws SQLException { return DriverManager.getConnection(url, usuario, senha); }
    private static void executar(Connection c, String sql, Object... args) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(sql)) {
            for (int i=0;i<args.length;i++) st.setObject(i+1,args[i]);
            st.executeUpdate();
        }
    }
    public List<Livro> livros() throws SQLException {
        List<Livro> lista=new ArrayList<>();
        try(Connection c=abrir(); PreparedStatement st=c.prepareStatement("SELECT id,titulo,autor,categoria,disponivel FROM livros ORDER BY id");ResultSet r=st.executeQuery()) {
            while(r.next()){Livro l=new Livro(r.getInt(1),r.getString(2),r.getString(3),r.getString(4));l.setDisponivel(r.getBoolean(5));lista.add(l);}
        } return lista;
    }
    public List<Leitor> leitores() throws SQLException {
        List<Leitor> lista=new ArrayList<>();
        try(Connection c=abrir(); PreparedStatement st=c.prepareStatement("SELECT id,nome,telefone,email FROM leitores ORDER BY id");ResultSet r=st.executeQuery()) {
            while(r.next())lista.add(new Leitor(r.getInt(1),r.getString(2),r.getString(3),r.getString(4)));
        } return lista;
    }
    public List<Emprestimo> emprestimos(List<Livro> livros,List<Leitor> leitores) throws SQLException {
        Map<Integer,Livro> lm=new HashMap<>();for(Livro l:livros)lm.put(l.getId(),l);
        Map<Integer,Leitor> rm=new HashMap<>();for(Leitor l:leitores)rm.put(l.getId(),l);
        List<Emprestimo> lista=new ArrayList<>();
        try(Connection c=abrir();PreparedStatement st=c.prepareStatement("SELECT id,livro_id,leitor_id,data_emprestimo,data_prevista,situacao FROM emprestimos ORDER BY id");ResultSet r=st.executeQuery()) {
            while(r.next()){
                Livro l=lm.get(r.getInt(2)); Leitor leitor=rm.get(r.getInt(3));
                if(l==null||leitor==null)throw new SQLException("Emprestimo sem livro ou leitor: " + r.getInt(1));
                boolean disponivel=l.isDisponivel();l.setDisponivel(true);
                Emprestimo e=Emprestimo.doBanco(r.getInt(1),l,leitor,r.getDate(4).toLocalDate(),r.getDate(5).toLocalDate(),r.getString(6));
                l.setDisponivel(disponivel);lista.add(e);
            }
        }return lista;
    }
    public void criarLivro(String titulo,String autor,String categoria)throws SQLException {
        try(Connection c=abrir()){executar(c,"INSERT INTO livros(titulo,autor,categoria,disponivel) VALUES(?,?,?,1)",titulo,autor,categoria);}
    }
    public void editarLivro(int id,String titulo,String autor,String categoria)throws SQLException {
        try(Connection c=abrir()){executar(c,"UPDATE livros SET titulo=?,autor=?,categoria=? WHERE id=?",titulo,autor,categoria,id);}
    }
    public void excluirLivro(int id)throws SQLException {
        try(Connection c=abrir()){executar(c,"DELETE FROM livros WHERE id=? AND NOT EXISTS (SELECT 1 FROM emprestimos WHERE livro_id=?)",id,id);}
    }
    public void criarLeitor(String nome,String telefone,String email)throws SQLException {
        try(Connection c=abrir()){executar(c,"INSERT INTO leitores(nome,telefone,email) VALUES(?,?,?)",nome,telefone,email);}
    }
    public void editarLeitor(int id,String nome,String telefone,String email)throws SQLException {
        try(Connection c=abrir()){executar(c,"UPDATE leitores SET nome=?,telefone=?,email=? WHERE id=?",nome,telefone,email,id);}
    }
    public void excluirLeitor(int id)throws SQLException {
        try(Connection c=abrir()){executar(c,"DELETE FROM leitores WHERE id=? AND NOT EXISTS (SELECT 1 FROM emprestimos WHERE leitor_id=?)",id,id);}
    }
    public void registrarEmprestimo(int livroId,int leitorId,LocalDate data,LocalDate prevista)throws SQLException {
        if(prevista.isBefore(data))throw new IllegalArgumentException("Data prevista anterior a data de emprestimo.");
        try(Connection c=abrir()) {
            c.setAutoCommit(false);
            try {
                int alterados;
                try(PreparedStatement st=c.prepareStatement("UPDATE livros SET disponivel=0 WHERE id=? AND disponivel=1")) {
                    st.setInt(1,livroId);alterados=st.executeUpdate();
                }
                if(alterados!=1)throw new IllegalStateException("Livro indisponivel ou inexistente.");
                executar(c,"INSERT INTO emprestimos(livro_id,leitor_id,data_emprestimo,data_prevista,situacao) VALUES(?,?,?,?,'EMPRESTADO')",livroId,leitorId,java.sql.Date.valueOf(data),java.sql.Date.valueOf(prevista));
                c.commit();
            }catch(SQLException|RuntimeException ex){c.rollback();throw ex;}
        }
    }
    public void devolverEmprestimo(int id)throws SQLException {
        try(Connection c=abrir()) {
            c.setAutoCommit(false);
            try {
                int livroId;
                try(PreparedStatement st=c.prepareStatement("SELECT livro_id FROM emprestimos WHERE id=? AND situacao='EMPRESTADO' FOR UPDATE")) {
                    st.setInt(1,id);
                    try(ResultSet r=st.executeQuery()){
                        if(!r.next())throw new IllegalStateException("Emprestimo inexistente ou ja devolvido.");
                        livroId=r.getInt(1);
                    }
                }
                executar(c,"UPDATE emprestimos SET situacao='DEVOLVIDO' WHERE id=?",id);
                executar(c,"UPDATE livros SET disponivel=1 WHERE id=?",livroId);
                c.commit();
            }catch(SQLException|RuntimeException ex){c.rollback();throw ex;}
        }
    }
}
