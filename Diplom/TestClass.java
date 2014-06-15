package Diplom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
//import java.lang.*;

public class TestClass extends JFrame implements WindowListener {
    private JPanel panel2;

    public static int getBlackZoneCost() {
        return blackZoneCost;
    }

    public static int blackZoneCost = 10000;
    public static int whiteZoneCost = 1;

    public JPanel getPanel2() {
        return panel2;
    }

    /**
     * Creates a new instance of TestClass
     */
    public TestClass() throws IOException {
        super("Flexible robots");

        addWindowListener(this);
        setSize(900, 600);
        final Image imgA, imgStart, imgB, imgC, imgD, infoImg;
        imgA= ImageIO.read(new File("A.png"));
        imgStart = ImageIO.read(new File("START.png"));
        imgB= ImageIO.read(new File("B.png"));
        imgC= ImageIO.read(new File("C.png"));
        imgD= ImageIO.read(new File("D.png"));
        infoImg = ImageIO.read(new File("Info.png"));
        panel2 = new JPanel() {
            public void paintComponent(Graphics g) {
                DeicstraArea.getInstance().paint(g);
                g.drawImage(imgA,(int)(2*Places.POINTA.getX()),(int)(2*Places.POINTA.getY()),null);
                g.drawImage(imgB,(int)(2*Places.POINTB.getX()),(int)(2*Places.POINTB.getY()),null);
                g.drawImage(imgStart,(int)(2*Places.STARTPOINT.getX()),(int)(2*Places.STARTPOINT.getY()),null);
                g.drawImage(imgD,(int)(2*Places.POINTD.getX()),(int)(2*Places.POINTD.getY()),null);
                g.drawImage(imgC,(int)(2*Places.POINTC.getX()),(int)(2*Places.POINTC.getY()),null);
                g.drawImage(infoImg,510,50,null);
                //АНИМАЦИЯ ТЕЛЕГ ЗДЕСЬ! РЕПЕЙНТИНГ ВЫЗЫВАЕТ ЭТОТ МЕТОД!!! :D
                for (int i = 0; i <Core.getRobots().size() ; i++) {
                    g.drawImage(Core.getRobots().get(i).getRobotImage(),(int)(2*Core.getRobots().get(i).getCurrentPoint().getX()+10),(int)(2*Core.getRobots().get(i).getCurrentPoint().getY()+10),null);
                }
            }
        };

        getContentPane().add(panel2);
        createArea();
        createZone();
        DeicstraArea tempA = DeicstraArea.getInstance();  //вся площадь
        tempA.makeArea(this.cellsCosts);

        show();
    }

    public static int getWhiteZoneCost() {
        return whiteZoneCost;
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
    void createZone() {    //задаем блоки непроходимых клеток
        ForIJarea(15, 40, 15, 150);
        ForIJarea(40, 60, 15, 25);
        ForIJarea(60, 125, 25, 50);
        ForIJarea(100, 130, 100, 120);
        ForIJarea(180, 230, 100, 120);
        ForIJarea(43, 150, 180, 200);
        ForIJarea(230, 250, 180, 200);
        ForIJarea(100, 120, 160, 180);
        ForIJarea(100, 120, 120, 140);
        ForIJarea(20, 100, 230, 256);
        ForIJarea(60, 80, 15, 150);
        ForIJarea(200, 256, 25, 50);
    }

    void ForIJarea(int xFrom, int xTo, int yFrom, int yTo) {
        //j высота, i ширина, начало координат лево верх
        for (int i = xFrom; i < xTo; i++)
            for (int j = yFrom; j < yTo; j++) {
                this.cellsCosts[i][j] = blackZoneCost;         //zonecost=10000
            }
    }
    //endregion

    void createArea() {
        Random rand = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < heigth; j++) {
                this.cellsCosts[i][j] = 1;
                if (rand.nextInt(100) < 0) this.cellsCosts[i][j] = -this.cellsCosts[i][j];

            }
        }
    }

    private final int width = 256;
    private final int heigth = 256;

    public int[][] getCellsCosts() {
        return cellsCosts;
    }

    private final int[][] cellsCosts = new int[width][heigth];

}

