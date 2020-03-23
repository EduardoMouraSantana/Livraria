package estudo_jdbc.estudo_jdbc;

import java.sql.Date;

import java.sql.SQLException;


public class App 
{
    public static void main( String[] args ) throws SQLException, ClassNotFoundException
    {
    	System.out.println("Inicio da execução");
    	Relatorio relatorio = new Relatorio();
    	relatorio.exibir();
		Venda venda1 = new Venda("Marcelo Silva", Date.valueOf("2020-04-20"), 3, "Harry Potter");
		venda1.adicionarLivros("Java Biblia", 1);
		venda1.fecharVenda();
		Venda venda2 = new Venda("Eduardo", Date.valueOf("2020-04-22"), 1, "Java Biblia");
		venda2.fecharVenda();
    	AtualizarVendas atualizacao = new AtualizarVendas();
    	atualizacao.ler();
    	 
    	atualizacao.modificarVenda(1, "Harry Potter", 3);
    	atualizacao.ler();
    	
    	atualizacao.deletar(2, "Java Biblia", 1);
    	
    	System.out.println("Final da execução");
    	
    }
    
   
}
