package pokemon_kanto_adventure;

public class City<T extends Comparable<T>, N extends Comparable<N>> {
    T cityInfo;
    int deg;
    City<T, N> nextCity;
    Edge<T, N> firstEdge;

    public City() {
        cityInfo = null;
        deg = 0;
        nextCity = null;
        firstEdge = null;
    }

    public City(T vInfo, City<T, N> next) {
        cityInfo = vInfo;
        deg = 0;
        nextCity = next;
        firstEdge = null;
    }
}
