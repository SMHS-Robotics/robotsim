import kotlinx.coroutines.*
import kotlin.math.pow

class SimulatorBot : BotAPI() {
    private var coroutine: Job? = null


    // Mutable, self-changed variables.
    // ---


    // In m / sec^2
    private var linearAcceleration: Double = 0.0

    // In m / sec
    private var linearVelocity: Double = 0.0

    // In m
    private var xPos: Double = 0.0

    // In m
    private var yPos: Double = 0.0

    private var angularAcceleration = 0.0

    private var angularVelocity = 0.0


    // Pre-defined constants.
    // ---


    private val loopLength: Long = 100    // In ms.

    private val wheelRadius = 0.0    // In m

    private val massOfRobot = 0.0    // In kg

    private val length = 0.0    // In m

    private val width = 0.0    // In m

    private val distanceToCOM = 0.0    // In m

    // TODO: get a better measurement for maxTorque - this is best case approximation, but motors decline.
    private val maxTorque = 0.8352    // In N * m


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
        angularVelocity += angularAcceleration * loopLength / 1000
        linearVelocity += linearAcceleration * loopLength / 1000
    }

    private fun setAccelFromPower() {
        // Forces exerted perpendicular to ground, all in Newtons
        val leftForce = maxTorque * leftMotorPower / wheelRadius
        val rightForce = maxTorque * rightMotorPower / wheelRadius

        if (leftForce == rightForce) {
            // F = ma, so a = F/m
            this.linearAcceleration = leftForce / massOfRobot
            adjustAngleFromForce(0.0, false);
        } else {
            val isTurningLeft = rightForce > leftForce;
            // F = ma, so a = F/m
            // Take the force that is "common" among both as linear...
            this.linearAcceleration = (if (isTurningLeft) leftForce else rightForce) / massOfRobot
            // ...and use the rest as torque.
            adjustAngleFromForce(
                if (isTurningLeft) {
                    rightForce - leftForce
                } else {
                    leftForce - rightForce
                }, isTurningLeft
            )
        }
    }

    private fun adjustAngleFromForce(difference: Double, directionIsLeft: Boolean) {
        // Moment of inertia for a solid rectangle. TODO: tweak to fit our scenario.
        val momentOfInertia: Double = massOfRobot * (length.pow(2.0) + width.pow(2.0)) / 12

        // Angular acceleration = torque * moment of inertia = force * radius * moment of inertia
        angularAcceleration = if (directionIsLeft) {
            difference * distanceToCOM / momentOfInertia
        } else {
            -difference * distanceToCOM / momentOfInertia
        }
    }
}
