package com.example.myfirstproject

data class Quote(
    var id: String,
    var content: String,
    var from: String
){
    fun toMap() : HashMap<String, Any>{
        var result: HashMap<String, Any> = HashMap()

        result["id"] = id
        result["content"] = content
        result["from"] = from

        return result
    }
}