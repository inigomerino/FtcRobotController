package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;
import org.firstinspires.ftc.teamcode.libs.math.BTLocation;
import org.firstinspires.ftc.teamcode.libs.robots.BT2023SeasonRobotV2;

import java.util.List;


@TeleOp(name="Positioning Test", group="Tests")
public class BTPositioningTestOpMode extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private long testDurationSeconds = 15;
    private BT2023SeasonRobotV2 robot;

    ////////////////////////////// telemetry logic //////////////////////////////
    private void resfreshDisplay() {
        // notify
        telemetry.addData("BTStat", telemetryLog.toString());
        telemetry.addData("Reading", currentReading);
        telemetry.update();
    }

    private void printUpdate(String message) {

        // add it
        telemetryLog.append(message).append("\n");

        // refresh
        this.resfreshDisplay();
    }

    private void updateReading(String reading) {

        // do it
        currentReading = reading;

        // refresh
        this.resfreshDisplay();
    }

    private void commitReading() {
        this.printUpdate(this.currentReading);
        this.updateReading("(none)");
    }
    ////////////////////////////// end of telemetry logic //////////////////////////////

    private void measure() {

        // notify
        this.printUpdate("Testing odometers. Each odometer will be reading for " + this.testDurationSeconds + " seconds. Confirm behavior and values printed are as expected.");

        while (opModeIsActive()) {

            this.robot.loop();
            BTLocation curPos = this.robot.getCurrentLocation();
            this.updateReading("Position: '" + curPos.toString() );
            // this.commitReading();

        }

        // done
        this.printUpdate("Odometer test complete");
    }


    @Override
    public void runOpMode() {

        try {

            // init the log
            telemetryLog = new StringBuilder();
            currentReading = new String();
            robot = new BT2023SeasonRobotV2();  // this will be at the origin

            // done
            this.printUpdate("********************************************************");
            this.printUpdate("*** Place robot at (0,0) and re-initialize if needed ***");
            this.printUpdate("********************************************************");
            this.printUpdate("Initialization complete");
            

            // Wait for the game to start (driver presses PLAY)
            waitForStart();
            this.printUpdate("Move robot to desired location to get a reading");

            // run tests
            this.measure();

        } catch (Exception e) {
            this.printUpdate("Unknown error in main loop: " + e);
        } finally {
            this.printUpdate("Suggested troubleshooting actions: " +
                    "Check robot configuration in the driver station. " +
                    "Drivetrain motor placement on control hub:  'dtFL' -> 0, 'dtFR' -> 1, 'dtBL' -> 2, 'dtBR -> 3" +
                    "Check that the failed elements are correctly connecter to the appropriate ports in the control station. " +
                    "Check that the opMode initialized the hardware map properly. " +
                    "Check that motors, wheels and other mechanisms are rotating freely and not rubbing. ");
        }
    }

}
