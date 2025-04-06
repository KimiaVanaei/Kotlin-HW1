import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.lang.Exception
import java.util.*

// Kimia Vanaei, 401107613

suspend fun fetchUser(username: String) {
    val userCache = loadCache()
    if (userCache.containsKey(username)) {
        println("\nUser is already in your memory! Data loading from cache...")
    } else {
        try {
            println("\nNew request! User data fetching from GitHub API...")
            val user = Dependencies.gitHub.getUser(username)
            val repos = Dependencies.gitHub.getRepositories(username)
            userCache[username] = CachedData(user, repos)
            saveCache(userCache)
        } catch (e: HttpException) {
            val error = Dependencies.gson.fromJson(
                e.response()?.errorBody()?.string(),
                ApiError::class.java
            )
            println(error)
        } catch (e: Exception) {
            println(e)
        }
    }
    displayUser(username, userCache)
}

fun displayUser(username: String, userCache: Map<String, CachedData>) {
    val (user, repos) = userCache[username] ?: return
    println("\nUser: ${user.login}")
    println("Number of Followers: ${user.followers}, Number of Followings: ${user.following}")
    println("Created account at: ${user.createdAt}")
    println("Number of Public Repositories: ${user.publicRepos}")
    println("Public Repositories: ${repos.joinToString { it.name }}")
}

fun main(): Unit = runBlocking {
    val scanner = Scanner(System.`in`)
    while (true) {
        println("\n** Main Menu **")
        println("1- Get user info")
        println("2- List cached users")
        println("3- Search user in cache")
        println("4- Search repository in cache")
        println("5- Exit")
        print("Choose an option (1 to 5): ")

        when (scanner.nextInt()) {
            1 -> {
                print("\nEnter GitHub username: ")
                val username = scanner.next()
                fetchUser(username)
            }

            2 -> listCachedUsers()

            3 -> {
                print("\nEnter username to search: ")
                val username = scanner.next()
                searchUserInCache(username)
            }

            4 -> {
                print("\nEnter repository name to search: ")
                val repoName = scanner.next()
                searchRepoInCache(repoName)
            }

            5 -> kotlin.system.exitProcess(0)
            else -> println("\nInvalid option!")
        }
    }
}