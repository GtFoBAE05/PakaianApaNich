package com.example.pakaianapanich.utils


fun getWeatherIcon(icon: String): String {
    return "https://openweathermap.org/img/wn/$icon.png"
}

fun getOutfitImage(icon: String): String {

    return when (icon) {
        "Clouds" -> {
            "https://cdn.diys.com/wp-content/uploads/2018/03/Floral-Silk-Shirt-with-Sneakers-and-a-Short-Skirt.jpg"
        }

        "Thunderstorm" -> {
            "https://cdn-icons-png.flaticon.com/512/570/570742.png?w=740&t=st=1686242304~exp=1686242904~hmac=9e1add4a8ca496b1abd0b615eab3774ef56571c639e72a4a5ae5c9d46ce5962e"
        }

        "Snow" -> {
            "https://img.freepik.com/free-vector/hand-drawn-flat-winter-clothes-essentials-collection_23-2149165214.jpg?w=740&t=st=1686242651~exp=1686243251~hmac=b042a9ba32320969f72fed197ed099314560c5158a223306ea5a1274d085f358"
        }

        "Rain" -> {
            "https://img.freepik.com/free-vector/umbrella-concept-illustration_114360-5506.jpg?w=740&t=st=1686242399~exp=1686242999~hmac=4c63c84bd7a04c50a9699de5bd3b4b1d23199a4d311b6441e6725cbbf4c92a2c"
        }

        "Atmosphere" -> {
            "https://img.freepik.com/free-photo/overhead-view-womans-casual-outfits_93675-133151.jpg?size=626&ext=jpg"
        }

        "Clear" -> {
            "https://img.freepik.com/free-vector/stylish-clothes-accessories_1114-167.jpg?w=740&t=st=1686242691~exp=1686243291~hmac=1cf487182a781fd0f82465ebfbc63bb1376e6723bc30dbae45f75e113f85bb0e"
        }

        else -> {
            ""
        }

    }
}
