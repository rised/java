package Diplom;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Core {
    public static boolean isStopped = false;
    public static final ArrayList<Robot> robots = new ArrayList<Robot>();

    public static ArrayList<Robot> getRobots() {
        return robots;
    }

    public static TestClass repainting;
    public static int finish = 0;
    public static int mapVersionByWalls=0;
    private static final int workTime = 30000;

    public static void main(String[] args) throws InterruptedException, NoWayException, IOException {
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
       makeUnforseenWalls();
        Thread.sleep(workTime);   //общее время работы программы
        isStopped = true;
        System.out.println("Остановка модели...");
        Thread.sleep(10000);
        System.out.println(String.format("Поступило заказов %s, выполнено заказов %s, потенциальных столкновений %s",Generator.countOfInputOrders, Generator.countOfCompleteOrders, Generator.PotentialCollisions));
    }

    public static void makeUnforseenWalls() throws InterruptedException {
        Thread.sleep(4500);
        makeFromTo(160,180,140,192,TestClass.blackZoneCost);
        Thread.sleep(5000);
        makeFromTo(140,160,90,130,TestClass.blackZoneCost);
        Thread.sleep(5000);
        //makeFromTo(96,128,160,222,1);
        makeFromTo(160,192,160,192,TestClass.blackZoneCost);
        Thread.sleep(3000);
        makeFromTo(160,180,140,192,TestClass.whiteZoneCost);
        Thread.sleep(5000);
        makeFromTo(140,160,90,130,TestClass.whiteZoneCost);


    }
    private static void makeFromTo(int xFrom, int xTo, int yFrom, int yTo,int zoneCost){
        for (int i = xFrom; i < xTo; i++)
            for (int j = yFrom; j < yTo; j++) {
                if (zoneCost==TestClass.blackZoneCost)
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

        public LightRobot(int ID) throws IOException {
            super(ID, LightRobot.MAX_CAPACITY, "Легковой", energyPerMove, MAX_ENERGY, ImageIO.read(new File("Robot.png")));
        }
    }

    public static class CargoRobot extends Robot {
        final static int MAX_CAPACITY = 100;
        final static int MAX_ENERGY = 15000;
        final static int energyPerMove = 2;

        public CargoRobot(int ID) throws IOException {
            super(ID, CargoRobot.MAX_CAPACITY, "Грузовой", energyPerMove, MAX_ENERGY, ImageIO.read(new File("RobotCargo.png")));
        }
    }

}
