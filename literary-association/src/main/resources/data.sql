insert into role (name) values ('ROLE_READER');
insert into role (name) values ('ROLE_WRITER');
insert into role (name) value ('ROLE_BOARD_MEMBER');
insert into role (name) value ('ROLE_ADMIN');
insert into role (name) value ('ROLE_EDITOR');

insert into permission (name) values ('create_order');

insert into role_permissions (role_id, permission_id) values (1,1);

insert into merchant (merchant_id, merchant_name, merchant_email, merchant_password, activated, error_url, failed_url, success_url)
values ('/yPS+cpGJ93Lzl5Lxw9m2mbi10QXeVEQuMG0DjNAFAU=|44QQKfRdORs1xRzU6Rd1eQ==', 'Vulkan knjizare', 'sb-nsr1z4072854@business.example.com', 'FPdIPGgwKU64+46+qJ54wA==|k7xwlTvPFeQ9aTT/XornBA==', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

-- sifra: reader123, username: reader123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points)
value ('Reader', 'reader@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader123', 'Bojana', 'Kliska', 'Beska', 'Srbija', true, true, true, 3);

-- sifra boardmember
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('BoardMember', 'board.member1@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember1', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('BoardMember', 'board.member2@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember2', 'Mika', 'Mikic', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: admin123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Admin', 'admin@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'admin123', 'Mika', 'Mikic', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: editor123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
value ('Editor', 'pera.praksa123@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor123', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

-- sifra: reader123, username: writer123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled)
    value ('Writer', 'car.insurance.praksa777@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer123', 'Pera', 'Peric', 'NS', 'Srbija', true, true);

insert into user_roles (user_id, role_id) values (1,1);
insert into user_roles (user_id, role_id) values (2,3);
insert into user_roles (user_id, role_id) values (3,3);
insert into user_roles (user_id, role_id) values (4,4); -- admin
insert into user_roles (user_id, role_id) values (5,5); -- editor
insert into user_roles (user_id, role_id) values (6,2); -- writer


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