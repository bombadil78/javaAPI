package collections;

import java.util.*;
import java.util.stream.Collectors;

public class MapUtils {

    private MapUtils() {

    }

    public static <K, V> Map<K, V> intersect(Map<K, V> m1, Map<K, V> m2) {
        Map<K, V> intersection = new HashMap<>(m1);
        intersection.entrySet().retainAll(m2.entrySet());
        return intersection;
    }

    public static <K> Map<K, K> removeTransitively(Map<K, K> m, K keyToRemove) {
        Map<K, K> map = new HashMap<>();
        map.putAll(m);

        if (!map.keySet().contains(keyToRemove)) {
            map.put(keyToRemove, keyToRemove);
        }

        Set<K> workList = new HashSet<>();
        workList.add(keyToRemove);

        while (true) {
            List<K> transitiveKeys = map.entrySet()
                    .stream()
                    .filter(e -> workList.contains(e.getValue()))
                    .map(e -> e.getKey())
                    .collect(Collectors.toList());

            boolean hasChanged = workList.addAll(transitiveKeys);
            if (!hasChanged) break;
        }

        map.keySet().removeAll(workList);
        return map;
    }
}