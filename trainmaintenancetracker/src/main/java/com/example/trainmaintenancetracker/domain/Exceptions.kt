package com.example.trainmaintenancetracker.domain


import retrofit2.Response

fun getException(response: Response<*>): Exception {
    return when (response.code()) {
        400 -> BadRequestException("Bad Request")
        401 -> UnauthorizedException("Unauthorized")
        403 -> ForbiddenException("Forbidden: The client does not have access rights to the content.")
        404 -> NotFoundException("Not Found: The server can not find the requested resource.")
        500 -> InternalServerErrorException("Internal Server Error")
        else -> RuntimeException("Unknown error: An unexpected error occurred.")
    }
}

class BadRequestException(message: String) : Exception(message)
class UnauthorizedException(message: String) : Exception(message)
class ForbiddenException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class InternalServerErrorException(message: String) : Exception(message)
