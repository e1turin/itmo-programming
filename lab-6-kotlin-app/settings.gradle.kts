rootProject.name = "lab-6-kotlin-app"
include("server")
include("common")
include("util")
include("client")
include("server:client")
    findProject(":server:client")?.name = "client"
include("server:service")
    findProject(":server:service")?.name = "service"
