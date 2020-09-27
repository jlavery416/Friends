package friends;
import java.util.*;
import java.io.*;

public class Driver {
	public static void main(String[] args) 
		throws FileNotFoundException {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a file to create a graph");
		System.out.println();
		Scanner sc = new Scanner(new File(input.next()));
		Graph graph = new Graph(sc);
		System.out.println("1. Run Shortest Chain");
		System.out.println("2. Run Cliques");
		System.out.println("3. Run Connectors");
		String next = input.next();
		if(next.equals("1")) {
			System.out.println("Please enter the starting person");
			String p1 = input.next();
			System.out.println("Please enter the ending person");
			String p2 = input.next();
			ArrayList<String> chain = Friends.shortestChain(graph, p1, p2);
			if(!chain.isEmpty()) {
				for(int i = 0; i < chain.size()-1; i++) {
					System.out.print(chain.get(i) + "->");
				}
				System.out.print(chain.get(chain.size()-1));
			} else {
				System.out.println("No path found");
			}
		} else if(next.equals("2")) {
			System.out.println("Enter a school for which you would like to see cliques");
			while(!input.hasNext()) {}
			String school = input.nextLine();
			while(school.isBlank()) {
				school = input.nextLine();
			}
			ArrayList<ArrayList<String>> cliques = Friends.cliques(graph, school);
			if(cliques.isEmpty()) {
				System.out.println("No cliques found");
			}
			for(int i = 0; i < cliques.size(); i++) {
				ArrayList<String> clique = cliques.get(i);
				System.out.println("Clique " + i + " is:");
				for(int j = 0; j < clique.size()-1; j++) {
					System.out.print(clique.get(j) + ", ");
				}
				System.out.print(clique.get(clique.size()-1));
				System.out.println();
			}
		} else if(next.equals("3")) {
			ArrayList<String> connectors = Friends.connectors(graph);
			if(!connectors.isEmpty()) {
				System.out.println("The connectors are as follows:");
				for(int i = 0; i < connectors.size()-1; i++) {
					System.out.print(connectors.get(i) + ", ");
				}
				System.out.println(connectors.get(connectors.size()-1));
			} else {
				System.out.println("No connectors");
			}
		} else {
			System.out.println("Not a valid entry");
		}
		sc.close();
		input.close();
	}
}
