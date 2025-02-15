
import java.util.*;

public class metroapplication {
    static class Node implements Comparable<Node> {
        String station;
        int distance;
        int time;

        public Node(String station, int distance, int time) {
            this.station = station.toLowerCase().trim();
            this.distance = distance;
            this.time = time;
        }

        @Override
        public int compareTo(Node other) {
            return this.distance - other.distance;  // Priority Queue prioritizes the node with shortest distance
        }
    }

    // Graph class representing the Delhi Metro Network
    static class Graph {
        private final Map<String, List<Node>> metroMap;

        public Graph() {
            metroMap = new HashMap<>();
        }

        // Add an edge between two stations with distance and time (bidirectional)
        public void addEdge(String source, String destination, int distance, int time) {
            source = source.toLowerCase().trim();
            destination = destination.toLowerCase().trim();
            metroMap.putIfAbsent(source, new ArrayList<>());
            metroMap.putIfAbsent(destination, new ArrayList<>());
            metroMap.get(source).add(new Node(destination, distance, time));
            metroMap.get(destination).add(new Node(source, distance, time));  // Ensure bidirectional
        }

        // List all the stations
        public void listStations() {
            System.out.println("List of all stations:");
            for (String station : metroMap.keySet()) {
                System.out.println("- " + capitalize(station));
            }
        }

        // Display the Metro Map (adjacency list)
        public void showMetroMap() {
            System.out.println("Metro Map:");
            for (String station : metroMap.keySet()) {
                System.out.print(capitalize(station) + " -> ");
                for (Node neighbor : metroMap.get(station)) {
                    System.out.print(capitalize(neighbor.station) + " (" + neighbor.distance + " km, " + neighbor.time + " min), ");
                }
                System.out.println();
            }
        }

        // Dijkstra’s algorithm to get shortest distance
        public void dijkstraShortestDistance(String start, String destination) {
            start = start.toLowerCase().trim();
            destination = destination.toLowerCase().trim();

            if (!metroMap.containsKey(start) || !metroMap.containsKey(destination)) {
                System.out.println("One or both stations do not exist in the map.");
                return;
            }

            Map<String, Integer> distances = new HashMap<>();
            PriorityQueue<Node> heap = new PriorityQueue<>();

            for (String station : metroMap.keySet()) {
                distances.put(station, Integer.MAX_VALUE);
            }

            distances.put(start, 0);
            heap.add(new Node(start, 0, 0));

            while (!heap.isEmpty()) {
                Node current = heap.poll();

                if (current.station.equals(destination)) break;

                for (Node neighbor : metroMap.get(current.station)) {
                    int newDist = distances.get(current.station) + neighbor.distance;
                    if (newDist < distances.get(neighbor.station)) {
                        distances.put(neighbor.station, newDist);
                        heap.add(new Node(neighbor.station, newDist, neighbor.time));
                    }
                }
            }

            if (distances.get(destination) == Integer.MAX_VALUE) {
                System.out.println("No path exists between " + capitalize(start) + " and " + capitalize(destination));
            } else {
                System.out.println("Shortest Distance from " + capitalize(start) + " to " + capitalize(destination) + ": " + distances.get(destination) + " km");
            }
        }

        // Dijkstra’s algorithm to get shortest time
        public void dijkstraShortestTime(String start, String destination) {
            start = start.toLowerCase().trim();
            destination = destination.toLowerCase().trim();

            if (!metroMap.containsKey(start) || !metroMap.containsKey(destination)) {
                System.out.println("One or both stations do not exist in the map.");
                return;
            }

            Map<String, Integer> times = new HashMap<>();
            PriorityQueue<Node> heap = new PriorityQueue<>(Comparator.comparingInt(n -> n.time));

            for (String station : metroMap.keySet()) {
                times.put(station, Integer.MAX_VALUE);
            }

            times.put(start, 0);
            heap.add(new Node(start, 0, 0));

            while (!heap.isEmpty()) {
                Node current = heap.poll();

                if (current.station.equals(destination)) break;

                for (Node neighbor : metroMap.get(current.station)) {
                    int newTime = times.get(current.station) + neighbor.time;
                    if (newTime < times.get(neighbor.station)) {
                        times.put(neighbor.station, newTime);
                        heap.add(new Node(neighbor.station, neighbor.distance, newTime));
                    }
                }
            }

            if (times.get(destination) == Integer.MAX_VALUE) {
                System.out.println("No path exists between " + capitalize(start) + " and " + capitalize(destination));
            } else {
                System.out.println("Shortest Time from " + capitalize(start) + " to " + capitalize(destination) + ": " + times.get(destination) + " minutes");
            }
        }

        // Shortest path (distance wise)
        public void shortestPathByDistance(String start, String destination) {
            start = start.toLowerCase().trim();
            destination = destination.toLowerCase().trim();

            if (!metroMap.containsKey(start) || !metroMap.containsKey(destination)) {
                System.out.println("One or both stations do not exist in the map.");
                return;
            }

            Map<String, Integer> distances = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<Node> heap = new PriorityQueue<>();

            for (String station : metroMap.keySet()) {
                distances.put(station, Integer.MAX_VALUE);
                previous.put(station, null);
            }

            distances.put(start, 0);
            heap.add(new Node(start, 0, 0));

            while (!heap.isEmpty()) {
                Node current = heap.poll();

                if (current.station.equals(destination)) break;

                for (Node neighbor : metroMap.get(current.station)) {
                    int newDist = distances.get(current.station) + neighbor.distance;
                    if (newDist < distances.get(neighbor.station)) {
                        distances.put(neighbor.station, newDist);
                        previous.put(neighbor.station, current.station);
                        heap.add(new Node(neighbor.station, newDist, neighbor.time));
                    }
                }
            }

            if (distances.get(destination) == Integer.MAX_VALUE) {
                System.out.println("No path exists between " + capitalize(start) + " and " + capitalize(destination));
            } else {
                System.out.println("Shortest Distance Path from " + capitalize(start) + " to " + capitalize(destination) + ":");
                printPath(previous, destination);
            }
        }

        // Shortest path (time wise)
        public void shortestPathByTime(String start, String destination) {
            start = start.toLowerCase().trim();
            destination = destination.toLowerCase().trim();

            if (!metroMap.containsKey(start) || !metroMap.containsKey(destination)) {
                System.out.println("One or both stations do not exist in the map.");
                return;
            }

            Map<String, Integer> times = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<Node> heap = new PriorityQueue<>(Comparator.comparingInt(n -> n.time));

            for (String station : metroMap.keySet()) {
                times.put(station, Integer.MAX_VALUE);
                previous.put(station, null);
            }

            times.put(start, 0);
            heap.add(new Node(start, 0, 0));

            while (!heap.isEmpty()) {
                Node current = heap.poll();
                System.out.println("Visiting station: " + capitalize(current.station) + ", current time: " + times.get(current.station));


                if (current.station.equals(destination)) break;

                for (Node neighbor : metroMap.get(current.station)) {
                    int newTime = times.get(current.station) + neighbor.time;
                    if (newTime < times.get(neighbor.station)) {
                        times.put(neighbor.station, newTime);
                        previous.put(neighbor.station, current.station);
                        heap.add(new Node(neighbor.station, neighbor.distance, newTime));
                    }
                }
            }

            if (times.get(destination) == Integer.MAX_VALUE) {
                System.out.println("No path exists between " + capitalize(start) + " and " + capitalize(destination));
            } else {
                System.out.println("Shortest Time Path from " + capitalize(start) + " to " + capitalize(destination) + ":");
                System.out.println("Path: ");
                printPath(previous, destination);
            }
        }

        // Helper function to print the path
        private void printPath(Map<String, String> previous, String destination) {
            List<String> path = new ArrayList<>();
            for (String at = destination; at != null; at = previous.get(at)) {
                path.add(at);
            }
            Collections.reverse(path);
            System.out.println(String.join(" -> ", path.stream().map(this::capitalize).toArray(String[]::new)));
        }

        // Helper function to capitalize station names
        private String capitalize(String station) {
            return station.substring(0, 1).toUpperCase() + station.substring(1);
        }
    }

    // Main Application class
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph metro = new Graph();

        // Adding stations and edges
        metro.addEdge("Rajiv Chowk", "Kashmere Gate", 5, 8);
        metro.addEdge("Rajiv Chowk", "Central Secretariat", 3, 5);
        metro.addEdge("Kashmere Gate", "Vishwavidyalaya", 4, 7);
        metro.addEdge("Central Secretariat", "Hauz Khas", 7, 12);
        metro.addEdge("Hauz Khas", "Saket", 4, 6);
        metro.addEdge("Vishwavidyalaya", "Saket", 9, 14);
        // Adding more stations
        metro.addEdge("Dwarka Sector 21", "Dwarka Sector 14", 2, 4);
        metro.addEdge("Dwarka Sector 21", "Dwarka Sector 13", 3, 5);
        metro.addEdge("Dwarka Sector 14", "Dwarka Sector 13", 1, 2);
        metro.addEdge("Hauz Khas", "AIIMS", 3, 5);
        metro.addEdge("AIIMS", "Moti Bagh", 4, 6);
        metro.addEdge("Moti Bagh", "Safdarjung", 3, 5);
        metro.addEdge("Safdarjung", "Dilli Haat", 5, 8);
        metro.addEdge("Dilli Haat", "Green Park", 2, 3);
        metro.addEdge("Green Park", "Saket", 2, 4);
        metro.addEdge("Saket", "Chhatarpur", 5, 9);
        metro.addEdge("Chhatarpur", "Qutub Minar", 6, 10);
        metro.addEdge("Qutub Minar", "Hauz Khas", 4, 8);
        metro.addEdge("Rajiv Chowk", "Connaught Place", 2, 3);
        metro.addEdge("Connaught Place", "Barakhamba Road", 1, 2);
        metro.addEdge("Barakhamba Road", "Patel Chowk", 2, 4);
        metro.addEdge("Patel Chowk", "Central Secretariat", 1, 2);

        System.out.println("*** WELCOME TO THE METRO APP ***");
        while (true) {
            System.out.println("\n--LIST OF ACTIONS~~");
            System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
            System.out.println("2. SHOW THE METRO MAP");
            System.out.println("3. GET SHORTEST DISTANCE FROM A SOURCE STATION TO DESTINATION STATION");
            System.out.println("4. GET SHORTEST TIME TO REACH FROM A SOURCE STATION TO DESTINATION STATION");
            System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A SOURCE STATION TO DESTINATION STATION");
            System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A SOURCE STATION TO DESTINATION STATION");
            System.out.println("7. EXIT THE MENU");
            System.out.print("ENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    metro.listStations();
                    break;
                case 2:
                    metro.showMetroMap();
                    break;
                case 3:
                    System.out.print("Enter source station: ");
                    String srcDist = scanner.nextLine();
                    System.out.print("Enter destination station: ");
                    String destDist = scanner.nextLine();
                    metro.dijkstraShortestDistance(srcDist, destDist);
                    break;
                case 4:
                    System.out.print("Enter source station: ");
                    String srcTime = scanner.nextLine();
                    System.out.print("Enter destination station: ");
                    String destTime = scanner.nextLine();
                    metro.dijkstraShortestTime(srcTime, destTime);
                    break;
                case 5:
                    System.out.print("Enter source station: ");
                    String srcPathDist = scanner.nextLine();
                    System.out.print("Enter destination station: ");
                    String destPathDist = scanner.nextLine();
                    metro.shortestPathByDistance(srcPathDist, destPathDist);
                    break;
                case 6:
                    System.out.print("Enter source station: ");
                    String srcPathTime = scanner.nextLine();
                    System.out.print("Enter destination station: ");
                    String destPathTime = scanner.nextLine();
                    metro.shortestPathByTime(srcPathTime, destPathTime);
                    break;
                case 7:
                    System.out.println("Exiting the application. Thank you!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 7.");
            }
        }
    }
}
