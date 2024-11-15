package model


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
)