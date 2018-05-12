package Main;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Block[][] matrix;
        int size, passengers;
        ArrayList<Passenger> passList = new ArrayList<Passenger>();
        ArrayList<Character> sourceLbl = new ArrayList<>(),
                destLbl = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        p("Enter map size: ");
        size = scan.nextInt();

        p("Enter number of passengers: ");
        passengers = scan.nextInt();
        s();
        scan.nextLine();

        matrix = new Block[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                matrix[x][y] = new Block();
            }
        }

        for (int i = 0; i < passengers; i++) {
            Passenger pass = new Passenger();
            String[] parts;

            p("Enter passenger #" + (i + 1) + " label: ");
            pass.Label = scan.nextLine().charAt(0);
            p("Enter passenger #" + (i + 1) + " source location: ");
            parts = scan.nextLine().trim().split(",");
            pass.Source = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            p("Enter passenger #" + (i + 1) + " destination location: ");
            parts = scan.nextLine().trim().split(",");
            pass.Destination = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            s();

            sourceLbl.add(pass.Label);
            destLbl.add(Character.toLowerCase(pass.Label));
            
            passList.add(pass);

            Block b = new Block();
            b.Type = 1;
            b.Value = pass.Label;
            b.Tag = pass;
            b.Duration = 0.0f;

            Block d = new Block();
            d.Type = 2;
            d.Value = Character.toLowerCase(pass.Label);
            d.Tag = pass;
            d.Duration = 0.0f;

            matrix[pass.Source.X][pass.Source.Y] = b;
            matrix[pass.Destination.X][pass.Destination.Y] = d;
        }

        pl("####################################");
        pl("             Map Output:            ");
        pl("####################################");
        s();

        p("   ");
        for (int i = 0; i < size; i++) {
            p(" " + i + " ");
        }
        s();
        p("   ");
        for (int i = 0; i < size; i++) {
            p("___");
        }
        s();

        for (int y = 0; y < size; y++) {
            p(y + " |");
            for (int x = 0; x < size; x++) {
                p(" " + matrix[y][x].Value.toString() + " ");
            }
            s();
        }

        s();
        pl("####################################");
        pl(sourceLbl.toString() + " = source passenger labels");
        pl(destLbl.toString() + " = destination passenger labels");
        pl("0 = empty square");
        pl("####################################");
    }

    private static void s() {
        System.out.println();
    }

    private static void p(String msg) {
        System.out.print(msg);
    }

    private static void pl(String msg) {
        System.out.println(msg);
    }
}
