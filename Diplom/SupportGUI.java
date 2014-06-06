package Diplom;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class SupportGUI extends JFrame implements WindowListener {


    private JButton increaseSpeed;
    private JButton addCargo;
    private JButton decreaseSpeed;
    private JButton btnclose;
    private JTextField currentSpeed;
    private JButton addLight;
    private JButton showCountOfComplete;
    private JButton showInputOrders;
    private JButton decreaseCapacity;
    private JButton increaseCapacity;
    private final JLabel completeOrders = new JLabel();
    private final JLabel numberOfLight=new JLabel();
    private final JLabel numberOfCargo = new JLabel();
    private final JLabel numberOfInputOrders = new JLabel();
    private static int startNumberOfRobots = 4;
    private static int numberOfLightRobots = 2;
    private static int numberOfCargoRobots = 2;

    public  SupportGUI(){
        super("Managing");

        addWindowListener(this);
        setSize(900, 200);
        setVisible(true);

        JPanel panel = new JPanel();

        decreaseCapacity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        increaseCapacity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        increaseSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Robot.setSpeed(Robot.getSpeed() - 1);
                currentSpeed.setText("Текущая скорость: " + String.valueOf((double) (100 / Robot.getSpeed())));
            }
        });
        decreaseSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Robot.setSpeed(Robot.getSpeed() + 1);
                currentSpeed.setText("Текущая скорость: " + String.valueOf((double) (100 / Robot.getSpeed())));
            }
        });
        addCargo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Core.CargoRobot cargoRobot1 = null;
                try {
                    cargoRobot1 = new Core.CargoRobot(startNumberOfRobots);
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                Thread robot1 = new Thread(cargoRobot1);
                Core.robots.add(cargoRobot1);
                robot1.start();
                startNumberOfRobots++;
                System.out.println("Добавлен грузовой робот " + startNumberOfRobots);
                numberOfCargoRobots++;
                numberOfCargo.setText("Грузовых: " + String.valueOf(numberOfCargoRobots));
            }
        });
        addLight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Core.LightRobot lightRobot1 = null;
                try {
                    lightRobot1 = new Core.LightRobot(startNumberOfRobots);
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                Thread robot1 = new Thread(lightRobot1);
                Core.robots.add(lightRobot1);
                robot1.start();
                startNumberOfRobots++;
                System.out.println("Добавлен легковой робот " + startNumberOfRobots);
                numberOfLightRobots++;
                numberOfLight.setText("Легковых роботов: " + String.valueOf(numberOfLightRobots));

            }
        });
        showCountOfComplete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeOrders.setText("Заказов выполнено: " + String.valueOf(Generator.countOfCompleteOrders));
            }
        });
        showInputOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOfInputOrders.setText("Заказов поступило: " + String.valueOf(Generator.countOfInputOrders));

            }
        });
        panel.add(showCountOfComplete);
        panel.add(btnclose);
        panel.add(increaseSpeed);
        panel.add(currentSpeed);
        panel.add(decreaseSpeed);
        panel.add(addLight);
        panel.add(addCargo);
        panel.add(numberOfLight);
        panel.add(numberOfCargo);
        panel.add(completeOrders);
        panel.add(showInputOrders);
        panel.add(numberOfInputOrders);
        panel.add(decreaseCapacity);
        panel.add(increaseCapacity);


        getContentPane().add(panel);
        show();
    }
    @Override
    public void windowOpened(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    {
    }

}
