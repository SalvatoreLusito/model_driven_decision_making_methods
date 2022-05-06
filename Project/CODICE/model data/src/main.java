import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class main {

	public static void main(String[] args) {
		
		//creo il file se non esiste
		try {
		      File myObj = new File("filename.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.\n");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		//creo il generatore
		Generator g = new Generator();

		//creo i nodi casualmente
		ArrayList<Character> nodes = new ArrayList<Character>();
		nodes = g.generategraph();
		
		System.out.println("Numero nodi:");
		System.out.println(nodes.size());
		System.out.println(" ");
		
		System.out.println("Numero archi:");
		System.out.println(g.binomial(nodes.size(), 2));
		System.out.println(" ");
		
		ArrayList<Integer> costi = new ArrayList<Integer>();
		costi = g.genera_costo(g.binomial(nodes.size(), 2));
		
		System.out.println("Costi archi:");
		System.out.println(costi);
		System.out.println(" ");
		
		
		
		//TODO: IMPOSTARE NUMERO ORDINI
		ArrayList<Ordine> ordini = new ArrayList<Ordine>();
		{
			int random = g.random(25,25);
			for(int i=0; i<random; i++)
			{
				ordini.add(g.genera_ordine(nodes.size()));
			}
		}
		
		System.out.println("Numero ordini: " + ordini.size());
		System.out.println(" ");
		
		System.out.println("Ordini: ");
		System.out.println(ordini);
		System.out.println(" ");
		
		
		//TODO: genero capacità archi in base agli ordini 
		Support s = new Support();		
		int max = s.cap_max(ordini);
		int min = s.cap_min(ordini);
		int mean = s.cap_mean(ordini);
		
		int max2 = max+max;
		
		//settare capacità archi
		int min2 = min/2;
		int min3 = min/15;
		
		ArrayList<Integer> capacita = new ArrayList<Integer>();
		for(int i=0; i<g.binomial(nodes.size(), 2); i++)
		{
			capacita.add(g.random(min2*ordini.size(), mean*ordini.size()));
			//capacita.add(g.random(10000,100000));
		}
		
		System.out.println("Totale Richiesta:");
		System.out.println(s.cap_tot(ordini));
		System.out.println(" ");
		
		System.out.println("Capacità archi:");
		System.out.println(capacita);
		System.out.println(" ");
		
		
		//genero il file di testo
		FileG file = new FileG();
		
		file.scrivi(ordini, nodes.size(), costi, capacita);
		
		
	}

}
