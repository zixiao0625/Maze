package misc.sanity;

import java.util.ArrayList;
import java.util.function.Function;

// Checkstyle will report an error with this line:
import datastructures.concrete.DoubleLinkedList;

public class SanityCheck {
    public static void main(String[] args) {
        // The following two lines should compile only if you correctly installed Java 8 or higher.
        Function<String, String> test = (a) -> a;
        System.out.println(test.apply("Java 8 or above is correctly installed!"));

        // The following four lines should run, but checkstyle should complain about style errors in both lines.
        // TODO: checkstyle will flag all todos as errors
        ArrayList<String> a = new ArrayList<>();
        a.add("test"); // Deliberately long line: ............................................................................................... end
        System.out.println( a.isEmpty());

        System.out.println("Sanity check complete: everything seems to have been configured correctly!");
    }
}
