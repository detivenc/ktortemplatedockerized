package model

object Errors {
    const val MISSING_CREDENTIALS = "Username and password are required"
    const val INVALID_CREDENTIALS = "Invalid credentials"
    const val USERNAME_EXISTS = "Username already exists"
    const val INVALID_POST_ID = "Invalid post ID"
    const val POST_NOT_FOUND = "Post not found"
    const val POST_NOT_FOUND_OR_UNAUTHORIZED = "Post not found or unauthorized"
    const val EMPTY_MESSAGE = "Message cannot be empty"
}

object HealthStatus {
    const val UP = "UP"
}

object AuthStatus {
    const val REGISTERED = "registered"
}

object PostStatus {
    const val UPDATED = "updated"
    const val DELETED = "deleted"
}