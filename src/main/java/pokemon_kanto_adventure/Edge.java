package pokemon_kanto_adventure;

public class Edge<T extends Comparable<T>, N extends Comparable<N>> { //edge class
    City<T, N> toCity; //pointer to destination city
    N weight; //weight of the edge
    Edge<T, N> nextEdge; //pointer to next edge

    public Edge() { //create edge object
        toCity = null;
        weight = null;
        nextEdge = null;
    }

    public Edge(City<T, N> destination, N w, Edge<T, N> a) { //create edge object with specific destination city, weight and pointer to next edge
        toCity = destination;
        weight = w;
        nextEdge = a;
    }
}
