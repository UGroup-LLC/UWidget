# UWidget
[![Maintainability Rating](http://badkiko.ddns.net:9000/api/project_badges/measure?project=UGroup-LLC_UWidget_AYUK1B1-vl74rDW6Kjeu&metric=sqale_rating&token=df81254f833d2c81548de86fce5df04b925a32cd)](http://localhost:9000/dashboard?id=UGroup-LLC_UWidget_AYUK1B1-vl74rDW6Kjeu)
[![Lines of Code](http://badkiko.ddns.net:9000/api/project_badges/measure?project=UGroup-LLC_UWidget_AYUK1B1-vl74rDW6Kjeu&metric=ncloc&token=df81254f833d2c81548de86fce5df04b925a32cd)](http://localhost:9000/dashboard?id=UGroup-LLC_UWidget_AYUK1B1-vl74rDW6Kjeu)
[![wakatime](https://wakatime.com/badge/user/28a2f792-7e9a-4dd3-97e0-a2515ca044d0/project/3cea3d49-8ae3-43b3-bdb0-fd5dabd61080.svg)](https://wakatime.com/badge/user/28a2f792-7e9a-4dd3-97e0-a2515ca044d0/project/3cea3d49-8ae3-43b3-bdb0-fd5dabd61080)
## 📄 Описание 📄
UWidget - является клиент-серверным приложением для отображения расписания занятий в виде виджета на рабочем столе устройства и распространения расписаний между пользователями

Вход осуществляется только через Google Account
После входа пользователь регистрирует свой аккаунт и попадает в главное окно расписания Dashboard
Пользователь может создать свое расписание или же присоединится к уже созданное кем то, для этого каждому расписанию выдаётся уникальный 6-и значений код.
Как администратор расписания обновляет информацию о расписании, она обновляется у всех участников расписания.

Приложение имеет 2 варианта заполнения расписания:
- Парсер
- Ручное

*В первом варианте*, пользователь вводит сайт и ищет селекторы на сайте, благодаря выделению текста предмета в Web-Диалоге (опорные точки для программы), далее программа будет автоматически обнавлять расписание и администратору не придется переживать об актуальности расписания

*Во втором варианте*, пользователь заполняет расписание вручную и обновляет когда это будет необходимо.

## ♟️ Tech-Stack ♟️
- Язык программирования: *Kotlin*
- Инструмент создания UI: *Jetpack Compose*
- Серверная БД: *Google Firebase Firestore*
- Локальная БД: *Room DB*
- API для парсинга: JSoup
- API для авторизации пользователя: *Google Firebase Auth*
- Аналитика: *Google Firebase Analytics*, *Google Firebase Performance*, *Google Firebase Crashlytics*

## 🎨 Скриншоты 🖌️
| Classic тема                                                                                              |                                                  Android 12 Custom                                                   |
|-----------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------:|
| ![scr1](https://github.com/BadKiko/UWidget/blob/master/IMG_20220616_174943_344.jpg?raw=true "Скриншот 1") | ![scr4](https://github.com/BadKiko/UWidget/blob/master/Screenshot_20220616-175104_UWidget.png?raw=true "Скриншот 4") |
| ![scr2](https://github.com/BadKiko/UWidget/blob/master/IMG_20220616_175050_350.jpg?raw=true "Скриншот 2") | ![scr5](https://github.com/BadKiko/UWidget/blob/master/Screenshot_20220616-175112_UWidget.png?raw=true "Скриншот 5") |
| ![scr3](https://github.com/BadKiko/UWidget/blob/master/IMG_20220616_175059_797.jpg?raw=true "Скриншот 3") | ![scr6](https://github.com/BadKiko/UWidget/blob/master/Screenshot_20220616-175126_UWidget.png?raw=true "Скриншот 6") | 

## 🧑‍💻 Разработчики 🧑‍💻
### 📱 Android App 📱
- [BadKiko](https://github.com/BadKiko)
- [Levosllavny](https://github.com/Levosllavny)

## 💡 To-Do 💡
- В будущем будет сделан порт на носимую электронику на базе WearOS и возможно TizenOS
- Web версия приложения
- Замены на день
- Расписания для вузов (четные и нечётные недели)
