rootProject.name = "lab-6-kotlin-app"
include("common")

//include(":library")
//include("library:protocol")
//include("library:util")
//    findProject(":library:core:protocol")?.name = "protocol"

include("client")

include("server")
include("server:client")
    findProject(":server:client")?.name = "client"
include("server:service")
    findProject(":server:service")?.name = "service"
include("server:common")
findProject(":server:common")?.name = "common"
