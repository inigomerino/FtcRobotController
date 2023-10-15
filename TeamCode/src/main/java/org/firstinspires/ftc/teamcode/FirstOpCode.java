package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

public class FirstOpCode extends LinearOpMode {

    private DcMotor dtBL = null;

    double dtBLPwr = 0;

    public void runOpMode() {

        dtBL = hardwareMap.get(DcMotor.class, "test-motor1");

        dtBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dtBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        dtBL.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){

            dtBLPwr = 0.5;
            dtBL.setPower(dtBLPwr);
            telemetry.addData("dtBL Current Pwr:", dtBLPwr);
            telemetry.update();
        }
    }
}
