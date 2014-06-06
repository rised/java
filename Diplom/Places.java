package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public enum Places {
    ;

    private static final int heigth = 256;
    private static final int width = 256;
    public static final Point STARTPOINT = DeicstraArea.getInstance().getCell(2, heigth - 2).getPosition();
    public static final Point POINTA = DeicstraArea.getInstance().getCell(width - 40, 2).getPosition();
    public static final Point POINTB = DeicstraArea.getInstance().getCell(width - 50, width - 100).getPosition();
    public static final Point POINTC = DeicstraArea.getInstance().getCell(width - 150, heigth - 200).getPosition();
    static final Point POINTD = DeicstraArea.getInstance().getCell(width - 20, heigth - 35).getPosition();
    public static final HashMap<Point, String> pointStringHashMap = new HashMap<Point, String>();


    private static final List<Point> VALUES = new ArrayList<Point>();

    static {
        pointStringHashMap.put(POINTA, "Точка А");
        pointStringHashMap.put(POINTB, "Точка B");
        pointStringHashMap.put(POINTC, "Точка С");
        pointStringHashMap.put(POINTD, "Точка D");
        pointStringHashMap.put(STARTPOINT, "Склад");
        VALUES.add(POINTA);
        VALUES.add(POINTB);
        VALUES.add(POINTC);
        VALUES.add(POINTD);
        DeicstraArea tempA = DeicstraArea.getInstance();  //вся площадь
        //region Задание начальных и конечных точек
        CustomCell StartPoint = tempA.getCell((int) Places.STARTPOINT.getX(), (int) Places.STARTPOINT.getY());
        CustomCell PointA = tempA.getCell((int) Places.POINTA.getX(), (int) Places.POINTA.getY());
        CustomCell PointB = tempA.getCell((int) Places.POINTB.getX(), (int) Places.POINTB.getY());
        CustomCell PointC = tempA.getCell((int) Places.POINTC.getX(), (int) Places.POINTC.getY());
        CustomCell PointD = tempA.getCell((int) Places.POINTD.getX(), (int) Places.POINTD.getY());
        StartPoint.setColor(Color.CYAN);
        PointA.setColor(Color.BLACK);
        PointB.setColor(Color.BLACK);
        PointC.setColor(Color.BLACK);
        PointD.setColor(Color.BLACK);
    }

    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Point randomPlace() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
