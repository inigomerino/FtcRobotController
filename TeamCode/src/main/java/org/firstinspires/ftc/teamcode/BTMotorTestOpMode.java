package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="BTMotorTestOpMode", group="Linear Opmode")
public class BTMotorTestOpMode extends LinearOpMode {

    // Declare motor variables
    private DcMotor dtFR, dtFL, dtBR, dtBL;

    @Override
    public void runOpMode() {
        // Initialize motors
        dtFR = hardwareMap.get(DcMotor.class, "dtFR");
        dtFL = hardwareMap.get(DcMotor.class, "dtFL");
        dtBR = hardwareMap.get(DcMotor.class, "dtBR");
        dtBL = hardwareMap.get(DcMotor.class, "dtBL");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Array of motors for easy iteration
        DcMotor[] motors = {dtFR, dtFL, dtBR, dtBL};
        String[] motorNames = {"dtFR", "dtFL", "dtBR", "dtBL"};

        // Iterate over each motor
        for (int i = 0; i < motors.length; i++) {
            telemetry.addData("Running Motor", motorNames[i]);
            telemetry.update();

            // Set motor power
            motors[i].setPower(0.5);

            // Run motor for a few seconds
            sleep(10000);

            // Stop the motor
            motors[i].setPower(0);

            // Short delay before the next motor starts
            sleep(2000);
        }

        telemetry.addData("Status", "All Motors Tested");
        telemetry.update();
    }
}
