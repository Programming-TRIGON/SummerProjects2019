package frc.robot;

import java.io.File;
import java.util.function.Supplier;
import com.spikes2212.dashboard.ConstantHandler;
import com.spikes2212.dashboard.DashBoardController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.CalibrateKa;
import frc.robot.commands.CalibrateKv;
import frc.robot.commands.CalibrateMaxSpeed;
import frc.robot.motionprofile.FollowPath;
import frc.robot.motionprofile.Path;
import frc.robot.motionprofile.PathCreater;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static DashBoardController dbc;
  public static Drivetrain drivetrain;
  public static OI oi;
  public static PathCreater pathCreater;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    Robot.drivetrain = new Drivetrain();

    Robot.dbc = new DashBoardController();

    dbc.addNumber("Left encoder", Robot.drivetrain::getLeftDistance);
    dbc.addNumber("Right encoder", Robot.drivetrain::getRightDistance);
    dbc.addNumber("Both encoders", Robot.drivetrain::getAverageDistance);
    dbc.addNumber("Gyro angle", Robot.drivetrain::getAngle);
    dbc.addNumber("Right velocity", Robot.drivetrain::getRightVelocity);
    dbc.addNumber("Left velocity", Robot.drivetrain::getLeftVelocity);
    dbc.addNumber("Right acceleration", Robot.drivetrain::getRightAcceleration);
    dbc.addNumber("left acceleration", Robot.drivetrain::getLeftAcceleration);
    dbc.addNumber("left ticks", Robot.drivetrain::getLeftTicks);
    dbc.addNumber("right ticks", Robot.drivetrain::getRightTicks);

    SmartDashboard.putData("test path", new FollowPath(Path.SCALE));
    SmartDashboard.putData("test jaci", new FollowPath(Path.TEST_JACI));
    SmartDashboard.putData("test csv reader", new FollowPath(Path.TEST));
    SmartDashboard.putData("Max speed Kv forward", new CalibrateMaxSpeed(false));
    SmartDashboard.putData("Max speed Kv Reversed", new CalibrateMaxSpeed(true));  
    SmartDashboard.putData("test ka",
        new CalibrateKa(RobotConstants.Calibration.leftForwardKv, RobotConstants.Calibration.rightForwardKv,
            RobotConstants.Calibration.leftForwardVi, RobotConstants.Calibration.rightForwardVi, false));
    Supplier<Double> voltageSupplier = ConstantHandler.addConstantDouble("voltage start", 0.45);
    InstantCommand reset = new InstantCommand(Robot.drivetrain::resetEncoders);
    reset.setRunWhenDisabled(true);
    SmartDashboard.putData("reset", reset);
    SmartDashboard.putData("test kv", new CalibrateKv(false, voltageSupplier));
    
    Robot.pathCreater = new PathCreater();
    Robot.oi = new OI();
  }

  @Override
  public void robotPeriodic() {
    Robot.dbc.update();

  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      break;
    }
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testPeriodic() {
  }
}