PROJETO INTEGRADOR - ETAPA 4 - SISTEMA BIBLIOTECA
Java Swing + JDBC + MySQL | NetBeans 8.2 | Ant | Java 8 | Sem Maven

1) No MySQL Workbench, conecte-se ao servidor local e execute sql/biblioteca_etapa4.sql.
   Execute o script de dados iniciais apenas UMA VEZ em banco novo para evitar duplicatas.
2) Abra config/banco.properties e altere db.user e db.password conforme o MySQL local.
   Nao compartilhe a senha preenchida em capturas ou entregas publicas.
3) O driver MySQL Connector/J 8.0.33 ja esta presente em lib/.
   Nao e necessario executar o script BAIXAR_DRIVER.ps1.
   O driver mysql-connector-j-8.0.33.jar JA ESTA incluso na pasta lib/.
4) Abra a pasta do projeto no NetBeans: Arquivo > Abrir Projeto.
5) Clique Limpar e Construir, depois Executar (F6).
6) Teste cadastro, edicao, exclusao, emprestimo, devolucao. Feche e reabra:
   os dados devem permanecer no banco MySQL.
7) Se aparecer 'Access denied', confira usuario e senha em config/banco.properties.
   Se aparecer 'Communications link failure', inicie o servico MySQL e confira porta 3306.

Persistencia definitiva: tabelas livros, leitores e emprestimos; JDBC PreparedStatement;
transacoes para emprestimo/devolucao; integridade referencial para historico.
Os dados iniciais sao exemplos didaticos, nao cadastros reais.

IMPORTANTE: O arquivo config/banco.properties deve ficar na raiz do projeto durante
execucao no NetBeans. A pasta dist/ inclui uma copia da configuracao e do driver
(quando disponivel), mas para executar o JAR fora do NetBeans, execute-o a partir
da pasta do projeto ou copie a pasta config para o diretorio de trabalho.

LIMITACAO DOS TESTES: A compilacao Ant pode ser verificada sem MySQL. O teste real
de conexao e CRUD requer servidor MySQL ativo e driver JDBC instalado no computador.

ANTES DE EXECUTAR: substitua ALTERE_AQUI pela senha do MySQL no arquivo
config/banco.properties. A senha pessoal foi retirada desta copia de entrega.
Nao envie a senha ao professor.
