package collections;

import java.util.HashMap;
import java.util.Map;

public enum Symbol {
    A, B, C, D;

    private static final Map<String, Symbol> representations = new HashMap<>();

    static {
        representations.put("A", A);
        representations.put("B", B);
        representations.put("C", C);
        representations.put("D", D);
    }

    public static Symbol fromString(String representation) throws Exception {
        Symbol found = representations.get(representation);
        if (found == null) {
            throw new Exception(String.format("There was no symbol found for the string '%s'", representation));
        } else {
            return found;
        }
    }
}
