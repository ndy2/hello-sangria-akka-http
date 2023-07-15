package realworld.domain

case class User(
    email: String,
    password: String,
    profile: Profile
)

case class Profile(
    username: String,
    bio: Option[String],
    image: Option[String]
)
