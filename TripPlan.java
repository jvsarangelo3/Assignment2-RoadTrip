import javax.sound.midi.SysexMessage;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class TripPlan {
    public static ArrayList<Integer> dist = new ArrayList<>();

    public static List<String> route(String start, String end, List<String> attractions) throws IOException {
        PathPlan plan = new PathPlan();
        String roadFile = "roads.csv", line;

        BufferedReader br = new BufferedReader(new FileReader(roadFile));
        while ((line = br.readLine()) != null) {
            String[] info = line.split(",");
            plan.addEdge(info[0], info[1], Integer.parseInt(info[2]));
            dist.add(Integer.parseInt(info[2]));
        }
        br.close();

        String attractionFile = "attractions.csv";
        br = new BufferedReader(new FileReader(attractionFile));
        while((line = br.readLine()) != null) {
            String[] data = line.split(",");
            plan.addPlace(data[1], data[0]);
        }
        br.close();

        for(String place: attractions) {
            plan.placeRoute(place);
        }

        LinkedList<String> route = plan.findRoute(start, end);
        return route;
    }

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter starting city: ");
        String start = input.nextLine();
        System.out.println("Enter ending city: ");
        String end = input.nextLine();

        ArrayList<String> attractions = new ArrayList<String>();
        while (true) {
            System.out.println("Enter attraction(s) [type 0 when finished]: ");
            String attraction = input.nextLine();
            if(!attraction.equals("0")) {
                attractions.add(attraction);
            } else {
                break;
            }
        }

        List<String> route = route(start, end, attractions);
        int finalDistance = 0;
        for (int i = 0; i < route.size(); i++) {
            int d = dist.get(i);
            finalDistance += d;
        }

        System.out.println("Final Route: " + route);
        System.out.println("Final Distance: " + finalDistance + " miles");
    }
}
