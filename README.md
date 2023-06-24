# Бот система
Бот-система представляет собой комплекс взаимосвязанных компонентов, которые обрабатывают сообщения от мессенджеров, преобразуют их в соответствии с заданными правилами и отправляют обратно ответные сообщения.
## Архитектура
![Diagram](https://github.com/4etell/bot-generator/blob/master/arch_v2.png)
## Config-service
В этот микросервис администратор может загрузить конфигурационный файл flow.yml, в нем описано поведение работы бот-системы.
### API для взаимодействия с config-service
```yaml
openapi: 3.0.1
info:
  title: Config Service
  description: API для использования config-service.
  version: "0.1"
paths:
  /config-service/admin/bot-file/download/{filePathInMinIO}:
    get:
      summary: Скачивание файла бота
      operationId: downloadBotFile
      parameters:
        - name: filePathInMinIO
          in: path
          description: "Полное название пути файла в MinIO, с его именем и расширени\
          ем"
          required: true
          schema:
            type: string
      responses:
        "200":
          description: Файл бота успешно скачан
          content:
            application/octet-stream:
              schema:
                type: string
                format: binary
  /config-service/admin/bot-file/upload/{filePathInMinIO}:
    post:
      summary: Загрузка файла бота
      operationId: uploadBotFile
      parameters:
        - name: filePathInMinIO
          in: path
          description: "Полное название пути файла в MinIO, с его именем и расширени\
          ем"
          required: true
          schema:
            type: string
      requestBody:
        content:
          multipart/form-data:
            schema:
              type: object
              properties:
                file:
                  type: string
                  format: binary
        required: true
      responses:
        "200":
          description: Файл бота успешно загружен
  /config-service/admin/flow-file/download:
    get:
      summary: Скачивание flow-конфигурационного файла
      operationId: downloadFlowFile
      responses:
        "200":
          description: Файл успешно скачан
          content:
            application/octet-stream:
              schema:
                type: string
                format: binary
  /config-service/admin/flow-file/upload:
    post:
      summary: Загрузка flow-конфигурационного файла
      operationId: uploadFlowFile
      requestBody:
        content:
          multipart/form-data:
            schema:
              type: object
              properties:
                file:
                  type: string
                  format: binary
        required: true
      responses:
        "200":
          description: Файл успешно загружен
```
## Документация для файла конфигурации flow.yml
Flow содержит список состояний state. State определяет контекст диалога бота с пользователем, а также действия, которые бот должен выполнить в данном состоянии.

В каждом состоянии бот может отправлять пользователю различные типы ответов, такие как текст, меню или файлы.

### Пример flow.yml
```yaml
states:
  - name: About
    command: "/about"
    response:
      - !text
        text: "Я бот."
  - name: Start
    command: "/start"
    response:
      - !menu
        text: "Привет я бот, ты можешь поменять мою конфигурацию в админ-панели!"
        rows:
          - items:
              - title: Обо мне
                command: "/about"
              - title: Разработчики
                command: "/developers"
          - items:
              - title: Что-то еще
                command: "/some"
  - name: Files
    command: "/getFiles"
    response:
      - !file
        text: "Файлы:"
        files:
          - filePathInMinIO: "/path/doc.pdf"
            isPicture: false
          - filePathInMinIO: "/path/pic.png"
            isPicture: true
```

### Структура StateModel
StateModel содержит следующие поля:
- name: уникальное имя состояния;
- command: уникальная команда, с которой ассоциировано данное состояние. Если пользователь вводит эту команду, то бот переходит в соответствующее состояние;
- variableName: имя переменной, которая будет сохранена в пользовательских данных для конкретного пользователя. Если пользователь вводит текст (не являющийся командой) в состоянии с указанным именем переменной, то message-handler сохранит этот текст в Redis. Это позволяет сохранять и использовать пользовательские данные в дальнейшем общении с пользователем;
- response: список типов ответов, которые бот должен отправить пользователю после выполнения действий в данном состоянии;
- nextStateName: имя следующего состояния, в которое перейдет бот после завершения текущего состояния. Переход в следующее состояние осуществляется, когда пользователь отправляет текстовое сообщение, которое не является командой, и это сообщение записывается в переменную с именем variableName;
- externalService: параметры внешнего сервиса, с которым должен взаимодействовать бот в данном состоянии.
### Типы ответов
#### Текстовое сообщение
!text

Отправляет пользователю текстовое сообщение. Принимает следующие аргументы:
- text (обязательный): текст сообщения.
  Пример использования:
```yaml
- name: About
  command: "/about"
  response:
    - !text
      text: "Я бот."
```
#### Сообщение с меню
!menu

Отправляет пользователю меню. Принимает следующие аргументы:
- text (обязательный): текст сообщения, которое будет отображено вместе с меню;
- rows (обязательный): список строк (rows) меню. Каждая строка состоит из списка элементов (items). Каждый элемент состоит из:
  - title (обязательный): текст элемента;
  - command (опциональный): команда, которую бот должен выполнить при выборе элемента. Если команда не указана, то элемент считается неактивным (невыбираемым).
```yaml
- name: Start
  command: "/start"
  response:
    - !menu
      text: "Привет я бот, ты можешь поменять мою конфигурацию в админ-панели!"
      rows:
        - items:
            - title: Обо мне
              command: "/about"
            - title: Разработчики
              command: "/developers"
        - items:
            - title: Что-то еще
              command: "/some"
```
#### Сообщение с файлами
!file

Отправляет пользователю файл. Принимает следующие аргументы:

- text (опциональный): текст сообщения, которое будет отображено вместе с файлом;
- files (обязательный): список файлов, которые бот должен отправить пользователю. Каждый файл состоит из следующих полей:
  - filePathInMinIO (обязательный): путь к файлу в MinIO, который был указан при загрузке файла в config-service;
  - isPicture (обязательный): логическое значение (true/false), указывающее, отправить ли файл как изображение.
  Пример использования:
```yaml
- name: Files
  command: "/getFiles"
  response:
    - !file
      text: "Файлы:"
      files:
        - filePathInMinIO: "/path/doc.pdf"
          isPicture: false
        - filePathInMinIO: "/path/pic.png"
          isPicture: true
```
### Переменная variableName и переход на следующее состояние nextStateName
В некоторых случаях может потребоваться сохранить данные, введенные пользователем, для использования их в следующем состоянии. Для этого в конфигурационном файле flow.yml используется поле variableName. Значение, введенное пользователем, будет сохранено в Redis в виде UserData. Для каждого пользователя в мессенджере создается своя UserData, таким образом для каждого пользователя Telegram будет создана своя UserData, для каждого пользователя VK - своя и т.д.

Кроме того, может потребоваться перенаправить пользователя в следующее состояние после завершения текущего. Для этого используется поле nextStateName, которое определяет имя следующего состояния, в него должен перейти бот.

Рассмотрим пример, в котором используются поля variableName и nextStateName:
```yaml
- name: ContactInfo
  command: "/contact_info"
  response:
    - !text
      text: "Введите Ваше имя и фамилию:"
  nextStateName: "OrderInfo"
  variableName: "client_name"
```

В этом примере состояние ContactInfo ожидает, что пользователь введет свое имя и фамилию. После того как пользователь введет данные, они будут сохранены в переменную client_name. Затем бот перейдет в состояние OrderInfo.

### Взаимодействие с внешними сервисами
Иногда боту может понадобиться взаимодействовать с внешними сервисами, например, для обработки пользовательского запроса. Для этого в конфигурационном файле flow.yml можно указать внешний сервис, с которым бот будет взаимодействовать в определенном состоянии.

Поле externalService определяет параметры внешнего сервиса. Рекомендуется создать микросервис и добавить его в одну сеть с message-handler для обеспечения безопасности передачи данных.

В модели ExternalServiceModel указаны следующие параметры:

- URL: адрес внешнего сервиса, на него будет отправлено сообщение пользователя и переменные с помощью POST-запроса;
- userVariables: список имен переменных (variableName), которые должны быть переданы внешнему сервису вместе с сообщением пользователя. Эти переменные берутся из пользовательских данных (UserData);
- response: типы возвращаемых сообщений, которые бот должен отправить пользователю после получения ответа от внешнего сервиса. Ответ должен быть одним из следующих типов: !menu, !text или !file. Бот обрабатывает ответ внешнего сервиса в соответствии с указанным типом и отправляет пользователю соответствующее сообщение. Если response не указан в externalService, то бот использует response из соответствующего состояния (state).

Запрос на URL посылается POST-запросом. В качестве тела запроса отправляется объект в формате JSON, где userMessageDto - это объект, который содержит текст сообщения и дополнительную информацию, а variables - это пользовательские данные (UserData).

Пример тела запроса:
```json
{
  "userMessageDto": {
    "command": "/request",
    "messageId": "12345",
    "chatId": "67890",
    "timeStamp": 1620632375,
    "subsystemName": "telegram"
  },
  "variables": {
    "requestText": "Текст запроса"
  }
}
```
Пример использования externalService в состоянии:
```yaml
- name: Request
  command: "/request"
  externalService:
    URL: "http://external-service:8080/api/request"
    userVariables:
      - "requestText"
    response:
      - !text
        text: "Спасибо за ваше сообщение!"
```
В данном примере, при вводе команды "/request" бот отправляет текстовое сообщение и переменную requestText внешнему сервису по URL. После получения ответа от внешнего сервиса бот отправляет текстовое сообщение "Спасибо за ваше сообщение!" пользователю.

Также можно указать response в самом состоянии, вместо указания его в externalService. Если response не указан в externalService, то ответ берется из response в состоянии.

Пример использования externalService с указанием response в состоянии:
```yaml
- name: Request
  command: "/request"
  externalService:
    URL: "http://external-service:8080/api/request"
    userVariables:
      - "requestText"
  response:
    - !text
      text: "Спасибо за ваше сообщение!"
```
В данном примере, при вводе команды "/request" бот отправляет текстовое сообщение и переменную requestText внешнему сервису по указанному URL. После получения ответа от внешнего сервиса бот отправляет текст
