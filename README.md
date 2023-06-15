# sqlite-persistencia
<h1 align="center">Java com sqlite</h1>

## Descrição do Projeto
<p align="center">Simples biblioteca para persistência de Classes no sqlite3</p>

A classe Sqlite controla a persistencia dos objetos contidos na List<Classes>

Utilizar as anotações @Tabela e @Coluna nos modelos para criar as tabelas sqlite.

Configura-se:
1. o diretorio do arquivo db com setFile;
2. o pacote com os modelos para scan com addPackScan;


INICIA-SE A PERSISTENCIA COM sqlite,init();

Persistência:
1. Save - Insert into;
2. Update - Update;
3. Delete - Delete;
4. get - Select;
5. all - FindAll;

