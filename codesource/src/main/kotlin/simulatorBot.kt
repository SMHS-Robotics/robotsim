import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class SimulatorBot : BotAPI() {
    private var coroutine: Job? = null


    // Mutable, self-changed variables.
    // ---


    // In m / sec
    private var linearVelocity: Double = 0.0

    // In m
    private var xPos: Double = 0.0

    // In m
    private var yPos: Double = 0.0

    private var angularVelocity = 0.0


    // Pre-defined constants.
    // ---


    private val loopLength: Long = 100    // In ms.

    private val wheelRadius = 0.0    // In m

    private val massOfRobot = 0.0    // In kg

    private val length = 0.0    // In m

    private val width = 0.0    // In m

    private val distanceToCOM = 0.0    // In m

    // TODO: get a better measurement for maxVelocity - this is high-weight approximation,
    // TODO: but our robot is likely considerably lighter.
    private val maxVelocity = 9000    // In degrees / second


    // Methods
    // ---


    /**
     * Starts the simulator
     * Creates a coroutine which simulates physics and records the results
     */
    fun start() {
        coroutine = GlobalScope.launch {
            while (true) {
                simulatorCalculation()
                delay(loopLength)
            }
        }
    }

    fun killRobot() {
        coroutine!!.cancel()
    }

    /**
     * Content of each loop of simulation
     */
    private fun simulatorCalculation() {
        setAccelFromPower()
        // Update current angle with the changes in the last loop
        this.currentAngle += angularVelocity * loopLength / 1000

        // Translate angle to unit circle and convert to radians
        val angleInRadians = (-currentAngle + 90) / 360 * Math.PI

        // calculate x component of distance traveled in the last loop
        yPos += sin(angleInRadians) * (linearVelocity * loopLength)

        // calculate y component of distance traveled in the last loop
        xPos += cos(angleInRadians) * (linearVelocity * loopLength)
    }

    private fun setAccelFromPower() {
        // How fast each side of the robot is moving
        // TODO: This calculation is wrong but I gotta get to class
        val leftLinearVelocity = maxVelocity * leftMotorPower / wheelRadius
        val rightLinearVelocity = maxVelocity * rightMotorPower / wheelRadius

        if (leftLinearVelocity == rightLinearVelocity) {
            // If velocity is the same, no rotation, so we'll just go straight
            this.linearVelocity = leftLinearVelocity
            adjustAngleFromLinearVelocity(0.0, false);
        } else {
            val isTurningLeft = rightLinearVelocity > leftLinearVelocity;
            // Take the velocity that is "common" among both as linear...
            this.linearVelocity = (if (isTurningLeft) leftLinearVelocity else rightLinearVelocity) / massOfRobot
            // ...and use the rest as angular velocity.
            adjustAngleFromLinearVelocity(
                if (isTurningLeft) {
                    rightLinearVelocity - leftLinearVelocity
                } else {
                    leftLinearVelocity - rightLinearVelocity
                }, isTurningLeft
            )
        }
    }

    private fun adjustAngleFromLinearVelocity(difference: Double, directionIsLeft: Boolean) {
        // Angular acceleration = torque * moment of inertia = force * radius * moment of inertia
        angularVelocity = if (directionIsLeft) {
            difference * distanceToCOM
        } else {
            -difference * distanceToCOM
        }
    }
}
