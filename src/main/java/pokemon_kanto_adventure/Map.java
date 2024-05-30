package pokemon_kanto_adventure;

import java.util.ArrayList;

public class Map<T extends Comparable<T>, N extends Comparable<N>> { //Map class
    City<T, N> head; //head, which the first city in the list of the map
    int size;//number of cities in the map

    public Map() { //create empty Map
        head = null;
        size = 0;
    }

    public int getSize() { //find the size/number of cities of the map
        return size;
    }

    public boolean hasCity(T v) { //search for a specific city in the map
        if (head == null) //if the map is empty
            return false;
        City<T, N> temp = head; //if map is not empty, set temp to head
        while (temp != null) { //iterate through all the city objects in the map
            if (temp.cityInfo.compareTo(v) == 0)//if found return true
                return true;
            temp = temp.nextCity; //if not found keep iterating
        }
        return false; //if city does not exist in the Map return false
    }

    public int getDeg(T v) { //get the degree of a specific city in the map
        if (hasCity(v)) { //check if the city exists in the map
            City<T, N> temp = head; //if exist, set temp to head
            while (temp != null) { //iterate through every city until found the city
                if (temp.cityInfo.compareTo(v) == 0) //if found the city
                    return temp.deg; //return the degree of the city
                temp = temp.nextCity; //if not found yet continue iterating
            }
        }
        return -1; //if city is not in the map, return -1
    }

    public boolean addCity(T v) { //add a city into the map
        if (!hasCity(v)) { //check if the city exists in the map
            City<T, N> temp = head; //if not set temp to head
            City<T, N> newCity = new City<T, N>(v, null); //create newCity using the data given
            if (head == null) { //if map is empty
                head = newCity; //head of map will be newCity
            } else { //if map is not empty
                City<T, N> previous = head; //set previous to head
                while (temp != null) { //iterate until the last city in the map
                    previous = temp; //set previous to temp
                    temp = temp.nextCity; //set temp to its next city
                }
                previous.nextCity = newCity; //set the newCity as the next city of the last city
            }
            size++;//increase the size of the map by 1
            return true; //return true
        }
        return false; //if city exists, then same city cannot be added again, return false
    }

    public int getIndex(T v) { //find the index of a specific city in the map
        City<T, N> tmp = head; //set tmp to head
        int pos = 0;//set pos, which is the value to return to 0
        while (tmp != null) { //iterate through all the cities
            if (tmp.cityInfo.compareTo(v) == 0) { //if the specific city is found
                return pos; //return its position, which is its index
            }
            tmp = tmp.nextCity; //if city is not found, keep iterating
            pos++; //increase the number of index by 1 every iteration
        }
        return -1; //if city is not found in map, return -1
    }

    public ArrayList<T> getAllCityObjects() { //return a list of all cities
        ArrayList<T> list = new ArrayList<>(); //create an ArrayList to be returned
        City<T, N> temp = head; //set temp to head
        while (temp != null) { //iterate through all the cities
            list.add(temp.cityInfo); //add the cityInfo of the city into the list
            temp = temp.nextCity;//set temp to its next city
        }
        return list; //return the list
    }

    public T getCity(int pos) { //get the city at a specific index
        if (pos >= size || pos < 0) { //check if the index is valid
            return null; //if index<0 or index>= the size of the map,return null as index is invalid
        } else { //if index is valid
            City<T, N> temp = head; //set temp to head
            for (int i = 0; i < pos; i++) { //iterate through the cities until temp becomes the target city at target index
                temp = temp.nextCity;
            }
            return temp.cityInfo; //return cityInfo of the target city
        }
    }

    public boolean hasEdge(T source, T destination) { //search for an edge in the map
        if (head == null) { //if map is empty
            return false;
        } else if (!hasCity(source) || !hasCity(destination)) { //if map does not have either source and destination city
            return false;
        }
        City<T, N> sourceCity = head; //set sourceCity to head
        while (sourceCity != null) { //iterate through all the cities is the map
            if (sourceCity.cityInfo.compareTo(source) == 0) { //if source city is found
                Edge<T, N> currentEdge = sourceCity.firstEdge; //set currentEdge to source city's first edge
                while (currentEdge != null) { //iterate through all the edges which source city is the mentioned source city
                    if (currentEdge.toCity.cityInfo.compareTo(destination) == 0) { //if the wanted destination city is found as the destination city of one of the edges
                        return true;
                    }
                    currentEdge = currentEdge.nextEdge; //if the destination city of the edge is not the wanted destination city, set currentEdge to its nextEdge
                }

            }
            sourceCity = sourceCity.nextCity; //if the target source city is not found yet, set sourceCity to its nextCity
        }
        return false; //if the edge is not found then return false
    }

    public boolean addPath(T source, T destination, N w) { //add a two-way path between two cities
        return addEdge(source, destination, w) && addEdge(destination, source, w);
    }

    public boolean addEdge(T source, T destination, N w) { //add an edge between two cities
        if (head == null) { //if map is empty
            return false;
        } else if (!hasCity(source) || !hasCity(destination)) { //if map does not have either the source or destination city
            return false;
        }
        City<T, N> sourceCity = head; //set sourceCity to head
        while (sourceCity != null) { //iterate through every city in the map
            if (sourceCity.cityInfo.compareTo(source) == 0) { //if target source city is found
                City<T, N> destinationCity = head; //set destinationCity to head
                while (destinationCity != null) { //iterate through every city in the map
                    if (destinationCity.cityInfo.compareTo(destination) == 0) { //if target destination city is found
                        Edge<T, N> currentEdge = sourceCity.firstEdge; //set currentEdge to firstEdge of sourceCity
                        Edge<T, N> newEdge = new Edge<>(destinationCity, w, currentEdge); //create newEdge, with the original firstEdge as its nextEdge
                        sourceCity.firstEdge = newEdge; //set the firstEdge of the sourceCity to newEdge
                        sourceCity.deg++; //increase the degree of sourceCity and destinationCity by 1
                        destinationCity.deg++;
                        return true;
                    }
                    destinationCity = destinationCity.nextCity; //if target destination is not found yet, set destinationCity to its next city
                }

            }
            sourceCity = sourceCity.nextCity; //if target sourceCity is not found yet, set sourceCity to its next city
        }
        return false; //if edge is not successfully added return false
    }

    public N getEdgeWeight(T source, T destination) { //get the weight of a specific edge
        if (head == null) { //if map is empty
            return null;
        } else if (!hasCity(source) || !hasCity(destination)) {//if map does not have either the source or destination city
            return null;
        }
        City<T, N> sourceCity = head; //set sourceCity to head
        while (sourceCity != null) {//iterate through every city in the map
            if (sourceCity.cityInfo.compareTo(source) == 0) { //if target source city is found
                Edge<T, N> currentEdge = sourceCity.firstEdge; //set currentEdge to sourceCity's firstEdge
                while (currentEdge != null) { //iterate through all the edges of the source city
                    if (currentEdge.toCity.cityInfo.compareTo(destination) == 0) { //if the wanted destination city is found as the destination city of one of the edges
                        return currentEdge.weight; //return the edge's weight
                    }
                    currentEdge = currentEdge.nextEdge; //if the destination city of the edge is not the wanted destination city, set currentEdge to its nextEdge
                }

            }
            sourceCity = sourceCity.nextCity;//if target sourceCity is not found yet, set sourceCity to its next city
        }
        return null;//if the edge is not found then return null
    }

    public ArrayList<T> getNeighbours(T v) { //get an arraylist of all neighboring cities of a city
        if (!hasCity(v)) //if the map does not have this city
            return null; //return null
        ArrayList<T> list = new ArrayList<>(); //create a list to be returned
        City<T, N> temp = head; //set temp to head
        while (temp != null) { //iterate through every city in the map
            if (temp.cityInfo.compareTo(v) == 0) { //if target city is found
                Edge<T, N> currentEdge = temp.firstEdge; //set currentEdge to its first edge
                while (currentEdge != null) { //iterate through all its edges
                    list.add(currentEdge.toCity.cityInfo); //add the cityInfo of the destination city of all the edges into the list
                    currentEdge = currentEdge.nextEdge;
                }
                return list; //return the list with all neighboring cities
            }
            temp = temp.nextCity;
        }
        return null;
    }

    public void printEdges() { //display all the edges of the map
        City<T, N> temp = head; //set temp to head
        while (temp != null) { //iterate through every city in the map
            System.out.println("# " + temp.cityInfo + " : "); //print out the current city's name
            Edge<T, N> currentEdge = temp.firstEdge; //set currentEdge to the first edge of the current city
            while (currentEdge != null) { //iterate through all the edges of the current city
                System.out.println("[ " + temp.cityInfo + "," + currentEdge.toCity.cityInfo + "]"); //print out the curent city and destination of the curent edge
                currentEdge = currentEdge.nextEdge;
            }
            System.out.println(""); //blank space
            temp = temp.nextCity;
        }
    }

}