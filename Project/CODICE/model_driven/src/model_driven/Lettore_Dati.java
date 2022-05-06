package model_driven;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Lettore_Dati 
{
	int n_nodi;
	ArrayList<Integer> c_unitario;
	ArrayList<Integer> capacita;
	ArrayList<Ordine> ordini;
	
	
	public Lettore_Dati()
	{
		c_unitario = new ArrayList<Integer>();
		capacita = new ArrayList<Integer>();
		ordini = new ArrayList<Ordine>();
	}
	
	public void LeggiDati()
	{
		
	 try {
	      File myObj = new File("filename.txt");
	      Scanner myReader = new Scanner(myObj);
	      
	      //LEGGO IL NUMERO DI NODI
	      n_nodi = Integer.valueOf(myReader.nextLine());
	      //System.out.println(n_nodi);
	      
	      
	      //LEGGO I COSTI DEGLI ARCHI
	      myReader.nextLine();
	      String data = myReader.nextLine();
	      
	      String[] temp = data.split(" ");
	      for(int i=0; i<temp.length;i++)
	      {
	    	  c_unitario.add(Integer.valueOf(temp[i]));
	      }
	      
	      //System.out.println(c_unitario.toString());
	      
	      
	      //LEGGO LE CAPACITA DEGLI ARCHI
	      myReader.nextLine();
	      data = myReader.nextLine();
	      
	      temp = data.split(" ");
	      for(int i=0; i<temp.length;i++)
	      {
	    	  capacita.add(Integer.valueOf(temp[i]));
	      }
	      
	      //System.out.println(capacita.toString());
	      
	      
	      //LEGGO GLI ORDINI
	      myReader.nextLine();
	      
	      while(myReader.hasNext())
	      {
	    	  String[] t = myReader.nextLine().split(" ");
	    	  
	    	  Ordine o = new Ordine(Integer.valueOf(t[0]), Integer.valueOf(t[1]), Integer.valueOf(t[2]));
	    	  ordini.add(o);
	    	  myReader.nextLine();
	      }
	      
	      //System.out.println(ordini.toString());
	      
	      myReader.close();
	    } 
	 
	 catch (FileNotFoundException e) 
	 {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	 }
	}

	public int getN_nodi() {
		return n_nodi;
	}

	public ArrayList<Integer> getC_unitario() {
		return c_unitario;
	}

	public ArrayList<Integer> getCapacita() {
		return capacita;
	}

	public ArrayList<Ordine> getOrdini() {
		return ordini;
	}
	
	
	

}
