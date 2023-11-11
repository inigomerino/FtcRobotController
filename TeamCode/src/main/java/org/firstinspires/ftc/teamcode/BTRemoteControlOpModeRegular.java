/* FTC Bot Tanks, 2023
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libs.subsystems.BTRobot;
import org.firstinspires.ftc.teamcode.libs.subsystems.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;

/*
 * Remote Control OpCode
 */
@TeleOp
public class BTRemoteControlOpModeRegular extends BTRemoteControlOpMode {

  // members

  // you have to define this
  @Override
  protected BTIRobot makeRobot() throws BTRobotInitializationException {
    return new BTRobot();
  }

}
