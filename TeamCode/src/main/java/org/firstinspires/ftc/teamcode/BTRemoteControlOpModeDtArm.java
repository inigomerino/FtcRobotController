/* FTC Bot Tanks, 2023
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTControlArmLiftInitializationException;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV1;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV3;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;

@TeleOp
public class BTRemoteControlOpModeDtArm extends BTRemoteControlOpMode {

  // members

  // you have to define this
  @Override
  protected BTIRobot makeRobot() throws BTRobotInitializationException {
    return new BT2023SeasonRobotV3();
  }

}
