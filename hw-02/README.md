## Домашнее задание №2
### Приложение по проведению тестирования студентов

### Цель:
создать приложение с помощью Spring IoC, чтобы познакомиться с основной функциональностью IoC, на которой строится весь Spring.
Результат: простое приложение, сконфигурированное аннотациями.

### Файл с вопросами
src/main/resources/data/quiz.csv

### Сборка
mvn clean package (wrapper'а нет)

### Тесты
src/test/java/ru/otus/dao/QuestionDaoImplTest.java

src/test/java/otus/service/QuizServiceImplTest.java

Тесты запускаются в maven lifecycle или mvn test

### Запуск uber-jar
java -jar target/hw-02-1.0.jar
