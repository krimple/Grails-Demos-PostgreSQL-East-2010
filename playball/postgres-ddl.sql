CREATE TABLE book
(
  book_id bigint NOT NULL primary key,
  "version" bigint NOT NULL,
  "title" character varying(255) NOT NULL,
  "price" decimal(6,2) NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE book OWNER TO postgres;

CREATE TABLE author
(
  author_id bigint NOT NULL primary key,
  firstname character varying(255) NOT NULL,
  lastname character varying(255) NOT NULL,
  "version" bigint NOT NULL
)
WITH ( 
  OIDS=FALSE
);

ALTER TABLE author OWNER TO postgres;

CREATE TABLE bookauthors
(
   book_id bigint not null,
   author_id bigint not null,
   "version" bigint NOT NULL,
   CONSTRAINT bookauthors_pk PRIMARY KEY (book_id, author_id),
   CONSTRAINT bookauthor_fk_book FOREIGN KEY (book_id)
       REFERENCES book(book_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT bookauthor_fk_author FOREIGN KEY (author_id)
       REFERENCES author(author_id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE sale
(
  book_id bigint not null,
  sale_id bigint not null,
  quantity int not null,
  sale_price decimal(6,2) NOT NULL,
  CONSTRAINT sale_pk PRIMARY KEY (book_id, sale_id),
  CONSTRAINT sale_book FOREIGN KEY (book_id)
    REFERENCES book(book_id)
    ON UPDATE NO ACTION ON DELETE NO ACTION
);

_
ALTER TABLE bookauthors OWNER TO postgres;

insert into author (author_id, firstname, lastname, "version")
  values(nextval('hibernate_sequence'), 'Eugene', 'O''Niell', 1);
  
insert into author (author_id, firstname, lastname, "version")
  values(nextval('hibernate_sequence'), 'Nathaniel', 'Hawthorne', 1);
  
insert into author (author_id, firstname, lastname, "version")
  values(nextval('hibernate_sequence'), 'Samuel', 'Beckett', 1);
  
insert into author (author_id, firstname, lastname, "version")
  values(nextval('hibernate_sequence'), 'William', 'Shakespeare', 1);
  
insert into book (book_id, title, price, version) 
  values (nextval('hibernate_sequence'),'Morning becomes Elektra', 12.00, 1 );
  
insert into book (book_id, title, price, version) 
  values (nextval('hibernate_sequence'),'The Scarlet Letter', 2.00, 1 );

insert into book (book_id, title, price, version) 
  values (nextval('hibernate_sequence'),'Waiting for Godot', 5.00, 1 );

insert into book (book_id, title, price, version) 
  values (nextval('hibernate_sequence'),'The complete works', 42.00, 1 );
  
insert into sale (book_id, sale_id, quantity, sale_price)
  values ((select book_id from book where title like 'Morning%'), 
  nextval('hibernate_sequence'), 10, 12.00);

insert into sale (book_id, sale_id, quantity, sale_price)
  values ((select book_id from book where title like 'Morning%'), 
  nextval('hibernate_sequence'), 10, 12.00);

insert into sale (book_id, sale_id, quantity, sale_price)
  values ((select book_id from book where title like 'Morning%'), 
  nextval('hibernate_sequence'), 10, 12.00);
    
insert into sale (book_id, sale_id, quantity, sale_price)
  values ((select book_id from book where title like 'The complete%'),
  nextval('hibernate_sequence'), 5, 30.50);

select b.title, sum(s.quantity * s.sale_price) amount from sale s, book b
where s.book_id = b.book_id
group by b.title;

       insert into bookauthors (book_id, author_id, version)
       values (
       (select book_id from book where title like 'The compl%'),
       (select author_id from author where lastname like 'Shake%'), 1);


CREATE OR REPLACE FUNCTION salesByBookId(
    book_id int)
    RETURNS decimal
    AS $$
DECLARE
  amount DECIMAL;
BEGIN
    select sum(s.quantity * s.sale_price) INTO amount 
    from sale s, book b
    where s.book_id = b.book_id
      and b.book_id = book_id; 

    RETURN amount;
END;
$$ LANGUAGE plpgsql;

select salesByBookId(80);
