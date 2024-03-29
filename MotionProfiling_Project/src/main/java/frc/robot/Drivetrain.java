package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is the drive, chassis, subsystem.
 */
public class Drivetrain extends Subsystem {

  private SpeedControllerGroup leftGroup, rightGroup;
  private DifferentialDrive driveTrain;
  private ADXRS450_Gyro gyro;
  private Encoder leftEncoder, rightEncoder;
  private double prevTime = 0, leftAcceleration = 0, rightAcceleration = 0,
   prevLeftVelocity = 0, prevRightVelocity = 0;
  
  public Drivetrain(){
    super();
    this.rightGroup = new SpeedControllerGroup(new WPI_TalonSRX(RobotMap.CAN.REAR_RIGHT_MOTOR),
        new WPI_TalonSRX(RobotMap.CAN.FRONT_RIGHT_MOTOR));
    this.leftGroup = new SpeedControllerGroup(new WPI_TalonSRX(RobotMap.CAN.REAR_LEFT_MOTOR),
        new WPI_TalonSRX(RobotMap.CAN.FRONT_LEFT_MOTOR));
    this.driveTrain = new DifferentialDrive(this.leftGroup, this.rightGroup);
    // this.gyro = new ADXRS450_Gyro();
    this.gyro = new ADXRS450_Gyro();
    this.leftEncoder = new Encoder(RobotMap.DIO.DRIVE_TRAIN_LEFT_ENCODER_CHANNEL_A,
        RobotMap.DIO.DRIVE_TRAIN_LEFT_ENCODER_CHANNEL_B);
    this.rightEncoder = new Encoder(RobotMap.DIO.DRIVE_TRAIN_RIGHT_ENCODER_CHANNEL_A,
        RobotMap.DIO.DRIVE_TRAIN_RIGHT_ENCODER_CHANNEL_B);

    this.leftEncoder.setDistancePerPulse(RobotConstants.TICKS_PER_METER_LEFT);
    this.rightEncoder.setDistancePerPulse(RobotConstants.TICKS_PER_METER_RIGHT);

    this.leftEncoder.setReverseDirection(true);
    this.rightEncoder.setReverseDirection(false);

  }

  public void tankDrive(double leftSpeed, double rightSpeed) {
    this.driveTrain.tankDrive(leftSpeed, rightSpeed);
  }

  public void arcadeDrive(double x, double y) {
    this.driveTrain.arcadeDrive(y, x);
  }

  public double getAngle() {
    return this.gyro.getAngle();
  }

  public double getLeftDistance() {
    return this.leftEncoder.getDistance();
  }

  public double getRightDistance() {
    return this.rightEncoder.getDistance();
  }

  /** Gets the distance in ticks the right motor has driven */
  public int getRightTicks() {
    return this.rightEncoder.get();
  }

  /** Gets the average distance in meters the robot has driven */
  public double getAverageDistance() {
    return (getLeftDistance() + getRightDistance()) / 2;
  }

  public double getRightVelocity() {
    return rightEncoder.getRate();
  }

  public double getLeftVelocity() {
    return leftEncoder.getRate();
  }

  public double getRightAcceleration() {
    return this.rightAcceleration;
  }

  public double getLeftAcceleration() {
    return this.leftAcceleration;
  }

  public void resetEncoders() {
    this.leftEncoder.reset();
    this.rightEncoder.reset();
  }

  public void resetGyro() {
    this.gyro.reset();
  }

  public void calibrateGyro() {
    this.gyro.calibrate();
  }

  public int getLeftTicks(){
    return this.leftEncoder.get();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveArcade(Robot.oi::getJoystickX, Robot.oi::getJoystickY));
  }

  /** we calculate the acceleration of the robot as well as its velocity*/
  @Override
  public void periodic() {
    double currentTime = Timer.getFPGATimestamp();

    this.leftAcceleration = (getLeftVelocity() - prevLeftVelocity) / (currentTime - prevTime);
    this.rightAcceleration = (getRightVelocity() - prevRightVelocity) / (currentTime - prevTime);

    this.prevLeftVelocity = getLeftVelocity();
    this.prevRightVelocity = getRightVelocity();

    this.prevTime = currentTime;
  }
}

