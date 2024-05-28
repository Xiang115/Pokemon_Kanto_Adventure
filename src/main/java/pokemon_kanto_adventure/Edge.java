package pokemon_kanto_adventure;

public class Edge<T extends Comparable<T>, N extends Comparable<N>> {
    City<T, N> toCity;
    N weight;
    Edge<T, N> nextEdge;

    public Edge() {
        toCity = null;
        weight = null;
        nextEdge = null;
    }

    public Edge(City<T, N> destination, N w, Edge<T, N> a) {
        toCity = destination;
        weight = w;
        nextEdge = a;
    }
}
