package za.co.varsitycollege.st10034334.geoguide

//User data class represents a user.
data class User(
    var name: String,
    var surname: String
)
{
    //Default constructor for the User class that initializes all properties with default values.
    constructor(): this("", "")
}
