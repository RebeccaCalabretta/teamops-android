package io.github.rebeccacalabretta.teamops.data.model

enum class EmployeeRole(
    val displayRole: String
) {
    WORKER("Mitarbeiter"),
    MANAGER("Objektleiter"),
    HR("HR"),
    ADMIN("Admin")
}
