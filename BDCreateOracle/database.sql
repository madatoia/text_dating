create database dictionary_ngram;
use dictionary_ngram;

set global max_connections = 1000;

create table words(
	id 		INT(50) UNSIGNED NOT NULL AUTO_INCREMENT,
	word 	VARCHAR(40) NOT NULL,
	year_ap INT(4) NOT NULL,
	ap_no	decimal(11,9),
	PRIMARY KEY (id)
);

