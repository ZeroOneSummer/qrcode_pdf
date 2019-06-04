namespace java com.tencet.entity

struct UserEntity{
    1:required i16 id,
    2:required string name,
    3:required i32 age
}

service UserService{
    UserEntity getUsers(),
    void UserInfo(UserEntity user)
}