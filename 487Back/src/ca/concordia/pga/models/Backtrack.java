package ca.concordia.pga.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class Backtrack {
	
	PlanningGraph pg;
	
	Backtrack(){
		this.pg = new PlanningGraph();
	}
	
	public Backtrack(PlanningGraph _pg)
	{
		this.pg = _pg;
	}

	public PlanningGraph getPg() {
		return pg;
	}

	public void setPg(PlanningGraph pg) {
		this.pg = pg;
	}
	
	public Vector<Service> perform()
	{
		Vector<Service> solutionSet = new Vector<Service>();
		Set<Concept> initialParams = pg.getPLevel(0);
		Set<Concept> goals = new HashSet<Concept>(pg.getGoalSet());
		Set<Concept> intersection;
		Set<Service> services = new HashSet<Service>();
		Set<Concept> intersection2 = new HashSet<Concept>();
		Vector<Service> relevant = new Vector<Service>();
		
		System.out.println("*******This solution graph for Backtracking is shown as follows*************");
		System.out.println();
		System.out.println("Input Concepts: " + initialParams);
		System.out.println("Goal Concepts: " + goals);
		System.out.println();
		System.out.println("************Level 0****************** ");
		System.out.println("Service: []");
		System.out.println("Parameters: " + goals);

		System.out.println("Numbers of concepts: " + goals.size());
		
		Random r = new Random();
		int idx = 0;
		int i = 1;
		boolean alreadyincluded = false;
		int level = pg.getALevels().size() - 1;
		long startTime = System.currentTimeMillis();
		while(!initialParams.containsAll(goals))
		{
			
			System.out.println("************Level " + level + "****************** ");
			
			for (Service s : pg.getALevel(level))
			{
				intersection = new HashSet<Concept>(goals);
				intersection.retainAll(s.getOutputConceptSet());

				
				if (!intersection.isEmpty() && !solutionSet.contains(s))
				{
					intersection2 = intersection;
					if (!pg.getPLevel(level - 1).containsAll(intersection))
					{
						
					
						relevant.add(s);
						goals.removeAll(s.getOutputConceptSet());
					}
				}

			}
			
			if (relevant.isEmpty())
			{
				System.out.println("Backtracking reached a deadend");
				return null;
			}
			
			idx = r.nextInt(relevant.size());
	
			//goals.removeAll(relevant.get(idx).getOutputConceptSet());
			//goals.clear();
			
			solutionSet.addAll(0, relevant);
			for (Service s : relevant )
			{
				goals.addAll(s.getInputConceptSet());
			}
			goals.addAll(relevant.get(idx).getInputConceptSet());
			System.out.println("Service: " + relevant.get(idx).getName());
			System.out.println("Parameters: " + goals);
			System.out.println("Numbers of concepts: " + goals.size());
		
			relevant.clear();
			
			i++;
			level--;
		}
		System.out.println("================================");
		long endTime = System.currentTimeMillis();
		System.out.println("Backtracking Time: " + (endTime - startTime) + "ms");
		System.out.println("Invoked Services: " + solutionSet.size());
		System.out.println("Levels: " + (pg.getALevels().size()));
		System.out.println("================================");
		System.out.println("Backtracking Time: " + (endTime - startTime) + "ms");

		
		
		return solutionSet;
	}
	

}
