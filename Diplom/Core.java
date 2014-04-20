package Diplom;


import java.awt.*;
import java.util.ArrayList;

public class Core {
    public static boolean isStopped = false;
    public static ArrayList<Robot> robots = new ArrayList<Robot>();
    public static TestClass repainting;
    public static int finish = 0;
    public static int mapVersionByWalls=0;

    public static void main(String[] args) throws InterruptedException, NoWayException {
        repainting = new TestClass();  //подключаем модуль отрисовки формы и АОП
        new SupportGUI();
        LightRobot lightRobot1 = new LightRobot(1);
        CargoRobot cargoRobot2 = new CargoRobot(2);
        LightRobot lightRobot3 = new LightRobot(3);
        CargoRobot cargoRobot4 = new CargoRobot(4);
        Thread robot1 = new Thread(lightRobot1);
        Thread robot2 = new Thread(cargoRobot2);
        Thread robot3 = new Thread(lightRobot3);
        Thread robot4 = new Thread(cargoRobot4);
        Thread generator = new Thread(Generator.getInstance());
        robots.add(lightRobot1);
        robots.add(cargoRobot2);
        robots.add(lightRobot3);
        robots.add(cargoRobot4);
        robot1.start();
        robot2.start();
        robot3.start();
        robot4.start();
        generator.start();
        //makeUnforseenWalls();
        Thread.sleep(15000);   //общее время работы программы
        isStopped = true;
        //while(Core.finish!=3){} //не работает!!!!
        System.out.println("Остановка модели...");
        Thread.sleep(10000);
        System.out.println(String.format("Выполнено заказов %s, потенциальных столкновений %s", Generator.countOfCompleteOrders, Generator.PotentialCollisions));
    }

    public static void makeUnforseenWalls() throws InterruptedException {
         int wallCount = 4;
         int zoneCost = 2;
        Thread.sleep(4500);
        makeFromTo(160,180,140,192,2);
        Thread.sleep(5000);
        makeFromTo(140,160,90,130,2);
        Thread.sleep(5000);
        //makeFromTo(96,128,160,222,1);
        makeFromTo(160,192,160,192,2);
        Thread.sleep(3000);
        makeFromTo(160,180,140,192,1);
        Thread.sleep(5000);
        makeFromTo(140,160,90,130,1);


    }
    private static void makeFromTo(int xFrom, int xTo, int yFrom, int yTo,int zoneCost){
        for (int i = xFrom; i < xTo; i++)
            for (int j = yFrom; j < yTo; j++) {
                if (zoneCost==2)
                DeicstraArea.getInstance().getCell(i, j).setColor(Color.BLACK);
                else
                DeicstraArea.getInstance().getCell(i, j).setColor(Color.WHITE);
                DeicstraArea.getInstance().getCell(i, j).setCost(zoneCost);
                Core.repainting.getCellsCosts()[i][j]=zoneCost;
            }
        mapVersionByWalls++;
    }

    public static class LightRobot extends Robot {
        final static int MAX_CAPACITY = 50;
        final static int MAX_ENERGY = 10000;
        final static int energyPerMove = 1;

        public LightRobot(int ID) {
            super(ID, LightRobot.MAX_CAPACITY, "Легковой", energyPerMove, MAX_ENERGY);
        }
    }

    public static class CargoRobot extends Robot {
        final static int MAX_CAPACITY = 100;
        final static int MAX_ENERGY = 15000;
        final static int energyPerMove = 2;

        public CargoRobot(int ID) {
            super(ID, CargoRobot.MAX_CAPACITY, "Грузовой", energyPerMove, MAX_ENERGY);
        }
    }

}
