package io.github.rebeccacalabretta.teamops.domain.error

class PermissionDeniedException : Exception(
    "User does not have permission for this action"
)