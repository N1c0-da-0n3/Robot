// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.AbsoluteDrive;
import frc.robot.commands.PrimeIndex;
import frc.robot.commands.ToggleIntake;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.SwerveSubsystem;
import java.io.File;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  private final SwerveSubsystem m_swerve =
      new SwerveSubsystem(new File(Filesystem.getDeployDirectory(), "swerve"));
  private final Intake m_intake = new Intake();
  private final Index m_index = new Index();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final XboxController driverXbox = new XboxController(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    AbsoluteDrive closedAbsoluteDrive =
        new AbsoluteDrive(
            m_swerve,
            // Applies deadbands and inverts controls because joysticks
            // are back-right positive while robot
            // controls are front-left positive
            () -> MathUtil.applyDeadband(driverXbox.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
            () -> MathUtil.applyDeadband(driverXbox.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
            () -> driverXbox.getRightX(),
            () -> -driverXbox.getRightY());

    m_swerve.setDefaultCommand(
        !RobotBase.isSimulation() ? closedAbsoluteDrive : closedAbsoluteDrive);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    JoystickButton leftBumper =
        new JoystickButton(driverXbox, XboxController.Button.kLeftBumper.value);
    leftBumper.onTrue(new ToggleIntake(m_intake));

    JoystickButton rightBumper =
        new JoystickButton(driverXbox, XboxController.Button.kRightBumper.value);
    rightBumper.onTrue(new PrimeIndex(m_index));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_swerve.getAutonomousCommand("Test Drive", true);
  }

  public void setDriveMode() {
    // drivebase.setDefaultCommand();
  }

  public void setMotorBrake(boolean brake) {
    m_swerve.setMotorBrake(brake);
  }
}
