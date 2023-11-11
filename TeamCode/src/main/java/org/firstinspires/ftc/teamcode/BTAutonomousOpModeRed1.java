package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.libs.subsystems.BTRobotAutoPilotRed1;
import org.firstinspires.ftc.teamcode.libs.subsystems.BTRobotAutoPilot;
import org.firstinspires.ftc.teamcode.libs.subsystems.BTIRobot;
import org.firstinspires.ftc.teamcode.libs.exceptions.BTRobotInitializationException;

@Autonomous()
public class BTAutonomousOpModeRed1 extends BTAutonomousOpMode {
    
    // you have to define this
    @Override
    protected BTRobotAutoPilot makeRobot() throws BTRobotInitializationException {
        return new BTRobotAutoPilotRed1();
    }

}


