package Diplom;

import javax.swing.*;
import java.awt.event.*;

public class SupportGUI extends JFrame implements WindowListener {


    private JButton increaseSpeed;
    private JButton addCargo;
    private JButton decreaseSpeed;
    private JButton btnclose;
    private JTextField currentSpeed;
    private JButton addLight;
    private JPanel panel;
    private JLabel numberOfLight=new JLabel();
    private JLabel numberOfCargo = new JLabel();
    public static int startNumberOfRobots = 4;
    public static int numberOfLightRobots = 2;
    public static int numberOfCargoRobots = 2;

    public  SupportGUI(){
        super();

        addWindowListener(this);
        setSize(600, 600);
        show();

        panel=new JPanel();
        btnclose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        getContentPane().add(panel);
        increaseSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Robot.setSpeed(Robot.getSpeed() - 1);
                currentSpeed.setText(String.valueOf((double) (100 / Robot.getSpeed())));
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
                Core.CargoRobot cargoRobot1 = new Core.CargoRobot(startNumberOfRobots);
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
                Core.LightRobot lightRobot1 = new Core.LightRobot(startNumberOfRobots);
                Thread robot1 = new Thread(lightRobot1);
                Core.robots.add(lightRobot1);
                robot1.start();
                startNumberOfRobots++;
                System.out.println("Добавлен легковой робот " + startNumberOfRobots);
                numberOfLightRobots++;
                numberOfLight.setText("Легковых роботов: " + String.valueOf(numberOfLightRobots));
            }
        });

        panel.add(btnclose);
        panel.add(increaseSpeed);
        panel.add(currentSpeed);
        panel.add(decreaseSpeed);
        panel.add(addLight);
        panel.add(addCargo);
        panel.add(numberOfLight);
        panel.add(numberOfCargo);
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
