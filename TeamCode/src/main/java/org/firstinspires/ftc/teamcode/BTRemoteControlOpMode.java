/* FTC Bot Tanks, 2023
 * 
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.BTHardwareMapProvider;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;
import org.firstinspires.ftc.teamcode.libs.robots.BTIRobot;

@TeleOp
public abstract class BTRemoteControlOpMode extends OpMode {

  // members
  private ElapsedTime runtime;
  private BTIRobot robot;

  // you have to define this
  protected abstract BTIRobot makeRobot() throws BTRobotInitializationException;

  /**
   * This method will be called once, when the INIT button is pressed.
   */
  @Override
  public void init() {
    
    try{

      // notify
      telemetry.addData("BTStat", "Initializing");

      // init hwMap factory -- has to be done b4 init'ing robot
      BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
      BTHardwareFactory.getInstance().initProvider( prov );

      // init members
      this.runtime = new ElapsedTime();
      this.robot = this.makeRobot();

      // notify
      telemetry.addData("BTStat", " Robot built");
      telemetry.addData("BTStat", "Initialized");

    } catch (BTRobotInitializationException e) {
        telemetry.addData("BTStat", "Initialization failed: " + e.getStackTraceAsString());
    }

  }

  /**
   * This method will be called repeatedly during the period between when
   * the init button is pressed and when the play button is pressed (or the
   * OpMode is stopped).
   */
  @Override
  public void init_loop() {
  }

  /**
   * This method will be called once, when the play button is pressed.
   */
  @Override
  public void start() {
    runtime.reset();
  }

  /**
   * This method will be called repeatedly during the period between when
   * the play button is pressed and when the OpMode is stopped.
   */
  @Override
  public void loop() {

    try {

      // do some stuff
      this.robot.loop();

      // debug
      telemetry.addData("Gamepad1 Left Stick X", gamepad1.left_stick_x);
      telemetry.addData("Gamepad1 A Button", gamepad1.a);
      telemetry.addData("Gamepad1 B Button", gamepad1.b);

      // notify
      telemetry.addData("BTStat", "Run Time: " + runtime.toString());

//    } catch (BTException e) {
//
//      // over here, should we try to re-initialize and re-start?
//      // error-recovery would be nice ...
//      // for now, just report the error
//
//      // notify
//      telemetry.addData("BTStat", "Error: " + e.getStackTraceAsString());

    } catch (Exception e) {

      // over here, should we try to re-initialize and re-start?
      // error-recovery would be nice ...
      // for now, just report the error

      // notify
      telemetry.addData("BTStat", "Unidentifier error: " + e);

    }

  }

  /**
   * This method will be called once, when this OpMode is stopped.
   * <p>
   * Your ability to control hardware from this method will be limited.
   */
  @Override
  public void stop() {

    // stop robot
    this.robot.stop();

  }

}
