# Тестовое задание для HSystems


![overview](https://github.com/zelspeno/hsTestTask/assets/75141607/08ffaa2f-a9fa-4108-954e-2e62a925c1b5)

## Скачивание и установка

Минимальная версия SDK = 24 (Android 7.0)

Для скачивания - перейдите в раздел Releases и выберите последнюю (latest) версию. Далее, скачайте файл с расширением .apk или перейдите по ссылке - 
[тык.](https://github.com/zelspeno/hsTestTask/releases/download/release/hsTestTask.apk)

И установите на android-устройство/эмулятор.

## Средства разработки 
Kotlin, Retrofit2, Gson, Dagger/Hilt, Room Database (для хранения кэша), Picasso, Corroutines, MVVM.

### Использованные запросы

http://mocky.io/v3/024e2df2-d062-40d5-98b3-3f1b3c02bbae - получение Json-файла с несколькими пунктами блюд.

Это временная ссылка, если не работает, пожалуйста, напишите мне.

Также, Вы можете самостоятельно сделать json-файл [тут](https://designer.mocky.io/design) и вставить в раздел 'HTTP Response Body' текст из документа testJson.txt, далее, в проекте, в объекте Const изменить константу DISHES_URL.
