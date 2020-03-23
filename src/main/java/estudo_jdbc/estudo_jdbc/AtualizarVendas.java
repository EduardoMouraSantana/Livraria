package estudo_jdbc.estudo_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class AtualizarVendas {
	// JDBC driver name and database URL 
	   static final String JDBC_DRIVER = "org.h2.Driver";   
	   static final String DB_URL = "jdbc:h2:~/LivrariaApp";  
	   
	//  Database credentials 
	   static final String USER = "sa"; 
	   static final String PASS = "";
	   
	   Connection conn = null; 
	   Statement stmt = null;
	   Statement stmtNomeLivro = null;
	   
	   String nomeLivro;
	   Scanner sc = new Scanner(System.in);
	   String nm_livro;
	   int quantidade;
	   int id_livro;
	   int estoque;
	   float preco;
	   float total;
	   
	   public void ler() {
		   System.out.println();
		   try {
			   Class.forName(JDBC_DRIVER);
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);
			   stmt = conn.createStatement();
			   stmtNomeLivro = conn.createStatement();
			   String sql = "SELECT * FROM itens_da_venda";
			   ResultSet rs = stmt.executeQuery(sql);
			   
			   while(rs.next()) {
				   int id_venda = rs.getInt("idvenda");
				   int id_livro = rs.getInt("id_livro");
				   int quantidade = rs.getInt("qtd");
				   String subtotal = rs.getNString("subtotal");
				   
				   String sqlNLivro = "SELECT titulo FROM LIVRO WHERE idlivro = ' " + id_livro + " ' ";
				   ResultSet rsLivro = stmtNomeLivro.executeQuery(sqlNLivro);
				   while(rsLivro.next()) {
					   nomeLivro = rsLivro.getNString("titulo");   
				   }
				   
				   
				   
				   System.out.println(id_venda + " " + nomeLivro + " " + quantidade + " " + subtotal);
			   }
			   
	
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   
	   }
	   
	   public void modificarVenda(int id_venda, String livro, int qtd) {
		   try {
			   Class.forName(JDBC_DRIVER);
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);
			   stmt = conn.createStatement();
			   System.out.println("Digite o livro que deseja");
			   nm_livro = sc.nextLine();
			   System.out.println("Digite a quantidade");
			   quantidade = Integer.parseInt(sc.nextLine());
			   
			   String sql = "SELECT idlivro, preco, estoque, titulo FROM LIVRO WHERE TITULO = '" + nm_livro +"' ";
			   ResultSet rs = stmtNomeLivro.executeQuery(sql);
			   if(rs.next()) {
				   this.id_livro = rs.getInt("idlivro");
				   this.preco = rs.getFloat("preco");
				   this.estoque = rs.getInt("estoque");
				   }
			   if(id_livro==0) {
				   System.out.println("Não existe um livro com esse nome");
				   return;
			   }
			   else {
				   
				   if(qtd>estoque) {
					   System.out.println("Não há essa quantidade, há apenas " + this.estoque + " " + nm_livro);
					   return;
				   }
				   else {
					   
				   
				   
				   String update = "UPDATE ITENS_DA_VENDA SET id_livro = '" + this.id_livro +"', qtd = '" + quantidade + "', subtotal = '"+ preco*quantidade +"'";
				   String sqlDwEs = "UPDATE LIVRO SET ESTOQUE ='" + (this.estoque - quantidade) + "' WHERE idlivro = '"+ this.id_livro +"'" ;
				   stmt.executeUpdate(sqlDwEs);
				     
				   String sqlLivro = "SELECT idlivro, preco, estoque FROM LIVRO WHERE TITULO = '" + livro + "'";
				   ResultSet rsLivro = stmtNomeLivro.executeQuery(sqlLivro);
				   if(rsLivro.next()) {
					   this.id_livro = rsLivro.getInt("idlivro");
					   this.preco = rsLivro.getFloat("preco");
					   this.estoque = rsLivro.getInt("estoque");
				   }
				   
				   update += "WHERE idvenda = '" + id_venda + "' and id_livro = '" + id_livro +"' and qtd ='"+ qtd +"'";
				   stmt.executeUpdate(update);
				   
				   String sqlUpEs = "UPDATE LIVRO SET ESTOQUE ='" + (this.estoque + qtd) + "' WHERE idlivro = '"+ this.id_livro +"'" ;
				   stmt.executeUpdate(sqlUpEs);
				   
				   System.out.println("Modificado com sucesso");
				   
				   rsLivro.close();
			   }
				   
			   }
			   
			   
		} catch (Exception e) {
			e.printStackTrace();
		}
	   }
	   
	   public void deletar(int id_venda, String livro, int qtd) {
		   try {
			   Class.forName(JDBC_DRIVER);
			   conn = DriverManager.getConnection(DB_URL,USER,PASS);
			   stmt = conn.createStatement();
			   
			   String sqlId = "SELECT idlivro, preco FROM LIVRO WHERE TITULO = '" + livro +"' ";
			   ResultSet rs = stmtNomeLivro.executeQuery(sqlId);
			   if(rs.next()) {
				   this.id_livro = rs.getInt("idlivro");
				   this.preco = rs.getFloat("preco");
				   }
			   
			   String sql = "DELETE FROM ITENS_DA_VENDA WHERE IDVENDA = '" + id_venda +"' and ID_LIVRO ='" + id_livro + "' and QTD = '" + qtd +"'";
			   stmt.executeUpdate(sql);
			   System.out.println("Deletado com sucesso");
			   
			   String sqlTotal = "SELECT TOTAL FROM VENDA WHERE IDVENDA='" + id_venda +"'";
			   ResultSet rsUp = stmt.executeQuery(sqlTotal);
			   while(rsUp.next()) {
				   total = rsUp.getFloat("total");
			   }
			   String update = "UPDATE VENDA SET TOTAL ='"+ (total-(preco*qtd)) + "' WHERE IDVENDA='" + id_venda +"'";
			   stmt.executeUpdate(update);
			   
		} catch (Exception e) {
			e.printStackTrace();
		}
	   }
}
