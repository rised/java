package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public enum Places
{;

    private static final int heigth=256;
    private static final int weigth=256;
    public static final Point STARTPOINT = DeicstraArea.getInstance().getCell(2, heigth - 2).getPosition();
    public static final Point POINTA = DeicstraArea.getInstance().getCell(weigth - 40, 2).getPosition();
    public static final Point POINTB = DeicstraArea.getInstance().getCell(weigth-50,weigth-100).getPosition();
    public static final Point POINTC = DeicstraArea.getInstance().getCell(weigth-150,heigth-200).getPosition();
    public static final HashMap<Point,String> pointStringHashMap = new HashMap<Point,String>();

    private static final List<Point> VALUES = new ArrayList<Point>();
    static{
        pointStringHashMap.put(POINTA,"Точка А");
        pointStringHashMap.put(POINTB,"Точка Б");
        pointStringHashMap.put(POINTC,"Точка С");
        pointStringHashMap.put(STARTPOINT,"Склад");
        VALUES.add(POINTA);
        VALUES.add(POINTB);
        VALUES.add(POINTC);
    }
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Point randomPlace()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
