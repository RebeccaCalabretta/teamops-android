package io.github.rebeccacalabretta.teamops.data.model

enum class EmployeeRole(
    val displayRole: String
) {
    WORKER("Arbeiter"),
    MANAGER("Objektleiter"),
    HR("Personalabteilung"),
    ADMIN("Admin")
}
