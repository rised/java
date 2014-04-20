package Diplom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;
//import java.lang.*;

public class TestClass extends JFrame implements WindowListener {
    private JPanel panel2;

    /**
     * Creates a new instance of TestClass
     */
    public TestClass() {
        super();

        addWindowListener(this);
        setSize(900, 600);

        panel2 = new JPanel() {
            public void paintComponent(Graphics g) {
                DeicstraArea.getInstance().paint(g);
            }

        };

        getContentPane().add(panel2);

        createArea();
        createZone();
        DeicstraArea tempA = DeicstraArea.getInstance();  //вся площадь
        tempA.makeArea(this.cellsCosts);

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
    public void createZone() {    //задаем блоки непроходимых клеток
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

    public void ForIJarea(int xFrom, int xTo, int yFrom, int yTo) {
        //j высота, i ширина, начало координат лево верх
        for (int i = xFrom; i < xTo; i++)
            for (int j = yFrom; j < yTo; j++) {
                this.cellsCosts[i][j] = 2;         //zonecost=2
            }
    }
    //endregion

    public void createArea() {
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

    private int[][] cellsCosts = new int[width][heigth];

}

