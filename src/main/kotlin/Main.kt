fun main() {
    val properties = FileHandler.readConfigFromFile()
    val controller = Controller(properties)
    controller.poll()
}
