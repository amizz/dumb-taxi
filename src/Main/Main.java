package Main;

import java.util.*;
import java.io.*;

public class Main {

    static Block[][] matrix;
    static int size, passengers;

    static Map<Character, Passenger> passList = new HashMap<Character, Passenger>();

    static ArrayList<Character> sourceLbl = new ArrayList<>(),
            destLbl = new ArrayList<>();
    static ArrayList<Block> passengerPoints = new ArrayList<>();

    static Random rnd = new Random();
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException, IOException {

        p("Enter map size: ");
        size = scan.nextInt();

        p("Enter number of passengers: ");
        passengers = scan.nextInt();
        s();
        scan.nextLine();

        matrix = new Block[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Block b = new Block();
                b.Location = new Point(x, y);
                matrix[x][y] = b;
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

            passList.put(pass.Label, pass);

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

            b.Location = pass.Source;
            d.Location = pass.Destination;

            passengerPoints.add(b);
            passengerPoints.add(d);
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
        s();

        PerformPathFinding();

        s();
    }

    private static void PerformPathFinding() throws FileNotFoundException, IOException {
        ArrayList<Block> routes = new ArrayList<Block>();
        ArrayList<Character> passengerOnTaxi = new ArrayList<>();
        ArrayList<Block> finalRoute = new ArrayList<Block>();

        Block cur_b = new Block();
        cur_b.Location = new Point(0, 0);
        cur_b.Value = "Beginning";
        cur_b.Duration = 0.0f;
        cur_b.Flag = "begin";

        routes.add(cur_b);

        while (passengerPoints.isEmpty() == false) {
            int distSquared = Integer.MAX_VALUE;
            int passIdx = -1;

            for (int i = passengerPoints.size() - 1; i >= 0; i--) {
                Block dist_b = passengerPoints.get(i);
                int b_distance = dist(cur_b.Location, dist_b.Location);
                if (b_distance < distSquared) {
                    if (dist_b.Type != 2) {
                        distSquared = b_distance;
                        passIdx = i;
                    } else {
                        if (passengerOnTaxi.contains(Character.toUpperCase((Character) dist_b.Value))) {
                            distSquared = b_distance;
                            passIdx = i;
                        }
                    }
                }
            }

            Block found = passengerPoints.remove(passIdx);
            found.Flag = (found.Type == 1 ? "pickup" : "dropoff");

            if (found.Type == 1) {
                passengerOnTaxi.add((Character) found.Value);
            } else {
                Character toFind = Character.toUpperCase((Character) found.Value);
                int index = passengerOnTaxi.indexOf(toFind);
                passengerOnTaxi.remove(index);
            }

            routes.add(found);
            cur_b = found;

        }

        // Fill in the paths for each routes.
        int routes_size = routes.size();
        for (int i = 0; i < routes_size - 1; i++) {
            Block b1 = routes.get(i);
            Block b2 = routes.get(i + 1);

            finalRoute.add(b1);

            int horizontal_delta = b2.Location.Y - b1.Location.Y;
            boolean positive_h = (horizontal_delta > -1);
            int vertical_delta = b2.Location.X - b1.Location.X;
            boolean positive_v = (vertical_delta > -1);

            if (positive_h && horizontal_delta != 0) {
                for (int c1 = 1; c1 <= horizontal_delta; c1++) {
                    int x_c1 = b1.Location.X + c1;
                    int y_c1 = b1.Location.Y;
                    Block b_new = new Block();
                    b_new.Value = new Point(x_c1, y_c1);
                    b_new.Flag = "right";
                    b_new.Location = new Point(x_c1, y_c1);
                    finalRoute.add(b_new);
                }
            } else if (positive_h == false) {
                horizontal_delta = horizontal_delta * -1;
                for (int c2 = 1; c2 <= horizontal_delta; c2++) {
                    int x_c2 = b1.Location.X - c2;
                    int y_c2 = b1.Location.Y;

                    Point p_c2 = new Point(x_c2, y_c2);

                    Block b_c2 = new Block();
                    b_c2.Value = p_c2;
                    b_c2.Flag = "left";
                    b_c2.Location = p_c2;

                    finalRoute.add(b_c2);
                }
            }

            if (positive_v && vertical_delta != 0) {
                for (int c1 = 1; c1 <= vertical_delta; c1++) {
                    int x_c1 = b2.Location.X;
                    int y_c1 = b1.Location.Y + 1;
                    Block b_new = new Block();
                    b_new.Value = new Point(x_c1, y_c1);
                    b_new.Flag = "down";
                    b_new.Location = new Point(x_c1, y_c1);
                    finalRoute.add(b_new);
                }
            } else if (positive_v == false) {
                vertical_delta = vertical_delta * -1;
                for (int c2 = 1; c2 <= vertical_delta; c2++) {
                    int x_c2 = b1.Location.X;
                    int y_c2 = b1.Location.Y - c2;

                    Point p_c2 = new Point(x_c2, y_c2);

                    Block b_c2 = new Block();
                    b_c2.Value = p_c2;
                    b_c2.Flag = "up";
                    b_c2.Location = p_c2;

                    finalRoute.add(b_c2);
                }
            }

            if (i == routes_size - 2) {
                finalRoute.add(b2);
            }
        }

        File fout = new File("log.txt");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

        writer.write("Execution Log:");
        writer.newLine();
        writer.newLine();

        int cumulativeIndex = 0;
        String logText = "";

        for (int i = 0; i < finalRoute.size(); i++) {
            Block b = finalRoute.get(i);
            cumulativeIndex += b.Duration;

            switch (b.Flag.toString()) {
                case "begin":
                    logText = "Taxi started";
                    break;
                case "pickup":
                    logText = "Taxi fetch Passenger " + b.Value;
                    break;
                case "dropoff":
                    logText = "Taxi dropped Passenger " + b.Value;
                    break;
                case "right":
                case "left":
                case "up":
                case "down":
                    logText = "Taxi move " + b.Flag;
                    break;
                default:
                    logText = "Error!";
            }

            writer.write("[" + cumulativeIndex + "] " + logText);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        writer = null;

        float mainDuration = 0;
        for (int i = 0; i < finalRoute.size(); i++) {
            Block b = finalRoute.get(i);
            mainDuration += b.Duration;

            if (b.Type == 1 || b.Type == 2) {
                Character b_label = Character.toUpperCase((Character) b.Value);
                if (b.Type == 1) {
                    passList.get(b_label).PickupTime = mainDuration;
                } else if (b.Type == 2) {
                    passList.get(b_label).DropoffTime = mainDuration;
                }
            }
        }

        pl("######################################");
        pl("                Result:               ");
        pl("######################################");
        for (HashMap.Entry<Character, Passenger> entry : passList.entrySet()) {
            Passenger pas = entry.getValue();
            pl("Passenger " + pas.Label + " wait for " + pas.getWaitingTime() + ", ride for " + pas.getRideTime());
        }
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

    private static int dist(Point a, Point b) {
        return (int) Math.pow((b.X - a.X), 2) + (int) Math.pow((b.Y - a.Y), 2);
    }

//    private static void PerformPathFinding() {
//        ArrayList<Block> route = new ArrayList<Block>();
//        /*
//        Step 1:
//        - Get all the points that we're interested.
//        - All the pickup and dropoff points so we can
//        - make a route to go to each points
//         */
//
//        // poi - Point Of Interests
//        Point[] poi = GetPointsOrder(matrix, size);
//
//        // Step 2: Put the origin block (0,0) to the route.
//        route.add(matrix[poi[0].X][poi[0].Y]);
//        route.get(0).Flag = "start";
//        
//        /*
//        Step 3:
//        - for each neighbouring points in 'poi',
//        - find the distance in terms of X and Y axis.
//        - Then, fill the distance with blocks, and add the
//        - filler blocks into the route.
//         */
//        for (int i = 0; i < poi.length - 1; i++) {
//            Point origin = poi[i];
//            Point dest = poi[i + 1];
//            Block b_origin = matrix[origin.X][origin.Y];
//            Block b_dest = matrix[dest.X][dest.Y];
//            
//            int y_delta = dest.Y - origin.Y;
//            boolean y_positive_dir = (y_delta > -1);
//            
//            pl("Y delta for " + origin + "(" + b_origin.Value +")"
//                    + " and " + dest + " (" + b_dest.Value + ") is " + y_delta);
//            
//            /*
//            int x_dist = dest.X - origin.X;
//            int y_dist = dest.Y - origin.Y;
//
//            Block origin_block = matrix[origin.X][origin.Y];
//            origin_block.Flag = (origin_block.Type == 1 ? "pickup" : "dropoff");
//            route.add(origin_block);
//            
//            // Fill in the horizontal gaps
//            // (The move left/right)
//            if (x_dist > -1) {
//                // Positive distance. Loop upwards.
//
//                for (int x_pos = origin.X; x_pos <= dest.X; x_pos++) {
//                    int y_pos = origin.Y;
//                    Block b = matrix[x_pos][y_pos];
//                    b.Flag = "goright";
//                    route.add(b);
//                }
//            } else {
//                // Negative distance. Loop backwards.
//                for (int x_pos = origin.X - 1; x_pos >= dest.X; x_pos--) {
//                    int y_pos = origin.Y;
//                    Block b = matrix[x_pos][y_pos];
//                    b.Flag = "goleft";
//                    route.add(b);
//                }
//            }
//
//            // Fill in the vertical gaps
//            // ( The move up/down )
//            if (y_dist > -1) {
//                // Positive distance. Loop upwards
//                for (int y_pos = origin.Y; y_pos <= dest.Y; y_pos++) {
//                    int x_pos = dest.X;
//                    Block b = matrix[x_pos][y_pos];
//                    b.Flag = "godown";
//                    route.add(b);
//                }
//            } else {
//                // Negative distance. Loop downwards
//                for (int y_pos = origin.Y - 1; y_pos >= dest.Y; y_pos--) {
//                    int x_pos = dest.X;
//                    Block b = matrix[x_pos][y_pos];
//                    b.Flag = "goup";
//                    route.add(b);
//                }
//            }*/
//        }
//
//        /*
//        Step 4:
//        - Showing the routes
//         */
//        for (int i = 0; i < route.size(); i++) {
//            Block b = route.get(i);
//            pl("Block type: " + b.Type + ", Flag: " + b.Flag + ", Tag: " + b.Tag);
//        }
//    }
//
//    private static Point[] GetPointsOrder(Block[][] matrix, int mapSize) {
//        ArrayList<Point> path = new ArrayList<>();
//        path.add(new Point(0, 0));
//
//        /*
//        Add all the points into the 'path' ArrayList
//         */
//        for (int count = 0; count < (passList.size() * 2); count++) {
//            Point lastPoint = path.get(path.size() - 1);
//
//            for (int i = 0; i < mapSize; i++) {
//                // Get all points in 'i' radius from point 'lastPoint'
//                Point[] pts = getSearchCoordinates(lastPoint.X, lastPoint.Y, i, mapSize);
//
//                for (Point p : pts) {
//                    // Exception to ignore OutOfBound error (when checking points at region outside of the map)
//                    try {
//                        Block b = matrix[p.X][p.Y];
//                        if (b.Type != 0) {
//                            if (!path.contains(p)) {
//                                path.add(p);
//                                break;
//                            }
//                        }
//                    } catch (Exception ex) {
//                    }
//                }
//            }
//        }
//
//        /*
//        
//        Check the early part of the path.
//        The first point must be a pickup point.
//        
//        If it's not a pickup point, then swap whatever with
//        the first occuring interest point.
//        
//         */
//        // 1. Get the first occuring pickup/dropoff point.
//        int pickIndex = -1, dropIndex = -1;
//        for (int i = 0; i < path.size(); i++) {
//            Block b = matrix[path.get(i).X][path.get(i).Y];
//            
//            if (b.Type == 1) {
//                pickIndex = i;
//            } else if (b.Type == 2) {
//                dropIndex = i;
//            }
//            
//            if (pickIndex > -1 && dropIndex > -1)
//                break;
//
//        }
//        
//        // 2. Check if pickup is after dropoff
//        //    If yes, swap them.
//        if (pickIndex > dropIndex) {
//            pl("Swapping");
//            Collections.swap(path, pickIndex, dropIndex);
//        }
//
//        return path.toArray(new Point[path.size()]);
//    }
//    
//    private static Point[] getSearchCoordinates(int x, int y, int radius, int border) {
//        ArrayList<Point> points = new ArrayList<Point>();
//
//        int upperBound = y - radius;//Math.max(0, y - radius);
//        int lowerBound = y + radius; //Math.min(border, y + radius);
//        int leftBound = x - radius; //Math.max(0, x - radius);
//        int rightBound = x + radius; //Math.min(border, x + radius);
//
//        // get all coordinates on the upper bound
//        // x is changing, y = upperBound
//        for (int i = leftBound; i < rightBound; i++) {
//            points.add(new Point(i, upperBound));
//        }
//
//        // get all coordinates on the right bound
//        // x = rightBound, y is changing
//        for (int j = upperBound; j < lowerBound; j++) {
//            points.add(new Point(rightBound, j));
//        }
//
//        // get all coordinates on the lower bound
//        // x is changing, y = lowerBound
//        for (int k = rightBound; k > leftBound; k--) {
//            points.add(new Point(k, lowerBound));
//        }
//
//        // get all coordinates on the left bound
//        // x = leftBound, y is changing
//        for (int l = lowerBound; l > upperBound; l--) {
//            points.add(new Point(leftBound, l));
//        }
//        return points.toArray(new Point[points.size()]);
//    }
}
