### Структура фреймворка API тестов

### main

**api** - хранит всё, связанное с использованием API методоd

**api.core** - хранит классы с методами обращения к API без какой-то логики, а так же названиями эндпоинтов, хэдеров и 
другой вспомогательной информации.

**api.methods** (и **api.methods.user** в частности) - предназначена для хранения классов с методами, имеющих
какую-то определённую логику и разбитые по категориям. Например, класс ApiUserMethods содержит всё, что относится 
к действиям с пользователем. 

**builders** - хранит классы, реализующие паттер билдер.

**configs** - хранит классы, работающие с окружением и любыми другими конфигами, которые
могут быть необходимы для проекта.

**helpers** - хранит вспомогательные классы для генерации данных, обработки ответов, работы с датами и т.д. и т.п.

**P.S.**  Данная структура папок - пример небольшого проекта. 
Средние и крупные проекты имеют значительно большее кол-во различных сущностей. 
А это значит, что и структура 
с высокой вероятностью будет меняться
вслед за ростом проекта.

### Настройка окружения
Папка resources хранит в себе файл env.properties, предназначенный для описания конфигурации окружения.
Этот файл содержит одно поле ENVIRONMENT. 
Класс EnvProperties считывает этот файл.
EnvironmentConfig вытаскивает значения считанных с помощью EnvProperties полей. 

Например, если установить **ENVIRONMENT=prod**, то EnvironmentConfig.getBaseUrl() вернёт
https://playground.learnqa.ru/api/, а если **ENVIRONMENT=dev**, то https://playground.learnqa.ru/api_dev/.
Параметры в env.properties можно добавлять какие угодно, в зависимости от требований.
