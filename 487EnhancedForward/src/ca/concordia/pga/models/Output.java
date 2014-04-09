package ca.concordia.pga.models;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Output {
	static final String RESULTS_URL = "resources/output/";
	static final String RESULTS_NAME = "result.txt";
	private PlanningGraph pg;
	private PrintWriter writer;
	private Vector<Service> sol;

	public Output(){
		pg = new PlanningGraph();
		sol = new Vector<Service>();
		
	}
	
	public Output(PlanningGraph _pg, Vector<Service> solution){
		this.pg = _pg;
		this.sol = solution;
		
		try {
			writer = new PrintWriter(RESULTS_URL + RESULTS_NAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void writeResult()
	{
		Set<Service> services = new HashSet<Service>();
		String line = "";
		
		for (int i = 1; i < pg.getALevels().size(); i++)
		{
			services = pg.getALevel(i);
			
			for (Service s : services)
			{
				line += s.getName() + ",";
			}
	
			writer.println(line.substring(0, line.lastIndexOf(",")));
			line = "";
		}
		writer.println();
		if (sol != null)
		{
			writer.print("Solution path: " + sol);
		}
		else
		{
			writer.print("Backtracking reached a deadend");
		}
		writer.close();
	}
}
