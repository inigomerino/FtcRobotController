package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.libs.exceptions.BTException;

import java.util.List;


@TeleOp(name="Raw Intake Test", group="Tests")
public class BTRawIntakeTestOpMode extends LinearOpMode {

    private StringBuilder telemetryLog;
    private String currentReading;
    private long testDurationSeconds = 15;
    private DcMotor[] motors = new DcMotor[2];

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

    private void runMotor(String name, int pos, double speed) {

        try {

            // notify
            this.printUpdate("Testing Motor '" + name + "' with " + speed + "%power ");

            // get it
            DcMotor m = hardwareMap.get(DcMotor.class, name);

            // add it
            this.motors[pos] = m;

            // run
            m.setPower(speed);

        } catch (Exception e) {
            this.printUpdate("Unknown error while testing motor '" + name + "': " + e);
        }
    }

    void runMotors(double speed, long seconds) {

        // do it
//        this.runMotor("intake1", 0, 0.0-speed);
        this.runMotor("intake2", 1, speed);

        // Run motors for a few seconds
        sleep(seconds * 1000);

        // Stop the motor
        for( DcMotor m : this.motors ) {
            m.setPower(0);
        }

        // done
        this.printUpdate("Test complete");
    }


    @Override
    public void runOpMode() {

        try {

            // init the log
            telemetryLog = new StringBuilder();
            currentReading = new String();

            // done
            this.printUpdate("Initialization complete");
            this.printUpdate("*** NOTE: Place robot in a safe position ***");

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run tests
            this.runMotors(1.0, 30);

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
