PROJETO INTEGRADOR - ETAPA 1
Sistema de Controle de Biblioteca
Aluno: Marcos Laureano

Projeto Java 8 / NetBeans com Ant (sem Maven).

COMO ABRIR
1. Extraia o ZIP.
2. No NetBeans, use Arquivo > Abrir Projeto e selecione a pasta PI_Etapa1_SistemaBiblioteca_MarcosLaureano.
3. Execute Limpar e Construir e depois Executar.
Alternativamente, execute ant clean jar run na pasta do projeto.

ORGANIZACAO
src/ - codigo-fonte Java
nbproject/project.xml - configuracao de projeto livre NetBeans
build.xml - compilacao Ant
sql/biblioteca.sql - esquema MySQL planejado para etapas futuras
documentacao/Projeto_Integrador_Etapa1.docx - documento de requisitos e prototipo
documentacao/Imagem_Projeto.jpeg - imagem original anexa

IMPORTANTE
O prototipo da Etapa 1 usa dados em memoria e demonstra cadastro de objetos e emprestimo.
Nao ha interface grafica nem conexao ativa com MySQL nesta etapa.

ENTREGA ETAPA 1
- Diagrama_UML_SistemaBiblioteca.jpeg: imagem UML na raiz do projeto.
- documentacao/Projeto_Integrador_Etapa1.docx: documento de planejamento.
- A etapa 1 apresenta prototipo em memoria; MySQL esta planejado para as proximas etapas.
- Para compilar: ant clean jar; para executar: ant run.
- A devolucao duplicada e impedida por validacao na classe Emprestimo.
