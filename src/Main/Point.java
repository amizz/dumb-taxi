/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author Hexareus
 */
public class Point {
    public int X;
    public int Y;
    
    public Point(int x, int y) { X = x; Y = y; }
    public String toString() {
        return "(" + X + "," + Y + ")";
    }
    
    public boolean insideRect(int x, int y, int w, int h) {
        return (X >= x && Y >= y && X <= (x + w) && Y <= (y + h));
    }
    
    @Override
    public boolean equals(Object p) {
        if (p instanceof Point == false) return false;
        return (this.X == ((Point)p).X && this.Y == ((Point)p).Y);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.X;
        hash = 29 * hash + this.Y;
        return hash;
    }
}
