package com.smushytaco.solar_apocalypse
interface BlockCache {
    var cacheIdentifier: String
    val cacheTags: HashSet<String>
    val cacheCorrectClasses: HashSet<String>
    val cacheIncorrectClasses: HashSet<String>
}