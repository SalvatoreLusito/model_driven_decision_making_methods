import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Generator 
{
	public Generator()
	{}
	
	//BINOMIALE PER CALCOLARE NUMERO ARCHI DI UN GRAFO COMPLETO
	public int binomial(int n, int k)
    {
        if (k>n-k)
            k=n-k;

        int b=1;
        for (int i=1, m=n; i<=k; i++, m--)
            b=b*m/i;
        return b;
    }
	
	
	//GENERARE NUMERO INTERO RANDOM TRA MIN-MAX
	public int random(int min, int max)
	{
		int number=0;
		
		number = (int) (Math.random() * (max - min + 1) + min);
		return number;
	}
	
	//GENERARE UNA LETTERA CASUALE DELL'ALFABETO
	public char randomchar()
	{
		Random rd= new Random();
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		char letter = abc.charAt(rd.nextInt(abc.length()));
		return letter;
	}
	
	//GENERARE GRAFO 
	//TODO: IMPOSTARE NUMERO DI NODI
	public ArrayList<Character> generategraph()
	{
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int numbernodes = this.random(10,10);
		ArrayList<Character> nodes = new ArrayList<Character>();
		
		for(int i=0; i<numbernodes; i++)
		{
			nodes.add(abc.charAt(0));
		}
		
		return nodes;
	}
	
	//GENERARE ARCHI
	public ArrayList<String> generateedges(ArrayList<Character> nodi)
	{
		//insieme di archi
		ArrayList<String> edges = new ArrayList<String>();
		
		//creo il grafo
		ArrayList<Character> nodes = new ArrayList<Character>();
		nodes = this.generategraph();
						
		//creo gli archi
		for(int i=0;i<nodi.size();i++)
		{
			for(int j=i+1;j<nodi.size();j++)
			{
				if(i!=j)
				{
					String edge = String.valueOf(nodi.get(i)) + String.valueOf(nodi.get(j));
					edges.add(edge);
				}
				else
				{}
				
					
			}
		}
		
		return edges;
	}
	
	//INVERTIRE UNA STRINGA
	public String reverseIt(String source) {
	    int i, len = source.length();
	    StringBuilder dest = new StringBuilder(len);

	    for (i = (len - 1); i >= 0; i--){
	        dest.append(source.charAt(i));
	    }

	    return dest.toString();
	}
	
	//GENERARE GLI ARCHI INVERTITI PER GRAFO NON ORIENTATO
	public ArrayList<String> reverse_edge(ArrayList<String> edges)
	{
		ArrayList<String> reverse = new ArrayList<String>();


		for(int i=0; i<edges.size();i++)
			reverse.add(this.reverseIt(edges.get(i)));
		
		return reverse;
	}
	
	//GENERARE COSTO ARCO
	//TODO: IMPOSTARE COSTO ARCHI
	public ArrayList<Integer> genera_costo(int dim)
	{
		ArrayList<Integer> costi = new ArrayList<Integer>();
		
		for(int i=0; i<dim; i++)
		{
			costi.add(this.random(1, 10));
		}
		
		return costi;
	}
	
	//GENERARE CAPACITA' ARCO
	//TODO: IMPOSTARE CAPACITA' ARCHI
	public ArrayList<Integer> genera_capacita(int dim)
	{
		ArrayList<Integer> capacita = new ArrayList<Integer>();
		
		for(int i=0; i<dim; i++)
		{
			capacita.add(this.random(10000, 100000));
		}
		
		return capacita;
	}
	
	//GENERARE ORDINI DI SPEDIZIONE
	public Ordine genera_ordine(int nodes)
	{
		
		//indice partenza e prelevo nodo
		int partenza = this.random(1, nodes);
		
		//indice arrivo e prelevo nodo
		int arrivo = this.random(1, nodes);
			
		//fino a quando partenza e arrivo non differiscono ripeto generazione arrivo
		while (partenza==arrivo)
		{
			arrivo = this.random(1, nodes);
		}
		
		//TODO: IMPOSTARE QUANTITA' ORDINE SPEDIZIONE
		int quantita = this.random(50, 100);
		
		Ordine d = new Ordine(partenza, arrivo, quantita);
		
		return d;		
	}
		
}
