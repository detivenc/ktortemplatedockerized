package model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.ResultRow

object Posts : Table("posts") {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val content = text("content")
    val authorId = integer("author_id").references(Users.id)
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val authorId: Int
)

@Serializable
data class PostRequest(
    val title: String,
    val content: String
)

fun ResultRow.toPost() = Post(
    id = this[Posts.id],
    title = this[Posts.title],
    content = this[Posts.content],
    authorId = this[Posts.authorId]
)
