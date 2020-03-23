package estudo_jdbc.estudo_jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Relatorio {
	// JDBC driver name and database URL 
	   static final String JDBC_DRIVER = "org.h2.Driver";   
	   static final String DB_URL = "jdbc:h2:~/LivrariaApp";  
	   
	//  Database credentials 
	   static final String USER = "sa"; 
	   static final String PASS = "";
	   
	   Connection conn = null; 
	   Statement stmt = null;
	   
	   int id_venda;
	   Date data;
	   float total;
	   int id_cliente;
	   String nome;
	   int qtd;
	   int id_livro;
	   
	   public void exibir() {
		   try {
			 Class.forName(JDBC_DRIVER);
			 conn = DriverManager.getConnection(DB_URL,USER,PASS);
			 stmt = conn.createStatement();
			 
			 // VENDA MAIS CARA
			 String sql2 = "SELECT * FROM VENDA ORDER BY TOTAL DESC";
			 ResultSet rs = stmt.executeQuery(sql2);
			   while(rs.next()) {
				  id_venda = rs.getInt("idvenda");
				  data = rs.getDate("data");
				  total = rs.getFloat("total");
				  id_cliente = rs.getInt("idcliente");
				  String nm_cliente = "SELECT NOME FROM CLIENTE WHERE IDCLIENTE ='"+ id_cliente +"'";
				  ResultSet rsNome = stmt.executeQuery(nm_cliente);
				  while(rsNome.next()) {
					  nome = rsNome.getNString("nome");
				  }
				  System.out.println("Venda de maior valor");
				  System.out.println("[ID_VENDA: "+ id_venda+", DATA: "+ data +", TOTAL: "+ total +", NOME DO CLIENTE: " + nome +"]");
				  break;
			   }
			   
			   // CLIENTE Q MAIS COMPRA
			   
			   String sql3 = "SELECT COUNT(IDCLIENTE), IDCLIENTE FROM VENDA ORDER BY IDCLIENTE";
			   ResultSet rsCliente = stmt.executeQuery(sql3);
			   while(rsCliente.next()) {
				   id_cliente = rsCliente.getInt("idcliente");
				   qtd = rsCliente.getInt("count(idcliente)");
				   String nm_cliente = "SELECT NOME FROM CLIENTE WHERE IDCLIENTE ='"+ id_cliente +"'";
					  ResultSet rsNome = stmt.executeQuery(nm_cliente);
					  while(rsNome.next()) {
						  nome = rsNome.getNString("nome");
					  }
				   System.out.println("Melhor comprador");
				   System.out.println("[NOME: " + nome +", QUANTIDADE DE ITENS COMPRADOS: " + qtd + "]");
				   break;
			   }
			   
			   //LIVRO MAIS VENDIDO
			   String sql1 = "SELECT ID_LIVRO, SUM(QTD) FROM ITENS_DA_VENDA GROUP BY ID_LIVRO ORDER BY QTD DESC";
			   ResultSet rsLivro = stmt.executeQuery(sql1);
			   while (rsLivro.next()) {
				   id_livro = rsLivro.getInt("ID_LIVRO");
				   qtd = rsLivro.getInt("SUM(QTD)");
				   String nmLivro = "SELECT TITULO FROM LIVRO WHERE IDLIVRO ='"+ id_livro +"'";
					  ResultSet rsNome = stmt.executeQuery(nmLivro);
					  while(rsNome.next()) {
						  nome = rsNome.getNString("TITULO");
					  }
				   
				   System.out.println("Livro mais vendido");
				   System.out.println("[TITULO: "+ nome +", QUANTIDADE:"+ qtd +"]");
				   break;
			   }
			   
			   conn.close();
			   rs.close();
			   rsCliente.close();
			   rsLivro.close();
			   stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	   }
}
