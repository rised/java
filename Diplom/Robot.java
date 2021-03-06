package Diplom;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public abstract class Robot implements Runnable {
    private final RobotExecutionType EXECtype = new RobotExecutionC(); // здесь задаем тип исполнения
    //region Типы исполнения

    public interface RobotExecutionType {
        public void pickOrderByQueue();
    }

    public class RobotExecutionA implements RobotExecutionType {    /* Робот просматривает всю очередь, и выбирает себе наиболее подходящий по весу - грузовой берет более тяжелый, легковой берет более легкий.
          Если тяжелых не будет - то грузовой не возьмет ничего.
          запускать с GeneratorA
    */

        @Override
        public void pickOrderByQueue() {
            for (Order order : queue) {
                if (getCAPACITY() >= order.getWeight() && order.getWeight() > Core.LightRobot.MAX_CAPACITY && getType().equals("Грузовой")) {
                    pickOrderFromTopQueue(order);
                   break;
                } else if (getCAPACITY() >= order.getWeight() && order.getWeight() <= Core.LightRobot.MAX_CAPACITY && getType().equals("Легковой")) {
                    pickOrderFromTopQueue(order);
                    break;
                }
            }

        }
    }

    public class RobotExecutionB implements RobotExecutionType {
        @Override
        public void pickOrderByQueue()
            /*FIFO+robottype*/ {

            ArrayList<Robot> freeRobots = new ArrayList<Robot>();
            System.out.println("Обзор свободных роботов на складе...");
            for (Robot robot : Core.robots) {
                if (robot.isFree() && robot.getCurrentPoint().equals(getStoragePoint())) {
                    freeRobots.add(robot);
                    System.out.println(String.format("Робот номер %s свободен", robot.getID()));
                }
            }
            if (freeRobots.size() == 1) {
                // System.out.println(String.format("Свободен только я - номер %s",getID()));
                for (int i = 0; i < queue.size(); i++) {
                    if (getCAPACITY() >= queue.get(i).getWeight()) {
                        pickOrderFromTopQueue(queue.get(i));
                        i--;
                    }
                }
            } else {     //здесь исходный поток может управлять другим потоком

                for (int j = 0; j < queue.size(); j++) {

                    for (Robot freeRobot : freeRobots) {
                        if (freeRobot.getCAPACITY() >= queue.get(j).getWeight() && queue.get(j).getWeight() > Core.LightRobot.MAX_CAPACITY && freeRobot.getType().equals("Грузовой")) {
                            if (getID()!=freeRobot.getID())
                                System.out.println(String.format("Робот %s передает сообщение: Робот %s бери заказ %s.", getID(), freeRobot.getID(), queue.get(j).getID()));
                            freeRobot.pickOrderFromTopQueue(queue.get(j));
                            j--;

                        } else if (freeRobot.getCAPACITY() >= queue.get(j).getWeight() && queue.get(j).getWeight() <= Core.LightRobot.MAX_CAPACITY && freeRobot.getType().equals("Легковой")) {
                            if (getID()!=freeRobot.getID())
                            System.out.println(String.format("Робот %s передает сообщение: Робот %s бери заказ %s",getID(),freeRobot.getID(),queue.get(j).getID()));
                            freeRobot.pickOrderFromTopQueue(queue.get(j));
                            j--;
                        }

                    }
                }
            }

        }
    }

    public class RobotExecutionC implements RobotExecutionType {
        @Override
        public void pickOrderByQueue()
            /* запускать с GeneratorA
            *Оптимизация-2 Роботы берут из очереди несколько заказов для разных получателей   (+развозить должны по задаче о комивояжере)
            *робот берет из очереди несколько заказов, Максимально загружаясь, втупую пытается взять подряд топ очереди
            */ {
            for (int i = 0; i < queue.size(); i++) {
                if (getCAPACITY() >= queue.get(i).getWeight()) {
                    pickOrderFromTopQueue(queue.get(i));
                    i--;
                }
            }
        }
    }
    //endregion


    /**
     * @param path - кратчайший путь из всех точек, которые необходимо дойти из одной точки в другую
     * @param multipath - список точек, которые нужно посетить роботу в случае если он взял больше одного заказа.
     */
    private final int ID;
    private Image robotImage;
    private final String type;
    private final Point StoragePoint = DeicstraArea.getInstance().getCell((int) Places.STARTPOINT.getX(), (int) Places.STARTPOINT.getY()).getPosition();
    private int capacity;
    private final int energyRatePerMove;
    private int energy;
    private boolean free = true;
    private static volatile ArrayList<Order> queue = Generator.getOrderQueue();
    private Point EndPoint;
    private Point CurrentPoint = StoragePoint;
    private final ArrayList<Order> orders = new ArrayList<Order>();
    private ArrayList<Point> path;
    private ArrayList<Point> multipath;
    private int mapVersion=1;
    private static int speed = 15;
    public Robot(int ID, int CAPACITY, String type, int energyPerMove, int energy, Image robotImage) {
        this.ID = ID;
        this.capacity = CAPACITY;
        this.type = type;
        this.energy = energy;
        this.energyRatePerMove = energyPerMove;
        this.robotImage=robotImage;
    }

    //region Сеттеры
    private void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private void setFree(boolean free) {
        this.free = free;
    }

    private void setEndPoint(Point endPoint) {
        EndPoint = endPoint;
    }

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {

        Robot.speed = speed;
    }

    private void setCurrentPoint(Point currentPoint) {
        CurrentPoint = currentPoint;
    }
    //endregion
    //region Геттеры
    public Image getRobotImage() {
        return robotImage;
    }
    int getEnergy() {
        return energy;
    }

    protected Point getCurrentPoint() {
        return CurrentPoint;
    }

    private Point getStoragePoint() {
        return StoragePoint;
    }

    private ArrayList<Order> getOrders() {
        return orders;
    }

    private String getType() {
        return type;
    }

    private int getID() {
        return ID;
    }

    private boolean isFree() {
        return free;
    }

    private int getCAPACITY()

    {
        return capacity;
    }

    //endregion

    @Override
    public void run() {
        while (!Core.isStopped) {

            //region Если робот свободен и на находится на складе, то взять заказ
            if (isFree())   // в EXECB один поток может поменять состояние другого, поэтому это условие очень удачно работает здесь
            { // одновременно доступ к очереди имеет только один робот - ложь
                synchronized (queue) {
                    if (!queue.isEmpty() && getCurrentPoint().equals(getStoragePoint())) {
                        try {
                            Thread.sleep(1000); // задержка на взятие заказа
                            EXECtype.pickOrderByQueue();
                        } catch (Exception e) {
                           // System.out.println("pickorder: " + e.getCause());
                        }

                    }

                }
            }

            //endregion
            createMultipathByTSPNN();
            if (orders.size() != 0) {
                //region Едет с заказом
                try {
                    //moveToDestinationWithOrder(EndPoint);
                    moveToDestinationWithOrder(multipath);
                } catch (Exception e) {
                   // System.out.println("moveToDest" + e.getMessage());
                }
                //endregion
                //region Едет пустой домой
                try {
                    if (!getCurrentPoint().equals(getStoragePoint()))
                        moveToStorage();
                } catch (Exception e) {
                   // System.out.println("simplemove:" + e.getMessage());
                }
                //endregion
            }
        }
        Core.finish++;
    }

    private void moveToDestinationWithOrder(Point endpoint) throws NoWayException, InterruptedException {
        //единственная точка назначения
        if (endpoint == null) return;
        System.out.println(String.format("%s Робот %s c заказом %s поехал в точку назначения %s", getType(), getID(), getOrders().get(0).getID(), Places.pointStringHashMap.get(getOrders().get(0).getDestinationPoint())));
        path = DeicstraArea.getInstance().findWay(endpoint, StoragePoint);// тут на самом деле переменные наоброт
        DeicstraArea.getInstance().optimizeWay(path);
        paintPath(path);
        setCurrentPoint(endpoint);
        setCapacity(getCAPACITY() + getOrders().get(0).getWeight());
        Generator.countOfCompleteOrders++;
        System.out.println(String.format("%s Робот %s c заказом %s приехал в точку назначения %s, стало ресурсов %s", getType(), getID(), getOrders().get(0).getID(), Places.pointStringHashMap.get(getOrders().get(0).getDestinationPoint()), getCAPACITY()));
        getOrders().remove(0);
        setFree(true);
        setEndPoint(getStoragePoint());
    }

    private void moveToDestinationWithOrder(ArrayList<Point> multipath) throws NoWayException, InterruptedException {
        if (multipath == null) return;
        for (int i = 0; i < multipath.size(); i++) {
            EndPoint = multipath.get(i);
            System.out.println(String.format("%s Робот %s c заказами поехал в точку назначения %s", getType(), getID(), Places.pointStringHashMap.get(EndPoint)));
            path = DeicstraArea.getInstance().findWay(EndPoint, CurrentPoint);
            DeicstraArea.getInstance().optimizeWay(path);
            paintPath(path);
            setCurrentPoint(EndPoint);
            System.out.println(String.format("%s Робот %s c заказоми приехал в точку назначения %s", getType(), getID(), Places.pointStringHashMap.get(EndPoint)));
            for (int j = 0; j < orders.size(); j++) {   //смотрим какие заказы надо отдать
                if (CurrentPoint.equals(orders.get(j).getDestinationPoint())) {
                    setCapacity(getCAPACITY() + getOrders().get(j).getWeight());
                    System.out.println(String.format("%s Робот %s в %s отдал заказ %s, стало ресурсов %s", getType(), getID(), Places.pointStringHashMap.get(orders.get(j).getDestinationPoint()), orders.get(j).getID(), getCAPACITY()));
                    orders.remove(j);
                    j--;
                    Generator.countOfCompleteOrders++;
                }
            }
            if (i != (multipath.size() - 1))
                System.out.println(String.format("%s Робот %s едет в следующую точку", getType(), getID()));

        }
        setFree(true);
        setEndPoint(getStoragePoint());


    }

    private void moveToStorage() throws NoWayException, InterruptedException {
        System.out.println(String.format("%s Робот %s едет обратно на склад", getType(), getID()));
        path = DeicstraArea.getInstance().findWay(StoragePoint, CurrentPoint);  //точки стоят наоборот чтобы правильно рисовало
        DeicstraArea.getInstance().optimizeWay(path);
        paintPath(path);
        System.out.println(String.format("%s Робот %s на складе, осталось энергии %S", getType(), getID(), getEnergy()));
        setCurrentPoint(getStoragePoint());
    }

    private void pickOrderFromTopQueue(Order order) {
        setEndPoint(order.getDestinationPoint());
        setCapacity(getCAPACITY() - order.getWeight());
        System.out.println(String.format("%s Робот %s взял заказ %s, приоритета %s, осталось ресурсов %s", getType(), getID(), order.getID(), order.getPriority(), getCAPACITY()));
        Generator.getInstance().getOrderQueue().remove(0);
        getOrders().add(order);
        setFree(false);
    }

    //region Colors
    enum Colors {
        ;
        private static final ArrayList<Color> VALUES = new ArrayList<Color>();

        static {
            VALUES.add(Color.red);
            VALUES.add(Color.blue);
            VALUES.add(Color.CYAN);
            VALUES.add(Color.ORANGE);
            VALUES.add(Color.YELLOW);
            VALUES.add(Color.MAGENTA);
            VALUES.add(new Color(27,88,24));
            VALUES.add(new Color(48, 104, 144));


        }

        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Color randomColor() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    public Point getEndPoint() {
        return EndPoint;
    }

    //endregion
    void paintPath(ArrayList<Point> path) throws InterruptedException, NoWayException {
        Color bodyColor = Colors.randomColor();
        Color headColor = Colors.randomColor();

        for (Point point : path) {
            int decrementX, decrementY;
            if (Core.mapVersionByWalls < mapVersion) {
                if (Core.repainting.getCellsCosts()[point.x][point.y] == TestClass.blackZoneCost) {
                    System.out.println(String.format("Потенциальное столкновение. Робот %s ждет", getID()));

                   //возможная реализация избежания столкновений: всего может быть 8 различных ситуаций, (по аналогии со звездой в DeicstraArea.
                //   )  Для этих ситуаций будут различные декременты по каждой из осей - 1, 0, -1. Тут надо дописывать код.

                    /*
                    *   1) х2>x1 y2>y1
                        decrx+1 decry=0, decrx=0 decry=1
                        2) x2<x1 y2>y1
                        decrx=-1 decry =0, decrx=0 decry =1
                        3) x2<x1 y2<y1 decrx=-1 decry=0, decrx=0 decry=-1
                        4) x2>x1 y2<y1 decrx=1 decry=0, decrx=0 decry=-1
                        5) x2=x1 y2>y1 decrx=1 decry=1, decrx=-1 decry=1
                        6) x2=x1 y2<y1 decrx=-1 decry=-1, decrx=1 decry=-1
                        7) y2=y1 x2>x1 decrx=1 decry=1, decrx=1 decry=-1
                        8) y2=y1 x2<x1 decrx=-1 decry=1, decrx=-1 decry=-1
                        Далее из каждых двух точек выбираем первую свободную и отступаем в нее
                        Если свободную точку не удалось найти, то отступаем на шаг назад, и повторяем весь алгоритм
                        */

                    if (this.getEndPoint().getX()-point.getX()>0) decrementX=1;
                    else decrementX=-1;
                    if (this.getEndPoint().getY()-point.getY()>0) decrementY=1;
                    else decrementY=-1;
                    if (this.getEndPoint().getX()-point.getX()==0) decrementX=0;
                    else decrementX=-1;
                    if (this.getEndPoint().getY()-point.getY()==0) decrementY=0;
                    else decrementY=-1;
                    Point pointGoTo = new Point(point.x+decrementX,point.y+decrementY);
                    setCurrentPoint(pointGoTo);
                    Thread.sleep((long)(speed/0.75));
                    System.out.println(String.format("Робот %s продолжает движение", getID()));
                    Generator.PotentialCollisions++;

                    DeicstraArea.getInstance().getCell(pointGoTo.x, pointGoTo.y).setColor(bodyColor);
                    Core.repainting.repaint();
                    DeicstraArea.getInstance().getCell(pointGoTo.x, pointGoTo.y).setCost(TestClass.blackZoneCost);    //клетка стала занятой
                    Core.repainting.getCellsCosts()[pointGoTo.x][pointGoTo.y] = TestClass.blackZoneCost;
                    Thread.sleep(speed);
                    energy = getEnergy() - energyRatePerMove;
                    DeicstraArea.getInstance().getCell(pointGoTo.x, pointGoTo.y).setCost(TestClass.whiteZoneCost);   // освободилась
                    Core.repainting.getCellsCosts()[pointGoTo.x][pointGoTo.y] = TestClass.whiteZoneCost;
                    DeicstraArea.getInstance().getCell(pointGoTo.x, pointGoTo.y).setColor(headColor);
                }
                else {
                setCurrentPoint(point);
                DeicstraArea.getInstance().getCell(point.x, point.y).setColor(bodyColor);
                Core.repainting.repaint();
                DeicstraArea.getInstance().getCell(point.x, point.y).setCost(TestClass.blackZoneCost);    //клетка стала занятой
                Core.repainting.getCellsCosts()[point.x][point.y] = TestClass.blackZoneCost;
                Thread.sleep(speed);
                energy = getEnergy() - energyRatePerMove;
                DeicstraArea.getInstance().getCell(point.x, point.y).setCost(TestClass.whiteZoneCost);   // освободилась
                Core.repainting.getCellsCosts()[point.x][point.y] = TestClass.whiteZoneCost;
                DeicstraArea.getInstance().getCell(point.x, point.y).setColor(headColor);
                }
            }
            else  {
                mapVersion++;
                System.out.println("Зафиксировано непредвиденное препятствие на маршруте следования...");
                paintPath(DeicstraArea.getInstance().findWay(EndPoint, CurrentPoint));

            break;
            }
        }
        /*try{
        for (int i =0;i<path.size();i++)
        {
            setCurrentPoint(path.get(i));
            if (Core.repainting.getCellsCosts()[path.get(i+1).x][path.get(i+1).y]==2)
            {
                 paintPath(DeicstraArea.getInstance().findWay(CurrentPoint,EndPoint));
                 break;
            }
            DeicstraArea.getInstance().getCell(path.get(i).x, path.get(i).y).setColor(bodyColor);
            Core.repainting.repaint();
            Core.repainting.getCellsCosts()[path.get(i).x][path.get(i).y]=2;    //клетка стала занятой
            Thread.sleep(30);
            Core.repainting.getCellsCosts()[path.get(i).x][path.get(i).y]=1;   // освободилась
        }      }
        catch (Exception e){
            System.out.println("Paintpath косяк" + e.getMessage());
        }   */ //тут сквозь стены не ездит, но ошибка блеать в алгортме поиска пути!!!

    }

    ArrayList<Point> buildUniqueListByOrders() {
        //метод находит в списке заказов робота уникальные точки, которые необходимо посетить для задачи комммивояжера
        ArrayList<Point> points = new ArrayList<Point>();
        for (Order order : orders) {
            points.add(order.getDestinationPoint());
        }
        HashSet<Point> hashSet = new HashSet<Point>();
        for (Point point : points) {
            hashSet.add(point);
        }
        ArrayList<Point> TSPpoints = new ArrayList<Point>();
        TSPpoints.addAll(hashSet);
        // System.out.println(set.size());   //сколько уникальных точек в списке заказов
        return TSPpoints;
    }

    private void createMultipathByTSPNN() {
        if (orders.size() > 0) {
            ArrayList<Point> TSPpoints = buildUniqueListByOrders();  // получаем точки которые нужно посетить
            try {
                TSPNN TSP = new TSPNN(TSPpoints);
                multipath = TSP.getPoints();
            } catch (Exception e) {
                System.out.println("TSPNN:" + e.getMessage());
            }
        }
    }

}
