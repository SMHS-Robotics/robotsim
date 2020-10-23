/**
 * Represents the robot object
 * Can be manipulated using instance methods
 * BotAPI contains the methods that are shared between Hardware and Simulator robots
 */
abstract class BotAPI {
    /**
     * Power of the front left motor.
     * Accepts values between -1.0 (full speed backward) and 1.0 (full speed forward)
     */
    var leftMotorPower: Double = 0.0

    /**
     * Power of the right front motor.
     * Accepts values between -1.0 (full speed backward) and 1.0 (full speed forward).
     */
    var rightMotorPower: Double = 0.0

    /**
     * Power of the right front motor.
     * Accepts values between -1.0 (full speed backward) and 1.0 (full speed forward).
     */
    var rightRearMotorPower: Double = 0.0

    /**
     * Power of the right front motor.
     * Accepts values between -1.0 (full speed backward) and 1.0 (full speed forward).
     */
    var leftRearMotorPower: Double = 0.0

    /**
     * Current angle.
     * Vertical pointed up is 0.
     * Horizontal pointed left is 270.
     * Horizontal pointed right is 90.
     * Vertical pointed down is 180.
     */
    var currentAngle: Double = 0.0

    /**
     * Individually sets the speed for each of the 4 motors.
     *
     * @param leftFront The power of the front left motor, between -1.0 and 1.0
     * @param rightFront The power of the front right motor, between -1.0 and 1.0
     * @param leftBack The power of the back left motor, between -1.0 and 1.0
     * @param rightBack The power of the back right motor, between -1.0 and 1.0
     */
    fun runMotors(leftFront: Double, rightFront: Double, leftBack: Double, rightBack: Double) {
        leftRearMotorPower = leftBack
        rightRearMotorPower = rightBack
        leftMotorPower = leftFront
        rightMotorPower = rightFront
    }
}
