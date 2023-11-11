/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTHardwareFactory;
import org.firstinspires.ftc.teamcode.libs.subsystems.BTTwoWheelRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;

/*
 * Remote Control OpCode
 */
@TeleOp
public class BTRandomTwoWheelOpMode extends OpMode {

  // members
  private ElapsedTime runtime;
  private BTTwoWheelRobot robot;

  /**
   * This method will be called once, when the INIT button is pressed.
   */
  @Override
  public void init() {
    
    try{

      // notify
      telemetry.addData("Status", "Initializing");

      // init hwMap factory -- has to be done b4 init'ing robot
      BTHardwareMapProvider prov = new BTHardwareMapProvider(this.hardwareMap, this.gamepad1, this.gamepad2 );
      BTHardwareFactory.getInstance().initProvider( prov );

      // init members
      this.runtime = new ElapsedTime();
      this.robot = new BTTwoWheelRobot();

      // notify
      telemetry.addData("Status", "Initialized");

    } catch (BTRobotInitializationException e) {
        telemetry.addData("Status", "Initialization failed: " + e.getStackTraceAsString());
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

      // notify
      telemetry.addData("Status", "Run Time: " + runtime.toString());

//    } catch (BTException e) {
//
//      // over here, should we try to re-initialize and re-start?
//      // error-recovery would be nice ...
//      // for now, just report the error
//
//      // notify
//      telemetry.addData("Status", "Error: " + e.getStackTraceAsString());

    } catch (Exception e) {

      // over here, should we try to re-initialize and re-start?
      // error-recovery would be nice ...
      // for now, just report the error

      // notify
      telemetry.addData("Status", "Unidentifier error: " + e);

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
