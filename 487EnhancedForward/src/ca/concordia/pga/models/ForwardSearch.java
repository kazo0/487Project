package ca.concordia.pga.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ForwardSearch {
	
	private Map<String, Service> serviceMap;
	PlanningGraph pg;
	
	ForwardSearch(){
		serviceMap = new HashMap<String, Service>();
		pg = new PlanningGraph();
	}
	
	public ForwardSearch(Map<String, Service> _serviceMap, PlanningGraph _pg)
	{
		this.serviceMap = new HashMap<String, Service>(_serviceMap);
		this.pg = _pg;
	}

	public Map<String, Service> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, Service> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public PlanningGraph getPg() {
		return pg;
	}

	public void setPg(PlanningGraph pg) {
		this.pg = pg;
	}
	
	
	public Vector<Service> perform()
	{
		System.out.println("*******This forward planning graph is shown as follows*************");
		System.out.println();
		System.out.println("Input Parameters: " + pg.getPLevel(0));
		System.out.println("Goal Parameters: " + pg.getGoalSet());
		System.out.println();
		
		int i = 0;
		Set<Concept> currentP = new HashSet<Concept>();
		Set<Service> currentS;
		Set<Service> used = new HashSet<Service>();
		
		long startTime = System.currentTimeMillis();
		while (!pg.getPLevel(i).containsAll(pg.getGoalSet()))
		{
			pg.addALevel(new HashSet<Service>());
			currentS = pg.getALevel(i+1);
			currentP = pg.getPLevel(i);
			
			System.out.println("************Level " + i + "****************** ");
			for (Service s : serviceMap.values())
			{
				if (currentP.containsAll(s.getInputConceptSet()) && !used.contains(s))
				{
					currentS.add(s);
					used.add(s);
				}
			}
			
			Set<Concept> newP = new HashSet<Concept>(currentP);
			pg.addPLevel(newP);
			
			Iterator<Service> ite = currentS.iterator();
			Service s;
			while (ite.hasNext())
			{
				s = ite.next();
				if (!pg.getPLevel(i+1).containsAll(s.getOutputConceptSet()))
				{
					pg.getPLevel(i+1).addAll(s.getOutputConceptSet());
				}
				else
				{
					ite.remove();
				}
				if (pg.getPLevel(i+1).containsAll(pg.getGoalSet()))
				{
					
					break;
				}
			}
			
			System.out.println("Services: " + pg.getALevel(i));
			System.out.println("Parameters: " + pg.getPLevel(i));
			System.out.println("Numbers of services: " + pg.getALevel(i).size());
			System.out.println("Numbers of parameters: " + pg.getPLevel(i).size());

			i++;
		}
		System.out.println("************Level " + i + "****************** ");
		System.out.println("Services: " + pg.getALevel(i));
		System.out.println("Parameters: " + pg.getPLevel(i));
		System.out.println("Numbers of services: " + pg.getALevel(i).size());
		System.out.println("Numbers of parameters: " + pg.getPLevel(i).size());
		
		Vector<Service> solutionSet = new Vector<Service>();
		if (pg.getPLevel(i).containsAll(pg.getGoalSet()))
		{
			
			for (Set<Service> level : pg.getALevels())
			{
				solutionSet.addAll(level);
			}
			
			System.out.println("================================");
			long endTime = System.currentTimeMillis();
			System.out.println("Forward Search Time: " + (endTime - startTime) + "ms");
			System.out.println("Invoked Services: " + solutionSet.size());
			System.out.println("Levels: " + (pg.getALevels().size()));
			System.out.println("================================");
			
			return solutionSet;
		}
		
		
		return null;
	}


}
