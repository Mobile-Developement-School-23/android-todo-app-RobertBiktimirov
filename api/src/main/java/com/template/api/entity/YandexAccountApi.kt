package com.template.api.entity

import kotlinx.serialization.SerialName


/*
{
   "first_name": "Иван",
   "last_name": "Иванов",
   "display_name": "ivan",
   "emails": [
      "test@yandex.ru",
      "other-test@yandex.ru"
   ],
   "default_email": "test@yandex.ru",
   "default_phone": {
      "id": 12345678,
      "number": "+79037659418"
   },
   "real_name": "Иван Иванов",
   "is_avatar_empty": false,
   "birthday": "1987-03-12",
   "default_avatar_id": "131652443",
   "login": "ivan",
   "old_social_login": "uid-mmzxrnry",
   "sex": "male",
   "id": "1000034426",
   "client_id": "4760187d81bc4b7799476b42b5103713",
   "psuid": "1.AAceCw.tbHgw5DtJ9_zeqPrk-Ba2w.qPWSRC5v2t2IaksPJgnge"
}
*/

@kotlinx.serialization.Serializable
data class YandexAccountApi(
    @SerialName("real_name") val name: String,
    @SerialName("login") val login: String,
    @SerialName("default_email") val email: String,
    @SerialName("birthday") val birthday: String,
    @SerialName("default_avatar_id") var avatarId: Long,
    @SerialName("default_phone") val phoneInfo: PhoneInfo,
)

@kotlinx.serialization.Serializable
data class PhoneInfo(
    @SerialName("number") val number: String
)
