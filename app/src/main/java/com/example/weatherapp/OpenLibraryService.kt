package com.example.weatherapp


import com.example.weatherapp.ui.book.Book
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.create
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

@Serializable
data class SearchResponse(
    val docs: List<BookDoc>
)

@Serializable
data class BookDoc(
    val title: String? = null,
    @SerialName("author_name") val authorName: List<String>? = null,
    @SerialName("cover_i") val coverId: Int? = null
)

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): SearchResponse
}

object OpenLibraryService {
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val api: OpenLibraryApi = retrofit.create()
    suspend fun searchBooks(query: String): List<Book> {
        val response = api.searchBooks(query)
        return response.docs.map {
            Book(
                title = it.title ?: "",
                author = it.authorName?.firstOrNull() ?: "",
                coverUrl = it.coverId?.let { id -> "https://covers.openlibrary.org/b/id/$id-M.jpg" } ?: "",
                progress = 0
            )
        }
    }
}