/**
 * Entry point for robot simulator.
 * Makes an instance of simulatorBot and runs the start() method
 */
fun main(args: Array<String>) {
    // disclaimer: this code does NOT translate exactly to physical code
    // examine documentation to see the (minor) differences.
    val sim = SimulatorBot()
    sim.start()
}
