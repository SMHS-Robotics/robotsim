import kotlinx.coroutines.delay

/**
 * Entry point for robot simulator.
 * Makes an instance of simulatorBot and runs the start() method
 */
suspend fun main(args: Array<String>) {
    // disclaimer: this code does NOT translate exactly to physical code
    // examine comments to see the (minor) differences.
    val sim = SimulatorBot()
    sim.start()
    val bot : BotAPI = sim

    // Update for test
    bot.leftMotorPower = 1.0
    bot.rightMotorPower = 0.5
    delay(2500L)
    bot.leftMotorPower = 0.5
    bot.rightMotorPower = 0.2
    delay(2500L)
    bot.leftMotorPower = 0.3
    bot.rightMotorPower = 0.3
    delay(2500L)
    println(sim.currentAngle)
    println(sim.xPos)
    println(sim.yPos)
}
