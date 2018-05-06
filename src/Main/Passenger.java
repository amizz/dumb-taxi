/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.*;

/**
 *
 * @author Amizz
 */
public class Passenger {

    public char Label;
    public Point Source;
    public Point Destination;
    public boolean Picked = false;
    public boolean Delivered = false;
    /*
    private static List<Point> _points = new ArrayList<>();
    private static char _label = 'A';
    private static Random rnd = new Random();

    public static Passenger generateRandom(int mapSize) {
        Passenger p = new Passenger();
        p.Label = _label;

        int count = 0;
        while (true) {
            count++;

            Point src = new Point(rnd.nextInt(mapSize), rnd.nextInt(mapSize));

            if (!_points.contains(src)) {
                p.Source = src;
                break;
            }
            
            if (count >= mapSize * mapSize) {
                break;
            }
        }
        
        while (true) {
            Point dest = new Point(rnd.nextInt(mapSize), rnd.nextInt(mapSize));
            
            if (!dest.equals(p.Source)) {
                p.Destination = dest;
                break;
            }
        }

        _label++;
        return p;
    }*/
}
