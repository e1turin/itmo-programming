package com.github.e1turin.lab5.containers

import com.github.e1turin.lab5.collection.StorageManager

class Request(
    sender: String,
    val type: RequestType,
    val taskType: StorageManager.TaskType,
    val arg: Any = "",
    content: String
) : Message(sender, content) {

}
