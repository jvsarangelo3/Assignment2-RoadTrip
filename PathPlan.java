import java.util.*;

public class PathPlan {
    private class Edge {
        // initialize index of the vertices and the distance
        public int v1, v2;
        int distance;
    }

    private class Vertex {
        // initialize the city and the arraylist for attractions
        public String city;
        public ArrayList<String> attractions;

        public Vertex(String name) {
            // set the city to the name and the attractions in the ArrayList
            city = name;
            attractions = new ArrayList<String>();
        }
    }

    // initialize the arraylist to store the vertex
    private ArrayList<Vertex> vertex;
    // initialize the arraylist to store the edge
    private ArrayList<ArrayList<Edge>> edge;
    // initialize a linked list to store the attraction
    private LinkedList<Integer> attraction;

    /*
     * function tests if the vertex exists for that name
     */
    public boolean checkVertex(String name) {
        return (index(name) >= 0);
    }

    /*
     * function that finds the index in the vertex list
     */
    public int index(String name) {
        // loops thru vertex
        for (int i = 0; i < vertex.size(); i++) {
            // checks if the city = the name
            if (vertex.get(i).city.equalsIgnoreCase(name)) {
                // return the index
                return i;
            }
        }
        // else if not found, return -1
        return -1;
    }

    /*
     * constructor for PathPlan
     */
    PathPlan() {
        // create arraylist for the vertex, end and linkedlist for the attraction
        vertex = new ArrayList<Vertex>();
        edge = new ArrayList<ArrayList<Edge>>();
        attraction = new LinkedList<Integer>();
    }

    /*
     * function that adds edge
     */
    public void addEdge(String vertex_name1, String vertex_name2, int d) {
        // if not check vertex for the vertex_name1
        if (!checkVertex(vertex_name1)) {
            // create vertex v1 and add v1 to the vertex
            Vertex v1 = new Vertex(vertex_name1);
            vertex.add(v1);
            // add edge to the arraylist
            edge.add(new ArrayList<Edge>());
        }
        // initialize v1 and set to the index of vertex_name1
        int v1 = index(vertex_name1);
        // create arraylist for edge1
        ArrayList<Edge> edge1 = edge.get(v1);

        // if not check vertex for vertex_name2
        if(!checkVertex(vertex_name2)) {
            // create vertex v2 and add v2 to the vertex
            Vertex v2 = new Vertex(vertex_name2);
            vertex.add(v2);
            // add edge to the arraylist
            edge.add(new ArrayList<Edge>());
        }
        // initialize v2 and set to the index of vertex_name2
        int v2 = index(vertex_name2);
        // create arraylist for edge 2
        ArrayList<Edge> edge2 = edge.get(v2);

        // create new edge, set the first vertex to vertex 1
        // set the second vertex to vertex 2
        // set the distance to the value of the distance
        Edge e1 = new Edge();
        e1.v1 = v1;
        e1.v2 = v2;
        e1.distance = d;

        // make another new edge and do the same
        Edge e2 = new Edge();
        e2.v1 = v2;
        e2.v2 = v1;
        e2.distance = d;

        // add those edges to the list of edges
        edge1.add(e1);
        edge2.add(e2);
    }

    /*
     * function adds list of attractions
     */
    public boolean addPlace(String city, String place) {
        if (!checkVertex(city)) { // checks if there is a vertex
            return false;
        }

        // set vertex_index to the index of the city
        int vertex_index = index(city);
        // get the info of the vertex_index and add it to the attractions list
        vertex.get(vertex_index).attractions.add(place);
        // return true
        return true;
    }

    /*
     * function that adds the name and index of the city with the attractions into the list
     */
    public boolean placeRoute(String place) {
        // loop thru vertex size
        for (int i = 0; i < vertex.size(); i++) {
            // get info
            Vertex v = vertex.get(i);
            // if the attraction is in that place, then add it to the attractions list
            if(v.attractions.contains(place)) {
                attraction.add(i);
                // return true
                return true;
            }
        }
        // else if it isn't found, return false
        return false;
    }

    /*
     * function that uses Dikjstra's alg
     */
    public void Dijkstra(int start, ArrayList<Integer> distance, ArrayList<Integer> previous) {
        // initializing flag to false, creating an ArrayList that stores info
        boolean isPath = false;
        ArrayList<Integer> list = new ArrayList<Integer>();
        // initializing variables
        int a_index, b_index, alternate, minDistance, currDistance, a, b;

        // loops thru the vertex of the ArrayList
        for (b = 0; b < vertex.size(); b++) {
            // add -1 to distance of arraylist and previous of arraylist
            distance.add(-1);
            previous.add(-1);
            // add the b veriable to the arraylist
            list.add(b);
        }
        // set the distance to the value of start and 0
        distance.set(start, 0);

        //while the list isn't empty, set the a_index to the minDistance of -1
        while(!list.isEmpty()) {
            a_index = minDistance = -1;
            // loop through the list
            for (b_index = 0; b_index < list.size(); b_index++) {
                // get the b_index from the list and set it to b
                b = list.get(b_index);
                // set the current distance to the distance from b
                currDistance = distance.get(b);
                // if the current distance is < 0 or the current distance is less than the min distance
                // then set the a_index to the b_index and the min distance to the current distance
                if(currDistance >= 0 && (a_index < 0 || currDistance < minDistance)) {
                    a_index = b_index;
                    minDistance = currDistance;
                }
            }
            // set a to get the a_index
            a = list.get(a_index);
            // remove a_index from the list
            list.remove(a_index);

            //loop thru the size of the edge of a
            for (b_index = 0; b_index < edge.get(a).size(); b_index++) {
                // set b the edge of a and get the b_index
                b = edge.get(a).get(b_index).v2;

                // if the list contains b
                if(list.contains(b)) {
                    // then set the alternate to the distance at a and the distance at b_index
                    alternate = distance.get(a) + edge.get(a).get(b_index).distance;

                    // if the distance at b is < 0 or the alternate is < the distance at b
                    if(distance.get(b) < 0 || alternate < distance.get(b)) {
                        // set the distance to b, alternate
                        distance.set(b, alternate);
                        // set the previous to b, a
                        previous.set(b, a);
                    }
                }
            }
        }
    }

    /*
     * function that pulls the shortest path
     */
    private List<Integer> path(int start, int end, ArrayList<Integer> previous) {
        // create linked list p that stores the path
        LinkedList<Integer> p = new LinkedList<Integer>();

        // set a to the end
        int a = end;
        // if a is = to the start or the previous of a is >= 0
        if (a == start || previous.get(a) >= 0) {
            // then while a >= 0
            while(a >= 0) {
                // add a to the path
                p.addFirst(a);
                // set a to the previous of a
                a = previous.get(a);
            }
        }
        // return the path
        return p;
    }

    /*
     * function that returns a list of all the variations
     */
    private static LinkedList<LinkedList<Integer>> var(LinkedList<Integer> l) {
        // create a linkedList list
        LinkedList<LinkedList<Integer>> list = new LinkedList<LinkedList<Integer>>();
        // create variable s that stores the size of l
        int s = l.size();

        // if the size = 1
        if (s == 1) {
            // create a new linkedlist
            LinkedList<Integer> l1 = new LinkedList<Integer>();
            // add the 0 index of l to that linked list as the last
            l1.addLast(l.get(0));
            // then add that new linkedlist into the list
            list.addLast(l1);
        } else {
            // loop thru the list
            for (int i =0; i < s; i++) {
                // create a new linkedlist
                LinkedList<Integer> l1 = new LinkedList<Integer>();
                // add all the values to that linked list
                for (int j = 0; j < s; j++) {
                    // if j is not equal to i,
                    // then add that info from the l.get(j) to the linked list
                    if (j != i) {
                        l1.add(l.get(j));
                    }
                }
                // recursively call function
                LinkedList<LinkedList<Integer>> list1 = var(l1);

                // add elems to the end of each variation
                for (int j = 0; j < list1.size(); j++) {
                    // create a linked list called array
                    LinkedList<Integer> array = list1.get(j);
                    // add info from the i to the 0 index
                    array.add(0, l.get(i));
                    // add the array to the list
                    list.add(array);
                }
            }
        }
        // return the list
        return list;
    }

    /*
     * function that finds the shortest route
     */
    public LinkedList<String> findRoute(String starting_city, String ending_city) {
        // initialize starting and ending variables according to their index
        int start = index(starting_city);
        int end = index(ending_city);

        // if there are no attractions in the list
        boolean places = !attraction.isEmpty();
        if(!places) {
            // then add the start to attraction
            attraction.add(start);
        }
        // call var for the attractions
        List<LinkedList<Integer>> lp = var(attraction);
        // store the numper of places
        int n_places = attraction.size();

        // if the places is empty
        if(places) {
            // add the start and end
            attraction.add(start);
            attraction.add(end);
        }

        // set start num to the number of places
        int start_num = n_places;
         // set end num to the number of places + 1
        int end_num = n_places + 1;
        // if the places is NOT empty
        if(!places) {
            // set start num to 0 and end num to 1
            start_num = 0;
            end_num = 1;
        }

        // initialize arraylists for the distance and previous
        ArrayList<ArrayList<Integer>> dist1 = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> prev1 = new ArrayList<ArrayList<Integer>>();


        // loop thru attractions
        for(int p_index = 0; p_index < attraction.size(); p_index++) {
            // set a to the index of p_index
            int a = attraction.get(p_index);
            // create arraylist for distance and previous
            ArrayList<Integer> dist = new ArrayList<Integer>();
            ArrayList<Integer> prev = new ArrayList<Integer>();
            // call dijkstra(a, dist, prev)
            Dijkstra(a, dist, prev);
            // add dis and prev to the dis1 and prev1 arraylists
            dist1.add(dist);
            prev1.add(prev);
        }

        // set the dist to -1 and min to -1
        int dist = -1;
        int min = -1;
        // loop thru lp
        for(int n = 0; n < lp.size(); n++) {
            // initialize var to store the variations
            LinkedList<Integer> var = lp.get(n);
            // set the curr_dist to 0 and the end2 to get the path of var 0
            int curr_dist = 0;
            int end2 = var.get(0);
            // set the curr_dist to the current_dist + the startnum of dist1 and the end2
            curr_dist += dist1.get(start_num).get(end2);

            // loop thru the next paths
            for (int i = 0; i < var.size() - 1; i++) {
                // set variables p1 to get the index of the var, p2 the index of var+1
                int p1 = var.get(i);
                int p2 = var.get(i + 1);
                // set the index of p1
                int p1_index = attraction.indexOf(p1);
                int p2_index = attraction.indexOf(p2);

                // set the current distance to the current distance + the p1_index and the distance of p2
                curr_dist += dist1.get(p1_index).get(p2);
                List<Integer> path = path(p1, p2, prev1.get(p1_index));
            }
            // get the last path
            end2 = var.get(var.size() - 1);
            // update current distance
            curr_dist += dist1.get(start_num).get(end2);
            // initialize a list with the best path
            List<Integer> bestPath = path(end, end2, prev1.get(end_num));
            // reverse the list in order
            Collections.reverse(bestPath);

            // if n = 0 or the distance is greater than the current path
            if(n == 0 || dist > curr_dist) {
                // then set the distance to the current distance and the min to n
                dist = curr_dist;
                min = n;
            }
        }

        // initialize the final linked list path
        LinkedList<String> finalPath = new LinkedList<String>();
        // initialize the min variation to get the min of lp
        LinkedList<Integer> varMin = lp.get(min);
        // add start as the first
        // add the end as the last
        varMin.addFirst(start);
        varMin.addLast(end);

        // loop through the variation min - 1
        for(int i = 0; i < varMin.size() - 1; i++ ) {
            // set p1 to get the variation min at i
            int p1 = varMin.get(i);
            // set p2 to get the variation min at i+1
            int p2 = varMin.get(i + 1);
            // set the indexes
            int p1_index = attraction.indexOf(p1);
            int p2_index = attraction.indexOf(p2);
            // set the path final
            List<Integer> pathFinal = path(p1, p2, prev1.get(p1_index));

            // loop thru the final path
            for(int j = 0; j < pathFinal.size() - 1; j++) {
                // add info to the final path
                finalPath.addLast(vertex.get(pathFinal.get(j)).city);
            }
        }
        // add the ending city and return the final path
        finalPath.addLast(vertex.get(end).city);
        return finalPath;
    }
}
