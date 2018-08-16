insert into GENRES (GENRE_ID, NAME) values (1, 'Нехудожественная литература');
insert into GENRES (GENRE_ID, NAME) values (2, 'Фантастика');
insert into GENRES (GENRE_ID, NAME) values (3, 'Детская литература');
insert into GENRES (GENRE_ID, NAME) values (4, 'Учебная литература');
insert into GENRES (GENRE_ID, NAME) values (1000, 'Тестовый жанр');

insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (1, 'Вайткене','Любовь','Дмитриевна');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (2, 'Макнаб','Крис');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (3, 'Цыферов','Г');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (4, 'Киз','Даниел');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (5, 'Лаурен','Смит');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (6, 'Данцев','А');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (7, 'Нефедова','Наталья');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (8, 'Костров','Всеволод', 'Викторович');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (9, 'Давлетов','Джалиль', 'Ахнафович');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (10, 'Нехай','Ольга');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (11, 'Володько','Светлана');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (12, 'Лопатько','Валентина');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME) values (13, 'Петрова','Анастасия');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (14, 'Агафонов','А','В');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (15, 'Пожарская','Светлана','Георгиевна');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (16, 'Ткаченко','Наталия','Александровна');
insert into AUTHORS (AUTHOR_ID, SURNAME, FIRSTNAME, MIDDLENAME) values (17, 'Тумановская','Мария','Петровна');

insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (1, 'Астрономия. Энциклопедия занимательных наук для детей', 3);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (2, 'Энциклопедия выживания. Опыт элитных подразделений спецназа в экстремальных ситуациях', 1);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (3, 'Жил на свете слоненок', 3);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (4, 'Цветы для Элджернона', 2);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (5, 'Пхукет. Путеводитель', 1);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (6, 'Русский язык и культура речи для технических вузов', 4);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (7, 'Шахматный учебник для детей и родителей. В 3 частях. Часть 1', 4);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (8, 'Самоучитель английского языка', 4);
insert into BOOKS (BOOK_ID, NAME, GENRE_ID) values (9, 'Самоучитель английского языка', 4);

insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (1, 1);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (2, 2);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (3, 3);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (4, 4);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (5, 5);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (6, 6);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (6, 7);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (7, 8);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (7, 9);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (8, 10);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (8, 11);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (8, 12);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values (9, 13);

insert into COMMENTS(COMMENT_ID, BOOK_ID, COMMENTATOR, CONTENT, SCORE) values (1, 1, 'Людмила', 'Замечательная книга', 5);
insert into COMMENTS(COMMENT_ID, BOOK_ID, CONTENT, SCORE) values (2, 1, 'Для детей самое то! Красочные иллюстрации. Ставлю 5 баллов', 5);
insert into COMMENTS(COMMENT_ID, BOOK_ID, COMMENTATOR, CONTENT, SCORE) values (3, 2, 'Коробейник Андрей', 'Эта Книга может быть как полезной, так и может сильно навредить. Текст - не адаптирован к РФ. Будьте осторожны. ', 3);
insert into COMMENTS(COMMENT_ID, BOOK_ID, COMMENTATOR, CONTENT, SCORE) values (4, 2, 'Крутилов Сергей', 'Увы, много воды и минимум практически осуществимых навыков. ', 2);