import javafx.geometry.Bounds;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
/**Nowa klasa kola */
public class MovingCircle extends Circle{
        
    public MovingCircle(double x, double y, double radius) {
        super(x, y, radius);
        setOnMouseClicked(new Moving());
        setOnMouseDragged(new Moving());
        setOnScroll(new Scaling()); 
    }
    /**Sprawdzenie czy kolo jest wybrane*/
    public boolean isActive(double x, double y) { 
       return getBoundsInLocal().contains(x,y);   
  
    }
    /**Przesuwanie kola w osi x*/
    public void moveX(double x) {  
        this.setCenterX(this.getCenterX() +x);
    }
    /**Przesuwanie kola w osi y*/
    public void moveY(double y) {         
      this.setCenterY(this.getCenterY() +y);
    }
    /**Zmiana promienia*/
    public void addRadius(double r) {    
      this.setRadius(this.getRadius()+r);
    }
    //Implementacja przesuwania
    class Moving implements EventHandler<MouseEvent>{
        MovingCircle circle;
        private double x,y;
        private void Move(MouseEvent event) {  
            double dx = event.getX() - x;
            double dy = event.getY() - y;
            if (circle.isActive(x, y)) {
                circle.moveX(dx);
                circle.moveY(dy);
            }
            x += dx;
            y += dy;            
        }
        @Override
        public void handle(MouseEvent event) {
            circle=(MovingCircle) event.getSource();
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
        MovingCircle circle;
        private void Scale(ScrollEvent e) {  
            double x = e.getX();
            double y = e.getY();
            if (circle.isActive(x, y)) {                 
                circle.addRadius(e.getDeltaY()*0.2);
            }
        }            
        @Override
        public void handle(ScrollEvent event) {
            circle = (MovingCircle) event.getSource();
            if (event.getEventType()==ScrollEvent.SCROLL){
                Scale(event);
            }
        }
    }
}