# Let's Cook

You don't know what you want to eat? Well, "Let' s Cook" will serve you a different recommended recipe that you should try every day.

What? You want to eat chicken but don't know what to make? You can go to "Category" section and pick chicken. We will show you recipes that use chicken as main ingredient.

"Let's Cook" also can save recipes that you like, just press the heart in the recipe page, and you can see that recipe anytime.

## Screenshots

![Home Page](https://i.imgur.com/giqCmmz.png) ![Category Page](https://i.imgur.com/9xpMtYQ.png)

![Home Page Dark Mode](https://i.imgur.com/9A5WZU5.png) ![Detail Page](https://i.imgur.com/gXAWxV7.png)

## Tech Stack
* [Kotlin](https://kotlinlang.org/)
  * [Coroutines](https://developer.android.com/kotlin/coroutines?gclid=CjwKCAjw-e2EBhAhEiwAJI5jg8PEjvuQA4yyVE0XKE2UOoz5h1LlCKJ7IOMZN3DIdDE9R8ghDWIg2xoCWdMQAvD_BwE&gclsrc=aw.ds) + [Flow](https://developer.android.com/kotlin/flow) (Concurrency)
* [MVVM Architecture with Repository Patern](https://developer.android.com/jetpack/guide#recommended-app-arch) 
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (Dependency injection library for Android)
* [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) (Single Navigation Graph)
  * For multiple navigation graph implementation, visit [HERE](https://github.com/khawasi/LetsCook/tree/mult_nav_graph)
* [Room Persistence Library](https://developer.android.com/training/data-storage/room) (Local Database)
* [Moshi](https://github.com/square/moshi) (A modern JSON library for Kotlin and Java)
* [OkHttp](https://square.github.io/okhttp/) + [Retrofit](https://square.github.io/retrofit/) (A type-safe HTTP client for Android) 
* [Coil](https://github.com/coil-kt/coil) (Image loading for Android backed by Kotlin Coroutines)
* [Shimmer](http://facebook.github.io/shimmer-android/) (Shimmer effect for Android)
* [Encrypted Shared Preferences](https://developer.android.com/topic/security/data)
* [LeakCanary](https://square.github.io/leakcanary/) (Memory leak detection library for Android)
* [.png to vector/.svg](https://www.autotracer.org/)
* [Screenshot Generator](https://theapplaunchpad.com/)

## API
API is prepared by [![TheMealDB](https://www.themealdb.com/images/logo-small.png)](https://www.themealdb.com/api.php)

## Disclaimer
This app is created based on what i have been learned through tutorial and codelabs by myself.

So, if there are (and i believe there will be) some problems, please do tell me about it.

And also if you have sugestions, again, please do tell me about it.

## License

    Copyright 2021 Akhmad Khawasi Mazaya

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
