/* FTC BotTanks Code, 2023 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.libs.math.BTVector;
import org.firstinspires.ftc.teamcode.libs.sdk_interface.hardware.BTIGamepad;

public class BTGamepadWrapper implements BTIGamepad {

    private Gamepad realGamepad;

    public BTGamepadWrapper(Gamepad gamepad) {
        this.realGamepad = gamepad;
    }

    // Button implementations
    @Override
    public boolean isAButtonPressed() {
        return realGamepad.a;
    }

    @Override
    public boolean isBButtonPressed() {
        return realGamepad.b;
    }

    @Override
    public boolean isXButtonPressed() {
        return realGamepad.x;
    }

    @Override
    public boolean isYButtonPressed() {
        return realGamepad.y;
    }

    @Override
    public boolean isLeftBumperPressed() {
        return realGamepad.left_bumper;
    }

    @Override
    public boolean isRightBumperPressed() {
        return realGamepad.right_bumper;
    }

    @Override
    public boolean isLeftStickButtonPressed() {
        return realGamepad.left_stick_button;
    }

    @Override
    public boolean isRightStickButtonPressed() {
        return realGamepad.right_stick_button;
    }

    @Override
    public boolean isDpadUpPressed() {
        return realGamepad.dpad_up;
    }

    @Override
    public boolean isDpadDownPressed() {
        return realGamepad.dpad_down;
    }

    @Override
    public boolean isDpadLeftPressed() {
        return realGamepad.dpad_left;
    }

    @Override
    public boolean isDpadRightPressed() {
        return realGamepad.dpad_right;
    }

    @Override
    public boolean isBackButtonPressed() {
        return realGamepad.back;
    }

    @Override
    public boolean isStartButtonPressed() {
        return realGamepad.start;
    }

    @Override
    public boolean isGuideButtonPressed() {
        // 'Guide' button is not available in all Gamepad implementations.
        // If it's not available, return false or handle it according to your requirements.
        return false;
    }

    // Joystick implementations
    @Override
    public float getLeftStickX() {
        return realGamepad.left_stick_x;
    }

    @Override
    public float getLeftStickY() {
        return realGamepad.left_stick_y;
    }

    @Override
    public float getRightStickX() {
        return realGamepad.right_stick_x;
    }

    @Override
    public float getRightStickY() {
        return realGamepad.right_stick_y;
    }

    // Trigger implementations
    @Override
    public float getLeftTriggerValue() {
        return realGamepad.left_trigger;
    }

    @Override
    public float getRightTriggerValue() {
        return realGamepad.right_trigger;
    }

    // controller interface items
    @Override
    public BTVector getDriveVector() {
        return new BTVector(this.getLeftStickX(), this.getLeftStickY());
    }

    @Override
    public BTVector getRotationVector() {
        return new BTVector(this.getRightStickX(), 0.0);
    }
}
