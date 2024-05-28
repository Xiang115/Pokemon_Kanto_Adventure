package pokemon_kanto_adventure;

import java.util.Random;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class RivalRace {
    private static final String[] raceDestinations = {
            "Fuschia City", "Pewter City", "Viridian City", "Pallet Town", "Cinnabar Island"
    };
    private static final String source = "Saffron City";
    private String Destination;
    private static Stack<String>locationStack; 

    public RivalRace() {
        Random r = new Random();
        Destination = raceDestinations[r.nextInt(raceDestinations.length)];
        System.out.printf("+%s+\n","-".repeat(90));
        System.out.printf("The battle has begun! Your ricval Gary has challenged you to a race to %s.\n", Destination);
        locationStack = new Stack<>();
    }
    public String getDestination(){
        return Destination;
    }
    public Stack<String> getStack(){
        return locationStack;
    }
    public void simulation() {
        System.out.println("Shortest Path:");
        ArrayList<String> shortestPath = dijkstra(source, Destination);
        PrintPath(shortestPath);
        for(int i = shortestPath.size()-1;i>0;i--){
            locationStack.push(shortestPath.get(i));
        }
        System.out.println();
        System.out.println("Goodluck on your race!");
        
    }

    private void PrintPath(ArrayList<String> path){
        StringBuilder sb = new StringBuilder();
        for(String location : path){
            sb.append(location).append(" -> ");
        }
        sb.setLength(sb.length()-3);
        System.out.println(sb.toString());
    }

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