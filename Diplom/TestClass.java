package Diplom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;
//import java.lang.*;

public class TestClass extends JFrame implements WindowListener{

    private JPanel panel1;

    /** Creates a new instance of TestClass */
    public TestClass() {
        super();

        addWindowListener(this);
        setSize(600,600);

        panel1 = new JPanel(){
            public void paintComponent(Graphics g){
                DeicstraArea.getInstance().paint(g);
            }
        }  ;


        getContentPane().add(panel1);
        createArea();
        createZone(2);
        DeicstraArea tempA = DeicstraArea.getInstance();  //вся площадь
        tempA.makeArea(this.area);

        //region Задание начальных и конечных точек
        CustomCell StartPoint = tempA.getCell((int)Places.STARTPOINT.getX(),(int) Places.STARTPOINT.getY());
        CustomCell PointA = tempA.getCell((int)Places.POINTA.getX(),(int) Places.POINTA.getY());
        CustomCell PointB = tempA.getCell((int)Places.POINTB.getX(),(int) Places.POINTB.getY());
        CustomCell PointC = tempA.getCell((int)Places.POINTC.getX(),(int) Places.POINTC.getY());
        StartPoint.setColor(Color.CYAN);
        PointA.setColor(Color.BLACK);
        PointB.setColor(Color.BLACK);
        PointC.setColor(Color.BLACK);
        //endregion



//

        show();
    }

    //region Всякие методы
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
    //endregion


    //region Создание стен
    public void createZone(int zoneCost){    //задаем блоки непроходимых клеток
        ForIJarea(15,40,15,150,zoneCost);
        ForIJarea(40,60,15,25,zoneCost);
        ForIJarea(60,125,25,50,zoneCost);
        ForIJarea(100,130,100,120,zoneCost);
        ForIJarea(180,230,100,120,zoneCost);
        ForIJarea(43,150,180,200,zoneCost);
        ForIJarea(230,250,180,200,zoneCost);
        ForIJarea(100,120,160,180,zoneCost);
        ForIJarea(100,120,120,140,zoneCost);
        ForIJarea(20,100,230,256,zoneCost);
        ForIJarea(60,80,15,150,zoneCost);
        ForIJarea(200,256,25,50,zoneCost);
    }
    public void ForIJarea(int xFrom, int xTo, int yFrom, int yTo, int zoneCost){
       //j высота, i ширина, начало координат лево верх
        for(int i=xFrom; i<xTo; i++)
            for(int j=yFrom; j<yTo; j++){
                this.area[i][j] = zoneCost;
            }
    }
    //endregion

    public void createArea(){
        Random rand = new Random();
        for(int i=0; i<width; i++){
            for(int j=0; j<heigth; j++){
                this.area[i][j] = 1;
                if (rand.nextInt(100)<0) this.area[i][j] = - this.area[i][j];
            }
        }
    }

    private final int width = 256;
    private final int heigth = 256;

    public int[][] getArea() {
        return area;
    }

    private int[][] area = new int[width][heigth];
}

