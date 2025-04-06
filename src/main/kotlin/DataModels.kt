import com.google.gson.annotations.SerializedName

data class GitHubUser(
    @SerializedName("name") val name: String,
    @SerializedName("login") val login: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("public_repos") val publicRepos: Int
)

data class Repository(
    @SerializedName("name") val name: String
)

data class CachedData(val user: GitHubUser, val repos: List<Repository>)

