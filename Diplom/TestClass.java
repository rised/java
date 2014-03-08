package Diplom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;
//import java.lang.*;

public class TestClass extends JFrame implements WindowListener{

    private JButton closeButton = new JButton("Close");
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
        ArrayList<Point> tempAL;  //массив точек от начала до конца

        //region Задание начальных и конечных точек
        CustomCell StartPoint = tempA.getCell((int)Places.STARTPOINT.getX(),(int) Places.STARTPOINT.getY());
        CustomCell PointA = tempA.getCell((int)Places.POINTA.getX(),(int) Places.POINTA.getY());
        CustomCell PointB = tempA.getCell((int)Places.POINTB.getX(),(int) Places.POINTB.getY());
        CustomCell PointC = tempA.getCell((int)Places.POINTC.getX(),(int) Places.POINTC.getY());
        StartPoint.setColor(Color.CYAN);
        PointA.setColor(Color.red);
        PointB.setColor(Color.red);
        PointC.setColor(Color.red);
        //endregion

        //region Нахождение пути и его покраска
        /*
        try {

            tempAL = tempA.findWay(StartPoint.getPosition(), PointA.getPosition());
            tempAL = tempA.optimizeWay(tempAL);
            for (Point point : tempAL){
                tempA.getCell(point.x, point.y).setColor(Color.GREEN);
            }
        } catch (NoWayException ex) {
            JOptionPane.showMessageDialog(this
                    ,"Путь 1 не найден!","Внимание"
                    ,JOptionPane.INFORMATION_MESSAGE);
        }

        try {
             //второй путь
            tempAL = tempA.findWay(StartPoint.getPosition(), PointB.getPosition());
            tempAL = tempA.optimizeWay(tempAL);  // ходит по диагонали (не вверх в сторону)
            for (Point point : tempAL){
                tempA.getCell(point.x, point.y).setColor(Color.YELLOW);
            }
        } catch (NoWayException ex) {
            JOptionPane.showMessageDialog(this
                    ,"Путь 2 не найден!","Внимание"
                    ,JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            //третий путь
            tempAL = tempA.findWay(StartPoint.getPosition(), PointC.getPosition());
            tempAL = tempA.optimizeWay(tempAL);
            for (Point point : tempAL){
                tempA.getCell(point.x, point.y).setColor(Color.BLUE);
            }
        } catch (NoWayException ex) {
            JOptionPane.showMessageDialog(this
                    ,"Путь 3 не найден!","Внимание"
                    ,JOptionPane.INFORMATION_MESSAGE);
        }     */
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
        ForIJarea(43,230,180,200,zoneCost);
        ForIJarea(100,230,100,120,zoneCost);
    }
    public void ForIJarea(int iFrom, int iTo, int jFrom, int jTo, int zoneCost){
       //j высота, i ширина, начало координат лево верх
        for(int i=iFrom; i<iTo; i++)
            for(int j=jFrom; j<jTo; j++){
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

    private int[][] area = new int[width][heigth];
}

