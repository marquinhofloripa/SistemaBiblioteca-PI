CREATE DATABASE IF NOT EXISTS biblioteca_pi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_pi;
CREATE TABLE IF NOT EXISTS livros (
 id INT AUTO_INCREMENT PRIMARY KEY,
 titulo VARCHAR(180) NOT NULL,
 autor VARCHAR(140) NOT NULL,
 categoria VARCHAR(100) NOT NULL,
 disponivel BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS leitores (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nome VARCHAR(160) NOT NULL,
 telefone VARCHAR(30),
 email VARCHAR(180) NOT NULL
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS emprestimos (
 id INT AUTO_INCREMENT PRIMARY KEY,
 livro_id INT NOT NULL,
 leitor_id INT NOT NULL,
 data_emprestimo DATE NOT NULL,
 data_prevista DATE NOT NULL,
 situacao ENUM('EMPRESTADO','DEVOLVIDO') NOT NULL DEFAULT 'EMPRESTADO',
 CONSTRAINT fk_emp_livro FOREIGN KEY (livro_id) REFERENCES livros(id) ON DELETE RESTRICT,
 CONSTRAINT fk_emp_leitor FOREIGN KEY (leitor_id) REFERENCES leitores(id) ON DELETE RESTRICT,
 CONSTRAINT ck_datas CHECK (data_prevista >= data_emprestimo)
) ENGINE=InnoDB;
-- Execute uma unica vez em banco novo; os dados permanecem apos fechar o programa.
INSERT INTO livros(titulo,autor,categoria,disponivel) VALUES
('Dom Casmurro','Machado de Assis','Literatura Brasileira',1),
('O Pequeno Principe','Antoine de Saint-Exupery','Ficcao',1),
('Capitaes da Areia','Jorge Amado','Literatura Brasileira',1);
INSERT INTO leitores(nome,telefone,email) VALUES
('Ana Silva','(47) 99999-0000','ana.silva@exemplo.com'),
('Carlos Souza','(48) 98888-0000','carlos.souza@exemplo.com');
