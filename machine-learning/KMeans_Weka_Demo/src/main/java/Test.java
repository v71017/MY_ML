public class Test {

    public static void main(String[] args) throws Exception {

        int i = 3;//Integer.parseInt(args[0]);
        System.out.println("Number of clusters: " + i);
        new Weka().clustering("users.arff", i);
    }
}
