import javafx.geometry.Bounds;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
/**Nowa klasa prostokata */
public class MovingRectangle extends Rectangle {
    public MovingRectangle(double x, double y, double width, double height) { 
        super(x, y, width, height);
        setOnMouseClicked(new Moving());
        setOnMouseDragged(new Moving());
        setOnScroll(new Scaling());
    }
    /**Sprawdzenie czy prostokat jest wybrany*/
    public boolean isActive(double x, double y) { 
        return getBoundsInLocal().contains(x,y);   
    }
    /**Przesuwanie prostokata w osi x*/
    public void moveX(double x) {  
        setX(getX()+x);
    }
    /**Przesuwanie prostokata w osi Y*/
    public void moveY(double y) {  
        setY(getY()+y);
    }
    /**Zmiana szerokosci*/
    public void addWidth(double w) {
        setWidth(getWidth()+w);
    }
    /**Zmiana wysokosci*/
    public void addHeight(double h) {     
        setHeight(getHeight()+h);
    }
    /**Implementacja przesuwania*/
    class Moving implements EventHandler<MouseEvent>{
        MovingRectangle rectangle;
        private double x,y;
        private void Move(MouseEvent event) {
            double dx = event.getX() - x;
            double dy = event.getY() - y;
            if (rectangle.isActive(x, y)) {
                rectangle.moveX(dx);
                rectangle.moveY(dy);
            }
            x += dx;
            y += dy;            
        }
        @Override
        public void handle(MouseEvent event) {
            rectangle = (MovingRectangle) event.getSource();
            if (event.getEventType()==MouseEvent.MOUSE_CLICKED){
                x = event.getX();
                y = event.getY();
            }
            if (event.getEventType()==MouseEvent.MOUSE_DRAGGED){
                Move(event);
            }
        }
    }
    /**Implementacja Skalowania*/
    class Scaling implements EventHandler<ScrollEvent>{
        MovingRectangle rectangle;
        private void Scale(ScrollEvent e) {
            double x = e.getX();
            double y = e.getY();
            if (rectangle.isActive(x, y)) {                 
                rectangle.addWidth(e.getDeltaY()*0.2);
                rectangle.addHeight(e.getDeltaY()*0.2);
                rectangle.moveX(e.getDeltaY()*(-0.1));
                rectangle.moveY(e.getDeltaY()*(-0.1));
            }
        }            
        @Override
        public void handle(ScrollEvent event) {
            rectangle = (MovingRectangle) event.getSource();
            if (event.getEventType()==ScrollEvent.SCROLL){
                Scale(event);
            }
        }
    }
}