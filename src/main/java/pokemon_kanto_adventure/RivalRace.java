package pokemon_kanto_adventure;

import java.util.Random;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/*
A class enable you to race with your oppopnent, Gary. The program will random select a destination to race and display
the shortest path from Saffron city to destination with clear direction.
 */
public class RivalRace {
    //an array used for the selectionn of race destination
    private static final String[] raceDestinations = {
            "Fuschia City", "Pewter City", "Viridian City", "Pallet Town", "Cinnabar Island"
    };
    //used to initialize the source of race
    private static final String source = "Saffron City";
    //used to store the destination
    private String Destination;
    //used to store the status of current location
    private static Stack<String> locationStack;

    //initialize the rivalrace class
    public RivalRace() {
        //declare a randomo object
        Random r = new Random();
        //select a destination
        Destination = raceDestinations[r.nextInt(raceDestinations.length)];
        System.out.printf("+%s+\n", "-".repeat(90));
        System.out.printf("The battle has begun! Your ricval Gary has challenged you to a race to %s.\n", Destination);
        //declare a stack object
        locationStack = new Stack<>();
    }

    //method used to declare the destination
    public String getDestination() {
        return Destination;
    }

    //method used to declare the stack of location
    public Stack<String> getStack() {
        return locationStack;
    }

    //mehtod used to simulate the race
    public void simulation() {
        System.out.println("Shortest Path:");
        ArrayList<String> shortestPath = dijkstra(source, Destination);
        PrintPath(shortestPath);
        for (int i = shortestPath.size() - 1; i > 0; i--) {
            locationStack.push(shortestPath.get(i));
        }
        System.out.println();
        System.out.println("Goodluck on your race!");

    }

    //method used to display the shortest path
    private void PrintPath(ArrayList<String> path) {
        StringBuilder sb = new StringBuilder();
        for (String location : path) {
            sb.append(location).append(" -> ");
        }
        sb.setLength(sb.length() - 3);
        System.out.println(sb.toString());
    }

    //mehtod used to find the shortest path from source to destination
    public ArrayList<String> dijkstra(String source, String destination) {
        HashMap<String, Integer> distances = new HashMap<>();
        HashMap<String, String> previousCities = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> distances.get(a) - distances.get(b));

        for (String city : library.kantoMap.getAllCityObjects()) {
            distances.put(city, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        pq.offer(source);

        // Dijkstra's algorithm
        while (!pq.isEmpty()) {
            String currentCity = pq.poll();
            if (currentCity.equals(destination)) {
                break;
            }

            for (String neighbor : library.kantoMap.getNeighbours(currentCity)) {
                int distanceToNeighbor = distances.get(currentCity) + library.kantoMap.getEdgeWeight(currentCity, neighbor);

                if (distanceToNeighbor < distances.get(neighbor)) {
                    distances.put(neighbor, distanceToNeighbor);
                    previousCities.put(neighbor, currentCity);
                    pq.offer(neighbor);
                }
            }
        }

        ArrayList<String> shortestPath = new ArrayList<>();
        String current = destination;
        while (current != null) {
            shortestPath.add(0, current);
            current = previousCities.get(current);
        }

        return shortestPath;
    }
}