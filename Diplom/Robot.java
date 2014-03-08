package Diplom;

import java.awt.*;
import java.util.ArrayList;

public abstract class Robot implements Runnable
{
    //region Типы исполнения

    public interface RobotExecutionType
    {
        public void pickOrderByQueue(ArrayList<Order> queue);
    }
    public class RobotExecutionA implements RobotExecutionType
    {    /* Робот просматривает всю очередь, и выбирает себе наиболее подходящий по весу - грузовой берет более тяжелый, легковой берет более легкий.
          Если тяжелых не будет - то грузовой не возьмет ничего.
    */

        @Override
        public void pickOrderByQueue(ArrayList<Order> queue)
        {
            for (Order order : queue)
            {
                if (getCAPACITY()>order.getWeight() && order.getWeight()> Core.LightRobot.MAX_CAPACITY && type.equals("Грузовой"))
                {
                    pickFromTopQueue(order);
                    break;
                }
                else if (getCAPACITY()>order.getWeight() && order.getWeight()<=Core.LightRobot.MAX_CAPACITY && type.equals("Легковой"))
                {
                    pickFromTopQueue(order);
                    break;
                }
            }

        }
    }
    public class RobotExecutionB implements RobotExecutionType
    {
        @Override
        public void pickOrderByQueue(ArrayList<Order> queue)
        {
           //To change body of implemented methods use File | Settings | File Templates.
        }
    }
    //endregion

    private final RobotExecutionType EXECtype = new RobotExecutionA(); // здесь задаем тип исполнения
    private final int ID;
    private final String type;
    private final Point StoragePoint = DeicstraArea.getInstance().getCell((int)Places.STARTPOINT.getX(),(int)Places.STARTPOINT.getY()).getPosition();
    private int capacity;
    private boolean free=true;
    private Point EndPoint;
    private Point CurrentPoint=StoragePoint;
    private ArrayList<Order> myOrders = new ArrayList<Order>();

    public Robot(int ID, int CAPACITY, String type){   //сделать ENUM для типов
        this.ID=ID;
        this.capacity = CAPACITY;
        this.type=type;
    }
    //region Сеттеры
    private void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }
    private void setFree(boolean free)
    {
        this.free = free;
    }

    private void setEndPoint(Point endPoint)
    {
        EndPoint = endPoint;
    }

    private void setCurrentPoint(Point currentPoint)
    {
        CurrentPoint = currentPoint;
    }
    //endregion
    //region Геттеры
    private Point getCurrentPoint()
    {
        return CurrentPoint;
    }
    private Point getStoragePoint()
    {
        return StoragePoint;
    }
    private ArrayList<Order> getMyOrders()
    {
        return myOrders;
    }
    /*public Point getStartPoint()
    {
        return StoragePoint;
    }      */
    private String getType(){
        return type;
    }

    private int getID()
    {
        return ID;
    }

    private boolean isFree()
    {
        return free;
    }

    private int getCAPACITY()

    {
        return capacity;
    }

    //endregion

    @Override
    public void run()
    {
        while(!Core.isStopped)
        {
            ArrayList<Order> queue = Generator.getInstance().getOrderQueue();
            //region Робот свободен, взятие заказа
            if(isFree())
            { // System.out.println(String.format("%s Робот %s готов",getType(),getID()));//
                synchronized (queue)
                {
                if (!queue.isEmpty() && getCurrentPoint().equals(getStoragePoint()))
                {
                        try
                        {
                            Thread.sleep(1000); // задержка на взятие заказа
                        }
                        catch (InterruptedException ignore)
                        {}
                            try
                            {
                               EXECtype.pickOrderByQueue(queue);
                            }
                         catch (NullPointerException ignore){}
                    }
                }
            }
            //endregion
            //region Едет с заказом
            try
            {
                moveToDestinationWithOrder(EndPoint);
            }
            catch (NoWayException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //endregion
            //region Едет пустой домой
            try
            {   if (!getCurrentPoint().equals(getStoragePoint()))
                simpleMove(getStoragePoint());
            }
            catch (NoWayException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //endregion
        }
    }
    private void moveToDestinationWithOrder(Point endpoint) throws NoWayException, InterruptedException
    {
        if (endpoint ==null )   return;
        System.out.println(String.format("%s Робот %s c заказом %s поехал в точку назначения %s", getType(), getID(), getMyOrders().get(0).getID(), getMyOrders().get(0).getDestinationPoint()));
        ArrayList<Point> path = DeicstraArea.getInstance().findWay(StoragePoint,endpoint);
        Thread.sleep(path.size()*10); // скорость (точнее время езды до пункта назначения)
        // по смыслу тут надо еще сделать createZone на каждую точку, потом тред слип, и обычная  точка
        for (Point point : path){
            DeicstraArea.getInstance().getCell(point.x, point.y).setColor(Color.ORANGE);
        }
        setCurrentPoint(endpoint);
        setCapacity(getCAPACITY() + getMyOrders().get(0).getWeight());
        Generator.countOfCompleteOrders++;
        System.out.println(String.format("%s Робот %s c заказом %s приехал в точку назначения %s, стало ресурсов %s", getType(), getID(), getMyOrders().get(0).getID(), getMyOrders().get(0).getDestinationPoint(), getCAPACITY()));
        getMyOrders().remove(0);
        setFree(true);
        setEndPoint(getStoragePoint());
    }
    private void simpleMove(Point endpoint) throws NoWayException, InterruptedException
    {
        if (endpoint ==null )   return;
        System.out.println(String.format("%s Робот %s едет обратно на склад", getType(), getID()));
        ArrayList<Point> path = DeicstraArea.getInstance().findWay(CurrentPoint,endpoint);
        Thread.sleep(path.size()*10);
        System.out.println(String.format("%s Робот %s на складе", getType(), getID()));
        setCurrentPoint(getStoragePoint());
    }
    private void pickFromTopQueue(Order order){
        setEndPoint(order.getDestinationPoint()); //добавить установка дестинейшна (EndPoint List) если берем несколько заказов
        setCapacity(getCAPACITY() - order.getWeight());
        System.out.println(String.format("%s Робот %s взял заказ %s, приоритета %s, осталось ресурсов %s",getType(),getID(),order.getID(),order.getPriority(),getCAPACITY()));
        Generator.getInstance().getOrderQueue().remove(0);  //  забираем топ очереди
        getMyOrders().add(order);
        setFree(false);
    }
}
