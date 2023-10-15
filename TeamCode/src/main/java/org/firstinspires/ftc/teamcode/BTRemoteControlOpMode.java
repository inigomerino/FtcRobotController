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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.math.BTVector;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTDcGamepadStub;
import org.firstinspires.ftc.teamcode.libs.subsystems.drive_train.BTDriveTrain;
import org.firstinspires.ftc.teamcode.libs.subsystems.active_intake.BTActiveIntake;

/*
 * Remote Control OpCode
 */
@TeleOp
public class BTRemoteControlOpMode extends OpMode {

  // members
  private ElapsedTime runtime;
  private BTDcGamepadStub gamepad1;
  private BTDriveTrain driveTrain;
  private BTActiveIntake activeIntake;

  /**
   * This method will be called once, when the INIT button is pressed.
   */
  @Override
  public void init() {
    
    // notify
    telemetry.addData("Status", "Initializing");

    // init members
    this.runtime = new ElapsedTime();
    this.gamepad1 = new BTDcGamepadStub();
    this.driveTrain = new BTDriveTrain();
    this.activeIntake = new BTActiveIntake();

    // notify
    telemetry.addData("Status", "Initialized");

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

    // do some stuff
    this.runActiveIntake();
    this.runEndOfArm();
    this.runDriveTrain();

    // notify
    telemetry.addData("Status", "Run Time: " + runtime.toString());

  }

  public void runActiveIntake() {

    // ============== Active Intake Code ============== //
    boolean do_intake = gamepad1.a;
    boolean do_expel  = gamepad1.b;

    // decide which way to go
    if (do_intake) {

      // start motor at full speed
      this.activeIntake.intake(1.0);

    } else if (do_expel) {

      // start motor at full speed
      this.activeIntake.expel(1.0);

    } else if (do_expel && do_intake) {

      // stop motor
      this.activeIntake.stop();

    }
  }

  public void runEndOfArm() {

    // ============== End Of Arm Tool Code ============== //

  }

  public void runDriveTrain() {

    // ============== Drive Train Code ============== //
    BTVector g1_movement_vector = new BTVector(gamepad1.left_stick_x,  gamepad1.left_stick_y);
    BTVector g1_rotation_vector = new BTVector(gamepad1.right_stick_x, gamepad1.right_stick_y);

    // tell drivetrain to use these numbers
    this.driveTrain.move(g1_movement_vector, g1_rotation_vector);
  }

  /**
   * This method will be called once, when this OpMode is stopped.
   * <p>
   * Your ability to control hardware from this method will be limited.
   */
  @Override
  public void stop() {

    // stop driving
    this.driveTrain.stop();

    // stop intake
    this.activeIntake.stop();

  }

}
