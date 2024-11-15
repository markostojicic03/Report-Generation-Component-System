package model

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class MyException(message: String
) : Exception(
    when (message) {
        "tooManyArguments" -> "Too many arguments provided. Please check your input."
        "emptyArgument" -> "Missing informations about formatting in configuration file"
        "invalidFormat" -> "Invalid format provided."
        "unknown" -> "Unknown error related to formatting."
        "fewArguments" -> "Missing keys arguments"
        else -> "Some unknown mistake happened"
    }
){

        companion object {
            private val logger: Logger = LoggerFactory.getLogger(MyException::class.java)
        }
        fun logError() {
            logger.error(this.message)
        }

}



