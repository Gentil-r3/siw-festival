insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Francis Ford', 'Coppola', '1939-04-07', 'Stati Uniti');
insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Marlon', 'Brando', '1924-04-03', 'Stati Uniti');
insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Al', 'Pacino', '1940-04-25', 'Stati Uniti');
insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Stanley', 'Kubrick', '1928-07-26', 'Stati Uniti');
insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Paolo', 'Sorrentino', '1970-05-31', 'Italia');
insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Toni', 'Servillo', '1959-01-25', 'Italia');
insert into artist (id, name, surname, date_of_birth, nationality) values(nextval('artist_seq'), 'Christopher', 'Nolan', '1970-07-30', 'Regno Unito');

insert into movie (id, title, year, duration, genre, production_country, director_id) values(nextval('movie_seq'), 'Il padrino', 1972, 175, 'Drammatico', 'Stati Uniti', (select id from artist where surname = 'Coppola'));
insert into movie (id, title, year, duration, genre, production_country, director_id) values(nextval('movie_seq'), 'Apocalypse Now', 1979, 147, 'Guerra', 'Stati Uniti', (select id from artist where surname = 'Coppola'));
insert into movie (id, title, year, duration, genre, production_country, director_id) values(nextval('movie_seq'), 'Full metal jacket', 1987, 116, 'Guerra', 'Regno Unito', (select id from artist where surname = 'Kubrick'));
insert into movie (id, title, year, duration, genre, production_country, director_id) values(nextval('movie_seq'), 'La grande bellezza', 2013, 142, 'Drammatico', 'Italia', (select id from artist where surname = 'Sorrentino'));
insert into movie (id, title, year, duration, genre, production_country, director_id) values(nextval('movie_seq'), 'Il divo', 2008, 110, 'Biografico', 'Italia', (select id from artist where surname = 'Sorrentino'));
insert into movie (id, title, year, duration, genre, production_country, director_id) values(nextval('movie_seq'), 'Inception', 2010, 148, 'Fantascienza', 'Regno Unito', (select id from artist where surname = 'Nolan'));

insert into festival (id, name, year, city, start_date, end_date, description) values(nextval('festival_seq'), 'Mostra del Cinema di Venezia', 2026, 'Venezia', '2026-09-02', '2026-09-12', 'Il festival di cinema più antico del mondo');
insert into festival (id, name, year, city, start_date, end_date, description) values(nextval('festival_seq'), 'Festa del Cinema di Roma', 2026, 'Roma', '2026-10-15', '2026-10-25', 'Rassegna internazionale all''Auditorium Parco della Musica');

insert into festival_movie (festival_id, movie_id) values((select id from festival where city = 'Venezia'), (select id from movie where title = 'Il padrino'));
insert into festival_movie (festival_id, movie_id) values((select id from festival where city = 'Venezia'), (select id from movie where title = 'La grande bellezza'));
insert into festival_movie (festival_id, movie_id) values((select id from festival where city = 'Venezia'), (select id from movie where title = 'Inception'));
insert into festival_movie (festival_id, movie_id) values((select id from festival where city = 'Roma'), (select id from movie where title = 'Il padrino'));
insert into festival_movie (festival_id, movie_id) values((select id from festival where city = 'Roma'), (select id from movie where title = 'Il divo'));
insert into festival_movie (festival_id, movie_id) values((select id from festival where city = 'Roma'), (select id from movie where title = 'Full metal jacket'));

insert into interpretation (id, character_name, movie_id, artist_id) values(nextval('interpretation_seq'), 'Vito Corleone', (select id from movie where title = 'Il padrino'), (select id from artist where surname = 'Brando'));
insert into interpretation (id, character_name, movie_id, artist_id) values(nextval('interpretation_seq'), 'Michael Corleone', (select id from movie where title = 'Il padrino'), (select id from artist where surname = 'Pacino'));
insert into interpretation (id, character_name, movie_id, artist_id) values(nextval('interpretation_seq'), 'Colonnello Kurtz', (select id from movie where title = 'Apocalypse Now'), (select id from artist where surname = 'Brando'));
insert into interpretation (id, character_name, movie_id, artist_id) values(nextval('interpretation_seq'), 'Jep Gambardella', (select id from movie where title = 'La grande bellezza'), (select id from artist where surname = 'Servillo'));
insert into interpretation (id, character_name, movie_id, artist_id) values(nextval('interpretation_seq'), 'Giulio Andreotti', (select id from movie where title = 'Il divo'), (select id from artist where surname = 'Servillo'));
insert into room (id, name, address, capacity) values(nextval('room_seq'), 'Sala Grande', 'Lungomare Marconi 30, Venezia', 1032);
insert into room (id, name, address, capacity) values(nextval('room_seq'), 'Sala Darsena', 'Lungomare Marconi 30, Venezia', 1409);
insert into room (id, name, address, capacity) values(nextval('room_seq'), 'Sala Sinopoli', 'Viale Pietro de Coubertin 30, Roma', 1200);

insert into screening (id, date, time, status, festival_id, movie_id, room_id) values(nextval('screening_seq'), '2026-09-03', '21:00', 'COMPLETED', (select id from festival where city = 'Venezia'), (select id from movie where title = 'Il padrino'), (select id from room where name = 'Sala Grande'));
insert into screening (id, date, time, status, festival_id, movie_id, room_id) values(nextval('screening_seq'), '2026-09-04', '18:30', 'COMPLETED', (select id from festival where city = 'Venezia'), (select id from movie where title = 'La grande bellezza'), (select id from room where name = 'Sala Darsena'));
insert into screening (id, date, time, status, festival_id, movie_id, room_id) values(nextval('screening_seq'), '2026-09-05', '21:00', 'CANCELLED', (select id from festival where city = 'Venezia'), (select id from movie where title = 'Inception'), (select id from room where name = 'Sala Grande'));
insert into screening (id, date, time, status, festival_id, movie_id, room_id) values(nextval('screening_seq'), '2026-10-24', '21:00', 'SCHEDULED', (select id from festival where city = 'Roma'), (select id from movie where title = 'Il padrino'), (select id from room where name = 'Sala Sinopoli'));
insert into screening (id, date, time, status, festival_id, movie_id, room_id) values(nextval('screening_seq'), '2026-10-25', '18:00', 'SCHEDULED', (select id from festival where city = 'Roma'), (select id from movie where title = 'Il divo'), (select id from room where name = 'Sala Sinopoli'));
insert into users (id, name, surname, email) values(nextval('users_seq'), 'Andrea', 'Admin', 'andrea.gentile3@stud.uniroma3.it');
insert into users (id, name, surname, email) values(nextval('users_seq'), 'Mario', 'Rossi', 'mario@email.it');
insert into users (id, name, surname, email) values(nextval('users_seq'), 'Anna', 'Bianchi', 'anna@email.it');
insert into credentials (id, username, password, role, user_id) values(nextval('credentials_seq'), 'andrea', '$2a$10$Qby.eTpkp.9JQWAiVws9UehZk6sItP7xZVvEue2Navg8kCmyR.h76', 'ADMIN', (select id from users where email = 'and.gentile3@stud.uniroma3.it'));
insert into credentials (id, username, password, role, user_id) values(nextval('credentials_seq'), 'mario', '$2a$10$V2wYOLjMu2ep4DZoQQiwAekyB8ls2M/fyNOFzVyYsyLkpUEKrApR.', 'DEFAULT', (select id from users where email = 'mario@email.it'));
insert into credentials (id, username, password, role, user_id) values(nextval('credentials_seq'), 'anna', '$2a$10$.uhoM3vSHDV1kS6RkXYPzusYcSCb9HIy8MTCkkLP5RVMS813yGvJy', 'DEFAULT', (select id from users where email = 'anna@email.it'));
insert into review (id, text, rating, date, movie_id, user_id) values(nextval('review_seq'), 'Un capolavoro assoluto, Brando e Pacino immensi.', 5, '2026-09-04', (select id from movie where title = 'Il padrino'), (select id from users where email = 'mario@email.it'));
insert into review (id, text, rating, date, movie_id, user_id) values(nextval('review_seq'), 'Lento all''inizio, ma il finale ripaga.', 4, '2026-10-25', (select id from movie where title = 'Il padrino'), (select id from users where email = 'anna@email.it'));
insert into review (id, text, rating, date, movie_id, user_id) values(nextval('review_seq'), 'Roma bellissima, Servillo perfetto.', 5, '2026-09-05', (select id from movie where title = 'La grande bellezza'), (select id from users where email = 'mario@email.it'));
