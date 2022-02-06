package com.smh.room

class MemberAlreadyExistException : Exception(
    "There is already a member with that username in teh room."
)