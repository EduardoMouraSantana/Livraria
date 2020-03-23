package estudo_jdbc.estudo_jdbc;
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement; 
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;



public class Venda {
	
	// JDBC driver name and database URL 
	   static final String JDBC_DRIVER = "org.h2.Driver";   
	   static final String DB_URL = "jdbc:h2:~/livrariaApp";  
	   
	//  Database credentials 
	   static final String USER = "sa"; 
	   static final String PASS = "";
	   
	   Connection conn = null; 
	   Statement stmt = null; 
	
	Date data;
	float total= 0;
	int quantidade;
	List<Integer> Livros = new ArrayList<Integer>(); 
	List<Float> Subtotal = new ArrayList<Float>();
	List<Integer> Quantidade = new ArrayList<Integer>();
	int id_cliente;
	int id_livro;
	int id_venda;
	int estoque;
	

	public Venda(String nm_cliente, Date data, int quantidade, String livro) {
		super();
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String estoque = "SELECT estoque FROM LIVRO WHERE TITULO = '" + livro + "' ";
			ResultSet rsEstoque = stmt.executeQuery(estoque);
			while(rsEstoque.next()) {
				this.estoque = rsEstoque.getInt("estoque");
			}
			rsEstoque.close();
			
		if(quantidade>this.estoque) {
			System.out.println("Não há essa quantidade disponível, há apenas "+ this.estoque + " " + livro);
		}
		else {
			
		this.data = data;
		
		String preco = "SELECT PRECO FROM LIVRO WHERE TITULO = '" + livro + "' ";
		ResultSet rsPreco = stmt.executeQuery(preco);
		while(rsPreco.next()){
			Float subtotal = rsPreco.getFloat("preco") * quantidade;
			Subtotal.add(subtotal);
		}
		rsPreco.close();
		
		String cliente = "SELECT IDCLIENTE FROM CLIENTE WHERE NOME = '" + nm_cliente + "'";
		ResultSet rsCliente = stmt.executeQuery(cliente);
		while(rsCliente.next()) {
			id_cliente = rsCliente.getInt("idcliente");
		}
		rsCliente.close();
		
		String idLivro = "SELECT IDLIVRO FROM LIVRO WHERE TITULO = '" + livro +"'";
		ResultSet rsLivro = stmt.executeQuery(idLivro);
		while(rsLivro.next()) {
			id_livro = rsLivro.getInt("idlivro");
			Livros.add(id_livro);
		}
		System.out.println("Venda iniciada");
		
		rsLivro.close();
		
		Quantidade.add(quantidade);
		
		conn.close();
		stmt.close();
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	
	
	public void adicionarLivros(String livro, int qt) {
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String estoque = "SELECT estoque FROM LIVRO WHERE TITULO = '" + livro + "' ";
			ResultSet rsEstoque = stmt.executeQuery(estoque);
			while(rsEstoque.next()) {
				this.estoque = rsEstoque.getInt("estoque");
			}
			rsEstoque.close();
			if(qt>this.estoque) {
				System.out.println("Não há essa quantidade disponível, há apenas "+ this.estoque + " " + livro);
			}
			else {
				String idLivro = "SELECT IDLIVRO FROM LIVRO WHERE TITULO = '" + livro+"'";
				ResultSet rsLivro = stmt.executeQuery(idLivro);
				while(rsLivro.next()) {
					id_livro = rsLivro.getInt("idlivro");
					Livros.add(id_livro);
				}
			rsLivro.close();
			
			String preco = "SELECT PRECO FROM LIVRO WHERE TITULO = '"+ livro +"'";
			ResultSet rs = stmt.executeQuery(preco);
			while(rs.next()) {
				Float subtotal = rs.getFloat("preco") * qt;
				Subtotal.add(subtotal);
			}
			
			System.out.println("Livro adicionado");
			
			rs.close();
			
			Quantidade.add(qt);
			
			conn.close();
			stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void fecharVenda() {
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			
			if(data == null) {
				System.out.println("Impossível concluir a venda");
				return;
				
			}
			
			for(Float f: Subtotal) {
				total += f;
			}
			
			String sqlVendas = "INSERT INTO venda VALUES(default,'" + data + "','" + total + "','"+ id_cliente +"')";
			stmt.executeUpdate(sqlVendas);
			
			//PEGAR ID DA VENDA
			
			String idVenda = "SELECT idvenda FROM venda WHERE data = '"+ data +"' AND total = '"+ total +"'";	
			ResultSet rsvenda = stmt.executeQuery(idVenda);
			while(rsvenda.next()) {
				this.id_venda = rsvenda.getInt("idvenda");
			}
			rsvenda.close();
			
			for(int i = 0; i <= Livros.size()-1; i++) {
				String sql = "INSERT INTO itens_da_venda VALUES(' "+ this.id_venda + " ',' " + Integer.parseInt(Livros.get(i).toString()) + " ',' " + Integer.parseInt(Quantidade.get(i).toString()) +" ',' "+ Subtotal.get(i).toString() + " ')";
				
				stmt.executeUpdate(sql);
				
				String estoque = "SELECT estoque FROM LIVRO WHERE idlivro = '" + Integer.parseInt(Livros.get(i).toString()) + "' ";
				ResultSet rsEstoque = stmt.executeQuery(estoque);
				while(rsEstoque.next()) {
					this.estoque = rsEstoque.getInt("estoque");
				}
				String sqlUp = "UPDATE LIVRO SET ESTOQUE ='" + (this.estoque-Integer.parseInt(Quantidade.get(i).toString())) + "' WHERE idlivro = '"+ Integer.parseInt(Livros.get(i).toString()) +"'" ;
				stmt.executeUpdate(sqlUp);
			}
			
			System.out.println("Venda finalizada");
			
			conn.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	@Override
	public String toString() {
		return "Venda [data=" + data + ", total=" + total + ", quantidade=" + quantidade + ", Livros=" + Livros
				+ ", Subtotal=" + Subtotal + ", Quantidade=" + Quantidade + ", id_cliente=" + id_cliente + ", id_livro="
				+ id_livro + ", id_venda=" + id_venda + ", stmt=" + stmt + "]";
	}



	

	

}
	
	

