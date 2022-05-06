import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileG {
	
	
	public void write()
	{
	 try {
	      FileWriter myWriter = new FileWriter("filename.txt");
	      myWriter.write("Files in Java might be tricky, but it is fun enough!");
	      myWriter.close();
	      System.out.println("Successfully wrote to the file.");
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	
	public void scrivi(ArrayList<Ordine> ordini, int nodi, ArrayList<Integer> costi, ArrayList<Integer> capacita)
	{
		try {
		      FileWriter myWriter = new FileWriter("filename.txt");
		      
		      Generator g = new Generator();
		      
		      myWriter.write(String.valueOf(nodi));
		      myWriter.write("\n");
		      myWriter.write("\n");
		      
		      for(int i=0; i<costi.size();i++)
		      {	    	  
		    	  myWriter.write(Integer.toString(costi.get(i)));
		    	  myWriter.write(" ");
		      }
		      myWriter.write("\n");
		      myWriter.write("\n");
		      
		      for(int i=0; i<capacita.size();i++)
		      {	    	  
		    	  myWriter.write(Integer.toString(capacita.get(i)));
		    	  myWriter.write(" ");
		      }
		      myWriter.write("\n");
		      myWriter.write("\n");
		      
		      for(int i=0; i<ordini.size(); i++)
		      {
		    	  myWriter.write(ordini.get(i).getPartenza() + " " + ordini.get(i).getArrivo() + " " + Integer.toString(ordini.get(i).getQuantita()));
		    	  myWriter.write("\n");
		    	  myWriter.write("\n");
		      }
		      
		      
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

}
