package pokemon_kanto_adventure;

public class City<T extends Comparable<T>,N extends Comparable<N>> { //City class for all the cities in the region
    T cityInfo; //cityInfo, which is the name of the city
    int deg; //degree of the city
    City<T,N>nextCity; //pointer to next city
    Edge<T,N>firstEdge; //pointer to first edge
    public City(){ //create city object
        cityInfo = null;
        deg = 0;
        nextCity = null;
        firstEdge = null;
    }
    public City(T vInfo,City<T,N>next){ //create city object with pointer to next city
        cityInfo = vInfo;
        deg = 0;
        nextCity = next;
        firstEdge = null;
    }
}
