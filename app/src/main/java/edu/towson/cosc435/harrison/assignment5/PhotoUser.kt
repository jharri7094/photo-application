package edu.towson.cosc435.harrison.assignment5

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key.Companion.T
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File


interface IPhotoUsersFetcher {
    suspend fun fetchTitle() : String
    suspend fun fetchUsers() : List<PhotoUserI>
    suspend fun fetchThumbnailUrl() : String
    suspend fun fetchUrl() : String
    suspend fun fetchThumbnail(thumbnail_url: String) : Bitmap
    suspend fun fetchPicture(url_ : String) : Bitmap
}

class UsersFetcher(private val cacheDir: File) : IPhotoUsersFetcher {
    override suspend fun fetchTitle() : String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().get().url("${API_URL}?.albumId=1")
                .build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val apiResult = Gson().fromJson(body, ApiResult::class.java)

            apiResult.title_
        }
    }

    override suspend fun fetchUsers(): List<PhotoUserI> {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().get().url("${API_URL}?.albumId=1")
                .build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val apiResult = Gson().fromJson(body, ApiResult::class.java)
            listOf(PhotoUserI(apiResult.album_Id, apiResult.id_, apiResult.title_, apiResult.url_, apiResult.thumbnail_Url, fetchThumbnail(apiResult.thumbnail_Url)))
        }
    }

    override suspend fun fetchThumbnailUrl() : String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().get().url("${API_URL}?.albumId=1")
                .build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val apiResult = Gson().fromJson(body, ApiResult::class.java)

            apiResult.thumbnail_Url
        }
    }

    override suspend fun fetchUrl() : String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().get().url("${API_URL}?.albumId=1")
                .build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val apiResult = Gson().fromJson(body, ApiResult::class.java)

            apiResult.url_
        }
    }

    override suspend fun fetchThumbnail(thumbnail_url: String) : Bitmap {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder()
                .cache(
                    Cache(
                        directory = cacheDir,
                        maxSize = 100L * 1024L
                    )
                ).build()
            val request = Request.Builder().get().url(thumbnail_url).build()
            val response = client.newCall(request).execute()
            val stream = response.body?.byteStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            bitmap
        }
    }

    override suspend fun fetchPicture(url_: String) : Bitmap {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient().newBuilder()
                .cache(
                    Cache(
                        directory = cacheDir,
                        maxSize = 100L * 1024L
                    )
                ).build()
            val request = Request.Builder().get().url(url_).build()
            val response = client.newCall(request).execute()
            val stream = response.body?.byteStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            bitmap
        }
    }


    companion object{
        val API_URL = "https://jsonplaceholder.typicode.com/photos"
    }

}

data class PhotoUserI(
    @SerializedName("albumId")
    val albumId: Int,

    @SerializedName("id")
    val user_id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    var thumbnail: Bitmap
//    val photo: MutableState<Bitmap>
)


class RestAPIViewModel(app : Application) : AndroidViewModel(app){
    private val _title: MutableState<String>
    val title: State<String>

    private val _thumbnail : MutableState<Bitmap?>
    val thumbnail : State<Bitmap?>

    private var _thumbnail_url : MutableState<String>
    var thumbnail_url : State<String>

    private var _users : MutableState<List<PhotoUserI>>
    var users : State<List<PhotoUserI>>




    val usersFetcher: IPhotoUsersFetcher = UsersFetcher(getApplication<Application>().cacheDir)

    init{

        _users = mutableStateOf(listOf())
        users = _users

        _title = mutableStateOf("")
        title = _title

        _thumbnail = mutableStateOf<Bitmap?>(null)
        thumbnail = _thumbnail

        _thumbnail_url = mutableStateOf("")
        thumbnail_url = _thumbnail_url


//        viewModelScope.launch {
//            fetchTitle()
//        }
//        viewModelScope.launch {
//            fetchThumbnail(thumbnail_url.toString())
//        }
        viewModelScope.launch {
            fetchUsers()
        }

    }

    suspend fun fetchUsers(){
        val users = usersFetcher.fetchUsers()
        users.forEach { user ->
            user.thumbnail =
                getApplication<Application>()
                    .theme
                    .getDrawable(R.drawable.ic_launcher_foreground)
                    .toBitmap()
        }
        _users.value = users
    }

    suspend fun fetchThumbnailUrl(){
        val thumbnailUrl = usersFetcher.fetchThumbnailUrl()
        _thumbnail_url = mutableStateOf(thumbnailUrl)
    }

    suspend fun fetchTitle(){
        val title = usersFetcher.fetchTitle()
        _title.value = title
    }

    suspend fun fetchThumbnail(url: String) {
        val thumbnail = usersFetcher.fetchThumbnail(url)
        _thumbnail.value = thumbnail
    }

    companion object {
        fun factory(app: Application): ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return RestAPIViewModel(app) as T
                }

            }
        }
    }

}

data class ApiResult(val album_Id: Int, val id_: Int, val title_: String, val url_: String,
                     val thumbnail_Url: String)


@Entity
data class PhotoUser (
    @PrimaryKey(autoGenerate = true)
    var key: Int = 0,
    @ColumnInfo(name = "albumId") var album_Id: String,
    @ColumnInfo(name = "id") var song_id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "thumbnailUrl") var thumbnailUrl: String
)

@Dao
interface PhotoUserDao {
    @Query("SELECT * FROM photoUser")
    fun getPhotoUsers(): LiveData<List<PhotoUser>>

    @Insert
    suspend fun addPhotoUser(photoUser: PhotoUser)

    @Delete
    suspend fun deleteUser(photoUser: PhotoUser)
}

@Database(entities = [PhotoUser::class], version = 1, exportSchema = false)
abstract class Assignment5Database : RoomDatabase() {
    abstract fun photoUserDao() : PhotoUserDao
}

class PhotoUsersViewModel(app : Application) : AndroidViewModel(app) {
    val photoUserDb : Assignment5Database

    private val _photoUsers: MutableState<List<PhotoUser>> = mutableStateOf(listOf())

    val photoUsers: State<List<PhotoUser>> = _photoUsers

    private var livePhotoUserList: LiveData<List<PhotoUser>>
    private var updateCallback: (List<PhotoUser>) -> Unit


    init{
        photoUserDb = Room.databaseBuilder(
            app,
            Assignment5Database::class.java,
            "photoUsers.db"
        ).fallbackToDestructiveMigration().build()

        livePhotoUserList = photoUserDb.photoUserDao().getPhotoUsers()

        updateCallback = {photoUsers: List<PhotoUser> -> _photoUsers.value = photoUsers}

        livePhotoUserList.observeForever(updateCallback)
    }

    fun addPhotoUser(photoUser: PhotoUser){
        viewModelScope.launch{
            photoUserDb.photoUserDao().addPhotoUser(photoUser)
        }
    }

    fun deletePhotoUser(photoUser: PhotoUser) {
        viewModelScope.launch {
            photoUserDb.photoUserDao().deleteUser(photoUser)
        }
    }

    companion object {
        fun factory(app: Application) : ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>) : T {
                    return PhotoUsersViewModel(app) as T
                }
            }
        }
    }

}