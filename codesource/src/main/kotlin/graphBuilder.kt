import hep.dataforge.meta.invoke
import kscience.plotly.Plotly
import kscience.plotly.makeFile
import kscience.plotly.trace
import java.awt.Font
import java.io.File
import kotlin.math.PI
import kotlin.math.sin

fun main() {

}

fun buildGraph(xPos: List<Double>, yPos: List<Double>) {

    val plot = Plotly.plot {

        trace {
            x.set(xPos)
            y.set(yPos)
        }

        layout {
            title = "Robot Movement Chart"
            xaxis {
                title = "x (m)"
                range = (-5.0).rangeTo(5.0)
            }
            yaxis {
                title = "y (m)"
                range = (-5.0).rangeTo(5.0)
            }
        }
    }

    plot.makeFile()
}
