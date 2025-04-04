import javafx.application.Application; 
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;
import javafx.geometry.Rectangle2D;
import javafx.collections.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**Glowna klasa programu */
public class Graphics extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edytor graficzny");
        Pane shapes=new Pane();
        BorderPane root = new BorderPane();
        ColorPicker color=new ColorPicker(Color.BLACK);   
        //Menu rysowania
        Menu draw_menu=new Menu("Shapes");
        MenuItem rect=new MenuItem("Rectangle");
        MenuItem circ=new MenuItem("Circle");
        MenuItem pol=new MenuItem("Polygon");
        draw_menu.getItems().addAll(rect,circ,pol);
        //Rysowanie figur
        rect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MovingRectangle r=new MovingRectangle(0,0,0,0);
                r.setFill(color.getValue());
                shapes.getChildren().add(r);
                root.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
                    double x,y;
                    @Override
                    public void handle(MouseEvent event) {
                    if (event.getEventType()==MouseEvent.MOUSE_PRESSED){
                        x=event.getX();
                        y=event.getY();
                        r.setX(x);
                        r.setY(y);
                    }
                    if (event.getEventType()==MouseEvent.MOUSE_DRAGGED){
                        r.setHeight(Math.abs(event.getY()-y));
                        r.setWidth(Math.abs(event.getX()-x));
                        if((event.getX()-x)<0){
                            r.setX(event.getX());
                        }
                        if((event.getY()-y)<0){
                            r.setY(event.getY());
                        }
                    }
                    if (event.getEventType()==MouseEvent.MOUSE_RELEASED){
                        root.removeEventFilter(MouseEvent.ANY, this);
                    }
                    }
                });
            }
        });
        circ.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MovingCircle c=new MovingCircle(0,0,0);
                c.setFill(color.getValue());
                shapes.getChildren().add(c);
                root.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
                    double x,y,x_temp,y_temp;
                    @Override
                    public void handle(MouseEvent event) {
                    if (event.getEventType()==MouseEvent.MOUSE_PRESSED){
                        x=event.getX();
                        y=event.getY();
                        c.setCenterX(x);
                        c.setCenterY(y);
                    }
                    if (event.getEventType()==MouseEvent.MOUSE_DRAGGED){
                        x_temp=event.getX();
                        y_temp=event.getY();
                        c.setCenterY(Math.abs((y_temp+y)/2));
                        c.setCenterX(Math.abs((x_temp+x)/2));
                        c.setRadius(Math.sqrt((x_temp-x)*(x_temp-x)+(y_temp-y)*(y_temp-y))/2);
                    }
                    if (event.getEventType()==MouseEvent.MOUSE_RELEASED){
                        root.removeEventFilter(MouseEvent.ANY, this);
                    }
                    }
                });
            }
        });
        pol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MovingPolygon p=new MovingPolygon();
                p.setFill(color.getValue());
                shapes.getChildren().add(p);
                root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton()==MouseButton.PRIMARY){
                            p.getPoints().addAll(event.getX(),event.getY());
                        }
                        if (event.getButton()==MouseButton.SECONDARY){
                            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        }
                    }
                });
            }
        });
        //Menu edycji
        Menu edit_menu=new Menu("Edit");
        MenuItem rotate=new MenuItem("Rotate");
        MenuItem fill=new MenuItem("Fill");    
        CustomMenuItem picker = new CustomMenuItem(color);
        picker.setHideOnClick(false);
        edit_menu.getItems().addAll(rotate,fill,picker);
        //Zmiana koloru
        fill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton()==MouseButton.PRIMARY){
                            if(event.getTarget() instanceof MovingRectangle){
                                MovingRectangle r=(MovingRectangle)event.getTarget();
                                r.setFill(color.getValue());
                            }
                            if(event.getTarget() instanceof MovingCircle){
                                MovingCircle c=(MovingCircle)event.getTarget();
                                c.setFill(color.getValue());
                            }
                            if(event.getTarget() instanceof MovingPolygon){
                                MovingPolygon p=(MovingPolygon)event.getTarget();
                                p.setFill(color.getValue());
                            }
                        }
                        if (event.getButton()==MouseButton.SECONDARY){

                            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        }
                    }
                });
            }
        });
        //Obracanie
        rotate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) { 
                        Rotate right=new Rotate();
                        Rotate left=new Rotate();
                        right.setAngle(5);
                        left.setAngle(-5);
                        if (event.getButton()==MouseButton.PRIMARY){
                            if(event.getTarget() instanceof MovingRectangle){
                                MovingRectangle r=(MovingRectangle)event.getTarget();
                                left.setPivotX((2*r.getX()+r.getWidth())/2);
                                left.setPivotY((2*r.getY()+r.getHeight())/2); 
                                r.getTransforms().add(left);
                            }
                            if(event.getTarget() instanceof MovingPolygon){
                                MovingPolygon p=(MovingPolygon)event.getTarget();
                                left.setPivotX(p.centerX());
                                left.setPivotY(p.centerY()); 
                                p.getTransforms().add(left);
                            }
                        }
                        else if (event.getButton()==MouseButton.SECONDARY){
                            if(event.getTarget() instanceof MovingRectangle){
                                MovingRectangle r=(MovingRectangle)event.getTarget();
                                right.setPivotX((2*r.getX()+r.getWidth())/2);
                                right.setPivotY((2*r.getY()+r.getHeight())/2); 
                                r.getTransforms().add(right);
                            }
                            if(event.getTarget() instanceof MovingPolygon){
                                MovingPolygon p=(MovingPolygon)event.getTarget();
                                right.setPivotX(p.centerX());
                                right.setPivotY(p.centerY()); 
                                p.getTransforms().add(right);
                            }
                        }
                        if(event.getButton()==MouseButton.MIDDLE){
                            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        }
                    }
                });
            }
        });
        //Menu zapisu
        Menu save_menu=new Menu("Save");
        MenuItem save=new MenuItem("Save");
        MenuItem load=new MenuItem("Load");
        TextArea choose=new TextArea();
        CustomMenuItem field=new CustomMenuItem(choose);
        save_menu.getItems().addAll(save,load,field);
        field.setHideOnClick(false);
        choose.setPrefSize(220,25);
        //Zapis do pliku
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton()==MouseButton.PRIMARY){
                            if(event.getTarget().toString().startsWith("Rectangle")||event.getTarget().toString().startsWith("Circle")||event.getTarget().toString().startsWith("Polygon")){
                                try {
                                    File newFile = new File(choose.getText());
                                    FileWriter writer = new FileWriter(choose.getText());
                                    writer.write(event.getTarget().toString());
                                    writer.close();
                                    root.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                                }
                                catch (IOException e) {} 
                            }                        
                        }
                        if (event.getButton()==MouseButton.SECONDARY){
                            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, this);
                        }
                    }
                });
            }
        });
        //Odczyt z pliku
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String data="";
                //Odczyt danych z pliku
                try{
                    File file = new File(choose.getText());
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    while((line = br.readLine()) != null){
                        data=line;
                    }}
                    catch (IOException e) {}
                    //Tworzenie odczytanej figury
                    if(data.startsWith("Rectangle")){
                        String[] parts=data.split(",");
                        String x,y,height,width,color;
                        x=parts[0].substring(12,parts[0].length());
                        y=parts[1].substring(3,parts[1].length());
                        width=parts[2].substring(7,parts[2].length());
                        height=parts[3].substring(8,parts[3].length());
                        color=parts[4].substring(6,parts[4].length()-1);
                        Color c = Color.web(color,1.0);
                        MovingRectangle s = new MovingRectangle(Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(width),Double.parseDouble(height));
                        s.setFill(c);
                        shapes.getChildren().add(s); 
                    }
                    if(data.startsWith("Circle")){
                        String[] parts=data.split(",");
                        String x,y,radius,color;
                        x=parts[0].substring(15,parts[0].length());
                        y=parts[1].substring(9,parts[1].length());
                        radius=parts[2].substring(8,parts[2].length());
                        color=parts[3].substring(6,parts[3].length()-1);
                        Color c = Color.web(color,1.0);
                        MovingCircle s = new MovingCircle(Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(radius));
                        s.setFill(c);
                        shapes.getChildren().add(s);
                    }
                    if(data.startsWith("Polygon")){
                        String[] parts=data.split(",");
                        Double[] points=new Double[parts.length];
                        for (int i=0;i<(parts.length-1);i++){
                            if(i==0){
                                points[i]=Double.parseDouble(parts[i].substring(16,parts[i].length()));
                            }
                            else if(i<(parts.length-2)){
                                points[i]=Double.parseDouble(parts[i]);
                            }
                            else{
                                points[i]=Double.parseDouble(parts[i].substring(0,parts[i].length()-1));
                            }
                        }
                        MovingPolygon s=new MovingPolygon();
                        s.getPoints().addAll(points);
                        String color=parts[parts.length-1].substring(6,parts[parts.length-1].length()-1);
                        Color c = Color.web(color,1.0);
                        s.setFill(c);
                        shapes.getChildren().add(s);
                    }
            }
        });
        //Menu informacyjne
        Menu help_menu=new Menu("Help");
        MenuItem info=new MenuItem("Info");
        MenuItem help=new MenuItem("User Manual");
        help_menu.getItems().addAll(info,help);
        //Info
        Dialog<String> info_text = new Dialog<String>();
        info_text.setTitle("Info");
        info_text.setContentText("Edytor graficzny\nAutor: Mikolaj Poprawa");
        ButtonType close = new ButtonType("Ok", ButtonData.OK_DONE);
        info_text.getDialogPane().getButtonTypes().add(close);
        info.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                info_text.showAndWait();
            }
        });
        //Instrukcja
        Dialog<String> manual = new Dialog<String>();
        manual.setTitle("User Manual");
        manual.setContentText("1.Rysowanie\n-Narysuj kolo lub prostokat wybierajac je z menu shapes i przeciagajac mysz po ekranie\n-Zeby narysowac wielokat wybierz go z menu shapes zaznacz wierzcholki kliknieciem myszki i nacisnij prawy przycisk myszy zeby zakonczyc rysowanie\n2.Edycja\n-Mozesz przesuwac figury za pomoca myszki i zmieniac ich rozmiar za pomoca scrolla\n-Po wybraniu opcji rotate mozesz obracac figury kliknieciem myszki\n-Prawy przycisk obraca je w prawo, lewy w lewo, a nacisniecie scrolla wylacza obracanie\n-Wybierz kolor rysowanych figur z menu color\n-Po wybraniu opcji fill mozesz zmieniac kolor figur kliknieciem myszki\n-Nacisnij prawy przycisk myszy aby zakonczyc zmiane koloru\n3.Zapis\n-W menu save mozesz zapisywac figury i wczytywac je z plikow\n-Wpisz w pole tekstowe nazwe pliku do zapisu lub wczytania");
        manual.getDialogPane().getButtonTypes().add(close);
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                manual.showAndWait();
            }
        });
        
        MenuBar menu = new MenuBar(draw_menu,edit_menu,save_menu,help_menu);
        root.setCenter(shapes);
        root.setTop(menu);       
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setScene(new Scene(root, primaryScreenBounds.getWidth()*0.75, primaryScreenBounds.getHeight()*0.75));
        primaryStage.show();
    }
}