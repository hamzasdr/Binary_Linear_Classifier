/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linear_classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Bashar Sader
 */
public class FXMLDocumentController implements Initializable {
    int numberOfData = 0;
    double learningRate = 0.5;
    int numberOfIterations=100;
    double threshold = 0.2;
    double w1 = 0, w2=0;
    boolean flag = false , chart_flag = false;
    int min_point = 10000 , max_point = -10000;
    Vector <Point> points = new Vector<>();
//    final NumberAxis xAxis = new NumberAxis();
//    final NumberAxis yAxis = new NumberAxis();
    @FXML LineChart<Number,Number> Chart;
    @FXML private NumberAxis xAxis;
    @FXML  private NumberAxis yAxis ;
    @FXML TextField  Iterations = new TextField (); 
    @FXML TextField  LR = new TextField ();
    @FXML TextField  Feature_1 = new TextField ();
    @FXML TextField  Feature_2 = new TextField ();
    
    @FXML Label Result = new Label();
    private XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
    private XYChart.Series<Number,Number> series2 = new XYChart.Series<Number,Number>();
    private XYChart.Series<Number,Number> res_series = new XYChart.Series<Number,Number>();
    
    public void Green_btn(ActionEvent event){
        int x = Integer.parseInt(Feature_1.getText());
        
        int y = Integer.parseInt(Feature_2.getText());      
        series.getData().add(new XYChart.Data<Number,Number>(x,y));
        Point point = new Point(x,y,1);
        points.add(point);
        numberOfData++;
        if(x > max_point)
            max_point = x;
        if(x < min_point)
            min_point = x;
        if(chart_flag){
        System.out.println("Triggered on G");
        Chart.getData().addAll(series,series2,res_series);
        chart_flag = false;
        }
    }
    
    
    public void Blue_btn(ActionEvent event){  
        int x = Integer.parseInt(Feature_1.getText());
        int y = Integer.parseInt(Feature_2.getText());     
        Point point = new Point(x,y,0);
        points.add(point);
        numberOfData++;
        series2.getData().add(new XYChart.Data<Number,Number>(x,y)); 
        if(x > max_point)
            max_point = x;
        if(x < min_point)
            min_point = x;
        if(chart_flag){
        System.out.println("Triggered on B");
        Chart.getData().addAll(series,series2,res_series);
        chart_flag = false;
        }
    }
    
    
    public void clear(ActionEvent event){  
        numberOfData = 0;  
        points.clear();
        series.getData().clear(); 
        series2.getData().clear(); 
        res_series.getData().clear();         
    }
    
    private static int step(double x){
        if(x >= 0) return 1;
        else return 0;
    }
    private static double sigmoid(double x){
           
            return (1/( 1 + Math.pow(Math.E,(-1*x))));

    }
    
    private static double activate(int x1, int x2, double w1, double w2, double thresh){
        double sum = (x1*w1 + x2*w2) + thresh;
        return sigmoid(sum);
    }
    
    private static double Loss_Function(double H,double Y){
        double result = -1 * Y * Math.log10(H) - (1-Y) * Math.log10(1 - H);
        System.out.println("for H :" + H + " = " + result);
        return result;
    }
    
//    public void btn(MouseEvent  event){  
//    if (event.getButton() == MouseButton.PRIMARY){
//         System.out.println("prim");
//         series.getData().add(new XYChart.Data<Number,Number>(1,2));
//         } 
//    else if (event.getButton() == MouseButton.SECONDARY){
//        System.out.println("sec");
//         series2.getData().add(new XYChart.Data<Number,Number>(3,2));
//        }
//      
//      double x = (event.getSceneX() - 201)-27;
//      double y = (194 - event.getSceneY())/23 ;
//              
//      System.out.println(x + " , " + y);
//    }
//    
   public void select_train_file(ActionEvent event) throws IOException{
        series.getData().clear();
        series2.getData().clear();
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
             try {
                 FileReader reader = new FileReader(selectedFile.getAbsolutePath());
                try (BufferedReader buffreader = new BufferedReader(reader)) {
                    String line = buffreader.readLine();
                    numberOfData = Integer.parseInt(line.trim());
//                    System.out.println(numberOfData);
                    while((line = buffreader.readLine()) != null)
                    {
                        String coords[] = line.split(",", 4);
                        int x = Integer.parseInt(coords[0]);
                        int y = Integer.parseInt(coords[1]);
                        int val = Integer.parseInt(coords[2]);
                        if(val == 1)
                            series.getData().add(new XYChart.Data<Number,Number>(x,y));
                        else
                            series2.getData().add(new XYChart.Data<Number,Number>(x,y));
                        Point point = new Point(x,y,val);
                        points.add(point);
                        if(x > max_point)
                            max_point = x;
                        if(x < min_point)
                            min_point = x;
                        
                    }
                    if(chart_flag){
                    Chart.getData().addAll(series,series2,res_series);
                    chart_flag = false;
                    }
                    flag = true;
                    buffreader.close();
                }
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
   }
   
   
     public void select_test_file(ActionEvent event) throws IOException{
        series.getData().clear();
        series2.getData().clear();
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
             try {
                 FileReader reader = new FileReader(selectedFile.getAbsolutePath());
                try (BufferedReader buffreader = new BufferedReader(reader)) {
                    String line = buffreader.readLine();
                    numberOfData = Integer.parseInt(line.trim());
//                    System.out.println(numberOfData);
                    while((line = buffreader.readLine()) != null)
                    {
                        String coords[] = line.split(",", 4);
                        int x1 = Integer.parseInt(coords[0]);
                        int x2 = Integer.parseInt(coords[1]);
                        double yActual = activate(x1, x2, w1, w2, threshold);
                        System.out.println(yActual);
                        if(yActual >= 0.5){
                            System.out.println("its 1");
                            series.getData().add(new XYChart.Data<Number,Number>(x1,x2));
                        }
                        else{
                            System.out.println("its 0");
                            series2.getData().add(new XYChart.Data<Number,Number>(x1,x2));
                        }
                    }
                    flag = true;
                    buffreader.close();
                }
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
   }
   
   
    public void train(ActionEvent event){
        learningRate = Float.parseFloat(LR.getText());
        numberOfIterations = Integer.parseInt(Iterations.getText());
//        System.out.println("LR : "+learningRate+" Epochs : "+numberOfIterations);
        Random rnd = new Random();
        w1 = -0.5 + (0.5 + 0.5) * rnd.nextDouble();
        w2 = -0.5 + (0.5 + 0.5) * rnd.nextDouble();
        threshold = -0.5 + (0.5 + 0.5) * rnd.nextDouble();
        System.out.println(""+w1+", "+w2);
//        if(flag){
            while(numberOfIterations > 0){
                for(int i=0; i< numberOfData; i++){
                    // Step 2: Activation
                    double yActual = activate(points.get(i).x, points.get(i).y, w1, w2, threshold);
                    // Step 3: Weight Training
                    double e =  points.get(i).val - yActual;
//                      double e = Loss_Function(yActual,points.get(i).val);
//                    System.out.println(e);
                    w1 = w1 + learningRate * e * points.get(i).x;
                    w2 = w2 + learningRate * e * points.get(i).y;
                    threshold = threshold + learningRate * e;
                }
                // Step 4: Iteration
                numberOfIterations -= 1;
            }
            
            /////////////////////////////////////////
            
//            double x1 = -3;
//            double y = -(x1 * w1 / w2) - ((threshold) / w2);
//            res_series.getData().add(new XYChart.Data<Number,Number>(x1,y));
//            x1 = 3;
//            double y2 = -(x1 * w1 / w2) - ((threshold) / w2);
//            res_series.getData().add(new XYChart.Data<Number,Number>(1,25));
            res_series.getData().clear();    
            for (int x = min_point-1; x <= max_point+1; x++) {
                double y = -(x * w1 / w2) - ((threshold) / w2);
		res_series.getData().add(new XYChart.Data<Number,Number>(x,y));
	   }
            String result = "Final weights are: w1="+w1+", w2="+w2;
            String w1_r = String.format("%.4f", w1);
            String w2_r = String.format("%.4f", w2);
            String theta_r = String.format("%.4f", threshold);
             System.out.println(result);
            Result.setText("w1= "+w1_r+"\nw2="+w2_r+"\nTheta="+theta_r);
    }
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        series.setName("Class 1");
        series2.setName("Class 0");
        res_series.setName("Boundary");
        
        
//        series.getData().add(new XYChart.Data<Number,Number>(2,1));    
//        series2.getData().add(new XYChart.Data<Number,Number>(2,11));    
//        res_series.getData().add(new XYChart.Data<Number,Number>(3,5));    
        Chart.getData().addAll(series,series2,res_series);
//        series.getData().clear();
//        series2.getData().clear();
//        res_series.getData().clear();
//        xAxis.setAutoRanging(false);
//        xAxis.setLowerBound(-100);
//        xAxis.setUpperBound(100);
//        xAxis.setTickUnit(1);
//
//        yAxis.setAutoRanging(false);
//        yAxis.setLowerBound(-100);
//        yAxis.setUpperBound(100);
//        yAxis.setTickUnit(1);
//        for(int x = -50 ;x <= 50 ; x++){
//           scale_series.getData().add(new XYChart.Data<Number,Number>(x,x));        
//        }
//           Chart.getData().addAll(series,series2,res_series,scale_series);
//           Chart.getData().removeAll(series,series2,res_series);
}
}