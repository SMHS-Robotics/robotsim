import kotlinx.coroutines.delay

/**
 * Entry point for robot simulator.
 * Makes an instance of simulatorBot and runs the start() method
 */
suspend fun main(args: Array<String>) {
    // disclaimer: this code does NOT translate exactly to physical code
    val sim = SimulatorBot()
    sim.start()
    val bot : SimulatorBot = sim

    // Take a slight path left
    bot.leftMotorPower = 1.0
    bot.rightMotorPower = 0.90

    delay(4000L)

    // Spin in place until we're horizontal (90 deg)
    bot.leftMotorPower = 0.8
    bot.rightMotorPower = -0.8
    while (bot.angleInRadians > 0) delay(20L)

    // Go straight for a bit
    bot.rightMotorPower = 0.8
    delay(2000L)
    bot.killRobot()

    buildGraph(xPos = bot.listPosX, yPos = bot.listPosY)
}
