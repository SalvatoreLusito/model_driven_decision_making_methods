package model_driven;

import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloModel;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Modello_Finale 
{

	public static void main(String[] args) 
	{
		//LEGGO DATI DAL FILE
		Lettore_Dati solver = new Lettore_Dati();
		solver.LeggiDati();
		int n_nodi = solver.getN_nodi();
		ArrayList<Integer> c_unitario = solver.getC_unitario();
		ArrayList<Integer> capacita = solver.getCapacita();
		ArrayList<Ordine> ordini = solver.getOrdini();
		
		//STAMPO PROBLEMA
		System.out.println("Numero nodi: " + n_nodi);
		System.out.println("Costo unitario archi: " + c_unitario.toString());
		System.out.println("Capacità archi: " + capacita.toString());
		System.out.println("Ordinazioni da soddisfare: " + ordini.toString());
		System.out.println("\n");
			
		//INIZIALIZZO I NODI DEL GRAFO 
		int[] nodi = new int[n_nodi];
		int n = 1;
		for(int i=0; i<n_nodi; i++)
		{
			nodi[i] = n;
			n++;
		}
		
		//INIZIALIZZO MATRICE DEI COSTI UNITARI LUNGO GLI ARCHI
		int[][] costi = new int [n][n];
		int index=0;
		for(int i=0; i<n_nodi; i++) //riempio matrice superiore
		{
			for(int j=i; j<n_nodi; j++)
			{
				if(i==j) costi[i][j] = 0;
				else
				{
					costi[i][j] = c_unitario.get(index);
					if(index==c_unitario.size()-1)
						index=0;
					else
						index++;
				}
			}
		}
		for(int j=0; j<n_nodi; j++) //riempio matrice inferiore
		{
			for(int i=j; i<n_nodi; i++)
			{
				if(i==j) costi[i][j] = 0;
				else
				{
					costi[i][j] = c_unitario.get(index);
					if(index==c_unitario.size()-1)
						index=0;
					else
						index++;
				}
			}			
		}	
		System.out.println("Matrice dei costi unitari: \n");
		for(int i=0;i<n_nodi;i++)
		{
			for(int j=0;j<n_nodi;j++)
			{
				System.out.print(costi[i][j] + " | ");
			}
			System.out.println("\n");
		}		
		
		//INIZIALIZZO MATRICE DELLE CAPACITA' DEGLI ARCHI
			int[][] cap_archi = new int [n][n];
			index=0;
			
			
			for(int i=0; i<n_nodi; i++) //riempio matrice superiore
			{
				for(int j=i; j<n_nodi; j++)
				{
					if(i==j) cap_archi[i][j] = 0;
					else
					{
						cap_archi[i][j] = capacita.get(index);
						if(index==capacita.size()-1)
							index=0;
						else
							index++;
					}
				}
			}
			for(int j=0; j<n_nodi; j++) //riempio matrice inferiore
			{
				for(int i=j; i<n_nodi; i++)
				{
					if(i==j) cap_archi[i][j] = 0;
					else
					{
						cap_archi[i][j] = capacita.get(index);
						if(index==capacita.size()-1)
							index=0;
						else
							index++;
					}
				}
			}	
			System.out.println("Matrice delle capacità archi: \n");
			for(int i=0;i<n_nodi;i++)
			{
				for(int j=0;j<n_nodi;j++)
				{
					System.out.print(cap_archi[i][j] + " | ");
				}
				System.out.println("\n");
			}
			
			// inizializzo ordini suddividendo partenze, arrivi e quantità da trasportare
			int[] partenze = new int[ordini.size()];
			int[] arrivi = new int[ordini.size()];
			int[] q = new int[ordini.size()];
			
			for(int i=0;i<ordini.size();i++)
			{
				partenze[i] = ordini.get(i).getPartenza();
				arrivi[i] = ordini.get(i).getArrivo();
				q[i] = ordini.get(i).getQuantita();
			}
			
/* --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- */
		
		//MODELLO	
		
		//TODO: VARIABILI
		try 
		{
			//dichiaro il solver
			IloCplex cplex = new IloCplex();
	
			
			//VARIABILI
			
			//Y = VARIABILE BOOLEANA CHE INDICA SE UN ARCO VIENE USATO O MENO 
			IloNumVar[][][][] y = new IloNumVar[n_nodi][n_nodi][ordini.size()][];
			for(int o=0;o<ordini.size();o++)
			{
				for(int i=0;i<n_nodi;i++)
				{
					for(int j=0;j<n_nodi;j++)
					{
							if(i!=j)
							{
								y[i][j][o] = cplex.boolVarArray(2);
							}
					}
					
				}
			}
			
			//X = QUANTITA' TRASPORTATA ATTRAVERSO OGNI ARCO
			IloNumVar[][][][] x = new IloNumVar[n_nodi][n_nodi][ordini.size()][];
			for(int o=0;o<ordini.size();o++)
			{
				for(int i=0;i<n_nodi;i++)
				{
					for(int j=0;j<n_nodi;j++)
					{
							if(i!=j)
							x[i][j][o] = cplex.numVarArray(2,0, Integer.MAX_VALUE);
					}
					
				}
			}
			
			
			//TODO: OBJ FUNCTION		
			//OBJECTIVE FUNCTION, FUNZIONE OBIETTIVO
			IloLinearNumExpr obj = cplex.linearNumExpr();
			for(int o=0;o<ordini.size();o++)
			{
				for(int k=0;k<2;k++)
				{
					for(int i=0;i<n_nodi;i++)
					{
						for(int j=0;j<n_nodi;j++)
						{
							if(i!=j)
							{
								obj.addTerm(costi[i][j], x[i][j][o][k]);
							}
						}
					}
				}
			}
			cplex.addMinimize(obj);
			
			
			//TODO: CONSTRAINTS
					
			//QUANTITA' TRASPORTATE COMPRESE TRA IL 40 E IL 60 PERCENTO DEL TOTALE

			for(int i=0; i<n_nodi; i++)
			{
				for(int j=0; j<n_nodi; j++)
				{
					for(int o=0; o<ordini.size(); o++)
					{
						for(int k=0; k<2; k++)
						{
							if(i!=j)
							{
								double min = (q[o]*0.4);
								double max = (q[o]*0.6);
								
								cplex.addGe(x[i][j][o][k], cplex.prod(min, y[i][j][o][k]));
								cplex.addLe(x[i][j][o][k], cplex.prod(max, y[i][j][o][k]));
								
							}
						}
					}
				}
			}
			
			//PERCORSI DISTINTI
			for(int o=0; o<ordini.size(); o++)
			{
				for(int i=0; i<n_nodi; i++)
				{
					for(int j=0; j<n_nodi; j++)
					{
						if(i!=j)
						{
							//cplex.addRange(0, cplex.sum(y[i][j][o][0],y[i][j][o][1]), 1);
							//cplex.addRange(0, cplex.sum(y[i][j][o][0],y[j][i][o][0]), 1);
							//cplex.addRange(0, cplex.sum(y[i][j][o][1],y[j][i][o][1]), 1);
							
							//cplex.addRange(0, cplex.sum(y[i][j][o][0],y[j][i][o][1]), 1);
							
							cplex.addLe(cplex.sum(y[i][j][o][0], y[i][j][o][1]), 1);	
							cplex.addLe(cplex.sum(y[i][j][o][0], y[j][i][o][0]), 1);
							cplex.addLe(cplex.sum(y[i][j][o][1], y[j][i][o][1]), 1);
						}
					}
				}
			}
			
			//la somma delle quantità uscenti dal nodo di partenza deve essere uguale a q[o]			
			IloLinearNumExpr p = cplex.linearNumExpr();
			for(int o=0; o<ordini.size(); o++)
			{
				int exit = q[o];
				for(int k=0; k<2; k++)
				{
					for(int i=0; i<n_nodi; i++)
					{
						if(i!=partenze[o]-1)
						{
							p.addTerm(x[partenze[o]-1][i][o][k], 1);
						}	
					}
				}	
				cplex.addEq(p, exit);
				p.clear();
			}
		
			//la somma delle quantità entranti nel nodo di destinazione deve essere uguale a q[o]	
			IloLinearNumExpr p2 = cplex.linearNumExpr();
			for(int o=0; o<ordini.size(); o++)
			{
				int exit = q[o];
				for(int k=0; k<2; k++)
				{
					for(int i=0; i<n_nodi; i++)
					{
						if(i!=arrivi[o]-1)
						p2.addTerm(x[i][arrivi[o]-1][o][k], 1);
					}
					
				}
				cplex.addEq(p2, exit);
				p2.clear();		
			}
			
			//la somma delle quantità entranti e uscenti nei nodi di passaggio deve essere uguale a 0
			IloLinearNumExpr p3 = cplex.linearNumExpr();
			IloLinearNumExpr p4 = cplex.linearNumExpr();
			for(int o=0; o<ordini.size(); o++)
			{
				for(int k=0;k<2;k++)
				{
					for(int i=0; i<n_nodi; i++)
					{
						for(int j=0; j<n_nodi; j++)
						{
							if((i!=j) && (i!= arrivi[o]-1) && (i!= partenze[o]-1))
							{
								p3.addTerm(1, x[i][j][o][k]);
								p4.addTerm(1, x[j][i][o][k]);
							}
						}
						cplex.addEq(cplex.diff(p3,p4), 0);
						p3.clear();
						p4.clear();
					}	
					
				}
				
			}
			
			
			//la somma delle quantità passanti per un arco non deve superare la sua capacità totale
			IloLinearNumExpr cap = cplex.linearNumExpr();
			for(int i=0; i<n_nodi; i++)
			{
				for(int j=0; j<n_nodi; j++)
				{
					if(i!=j)
					{
						for(int o=0; o<ordini.size(); o++)
						{
							for(int k=0; k<2; k++)
							{
								cap.addTerm(1, x[i][j][o][k]);
								cap.addTerm(1, x[j][i][o][k]);
							}
						}
						cplex.addLe(cap, cap_archi[i][j]);
						cap.clear();
					}
				}
			}
			
			
			
			//TODO: GESTIONE EURISTICHE
			
			//START ALGORITHM
			//0	CPX_ALG_AUTOMATIC	Automatic: let CPLEX choose; default
			//1	CPX_ALG_PRIMAL	Primal Simplex
			//2	CPX_ALG_DUAL	Dual Simplex
			//3	CPX_ALG_NET	Network Simplex
			//4	CPX_ALG_BARRIER	Barrier
			//5	CPX_ALG_SIFTING	Sifting
			cplex.setParam(IloCplex.Param.MIP.SubMIP.StartAlg, 0);
			
			
			//EMPHASIS 
			//0	CPX_MIPEMPHASIS_BALANCED	Balance optimality and feasibility; default
			//1	CPX_MIPEMPHASIS_FEASIBILITY	Emphasize feasibility over optimality
			//2	CPX_MIPEMPHASIS_OPTIMALITY	Emphasize optimality over feasibility
			//3	CPX_MIPEMPHASIS_BESTBOUND	Emphasize moving best bound
			//4	CPX_MIPEMPHASIS_HIDDENFEAS	Emphasize finding hidden feasible solutions
			cplex.setParam(IloCplex.Param.Emphasis.MIP, 1);
			
			
			//PROBING
			// -1	No probing
			// 0	Automatic: let CPLEX choose; default
			// 1	Moderate probing level
			// 2	Aggressive probing level
			// 3	Very aggressive probing level
			cplex.setParam(IloCplex.Param.MIP.Strategy.Probe, 3);
			//cplex.setParam(IloCplex.Param.MIP.Limits.ProbeTime, 200);
			
	
			//POLISH
			//cplex.setParam(IloCplex.Param.MIP.PolishAfter.Solutions, 1);
			cplex.setParam(IloCplex.Param.MIP.PolishAfter.Time, 180);
			
			
			//CUT SETTING
			//-1	Turn off MCF cuts
			//0	Automatic: let CPLEX decide whether to generate MCF cuts; default
			//1	Generate a moderate number of MCF cuts
			//2	Generate MCF cuts aggressively
			cplex.setParam(IloCplex.Param.MIP.Cuts.MCFCut, 2);
			
			
			// VARIABLE SELECTION STRATEGY
			// *-1	CPX_VARSEL_MININFEAS	Branch on variable with minimum infeasibility
			// 0	CPX_VARSEL_DEFAULT	Automatic: let CPLEX choose variable to branch on; default
			// 1	CPX_VARSEL_MAXINFEAS	Branch on variable with maximum infeasibility
			// 2	CPX_VARSEL_PSEUDO	Branch based on pseudo costs
			// *3	CPX_VARSEL_STRONG	Strong branching
			// 4	CPX_VARSEL_PSEUDOREDUCED	Branch based on pseudo reduced costs
					
			cplex.setParam(IloCplex.Param.MIP.Strategy.VariableSelect, 4);
			
			
			
			// NODE SELECTION:	
			// 0	Depth-first search
			// 1	Best-bound search; [DEFAULT]
			// 2	Best-estimate search
			// 3	Alternative best-estimate search
			
			//cplex.setParam(IloCplex.Param.MIP.Strategy.NodeSelect, 3);
			
			
			
			// EURISTICA RINSH 
			// -1	None: do not apply RINS heuristic
			// 0	Automatic: let CPLEX choose; [DEFAULT]
			// Any positive integer	Frequency to apply RINS heuristic
			
			//cplex.setParam(IloCplex.Param.MIP.Strategy.RINSHeur, 1);
			
			
			
			// SCALING 
			// -1	No scaling
			// 0	Equilibration scaling; [DEFAULT]
			// 1	More aggressive scaling
			
			//cplex.setParam(	IloCplex.Param.MIP.SubMIP.Scale, 1);
			
			
			//Feasibility pump switch
			// -1	Do not apply the feasibility pump heuristic
			// 0	Automatic: let CPLEX choose; [DEFAULT]
			// 1	Apply the feasibility pump heuristic with an emphasis on finding a feasible solution
			// 2	Apply the feasibility pump heuristic with an emphasis on finding a feasible solution with a good objective value
			
			//cplex.setParam(IloCplex.Param.MIP.Strategy.FPHeur, 2);
			
			
			
			// LOCAL BRANCH HEURISTIC
			// 0	false	CPX_OFF	no	Local branching heuristic is off; default
			// 1	true	CPX_ON	yes	Apply local branching heuristic to new incumbent
			
			//cplex.setParam(IloCplex.Param.MIP.Strategy.LBHeur, true);
			

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
				
						
				
			// LANCIO IL SOLVER
			
			try {
				OutputStream a = new FileOutputStream("logfile.txt");
				cplex.setOut(a);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
					
			System.out.println("--- --- --- --- --- --- --- ---");
				
			cplex.setParam(IloCplex.Param.Simplex.Display, 2);
			//cplex.setParam(IloCplex.Param.MIP.Tolerances.MIPGap, 0.1);
			cplex.setParam(IloCplex.DoubleParam.WorkMem, 4000);
			cplex.setParam(IloCplex.DoubleParam.TreLim, 4000);
			cplex.setParam(IloCplex.Param.TimeLimit, 300);
				
			cplex.solve();			
			Toolkit.getDefaultToolkit().beep();

			
			//System.out.println(cplex.getModel());
			System.out.println(cplex.getStatus());
			
			
			
			
			
			//creo il file se non esiste
			
			
			
			System.out.println("--- --- --- --- --- --- --- ---");
			
			System.out.println("Valore ottimale della funzione obiettivo: ");
			System.out.println(cplex.getObjValue());
			System.out.println("Valore migliore della funzione obiettivo ottenuto: ");
			System.out.println(cplex.getBestObjValue());
			
			System.out.println(" ");
			
			

			//TODO: STAMPA
			
			
			    
			
			//OUTPUT PER OTTENERE VALORI SPECIFICI
		try {
		      FileWriter myWriter = new FileWriter("solution.txt");
		      FileWriter model = new FileWriter("model.txt");
		      
		      //export del modello
		      Scanner myObj = new Scanner(System.in);
		      String decision;
		      
		      // Enter username and press Enter
		      System.out.println("Vuoi salvare il modello? (Y|N)"); 
		      decision = myObj.nextLine();   
		         
		      if(decision.compareTo("Y") == 0)
		      {
		    	  IloModel m = cplex.getModel();
			      model.write(m.toString());
			      model.close();
		      }
		      else
		      {}
		      
		      
		      
		      myWriter.write("Valore della funzione obiettivo: " + cplex.getObjValue());
		      myWriter.write("\n");
		      myWriter.write("\n");
			for(int o=0; o<ordini.size(); o++)
			{
				myWriter.write("Ordine: " + (o+1) + " || Partenza: " + (partenze[o]) + " - Arrivo: " + (arrivi[o]));
				myWriter.write("\n");
				System.out.println("Ordine: " + (o+1) + " || Partenza: " + (partenze[o]) + " - Arrivo: " + (arrivi[o]));	
				for(int k=0; k<2; k++)
				{
					for(int i=0; i<n_nodi; i++)
					{
						for(int j=0; j<n_nodi; j++)
						{
							if(i!=j)
							{
								if((cplex.getValue(x[i][j][o][k])>0))
								{
									System.out.println("Quantità trasferita per l'arco: [" + (i+1) +"-" + (j+1) + "] " + ":  " + Math.abs(cplex.getValue(x[i][j][o][k])));
									myWriter.write("Quantità trasferita per l'arco: [" + (i+1) +"-" + (j+1) + "] " + ":  " + Math.abs(cplex.getValue(x[i][j][o][k])));
									myWriter.write("\n");
								}
							}
						}
					}
				}
				System.out.println("--- --- --- --- --- ");
				myWriter.write("\n");
			}
			  myObj.close();
			  myWriter.close();
			  
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
			
			
			
	
			cplex.close();
				
			} 
			catch (IloException e) 
			{
				e.printStackTrace();
			}				
	}
}
