/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linear_classifier;

/**
 *
 * @author Bashar Sader
 */
public class Point {
    public double x;
    public double y;
    public int val;
    public Point(){
        x = 0.0;
        y = 0.0;
        val = 0;
    }

    public Point(double x, double y, int val) {
       this.x = x; this.y = y; this.val = val;
    }
}

