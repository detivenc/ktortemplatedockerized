package service

import model.Post
import model.PostRequest
import model.Posts
import model.toPost
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PostService {

    fun getAllPosts(): List<Post> = transaction {
        Posts.selectAll().map { it.toPost() }
    }

    fun getPostById(id: Int): Post? = transaction {
        Posts.selectAll().where { Posts.id eq id }
            .map { it.toPost() }
            .singleOrNull()
    }

    fun createPost(request: PostRequest, authorId: Int): Int = transaction {
        val result = Posts.insert {
            it[title] = request.title
            it[content] = request.content
            it[Posts.authorId] = authorId
        }
        result[Posts.id]
    }

    fun updatePost(id: Int, request: PostRequest, authorId: Int): Boolean = transaction {
        val affectedRows = Posts.update({ (Posts.id eq id) and (Posts.authorId eq authorId) }) {
            it[title] = request.title
            it[content] = request.content
        }
        affectedRows > 0
    }

    fun deletePost(id: Int, authorId: Int): Boolean = transaction {
        val affectedRows = Posts.deleteWhere { (Posts.id eq id) and (Posts.authorId eq authorId) }
        affectedRows > 0
    }
}
