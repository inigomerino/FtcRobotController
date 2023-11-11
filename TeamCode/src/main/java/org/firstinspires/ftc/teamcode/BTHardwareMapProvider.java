package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.libs.sdk_interface.BTIHardwareProvider;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIDcMotor;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTDcMotorWrapper;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIGamepad;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTGamepadWrapper;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIServo;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTServoWrapper;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTHardwareNotFoundException;

public class BTHardwareMapProvider implements BTIHardwareProvider {

    //---------- members ---------- 
    // this is where we will be dynamically loading junk from
    private final HardwareMap hwMap;
    private final BTIGamepad gamepad1;
    private final BTIGamepad gamepad2;
    
    public BTHardwareMapProvider(HardwareMap hwMap) {
        this.hwMap = hwMap;
        this.gamepad1 = null;
        this.gamepad2 = null;
    }

    public BTHardwareMapProvider(HardwareMap hwMap, Gamepad gamepad1, Gamepad gamepad2) {
        this.hwMap = hwMap;
        this.gamepad1 = new BTGamepadWrapper(gamepad1);
        this.gamepad2 = new BTGamepadWrapper(gamepad2);
    }

    //---------- methods ---------- 

    @Override
    public BTIGamepad getGamepad(String name) throws BTHardwareNotFoundException {

        // gamepad 1
        if ( name.equals("gamepad1") ) {
            
            // see if it has been initialzied or not
            if (this.gamepad1 == null) {
                throw new BTHardwareNotFoundException("Gamepad1 has not been initialized");
            }

            // if we are here, we have a gamepad to return
            return this.gamepad1;
        }

        // gamepad 2
        if ( name.equals("gamepad2") ) {
            
            // see if it has been initialzied or not
            if (this.gamepad2 == null) {
                throw new BTHardwareNotFoundException("Gamepad2 has not been initialized");
            }

            // if we are here, we have a gamepad to return
            return this.gamepad2;
        }

        throw new BTHardwareNotFoundException("Name '" + name + "' is not valid for gamepads");
    }

    @Override
    public BTIDcMotor getMotor(String name) throws BTHardwareNotFoundException {
        
        // get it
        DcMotor hw = this.hwMap.get(DcMotor.class, name);

        // check it
        if (hw == null) {
            throw new BTHardwareNotFoundException("DC Motor '" + name + "' not found");
        }

        // done
        return new BTDcMotorWrapper(hw);
    }

    @Override
    public BTIServo getServo(String name) throws BTHardwareNotFoundException {
        
        // get it
        Servo hw = this.hwMap.get(Servo.class, name);

        // check it
        if (hw == null) {
            throw new BTHardwareNotFoundException("Servo '" + name + "' not found");
        }

        // done
        return new BTServoWrapper(hw);
    }
    
}