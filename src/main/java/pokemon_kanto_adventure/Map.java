package pokemon_kanto_adventure;

import java.util.ArrayList;

public class Map<T extends Comparable<T>, N extends Comparable<N>> {
    City<T, N> head;
    int size;

    public Map() {
        head = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean hasCity(T v) {
        if (head == null)
            return false;
        City<T, N> temp = head;
        while (temp != null) {
            if (temp.cityInfo.compareTo(v) == 0)
                return true;
            temp = temp.nextCity;
        }
        return false;
    }

    public int getDeg(T v) {
        if (hasCity(v)) {
            City<T, N> temp = head;
            while (temp != null) {
                if (temp.cityInfo.compareTo(v) == 0)
                    return temp.deg;
                temp = temp.nextCity;
            }
        }
        return -1;
    }

    public boolean addCity(T v) {
        if (!hasCity(v)) {
            City<T, N> temp = head;
            City<T, N> newCity = new City<T, N>(v, null);
            if (head == null) {
                head = newCity;
            } else {
                City<T, N> previous = head;
                while (temp != null) {
                    previous = temp;
                    temp = temp.nextCity;
                }
                previous.nextCity = newCity;
            }
            size++;
            return true;
        }
        return false;
    }

    public int getIndex(T v) {
        City<T, N> tmp = head;
        int pos = 0;
        while (tmp != null) {
            if (tmp.cityInfo.compareTo(v) == 0) {
                return pos;
            }
            tmp = tmp.nextCity;
            pos++;
        }
        return -1;
    }

    public ArrayList<T> getAllCityObjects() {
        ArrayList<T> list = new ArrayList<>();
        City<T, N> temp = head;
        while (temp != null) {
            list.add(temp.cityInfo);
            temp = temp.nextCity;
        }
        return list;
    }

    public T getCity(int pos) {
        if (pos >= size || pos < 0) {
            return null;
        } else {
            City<T, N> temp = head;
            for (int i = 0; i < pos; i++) {
                temp = temp.nextCity;
            }
            return temp.cityInfo;
        }
    }

    public boolean hasEdge(T source, T destination) {
        if (head == null) {
            return false;
        } else if (!hasCity(source) || !hasCity(destination)) {
            return false;
        }
        City<T, N> sourceCity = head;
        while (sourceCity != null) {
            if (sourceCity.cityInfo.compareTo(source) == 0) {
                Edge<T, N> currentEdge = sourceCity.firstEdge;
                while (currentEdge != null) {
                    if (currentEdge.toCity.cityInfo.compareTo(destination) == 0) {
                        return true;
                    }
                    currentEdge = currentEdge.nextEdge;
                }

            }
            sourceCity = sourceCity.nextCity;
        }
        return false;
    }

    public boolean addPath(T source, T destination, N w) {
        return addEdge(source, destination, w) && addEdge(destination, source, w);
    }

    public boolean addEdge(T source, T destination, N w) {
        if (head == null) {
            return false;
        } else if (!hasCity(source) || !hasCity(destination)) {
            return false;
        }
        City<T, N> sourceCity = head;
        while (sourceCity != null) {
            if (sourceCity.cityInfo.compareTo(source) == 0) {
                City<T, N> destinationCity = head;
                while (destinationCity != null) {
                    if (destinationCity.cityInfo.compareTo(destination) == 0) {
                        Edge<T, N> currentEdge = sourceCity.firstEdge;
                        Edge<T, N> newEdge = new Edge<>(destinationCity, w, currentEdge);
                        sourceCity.firstEdge = newEdge;
                        sourceCity.deg++;
                        destinationCity.deg++;
                        return true;
                    }
                    destinationCity = destinationCity.nextCity;
                }

            }
            sourceCity = sourceCity.nextCity;
        }
        return false;
    }

    public N getEdgeWeight(T source, T destination) {
        if (head == null) {
            return null;
        } else if (!hasCity(source) || !hasCity(destination)) {
            return null;
        }
        City<T, N> sourceCity = head;
        while (sourceCity != null) {
            if (sourceCity.cityInfo.compareTo(source) == 0) {
                Edge<T, N> currentEdge = sourceCity.firstEdge;
                while (currentEdge != null) {
                    if (currentEdge.toCity.cityInfo.compareTo(destination) == 0) {
                        return currentEdge.weight;
                    }
                    currentEdge = currentEdge.nextEdge;
                }

            }
            sourceCity = sourceCity.nextCity;
        }
        return null;
    }

    public ArrayList<T> getNeighbours(T v) {
        if (!hasCity(v))
            return null;
        ArrayList<T> list = new ArrayList<>();
        City<T, N> temp = head;
        while (temp != null) {
            if (temp.cityInfo.compareTo(v) == 0) {
                Edge<T, N> currentEdge = temp.firstEdge;
                while (currentEdge != null) {
                    list.add(currentEdge.toCity.cityInfo);
                    currentEdge = currentEdge.nextEdge;
                }
                return list;
            }
            temp = temp.nextCity;
        }
        return null;
    }

    public void printEdges() {
        City<T, N> temp = head;
        while (temp != null) {
            System.out.println("# " + temp.cityInfo + " : ");
            Edge<T, N> currentEdge = temp.firstEdge;
            while (currentEdge != null) {
                System.out.println("[ " + temp.cityInfo + "," + currentEdge.toCity.cityInfo + "]");
                currentEdge = currentEdge.nextEdge;
            }
            System.out.println("");
            temp = temp.nextCity;
        }
    }

}
