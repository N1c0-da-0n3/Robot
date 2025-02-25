// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climb extends SubsystemBase {
  // FIXME; Climb is going to change in some way
  private CANSparkMax winch = new CANSparkMax(Constants.Climb.leftCANID, MotorType.kBrushless);
  private DoubleSolenoid solenoid =
      new DoubleSolenoid(
          Constants.pneumaticsControlModuleCANID, PneumaticsModuleType.CTREPCM, 4, 5);
  private boolean armExtended = false;
  private DigitalInput winchLimitLeft = new DigitalInput(Constants.Climb.winchLimitLeft);
  private DigitalInput winchLimitRight = new DigitalInput(Constants.Climb.winchLimitRight);

  /** Creates a new Climb. */
  public Climb() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (winchLimitLeft.get()) {
      stopWinch();
    }
  }

  public void release() {
    solenoid.set(Value.kReverse);
    armExtended = true;
  }

  public boolean winchLimitIsReached() {
    return winchLimitLeft.get();
  }

  public void runWinch() {
    if (armExtended) {
      winch.set(-0.4);
    }
  }

  // This stops the winch
  public void stopWinch() {
    winch.set(0);
  }
}
