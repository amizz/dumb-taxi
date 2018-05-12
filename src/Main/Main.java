package Main;

import java.util.*;
import java.io.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * START GUI
     */
    static Stage window;
    static Scene setupScene, taxiScene, resultScene;
    static GridPane taxiLayout;
    static VBox resultLayout;
    /**
     * END GUI
     */

    static Block[][] matrix;
    static int size, passengers;

    static Map<Character, Passenger> passList = new HashMap<Character, Passenger>();

    static ArrayList<Character> sourceLbl = new ArrayList<>(),
            destLbl = new ArrayList<>();
    static ArrayList<Block> passengerPoints = new ArrayList<>();

    static Random rnd = new Random();
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException, IOException {
        launch(args);

//        p("Enter map size: ");
//        size = scan.nextInt();
//
//        p("Enter number of passengers: ");
//        passengers = scan.nextInt();
//        s();
//        scan.nextLine();
//
//        matrix = new Block[size][size];
//
//        for (int y = 0; y < size; y++) {
//            for (int x = 0; x < size; x++) {
//                Block b = new Block();
//                b.Location = new Point(x, y);
//                matrix[x][y] = b;
//            }
//        }
//
//        for (int i = 0; i < passengers; i++) {
//            Passenger pass = new Passenger();
//            String[] parts;
//
//            p("Enter passenger #" + (i + 1) + " label: ");
//            pass.Label = scan.nextLine().charAt(0);
//
//            p("Enter passenger #" + (i + 1) + " source location: ");
//            parts = scan.nextLine().trim().split(",");
//            pass.Source = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
//
//            p("Enter passenger #" + (i + 1) + " destination location: ");
//            parts = scan.nextLine().trim().split(",");
//            pass.Destination = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
//
//            s();
//
//            sourceLbl.add(pass.Label);
//            destLbl.add(Character.toLowerCase(pass.Label));
//
//            passList.put(pass.Label, pass);
//
//            Block b = new Block();
//            b.Type = 1;
//            b.Value = pass.Label;
//            b.Tag = pass;
//            b.Duration = 0.0f;
//
//            Block d = new Block();
//            d.Type = 2;
//            d.Value = Character.toLowerCase(pass.Label);
//            d.Tag = pass;
//            d.Duration = 0.0f;
//
//            matrix[pass.Source.X][pass.Source.Y] = b;
//            matrix[pass.Destination.X][pass.Destination.Y] = d;
//
//            b.Location = pass.Source;
//            d.Location = pass.Destination;
//
//            passengerPoints.add(b);
//            passengerPoints.add(d);
//        }
//
//        pl("####################################");
//        pl("             Map Output:            ");
//        pl("####################################");
//        s();
//
//        p("   ");
//        for (int i = 0; i < size; i++) {
//            p(" " + i + " ");
//        }
//        s();
//        p("   ");
//        for (int i = 0; i < size; i++) {
//            p("___");
//        }
//        s();
//
//        for (int y = 0; y < size; y++) {
//            p(y + " |");
//            for (int x = 0; x < size; x++) {
//                p(" " + matrix[y][x].Value.toString() + " ");
//            }
//            s();
//        }
//
//        s();
//        pl("####################################");
//        pl(sourceLbl.toString() + " = source passenger labels");
//        pl(destLbl.toString() + " = destination passenger labels");
//        pl("0 = empty square");
//        pl("####################################");
//        s();
//
//        PerformPathFinding();
//
//        s();
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

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        /**
         * Scene 1
         */
        VBox setupLayout = new VBox();
        Label intro = new Label("Welcome to DumbTaxi Inc");
        intro.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;"
                + "-fx-text-alignment: center; -fx-alignment: center; -fx-max-width:Infinity; -fx-padding: 5;");

        Label sizeLbl = new Label("Size");
        sizeLbl.setStyle("-fx-padding: 20 0 0 0;");
        TextField size = new TextField();
        size.setText("5");

        Label numPassLbl = new Label("Number of Passenger");
        TextField passenger = new TextField();
        passenger.setText("2");

        //Submit button
        VBox buttonLayout = new VBox();
        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(e -> {
            int parseSize = Integer.parseInt(size.getText());
            int parsePassNum = Integer.parseInt(passenger.getText());
            
            taxiLayout.getChildren().clear();
            taxiLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));
            table(parseSize);
            setPassenger(parsePassNum, parseSize);
            window.setScene(taxiScene);
        });
        buttonLayout.getChildren().add(submitBtn);
        buttonLayout.setPadding(new Insets(20, 10, 10, 10));
        buttonLayout.setAlignment(Pos.CENTER);

        setupLayout.getChildren().add(intro);
        setupLayout.getChildren().add(sizeLbl);
        setupLayout.getChildren().add(size);
        setupLayout.getChildren().add(numPassLbl);
        setupLayout.getChildren().add(passenger);
        setupLayout.getChildren().add(buttonLayout);

        setupLayout.setPadding(new Insets(20, 20, 20, 20));
        setupScene = new Scene(setupLayout, 300, 250);

        /**
         * Scene taxi
         */
        VBox rootTaxi = new VBox();
        Label intro2 = new Label("Let the taxi move!");
        intro2.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;"
                + "-fx-text-alignment: center; -fx-alignment: center; -fx-max-width:Infinity; -fx-padding: 5;");
        taxiLayout = new GridPane();
        Button resultBtn = new Button("Result");
        resultBtn.setOnAction(e -> {
            try {
                PerformPathFinding();
            } catch(Exception error) {
                System.out.println(error.getMessage());
            }
            resultBox();
            window.setScene(resultScene);
        });

        rootTaxi.getChildren().add(intro2);
        rootTaxi.getChildren().add(taxiLayout);
        rootTaxi.getChildren().add(resultBtn);
        rootTaxi.setSpacing(10);
        rootTaxi.setPadding(new Insets(10));
        taxiScene = new Scene(rootTaxi, 720, 360);

        /**
         * Scene result
         */
        resultLayout = new VBox();
        Label intro3 = new Label("DumbTaxi Result");
        intro3.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;"
                + "-fx-text-alignment: center; -fx-alignment: center; -fx-max-width:Infinity; -fx-padding: 5;");

        Button restartBtn = new Button("Restart");
        restartBtn.setOnAction(e -> {
            reset();
            window.setScene(setupScene);
        });
        resultLayout.getChildren().add(intro3);
        resultLayout.getChildren().add(restartBtn);
        resultLayout.setPadding(new Insets(10));

        resultScene = new Scene(resultLayout, 720, 360);

        /**
         * Global
         */
        window.setTitle("DumbTaxi");
        window.setScene(setupScene);
        window.show();
    }

    public static void alert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static GridPane table(int size) {
        for (int i = 0; i < size; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / size);
            colConst.setHgrow(Priority.ALWAYS);
            taxiLayout.getColumnConstraints().add(colConst);

            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / size);
            rowConst.setVgrow(Priority.ALWAYS);
            taxiLayout.getRowConstraints().add(rowConst);
        }

        taxiLayout.setGridLinesVisible(true);
        return taxiLayout;
    }

    public static GridPane setPassenger(int passengerNum, int sizeGrid) {
        Random r = new Random();
        String alphabets = "ABCDEFGHIJKLMNOPQRTUVWXYZ";

        matrix = new Block[sizeGrid][sizeGrid];

        for (int y = 0; y < sizeGrid; y++) {
            for (int x = 0; x < sizeGrid; x++) {
                Block b = new Block();
                b.Location = new Point(x, y);
                matrix[x][y] = b;
            }
        }

        for (int i = 0; i < passengerNum; i++) {
            Passenger pass = new Passenger();
            String[] parts;

            pass.Label = alphabets.charAt(r.nextInt(alphabets.length()));
            pass.Source = new Point(r.nextInt(sizeGrid), r.nextInt(sizeGrid));
            Point temp = new Point(r.nextInt(sizeGrid), r.nextInt(sizeGrid));
            while (temp.equals(pass.Source)) {
                temp = new Point(r.nextInt(sizeGrid), r.nextInt(sizeGrid));
            }
            pass.Destination = new Point(r.nextInt(sizeGrid), r.nextInt(sizeGrid));
            System.out.println(pass.Destination);
            sourceLbl.add(pass.Label);
            System.out.println(sourceLbl.toString());
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

            Label pickup = new Label(Character.toString(pass.Label));
            pickup.setPadding(new Insets(10));
            taxiLayout.add(pickup, pass.Source.X, pass.Source.Y);

            Label dropoff = new Label(Character.toString(Character.toLowerCase(pass.Label)));
            dropoff.setPadding(new Insets(10));
            taxiLayout.add(dropoff, pass.Destination.X, pass.Destination.Y);
        }

        return taxiLayout;
    }

    public static VBox resultBox() {

        Label sourceLabel = new Label("####################################\n" + sourceLbl.toString() + " = source passenger labels");
        Label destLabel = new Label(destLbl.toString() + " = destination passenger labels");
        Label emptyLabel = new Label("0 = empty square\n" + "####################################");
        ArrayList<Label> time = new ArrayList();


        resultLayout.getChildren().add(sourceLabel);
        resultLayout.getChildren().add(destLabel);
        resultLayout.getChildren().add(emptyLabel);
        
        int count = 0;
        for (HashMap.Entry<Character, Passenger> entry : passList.entrySet()) {
            Passenger pas = entry.getValue();
            time.add(new Label("Passenger " + pas.Label + " wait for " + pas.getWaitingTime() + ", ride for " + pas.getRideTime()));
            resultLayout.getChildren().add(time.get(count));
            count++;
        }
        return resultLayout;
    }
    
    public static void reset() {
        
        matrix = new Block[1][1];
        size=0;
        passengers=5;

        passList = new HashMap<Character, Passenger>();

        sourceLbl = new ArrayList<>();
        destLbl = new ArrayList<>();
        passengerPoints = new ArrayList<>();

        rnd = new Random();
        scan = new Scanner(System.in);
    }
}
