## Домашнее задание №1
### Приложение по проведению тестирования студентов (только вывод вопросов)

### Цель:
создать приложение с помощью Spring IoC, чтобы познакомиться с основной функциональностью IoC, на которой строится весь Spring.
Результат: простое приложение, сконфигурированное XML-контекстом.

### Файл с вопросами
src/main/resources/data/quiz.csv

### Сборка
mvn clean package (wrapper'а нет)

### Тесты
src/test/java/ru/otus/dao/QuestionDaoImplTest.java

Тесты запускаются в maven lifecycle или mvn test

### Запуск uber-jar
java -jar target/hw-01-1.0.jar
