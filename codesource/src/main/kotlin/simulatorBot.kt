import kotlinx.coroutines.*
import kotlin.math.*

/**
 * An extension of BotAPI that is designed to simulate the physics of the robot
 * It runs a thread that will update the variables that represent physical components
 *
 * graphBuilder.kt can generate a graph based on this data using [buildGraph].
 */
class SimulatorBot : BotAPI() {
    private var coroutine: Job? = null
    var listPosX = mutableListOf<Double>();
    var listPosY = mutableListOf<Double>();


    // Mutable, self-changed variables.
    // ---


    // In m / sec
    private var linearVelocity: Double = 0.0

    // In m
    var xPos: Double = 0.0

    // In m
    var yPos: Double = 0.0

    // In deg / sec
    private var angularVelocity = 0.0


    // Pre-defined constants.
    // ---


    // Length of the calculation loop, controls how much time between update of physical variables.
    private val loopLength: Long = 20    // In ms

    // Rate of text refresh (di splaying statistics) *multiple of loop length*
    private val screenRefresh: Long = 500   // In ms

    // I'm not going to bother documenting this.
    private val wheelRadius = 0.0508    // In m

    // Patty daddy will take points off if this is weight. You must measure it without gravity.
    private val massOfRobot = 5.0    // In kg

    // Avg. distance from edge to center of mass
    private val distanceToCOM = 0.1524    // In m

    // TODO: get a better measurement for maxVelocity - this is high-power approximation,
    // TODO: but our robot is likely considerably different.
    // Maximum speed that the wheel can rotate
    private val maxVelocityOfWheels = 6000    // In degrees / second (9000 ~ 2.5 rotations / second)

    var angleInRadians = Math.PI/2;

    // Methods
    // ---


    /**
     * Starts the simulator
     * Creates a coroutine which simulates physics and records the results
     */
    fun start() {
        coroutine = GlobalScope.launch {
            var stopwatch: Long = 0;
            while (true) {
                simulatorCalculation()
                listPosX.add(xPos)
                listPosY.add(yPos)

                if (stopwatch % screenRefresh == 0L) print("\r y: ${yPos}, x: $xPos, deg: $currentAngle")
                delay(loopLength)
                stopwatch += loopLength
            }
        }
    }

    /**
     * Terminates the thread that updates the robot
     */
    fun killRobot() {
        coroutine!!.cancel()
    }

    /**
     * Content of each loop of simulation
     */
    private fun simulatorCalculation() {
        setVelFromPower()

        // Update current angle with the changes in the last loop
        this.currentAngle += angularVelocity * loopLength / 1000

        // Translate angle to unit circle and convert to radians
        this.angleInRadians = (-currentAngle + 90) / 180 * Math.PI

        // calculate x component of distance traveled in the last loop
        yPos += sin(angleInRadians) * (linearVelocity * loopLength / 1000)

        // calculate y component of distance traveled in the last loop
        xPos += cos(angleInRadians) * (linearVelocity * loopLength / 1000)
    }

    /**
     * Based on the power of the wheels, updates the velocity of the robot
     */
    private fun setVelFromPower() {
        // How fast each side of the robot is moving in meters per second
        val leftLinearVelocity = maxVelocityOfWheels / 360 * leftMotorPower * Math.PI * wheelRadius * 2
        val rightLinearVelocity = maxVelocityOfWheels / 360 * rightMotorPower * Math.PI * wheelRadius * 2

        if (leftLinearVelocity == rightLinearVelocity) {
            // If velocity is the same, no rotation, so we'll just go straight
            this.linearVelocity = leftLinearVelocity
            adjustAngleFromLinearVelocity(0.0);
        } else {
            val isTurningLeft = rightLinearVelocity > leftLinearVelocity;
            val differenceBetween = abs(leftLinearVelocity - rightLinearVelocity)
            // Take the velocity that is "common" among both as linear velocity, unless one is opposite direction,
            // in which case we assume that the robot has no linear velocity.
            this.linearVelocity =
                (if (differenceBetween < max(abs(leftLinearVelocity), abs(rightLinearVelocity))) {
                    if (isTurningLeft) {
                        leftLinearVelocity
                    } else {
                        rightLinearVelocity
                    }
                } else {
                    0.0
                }) / massOfRobot
            // ...and use the rest as angular velocity.
            adjustAngleFromLinearVelocity(
                rightLinearVelocity-leftLinearVelocity
            )
        }
    }

    private fun adjustAngleFromLinearVelocity(difference: Double) {
        angularVelocity = -difference * distanceToCOM / Math.PI * 180
    }
}
