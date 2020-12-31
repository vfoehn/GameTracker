// Entry point of the jar.
fun main() {
    val properties = FileHandler.readConfigFromFile()
    val controller = Controller(properties)
    controller.poll()
}
