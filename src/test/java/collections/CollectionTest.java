package collections;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static collections.MapUtils.intersect;
import static collections.Symbol.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;

public class CollectionTest {

    @Test
    public void conversionContructor() {
        List<String> l = new ArrayList<>(Arrays.asList("1", "2", "3", "3"));
        Set<String> s = new HashSet<>(l);
        assertEquals(3, s.size());
    }

    @Test
    public void collectionRemoveInList_removesFirstOccurence() {
        List<String> l = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "3"));
        l.remove("3");
        assertEquals(4, l.size());
        assertEquals("1", l.get(0));
        assertEquals("2", l.get(1));
        assertEquals("4", l.get(2));
        assertEquals("3", l.get(3));
    }

    @Test
    public void collectionRemoveAll_removesAllOccurences() {
        List<String> l = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "3"));
        l.removeAll(Collections.singleton("3"));
        assertEquals(3, l.size());
        assertEquals("1", l.get(0));
        assertEquals("2", l.get(1));
        assertEquals("4", l.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void arraysAsList_vsNormalArrayList() {
        List<Integer> alOne = Arrays.asList(1, 2, 3);
        List<Integer> alTwo = new ArrayList<>(alOne);

        System.out.println(alOne.getClass().getName());
        System.out.println(alTwo.getClass().getName());

        alTwo.add(4);
        assertEquals(4, alTwo.size());
        alOne.add(4);
    }

    @Test(expected = NoSuchElementException.class)
    public void listIteration_forwardAndBackward() {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        ListIterator<String> it = list.listIterator();

        assertEquals(-1, it.previousIndex());
        assertEquals(0, it.nextIndex());
        assertEquals("a", it.next());
        assertEquals(0, it.previousIndex());
        assertEquals(1, it.nextIndex());

        assertEquals("b", it.next());
        assertEquals("b", it.previous());

        it.next(); // at b
        it.next(); // at c
        it.next(); // at d
        it.next(); // at e

        assertEquals(5, it.nextIndex());
        assertFalse(it.hasNext());
        it.next(); // boom
    }

    @Test
    public void listIteration_addAndSet() {
        List<Integer> ints = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ListIterator<Integer> li = ints.listIterator();

        li.next();
        li.set(10);
        assertTrue(10 == ints.get(0));
        li.previous();
        li.add(0);
        assertTrue(0 == ints.get(0));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void listSubListIsAView_throwsExceptionIfBackedListIsStructurallyModified() {
        List<Integer> l = new ArrayList<>(Arrays.asList(1, 2, 3));
        System.out.println("Implementation of l: " + l.getClass().getName());
        List<Integer> ll = l.subList(0, 2);
        System.out.println("Implementation of ll: " + ll.getClass().getName());

        assertEquals(2, ll.size());
        assertEquals(1, ll.get(0).intValue());
        assertEquals(2, ll.get(1).intValue());

        l.size();
        ll.size();

        l.clear();
        ll.size();
    }

    @Test
    public void listSubList_canChangeBackedList() {
        List<Integer> ints = Stream.iterate(0, n -> n + 1).limit(10).collect(Collectors.toList());
        List<Integer> zeroAndOne = ints.subList(0, 2);

        assertEquals(10, ints.size());
        zeroAndOne.clear();
        assertEquals(8, ints.size());
    }

    @Test
    public void listRotation_canRotateForwardAndBackward() {
        List<Integer> ints = Stream.iterate(0, (n) -> n + 1).limit(10).collect(Collectors.toList());
        Collections.rotate(ints, -1);
        assertEquals(1, ints.get(0).intValue());
        Collections.rotate(ints, 2);
        assertEquals(9, ints.get(0).intValue());
    }

    @Test
    public void setIntersection() {
        Set<Integer> s1 = new HashSet<>();
        Set<Integer> s2 = new HashSet<>();
        s1.addAll(Arrays.asList(1, 2, 3));
        s2.addAll(Arrays.asList(2, 3, 4));

        Set<Integer> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);

        assertEquals(2, intersection.size());
        assertTrue(intersection.contains(2));
        assertTrue(intersection.contains(3));
    }

    @Test(expected = NoSuchElementException.class)
    public void queueTests() {
        Queue<Integer> queue = new LinkedList<>(Arrays.asList(1, 5, 2, 4, 3));

        // inspect: peek() or element()
        assertEquals(1, queue.peek().intValue());
        assertEquals(1, queue.element().intValue());

        // remove: poll()
        while (!queue.isEmpty()) {
            Integer next = queue.poll();
            System.out.println(next);
        }
        assertNull(queue.poll());

        // insert: offer() or add()
        for (int i = 0; i < 10; i++) {
            queue.offer(i);
        }
        for (int i = 0; i < 10; i++) {
            queue.add(i);
        }

        // remove: remove()
        while (true) {
            System.out.println(queue.remove());
        }
    }

    @Test
    public void dequeAsStack() {
        Deque<String> stack = new ArrayDeque<>();
        stack.addLast("o");
        stack.addLast("l");
        stack.addLast("l");
        stack.addLast("e");
        stack.addLast("h");
        StringBuilder sb = new StringBuilder();

        while (!stack.isEmpty()) {
            sb.append(stack.pollLast());
        }
        assertEquals("hello", sb.toString());
    }

    @Test
    public void mapInvert_JDK8() {
        List<String> capitals = Arrays.asList("Berlin", "Budapest", "London", "Lissabon", "Stockholm");
        List<String> captials2 = new ArrayList<>(capitals);

        Stream<String> words = capitals.stream();
        Map<String, List<String>> capitalsByFirstLetter = words.collect(Collectors.groupingBy(this::firstLetter));
        System.out.println(capitalsByFirstLetter);

        Stream<String> words2 = captials2.stream();
        Map<String, List<String>> capitalsByLastLetter = words2
                .collect(Collectors.groupingBy(c -> c.substring(c.length() - 1, c.length())));
        System.out.println(capitalsByLastLetter);
    }

    @Test
    public void mapGetAndPut() {
        List<String> words = Arrays.asList("A", "B", "A", "C", "D", "C");

        Map<String, Integer> counted = new HashMap<>();
        Set<String> distinctWords = new HashSet<>(words);
        distinctWords.forEach(dw -> counted.put(dw, 0));

        for (String word: words) {
            Integer count = counted.get(word);
            counted.put(word, count + 1);
        }

        System.out.println(counted);

        Set<String> dw = new HashSet(Arrays.asList("C", "A", "B", "D"));
        assertEquals(dw, counted.keySet());
        assertEquals(2, counted.get("A").intValue());
        assertEquals(1, counted.get("B").intValue());
        assertEquals(2, counted.get("C").intValue());
        assertEquals(1, counted.get("D").intValue());
    }

    @Test
    public void mapEntryIteration() {
        Map<String, List<String>> citiesByCanton = new HashMap<>();
        citiesByCanton.put("AG", Arrays.asList("Baden", "Brugg", "Aarau"));
        citiesByCanton.put("BS", Arrays.asList("Basel"));
        citiesByCanton.put("UR", Arrays.asList("Altdorf", "Erstfeld"));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : citiesByCanton.entrySet()) {
            sb.append(String.format("Canton %s has cities %s", entry.getKey(), entry.getValue()));
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    @Test
    public void mapSortedKeys_egForDictionaryDataStructure() {
        Map<String, List<String>> citiesByCanton = new TreeMap<>();
        citiesByCanton.put("BS", Arrays.asList("Basel"));
        citiesByCanton.put("AG", Arrays.asList("Baden", "Brugg", "Aarau"));
        citiesByCanton.put("UR", Arrays.asList("Altdorf", "Erstfeld"));
        System.out.println(citiesByCanton);

        List<String> sortedKeys = new ArrayList<>(citiesByCanton.keySet());
        assertEquals(3, sortedKeys.size());
        assertEquals("AG", sortedKeys.get(0));
        assertEquals("BS", sortedKeys.get(1));
        assertEquals("UR", sortedKeys.get(2));
    }

    @Test
    public void mapKeepInsertionOrder_egIfOrderOfPutIsRelevant_firstPutMatters() {
        Map<String, List<String>> citiesByCanton = new LinkedHashMap<>();
        citiesByCanton.put("BS", Arrays.asList("Basel"));
        citiesByCanton.put("AG", Arrays.asList("Baden", "Brugg", "Aarau"));
        citiesByCanton.put("UR", Arrays.asList("Altdorf", "Erstfeld"));
        citiesByCanton.put("BS", Arrays.asList("Basel"));
        citiesByCanton.put("UR", Arrays.asList("Altdorf", "Erstfeld"));
        citiesByCanton.put("BS", Arrays.asList("Basel"));
        System.out.println(citiesByCanton);

        List<String> keyInsertionOrder = new ArrayList<>(citiesByCanton.keySet());
        assertEquals(3, keyInsertionOrder.size());
        assertEquals("BS", keyInsertionOrder.get(0));
        assertEquals("AG", keyInsertionOrder.get(1));
        assertEquals("UR", keyInsertionOrder.get(2));
    }

    @Test
    public void mapPutAll_useItForDefaultValues() {
        Map<String, Integer> managerSalaries = new HashMap<>();
        managerSalaries.put("CEO", 180000);
        managerSalaries.put("Manager", 150000);

        Map<String, Integer> technicianSalaries = new HashMap<>();
        technicianSalaries.put("Architect", 130000);
        technicianSalaries.put("Developer", 110000);

        Map<String, Integer> mySalaries = new HashMap<>();
        mySalaries.putAll(managerSalaries);
        mySalaries.putAll(technicianSalaries);

        System.out.println(mySalaries);
    }

    @Test
    public void mapRemoveKeys_useKeySetAndRemoveAll() {
        Map<Integer, Integer> squares = new HashMap<>();
        squares.put(-1, 1);
        squares.put(0, 0);
        squares.put(1, 1);
        squares.put(2, 4);

        assertEquals(4, squares.size());
        squares.keySet().removeAll(Arrays.asList(-1, 0));
        assertEquals(2, squares.size());
    }

    @Test
    public void mapAlgebra_isSubMapTest() {
        Map<Integer, Integer> m1 = new HashMap<>();
        m1.put(1, 1);
        m1.put(2, 2);

        Map<Integer, Integer> m2 = new HashMap<>();
        m2.put(1, 1);

        assertTrue(m1.entrySet().containsAll(m2.entrySet()));
        assertFalse(m2.entrySet().containsAll(m1.entrySet()));
    }

    @Test
    public void mapAlgebra_supportsSameKeys() {
        Map<Integer, Integer> m1 = new HashMap<>();
        m1.put(1, 1);
        m1.put(2, 2);

        Map<Integer, Integer> m2 = new HashMap<>();
        m2.put(1, 1);

        assertTrue(m1.keySet().containsAll(m2.keySet()));
        assertFalse(m2.keySet().containsAll(m1.keySet()));
    }

    @Test
    public void mapAlgebra_intersectMaps_keyAndValueMustMatch() {
        Map<Integer, Integer> m1 = new HashMap<>();
        m1.put(1, 1);
        m1.put(2, 2);
        m1.put(3, 3);

        Map<Integer, Integer> m2 = new HashMap<>();
        m2.put(1, 3);
        m2.put(2, 2);
        m2.put(3, 1);

        Map<Integer, Integer> m1AndM2 = intersect(m1, m2);
        assertFalse(m1 == m1AndM2);
        assertFalse(m2 == m1AndM2);
        assertEquals(1, m1AndM2.keySet().size());
        assertEquals(2, m1AndM2.get(2).intValue());

        Map<Integer, Integer> m3 = new HashMap<>();
        m3.put(2, 99);
        Map<Integer, Integer> m1AndM3 = intersect(m1, m3);
        assertFalse(m1 == m1AndM3);
        assertFalse(m3 == m1AndM3);
        assertTrue(m1AndM3.isEmpty());
    }

    @Test
    public void mapAlgebra_removeTransitively() {
        Map<String, String> managerByEmployee = new HashMap<>();
        managerByEmployee.put("Christian", "Muzaffer");
        managerByEmployee.put("Christoph", "Muzaffer");
        managerByEmployee.put("Tobias", "Muzaffer");
        managerByEmployee.put("Markus", "Muzaffer");
        managerByEmployee.put("Muzaffer", "Tom");
        managerByEmployee.put("Berthold", "Tom");
        managerByEmployee.put("Daniel", "Tom");
        managerByEmployee.put("Andreas", "Tom");
        managerByEmployee.put("Fred", "Berthold");
        managerByEmployee.put("Christoph N.", "Berthold");
        managerByEmployee.put("Christian S.", "Berthold");
        managerByEmployee.put("Michael", "Berthold");

        Map<String, String> removeTom = MapUtils.removeTransitively(managerByEmployee, "Tom");
        Map<String, String> removeBerthold = MapUtils.removeTransitively(managerByEmployee, "Berthold");
        Map<String, String> removeChristoph = MapUtils.removeTransitively(managerByEmployee, "Christoph");

        assertTrue(0 == removeTom.size());

        assertTrue(7 == removeBerthold.size());
        assertEquals(
                new HashSet<>(Arrays.asList("Christian", "Christoph", "Tobias", "Markus", "Muzaffer", "Daniel", "Andreas")),
                removeBerthold.keySet()
        );

        assertTrue(11 == removeChristoph.size());
        assertEquals(
                new HashSet<>(Arrays.asList("Christian", "Tobias", "Markus", "Muzaffer", "Daniel", "Andreas",
                        "Berthold", "Fred", "Christoph N.", "Christian S.", "Michael")),
                removeChristoph.keySet()
        );

        System.out.println(removeTom);
        System.out.println(removeBerthold);
        System.out.println(removeChristoph);
    }

    @Test
    public void map_WhatHappensIfKeyIsMutable() {
        Map<EvilPerson, Integer> ageByPerson = new HashMap<>();

        EvilPerson p1 = new EvilPerson("A");
        EvilPerson p2 = new EvilPerson("B");
        EvilPerson p3 = new EvilPerson("C");

        ageByPerson.put(p1, 10);
        ageByPerson.put(p2, 20);
        ageByPerson.put(p3, 30);

        assertEquals(10, ageByPerson.get(p1).intValue());
        assertEquals(20, ageByPerson.get(p2).intValue());
        assertEquals(30, ageByPerson.get(p3).intValue());

        p2.setName("foobar");
        assertNull(ageByPerson.get(p2));
    }

    @Test
    public void comparatorReversed() {
        Map<Integer, Integer> salesByDay = new HashMap<>();
        salesByDay.put(1, 6);
        salesByDay.put(2, 10);
        salesByDay.put(3, 16);
        salesByDay.put(4, 6);
        salesByDay.put(5, 8);

        Comparator<Integer> normalOrder = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        };

        List<Integer> days = new ArrayList<>(salesByDay.keySet());
        Collections.sort(days, normalOrder.reversed());
        System.out.println(days);
    }

    @Test
    public void treeSet_useCustomComparator() {
        SortedSet<Person> persons = new TreeSet<>();
        Person c = new Person("A", 4);
        Person t = new Person("B", 3);
        Person s = new Person("C", 2);
        Person a = new Person("D", 1);
        persons.addAll(Arrays.asList(c, t, s, a));
        assertEquals(4, persons.size());

        assertEquals("A", persons.first().name);
        SortedSet<Person> agePerson = new TreeSet<>(Comparator.comparing(Person::getAge));
        agePerson.addAll(persons);
        assertEquals("D", agePerson.first().name);
    }

    @Test
    public void sortedSet_headAndTailSets_takeValues() {
        SortedSet<Integer> evenNumbers = new TreeSet<>(
                Stream.iterate(0, n -> n + 2).limit(50).collect(Collectors.toSet())
        );

        assertEquals(50, evenNumbers.size());

        // exclusive upper-bound
        assertEquals(5, evenNumbers.headSet(10).size());
        assertEquals(new HashSet<>(Arrays.asList(0, 2, 4, 6, 8)), evenNumbers.headSet(10));

        // inclusive lower-bound
        assertEquals(1, evenNumbers.tailSet(98).size());
        assertEquals(new HashSet<>(Arrays.asList(98)), evenNumbers.tailSet(98));
    }

    @Test
    public void sortedSet_rangeView_subSet_takesValuesAsWell() {
        SortedSet<Integer> oddNumbers = new TreeSet<>(
                Stream.iterate(1, n -> n + 2).limit(50).collect(Collectors.toSet())
        );

        Set<Integer> oddNumbersBetween20And30 = oddNumbers.subSet(20, 31);
    }

    @Test
    public void sortedSet_rangeViews_writeBackToView() {
        SortedSet<Integer> ints = new TreeSet<>(Stream.iterate(0, n -> n + 1).limit(10).collect(Collectors.toSet()));
        SortedSet<Integer> belowFive = ints.subSet(0, 5);

        assertEquals(10, ints.size());
        assertEquals(5, belowFive.size());
        ints.remove(0);
        ints.remove(1);

        // now you CAN access the range-view
        assertEquals(3, belowFive.size());
    }

    @Test(expected = ConcurrentModificationException.class)
    public void list_rangeView_writingBackToViewThrowsException() {
        List<Integer> ints = Stream.iterate(0, n -> n + 1).limit(10).collect(Collectors.toList());
        List<Integer> topThree = ints.subList(0, 3);
        assertEquals(10, ints.size());
        assertEquals(3, topThree.size());

        // remove from backing list ...
        ints.remove(0);
        ints.remove(1);
        ints.remove(2);
        assertEquals(7, ints.size());

        // ... but cannot use range-view any more
        assertEquals(3, topThree.size());
    }

    @Test
    public void list_rangeView_mutation() {
        List<Integer> ints = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(5, ints.size());
        ints.subList(0, 1).clear();
        assertEquals(4, ints.size());
    }

    @Test
    public void sortedSet_subSet_isHalfOpen() {
        SortedSet<String> dictionary = new TreeSet<>(Arrays.asList(
                "alfred",
                "anton",
                "berta",
                "berthold",
                "ceclie",
                "christian",
                "dora"
        ));

        // including A, excluding B
        dictionary.subSet("a", "b").forEach(System.out::println);
    }

    @Test
    public void sortedSet_subSet_modelClosedInterval_usingNextString() {
        SortedSet<String> dictionary = new TreeSet<>(Arrays.asList(
                "alfred",
                "anton",
                "berta",
                "berthold",
                "ceclie",
                "christian",
                "dora"
        ));

        assertEquals(3, dictionary.subSet("alfred", "berta\0").size());
    }

    @Test
    public void sortedSet_headSet_notIncluding() {
        SortedSet<String> dictionary = new TreeSet<>(Arrays.asList(
                "alfred",
                "anton",
                "berta",
                "berthold",
                "ceclie",
                "christian",
                "dora"
        ));

        assertEquals(2, dictionary.headSet("b").size());
        assertEquals(5, dictionary.tailSet("b").size());
    }

    @Test
    public void collection_rotating() {
        List<String> strings = new ArrayList<>(Arrays.asList("a", "b", "c"));

        List<String> currentPattern = new ArrayList<>(strings);
        List<List<String>> patterns = new ArrayList<>();
        do {
            Collections.rotate(currentPattern, 1);
            List<String> newCycle = new ArrayList<>(currentPattern);
            newCycle.add(newCycle.get(0));
            patterns.add(newCycle);
        } while (!currentPattern.equals(strings));

        patterns.forEach(System.out::println);
    }

    @Test
    public void chessBoard() {
        int size = 2;
        Symbol[][] board = new Symbol[size][size];
        board[0][0] = A;
        board[0][1] = B;
        board[1][0] = C;
        board[1][1] = D;

        printBoard(board);

        Symbol[] firstRow = board[0];
        Arrays.fill(firstRow, D);

        printBoard(board);
    }

    private void printBoard(Symbol[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j]);
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private class Person implements Comparable<Person> {

        private final String name;
        private final int age;

        public Person(String name, int age) {
            if (name == null) throw new IllegalArgumentException("Name must not be null!");
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            if (age != person.age) return false;
            return name.equals(person.name);
        }

        @Override
        public int compareTo(Person otherPerson) {
            if (otherPerson == null) throw new IllegalArgumentException("Cannot compare to null!");

            int nameCompare = name.compareTo(otherPerson.name);
            return nameCompare != 0 ? nameCompare : Integer.compare(age, otherPerson.age);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + age;
            return result;
        }
    }

    private class EvilPerson {

        private String name;

        public String getName() {
            return name;
        }

        public EvilPerson(String name) {
            setName(name);
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof EvilPerson)) return false;
            EvilPerson otherEvilPerson = (EvilPerson) other;
            return name.equals(otherEvilPerson.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private String firstLetter(String s) {
        return s.substring(0, 1);
    }
}