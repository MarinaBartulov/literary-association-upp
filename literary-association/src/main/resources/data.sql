insert into merchant (merchant_id, merchant_email, password, error_url, failed_url, success_url)
values ('46QfIZh9KGe62AMDAStgnRbsK1fcX4', 'sb-nsr1z4072854@business.example.com', 'Merchant123!', 'http://localhost:3000/error', 'http://localhost:3000/failed', 'http://localhost:3000/success');

insert into user (type, email, password, username, first_name, last_name, city, country, verified, beta_reader, penalty_points)
value ('Reader', 'reader@gmail.com', 'bojana', 'reader', 'Bojana', 'Kliska', 'Beska', 'Srbija', true, true, 3);

insert into genre (name, description) value ('Thriller', 'Thrillers are characterized by fast pacing, frequent action, and resourceful heroes who must thwart the plans of more-powerful and better-equipped villains. Literary devices such as suspense, red herrings and cliffhangers are used extensively.');
insert into genre (name, description) value ('Romance', 'Romance fiction is smart, fresh and diverse. Two basic elements comprise every romance novel: a central love story and an emotionally satisfying and optimistic ending.');
insert into genre (name, description) value ('Drama', 'Drama is a mode of fictional representation through dialogue and performance. It is one of the literary genres, which is an imitation of some action. It contains conflict of characters, particularly the ones who perform in front of audience on the stage.');
insert into genre (name, description) value ('Crime', 'The crime genre includes the broad selection of books on criminals and the court system, but the most common focus is investigations and sleuthing. Mystery novels are usually placed into this category, although there is a separate division for "crime".');
insert into genre (name, description) value ('Biography', 'A biography, or simply bio, is a detailed description of a persons life. It involves more than just the basic facts like education, work, relationships, and death; it portrays a persons experience of these life events.');
insert into genre (name, description) value ('Classic', 'A classic is a novel makes a contribution to literature. Classics come from all cultures and all years, and can reflect a time period, a societal standard or may offer commentary on a subject.');

insert into reader_genre (reader_id,genre_id) value (1, 1);
insert into reader_genre (reader_id,genre_id) value (1, 2);

insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 1);
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 2);