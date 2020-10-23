import kotlinx.coroutines.*

class SimulatorBot : BotAPI() {
    private var coroutine : Job? = null

    // TODO: Document expected units

    // Mutable, self-changed variables.
    private var acceleration : Double = 0.0
    private var velocity : Double = 0.0
    private var xPos : Double = 0.0
    private var yPos : Double = 0.0

    // Pre-defined constants.
    private val loopLength: Long = 100
    private val wheelDiameter: Double = TODO("TBD")
    private val friction: Double = TODO("TBD")
    private val maxTorque: Double = TODO("TBD")

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

    fun kill() {
        coroutine!!.cancel()
    }

    /**
     * Content of each loop of simulation
     */
    private fun simulatorCalculation() {
        setAccelFromPower()
        adjustAngleFromPower()
    }

    private fun setAccelFromPower() {

    }

    private fun adjustAngleFromPower () {

    }
}
