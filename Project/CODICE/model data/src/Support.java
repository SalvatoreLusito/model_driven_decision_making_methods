
import java.util.ArrayList;

public class Support 
{
	
	
	public int cap_max(ArrayList<Ordine> ordini)
	{
		int max=0;
		int temp=0;
		
		for(int i=0; i<ordini.size(); i++)
		{
			temp = ordini.get(i).getQuantita();
			if(temp>max)
			{
				max = temp;
			}
				
		}
		return max;
	}
	
	
	public int cap_min(ArrayList<Ordine> ordini)
	{
		int min=ordini.get(0).getQuantita();
		int temp=0;
		
		for(int i=0; i<ordini.size(); i++)
		{
			temp = ordini.get(i).getQuantita();
			if(temp < min)
			{
				min = temp;
			}
		}
		
		
		return min;
	}
	
	
	public int cap_mean(ArrayList<Ordine> ordini)
	{
		int quantità=0;
		
		for(int i=0; i<ordini.size(); i++)
		{
			quantità+= ordini.get(i).getQuantita();
		}
		
		quantità = (int) (quantità/ordini.size());
		
		return quantità;
	}
	
	public int cap_tot(ArrayList<Ordine> ordini)
	{
		int totale=0;
		
		for(int i=0; i<ordini.size(); i++)
		{
			totale+= ordini.get(i).getQuantita();
		}

		return totale;

	}

}
