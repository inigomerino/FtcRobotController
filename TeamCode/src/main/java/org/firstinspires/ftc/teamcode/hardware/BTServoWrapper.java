/* FTC BotTanks Code, 2023 */

package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIServo;
import com.qualcomm.robotcore.hardware.Servo;

public class BTServoWrapper implements BTIServo {

    private Servo realServo;

    public BTServoWrapper(Servo servo) {
        this.realServo = servo;
    }

    @Override
    public void setPosition(double position) {
        // Directly set the position without a try-catch block.
        // If you need to handle specific exceptions, you can add them back.
        realServo.setPosition(position);
    }

    @Override
    public double getPosition() {
        // Directly return the position.
        return realServo.getPosition();
    }

    @Override
    public void setDirection(BTIServo.Direction direction) {
        Servo.Direction ftcDirection = convertToFTCDirection(direction);
        realServo.setDirection(ftcDirection);
    }
    
    @Override
    public BTIServo.Direction getDirection() {
        Servo.Direction ftcDirection = realServo.getDirection();
        return convertToBTIDirection(ftcDirection);
    }    

    /////////////// conversion to SDK /////////////
    private Servo.Direction convertToFTCDirection(BTIServo.Direction direction) {
        switch (direction) {
            case FORWARD:
                return Servo.Direction.FORWARD;
            case REVERSE:
                return Servo.Direction.REVERSE;
            default:
                throw new IllegalArgumentException("Unknown Direction: " + direction);
        }
    }

    private BTIServo.Direction convertToBTIDirection(Servo.Direction direction) {
        switch (direction) {
            case FORWARD:
                return BTIServo.Direction.FORWARD;
            case REVERSE:
                return BTIServo.Direction.REVERSE;
            default:
                throw new IllegalArgumentException("Unknown Direction: " + direction);
        }
    }
    
}
