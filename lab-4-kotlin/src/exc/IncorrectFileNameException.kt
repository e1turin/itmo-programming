package exc

class IncorrectFileNameException(errorMessage: String?) : Exception(errorMessage) {
    companion object {
        fun isCorrectFileName(path: String): Boolean {
            return path == "C:/meme.txt"
        }
    }
}