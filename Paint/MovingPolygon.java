import javafx.geometry.Bounds;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;
import javafx.collections.*;
import java.util.*;
/**Nowa klasa wielokata */
public class MovingPolygon extends Polygon {
    public MovingPolygon() { 
        setOnMouseClicked(new Moving());
        setOnMouseDragged(new Moving());
        setOnScroll(new Scaling());
    }
    /**Sprawdzanie czy wielokat jest wybrany*/
    public boolean isActive(double x, double y) { 
        return getBoundsInLocal().contains(x,y);   
    }
    /**Znalezienie wspolrzednej x srodka wielokata*/
    public double centerX(){
        double x=0;
        for (int i = 0 ; i < this.getPoints().size(); i+=2) {
            x += this.getPoints().get(i);
        }
        x=2*x/this.getPoints().size();
        return x;
    }
    /**Znalezienie wspolrzednej y srodka wielokata*/
    public double centerY(){
        double x=0;
        for (int i = 1 ; i < this.getPoints().size(); i+=2) {
            x += this.getPoints().get(i);
        }
        x=2*x/this.getPoints().size();
        return x;
    }
    /**Zmiana polozenia*/
    public void add(double dx,double dy){
        ObservableList<Double> points=this.getPoints();
        for (int i = 0 ; i < this.getPoints().size(); i+=1) {
            if(i%2==0){
                points.set(i,points.get(i)+dx);
            }
            else{
                points.set(i,points.get(i)+dy);
            }
        }
    }
    //Implementacja przesuwania
    class Moving implements EventHandler<MouseEvent>{
        MovingPolygon polygon;
        private double x,y;
        private void Move(MouseEvent event) {
            double dx = event.getX() - x;
            double dy = event.getY() - y;
            if (polygon.isActive(x, y)) {
                polygon.add(dx,dy);
            }
            x += dx;
            y += dy;            
        }
        @Override
        public void handle(MouseEvent event) {
            polygon = (MovingPolygon) event.getSource();
            if (event.getEventType()==MouseEvent.MOUSE_PRESSED){
                x = event.getX();
                y = event.getY();
            }
            if (event.getEventType()==MouseEvent.MOUSE_DRAGGED){
                Move(event);
            }
        }
    }
    //Implementacja skalowania 
    class Scaling implements EventHandler<ScrollEvent>{
        MovingPolygon polygon;
        private void Scale(ScrollEvent e) {
            double x = e.getX();
            double y = e.getY();
            double center_x=polygon.centerX();
            double center_y=polygon.centerY();
            if (polygon.isActive(x, y)) {                 
                Scale sc=new Scale();
                sc.setX(1+e.getDeltaY()*0.001);
                sc.setY(1+e.getDeltaY()*0.001);
                sc.setPivotX(center_x);
                sc.setPivotY(center_y);
                polygon.getTransforms().add(sc);
            }
        }            
        @Override
        public void handle(ScrollEvent event) {
            polygon = (MovingPolygon) event.getSource();
            if (event.getEventType()==ScrollEvent.SCROLL){
                Scale(event);
            }
        }
    }
}