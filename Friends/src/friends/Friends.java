package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		String person1 = p1.toLowerCase();
		String person2 = p2.toLowerCase();
		ArrayList<String> res = new ArrayList<String>();
		if((g.map.get(person1) == null) || (g.map.get(person2) == null)) {return res;}
		Stack<String> stack = new Stack<String>();
		String[] preds = new String[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		Queue<Person> queue = new Queue<Person>();
		int p1Index = g.map.get(person1);
		visited[p1Index] = true;													
		queue.enqueue(g.members[p1Index]);																									
		while(!queue.isEmpty()) {															
			Person pred = queue.dequeue();										
			for(Friend ptr = pred.first; ptr != null; ptr = ptr.next) {			
				int FrIndex = ptr.fnum;											
				if(!visited[FrIndex]) {											
					visited[FrIndex] = true;							
					queue.enqueue(g.members[FrIndex]);
					preds[FrIndex] = pred.name;
				}
			}
		}
		int p2Index = g.map.get(person2);
		if (!visited[p2Index]) {
			return res;
		} else {
			stack.push(person2);
			String predecessor = preds[p2Index];
			while(!predecessor.equals(person1)) {
				stack.push(predecessor);
				predecessor = preds[g.map.get(predecessor)];
			}
			stack.push(person1);
			while(!stack.isEmpty()) {
				res.add(stack.pop());
			}
		}
		return res;
	}
	
	private static ArrayList<String> dfs(Graph g, String school, int start, boolean[] visited) {
		visited[start] = true;
		ArrayList<String> superclique = new ArrayList<String>();
		superclique.add(g.members[start].name);
		for(Friend next = g.members[start].first; next != null; next = next.next) {
			if(g.members[next.fnum].school == null) {continue;}
			if((!visited[next.fnum]) && (school.toLowerCase().equals(g.members[next.fnum].school.toLowerCase()))) {
				ArrayList<String> subclique = dfs(g, school, next.fnum, visited);
				superclique.addAll(subclique);
			}
		}
		return superclique;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		

		boolean[] visited = new boolean[g.members.length];
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < g.members.length; i ++) {
			if(g.members[i].school == null) {
				continue;
			}
			if(!school.toLowerCase().equals(g.members[i].school.toLowerCase())) {
				continue;
			}
			if (!visited[i]) {
				ArrayList<String> clique = dfs(g, school, i, visited);
				res.add(clique);
			}
		}
		return res;
		
	}
	
	private static boolean sPCheck(Friend friend, boolean[] visited) {
		while(friend != null) {
			if(!visited[friend.fnum]) {
				return true;
			}
			friend = friend.next;
		}
		return false;
	}
	
	private static void connectHelper(Graph g, boolean[] visited, int[] dfsNum, int[] back, boolean[] connectors, int startingPoint, int index, int[] trace) {
		visited[index] = true;
		dfsNum[index] = trace[0];
		back[index] = trace[0];
		trace[0] = trace[0] + 1;
		for(Friend friend = g.members[index].first; friend != null; friend = friend.next) {
			if(!visited[friend.fnum]) {
				connectHelper(g, visited, dfsNum, back, connectors, startingPoint, friend.fnum, trace);
				if(dfsNum[index] > back[friend.fnum]) { 
					back[index] = Math.min(back[index], back[friend.fnum]);
				} else {
					if(index != startingPoint) {
						connectors[index] = true;
					} else {
						if(connectors[index] == false) {
							boolean check = sPCheck(friend.next, visited);
							if(check) {
								connectors[index] = true;
							}
						}
					}
				}
			} else {
				back[index] = Math.min(back[index], dfsNum[friend.fnum]);
			}
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		boolean[] visited = new boolean[g.members.length];
		int[] dfsNum = new int[g.members.length];
		int[] back = new int[g.members.length];
		boolean[] connectors = new boolean[g.members.length];
		ArrayList<String> res = new ArrayList<String>();
		int[] trace = new int[1];
		for(int i = 0; i < g.members.length; i++) {
			if(!visited[i]) {
				trace[0] = 1;
				connectHelper(g, visited, dfsNum, back, connectors, i, i, trace);
			}
		}
		
		for(int i = 0; i < connectors.length; i++) {
			if(connectors[i]) {
				res.add(g.members[i].name);
			}
		}
		return res;
	}
}

