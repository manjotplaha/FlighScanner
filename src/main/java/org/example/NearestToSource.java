package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class NearestToSource {

    // Method for finding shortest path between source city and other mentioned
    // cities
    public static void djikstra(String source) {
        Map<String, Map<String, Integer>> canadaCities = new HashMap<>();
        canadaCities.put("Toronto", Map.of("Montreal", 540, "Vancouver", 3950, "Calgary", 2965, "Halifax", 1465));
        canadaCities.put("Montreal", Map.of("Toronto", 540, "Vancouver", 4255, "Calgary", 3420, "Halifax", 1245));
        canadaCities.put("Vancouver", Map.of("Toronto", 3950, "Montreal", 4255, "Calgary", 970, "Halifax", 5515));
        canadaCities.put("Calgary", Map.of("Toronto", 2965, "Montreal", 3420, "Vancouver", 970, "Halifax", 4075));
        canadaCities.put("Halifax", Map.of("Toronto", 1465, "Montreal", 1245, "Vancouver", 5515, "Calgary", 4075));

        String startCity = source;
        if (canadaCities.containsKey(startCity)) {
            Map<String, Integer> distances = new HashMap<>();
            Map<String, String> previousCities = new HashMap<>();
            PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
            Set<String> visitedCities = new HashSet<>();

            // Initialize the distances and previous cities maps
            for (String city : canadaCities.keySet()) {
                distances.put(city, Integer.MAX_VALUE);
                previousCities.put(city, null);
            }
            distances.put(startCity, 0);
            queue.offer(startCity);

            // Dijkstra's algorithm
            while (!queue.isEmpty()) {
                String currentCity = queue.poll();
                visitedCities.add(currentCity);
                for (Map.Entry<String, Integer> entry : canadaCities.get(currentCity).entrySet()) {
                    String neighborCity = entry.getKey();
                    int distanceToNeighbor = entry.getValue();
                    int totalDistance = distances.get(currentCity) + distanceToNeighbor;

                    if (totalDistance < distances.get(neighborCity)) {
                        distances.put(neighborCity, totalDistance);
                        previousCities.put(neighborCity, currentCity);
                    }

                    if (!visitedCities.contains(neighborCity)) {
                        queue.offer(neighborCity);
                    }
                }
            }

            List<String> sortedCities = sortDistances(distances);

            System.out.println("-----------------------------------------------------");

            // Print the distances and previous cities
            for (String city : sortedCities) {
                if (city.equals(source)) {
                    continue;
                }
                System.out.printf("%s: %d (previous city: %s)%n", city, distances.get(city), previousCities.get(city));
            }
        }
    }

    public static List<String> sortDistances(Map<String, Integer> distances) {
        return distances.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
