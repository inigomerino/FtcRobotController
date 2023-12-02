/* FTC BotTanks Code, 2023 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIDcMotor;

public class BTDcMotorWrapper implements BTIDcMotor {

    private DcMotorEx realMotor;

    public BTDcMotorWrapper(DcMotorEx motor) {
        this.realMotor = motor;
    }

    @Override
    public void setPower(double power) {
        this.realMotor.setPower(power);
    }

    @Override
    public double getPower() {
        return this.realMotor.getPower();
    }

    @Override
    public void setDirection(Direction direction) {
        DcMotorSimple.Direction motorDirection;

        // Convert the custom Direction type to the standard DcMotorSimple.Direction
        if (direction == org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIDcMotor.Direction.FORWARD) {
            motorDirection = DcMotorSimple.Direction.FORWARD;
        } else if (direction == org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIDcMotor.Direction.REVERSE) {
            motorDirection = DcMotorSimple.Direction.REVERSE;
        } else {
            // Handle other cases or throw an exception
            throw new IllegalArgumentException("Invalid motor direction");
        }

        // Set the direction of the real motor
        this.realMotor.setDirection(motorDirection);
    }

    @Override
    public BTIDcMotor.Direction getDirection() {
        DcMotorSimple.Direction realMotorDirection = this.realMotor.getDirection();

        // Convert the DcMotorSimple.Direction to BTIDcMotor.Direction
        if (realMotorDirection == DcMotorSimple.Direction.FORWARD) {
            return BTIDcMotor.Direction.FORWARD;
        } else if (realMotorDirection == DcMotorSimple.Direction.REVERSE) {
            return BTIDcMotor.Direction.REVERSE;
        } else {
            // Handle other cases or throw an exception
            throw new IllegalArgumentException("Unexpected motor direction");
        }
    }

    @Override
    public void setMode(BTIDcMotor.RunMode mode) {
        DcMotor.RunMode realMotorMode = this.convertToFTCRunMode(mode);
        this.realMotor.setMode(realMotorMode);
    }

    @Override
    public BTIDcMotor.RunMode getMode() {
        DcMotor.RunMode realMotorMode = this.realMotor.getMode();

        // Map DcMotor.RunMode to your custom RunMode
        return this.convertFromFTCRunMode(realMotorMode);
    }


    @Override
    public void setTargetPosition(int position) {
        this.realMotor.setTargetPosition(position);
    }

    @Override
    public int getTargetPosition() {
        return this.realMotor.getTargetPosition();
    }

    @Override
    public boolean isBusy() {
        return this.realMotor.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return this.realMotor.getCurrentPosition();
    }

    ////////////////// DcMotorEx ones //////////////////

    @Override
    public BTIDcMotor.MotorType getMotorType() {
        MotorConfigurationType motorConfig = this.realMotor.getMotorType();
    
        // Example criteria - adjust these based on the actual characteristics of your motors
        if (motorConfig.getMaxRPM() == 312.0 && motorConfig.getTicksPerRev() == 537.7) {
            return BTIDcMotor.MotorType.GOBILDA_5202_SERIES;
        } else if (motorConfig.getMaxRPM() == 0.0 && motorConfig.getTicksPerRev() == 2000.0) {
            return BTIDcMotor.MotorType.GOBILDA_ODOMETER_POD;
        } else {
            // Default case
            return BTIDcMotor.MotorType.OTHER;
        }
    }
    
    @Override
    public void setVelocity(double velocity) {
        realMotor.setVelocity(velocity);
    }

    @Override
    public double getVelocity() {
        return realMotor.getVelocity();
    }

    @Override
    public void setPIDFCoefficients(BTIDcMotor.RunMode mode, BTIDcMotor.PIDFCoefficients pidfCoefficients) {
        DcMotor.RunMode ftcRunMode = convertToFTCRunMode(mode);
        com.qualcomm.robotcore.hardware.PIDFCoefficients ftcPIDFCoefficients = convertToFTCPIDFCoefficients(pidfCoefficients);
        realMotor.setPIDFCoefficients(ftcRunMode, ftcPIDFCoefficients);
    }    

    @Override
    public BTIDcMotor.PIDFCoefficients getPIDFCoefficients(BTIDcMotor.RunMode mode) {
        DcMotor.RunMode ftcRunMode = convertToFTCRunMode(mode);
        com.qualcomm.robotcore.hardware.PIDFCoefficients ftcPIDFCoefficients = realMotor.getPIDFCoefficients(ftcRunMode);
        return new BTIDcMotor.PIDFCoefficients(ftcPIDFCoefficients.p, ftcPIDFCoefficients.i, ftcPIDFCoefficients.d, ftcPIDFCoefficients.f);
    }

    @Override
    public void setZeroPowerBehavior(BTIDcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        DcMotor.ZeroPowerBehavior ftcZeroPowerBehavior = convertToFTCZeroPowerBehavior(zeroPowerBehavior);
        realMotor.setZeroPowerBehavior(ftcZeroPowerBehavior);
    }
    
    @Override
    public BTIDcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        DcMotor.ZeroPowerBehavior ftcZeroPowerBehavior = realMotor.getZeroPowerBehavior();
        return convertToBTIZeroPowerBehavior(ftcZeroPowerBehavior);
    }
    
    /////////// conversion between BT and SDK types ///////////

    private DcMotor.ZeroPowerBehavior convertToFTCZeroPowerBehavior(BTIDcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        switch (zeroPowerBehavior) {
            case BRAKE:
                return DcMotor.ZeroPowerBehavior.BRAKE;
            case FLOAT:
                return DcMotor.ZeroPowerBehavior.FLOAT;
            default:
                throw new IllegalArgumentException("Unknown ZeroPowerBehavior: " + zeroPowerBehavior);
        }
    }

    private BTIDcMotor.ZeroPowerBehavior convertToBTIZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        switch (zeroPowerBehavior) {
            case BRAKE:
                return BTIDcMotor.ZeroPowerBehavior.BRAKE;
            case FLOAT:
                return BTIDcMotor.ZeroPowerBehavior.FLOAT;
            default:
                throw new IllegalArgumentException("Unknown ZeroPowerBehavior: " + zeroPowerBehavior);
        }
    }
    
    private com.qualcomm.robotcore.hardware.PIDFCoefficients convertToFTCPIDFCoefficients(BTIDcMotor.PIDFCoefficients pidfCoefficients) {
        return new com.qualcomm.robotcore.hardware.PIDFCoefficients(
            pidfCoefficients.getP(),
            pidfCoefficients.getI(),
            pidfCoefficients.getD(),
            pidfCoefficients.getF()
        );
    }
    

    private DcMotor.RunMode convertToFTCRunMode(BTIDcMotor.RunMode mode) {
        switch (mode) {
            case RUN_WITHOUT_ENCODER:
                return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
            case RUN_USING_ENCODER:
                return DcMotor.RunMode.RUN_USING_ENCODER;
            case RUN_TO_POSITION:
                return DcMotor.RunMode.RUN_TO_POSITION;
            case STOP_AND_RESET_ENCODER:
                return DcMotor.RunMode.STOP_AND_RESET_ENCODER;
            default:
                throw new IllegalArgumentException("Unknown RunMode: " + mode);
        }
    }

    private BTIDcMotor.RunMode convertFromFTCRunMode(DcMotor.RunMode mode) {
        switch (mode) {
            case RUN_WITHOUT_ENCODER:
                return BTIDcMotor.RunMode.RUN_WITHOUT_ENCODER;
            case RUN_USING_ENCODER:
                return BTIDcMotor.RunMode.RUN_USING_ENCODER;
            case RUN_TO_POSITION:
                return BTIDcMotor.RunMode.RUN_TO_POSITION;
            case STOP_AND_RESET_ENCODER:
                return BTIDcMotor.RunMode.STOP_AND_RESET_ENCODER;
            default:
                throw new IllegalArgumentException("Unknown RunMode: " + mode);
        }
    }

}

