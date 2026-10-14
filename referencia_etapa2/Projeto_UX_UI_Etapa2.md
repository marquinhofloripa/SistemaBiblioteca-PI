# Projeto de UX/UI — Sistema Biblioteca | Etapa 2

**Projeto Integrador:** Sistema Biblioteca — Marcos Laureano  
**Plataforma planejada:** desktop Java (NetBeans/Ant), com MySQL em etapas futuras.  
**Escopo desta entrega:** projeto de interfaces, não implementação funcional.

## 1. Objetivo e público

Criar interfaces simples para o atendimento da biblioteca. Público principal: funcionários e responsáveis por cadastrar livros, leitores e empréstimos. Considerar usuários com pouca experiência em informática.

## 2. Funcionalidades e telas previstas

| Tela | Objetivo | Ação principal |
| --- | --- | --- |
| Início | Oferecer visão geral e atalhos | Acessar cadastros |
| Livros | Cadastrar e consultar livros | Salvar livro |
| Leitores | Cadastrar e consultar leitores | Salvar leitor |
| Empréstimos | Registrar empréstimos e devoluções | Registrar empréstimo |

Os três tipos de registro do projeto original são **Livro, Leitor e Empréstimo**. As telas foram pensadas para posterior implementação em Java Swing, sem alterar a arquitetura de domínio da Etapa 1.

## 3. Pesquisa e decisões de UX

**Cenário de uso:** atendente precisa registrar um empréstimo durante atendimento presencial, com poucos passos e confirmação clara.  
**Hipóteses de projeto (não validadas com usuários reais):** navegação lateral constante facilita localização; formulários curtos reduzem erros; disponibilidade visível evita tentativas de empréstimo de exemplares indisponíveis.

**Fluxo principal:** Início → Empréstimos → Selecionar livro disponível → Selecionar leitor → Conferir datas → Registrar → Receber confirmação.  
**Fluxo de devolução:** Empréstimos → Identificar empréstimo aberto → Registrar devolução → Atualizar disponibilidade.

**Regras de negócio a refletir na interface:** impedir empréstimo de livro indisponível; exigir leitor e livro válidos; conferir datas; impedir devolução duplicada; informar sucesso ou erro por texto.

## 4. UI — identidade visual

| Uso | Cor | Código |
| --- | --- | --- |
| Menu lateral e títulos | Azul-marinho | `#172B4D` |
| Ações primárias e seleção | Azul | `#2458A6` |
| Fundo da janela | Cinza muito claro | `#F4F7FB` |
| Superfícies e campos | Branco | `#FFFFFF` |
| Bordas | Cinza-azulado | `#CBD5E1` |
| Texto secundário | Cinza-azulado escuro | `#52647A` |
| Sucesso (mensagem + ícone) | Verde | `#167347` |
| Destaque de foco | Amarelo | `#F4B740` |

**Tipografia:** Arial ou fonte sans-serif nativa equivalente. Corpo de texto: 16 px; rótulos: 15–16 px; títulos: 24–30 px.  
**Dimensão de referência dos wireframes:** 1280 × 800 px; interface com menu lateral fixo e área de conteúdo.  
**Consistência:** mesmas posições para título, formulários, botões e tabelas; rótulos sempre visíveis; ações principais em azul.

## 5. Acessibilidade planejada

1. Garantir contraste de pelo menos 4,5:1 para textos normais; revisar combinações finais ao implementar.
2. Nunca comunicar disponibilidade ou erro apenas por cor; acrescentar texto como “Disponível”, “Emprestado” e mensagens explicativas.
3. Oferecer navegação por teclado, ordem lógica de foco, atalhos adequados e indicação visível de foco.
4. Associar rótulos a campos e exibir instruções e erros próximos ao controle afetado.
5. Utilizar botões com texto claro (“Salvar livro”, “Registrar empréstimo”) e áreas clicáveis confortáveis.
6. Evitar bloqueio por tempo e permitir corrigir dados antes de confirmar.
7. Manter legibilidade com redimensionamento de janela e fontes do sistema; verificar comportamento em 1366×768 e 1920×1080.
8. Em Java Swing, configurar nomes e descrições acessíveis (`AccessibleContext`) e navegação por Tab.

## 6. Validação de usabilidade proposta

Testar com 2–3 pessoas, se possível, observando: (a) localizar cadastro de livro; (b) cadastrar leitor; (c) registrar empréstimo; (d) identificar livro indisponível. Registrar tempo, dúvidas, erros e melhorias. **Esses testes são propostos; não foram executados nesta entrega.**

## 7. Limites do protótipo

Os wireframes são representações estáticas e o HTML permite alternar telas. Não existe persistência, autenticação, integração MySQL ou gravação real. Os dados exibidos são fictícios. O Figma é recomendado pelo enunciado, mas não obrigatório; SVGs editáveis podem ser importados ao Figma para refinamento posterior.
