import java.io.File
import com.google.gson.reflect.TypeToken

val cacheFile = File("cached_users.json")

fun loadCache(): MutableMap<String, CachedData> {
    if (!cacheFile.exists()) return mutableMapOf()
    val type = object : TypeToken<MutableMap<String, CachedData>>() {}.type
    return Dependencies.gson.fromJson(cacheFile.readText(), type) ?: mutableMapOf()
}

fun saveCache(cache: MutableMap<String, CachedData>) {
    cacheFile.writeText(Dependencies.gson.toJson(cache))
}

fun listCachedUsers() {
    val userCache = loadCache()
    if (userCache.isEmpty()) {
        println("\nNo users in cache!")
    } else {
        println("\nCached users: ")
        userCache.keys.forEach { println(it) }
    }
}

fun searchUserInCache(username: String) {
    val userCache = loadCache()
    if (userCache.containsKey(username)) {
        displayUser(username, userCache)
    } else {
        println("\nUser not found in cache!")
    }
}

fun searchRepoInCache(repoName: String) {
    val userCache = loadCache()
    val foundUsers = userCache.filter { it.value.repos.any { repo -> repo.name == repoName } }
    if (foundUsers.isEmpty()) {
        println("\nRepository not found in cache!")
    } else {
        foundUsers.forEach { (user, _) -> println("\n$repoName found under user: $user") }
    }
}
