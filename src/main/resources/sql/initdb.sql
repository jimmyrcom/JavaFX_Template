-- by jimmyruska@gmail.com , simple generic application storage in h2.
-- Just using a KV store with json meets many usecases. If it needs to be fast customize your own tables.
-- For more advanced usecases h2 can also store Java serialized objects.


--Copyright (c) 2015 Jimmy Ruska (jimmyruska@gmail.com)
--All rights reserved.
--
--Redistribution and use in source and binary forms are permitted
--provided that the above copyright notice and this paragraph are
--duplicated in all such forms and that any documentation,
--advertising materials, and other materials related to such
--distribution and use acknowledge that the software was developed
--by the <organization>. The name of the
--<organization> may not be used to endorse or promote products derived
--from this software without specific prior written permission.
--THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
--IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
--WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.



-- Create a key value store of String -> String
CREATE TABLE IF NOT EXISTS KV(KEY VARCHAR PRIMARY KEY, VALUE VARCHAR);
-- Create a key value store where there can be multiple repeated keys
CREATE TABLE IF NOT EXISTS BAG(ID INT auto_increment PRIMARY KEY, KEY VARCHAR, VALUE VARCHAR);
CREATE INDEX IF NOT EXISTS IDX_BAG_KEY ON BAG(KEY);
-- Create a key value store where there is only one key but many values
CREATE TABLE IF NOT EXISTS KV_ARR(KEY VARCHAR PRIMARY KEY, ID INT);
CREATE TABLE IF NOT EXISTS KV_ARR_VALUES(ID INT PRIMARY KEY, VALUE VARCHAR);
-- Create a graph database structure, with everything indexed
CREATE TABLE IF NOT EXISTS NODE(ID INT auto_increment PRIMARY KEY, KEY VARCHAR, VALUE VARCHAR, TYPE VARCHAR DEFAULT 'NODE');
CREATE TABLE IF NOT EXISTS EDGE(ID INT auto_increment PRIMARY KEY, SOURCE INT, TARGET INT, PREDICATE VARCHAR);
CREATE TABLE IF NOT EXISTS NODE_PROPERTIES(ID INT,KEY VARCHAR, VALUE VARCHAR);
CREATE TABLE IF NOT EXISTS EDGE_PROPERTIES(ID INT,KEY VARCHAR, VALUE VARCHAR);
CREATE INDEX IF NOT EXISTS IDX_NODE_KEY ON NODE(KEY);
CREATE INDEX IF NOT EXISTS IDX_EDGE_KEY ON EDGE(PREDICATE);
CREATE INDEX IF NOT EXISTS IDX_EDGE_SOURCE ON EDGE(SOURCE);
CREATE INDEX IF NOT EXISTS IDX_EDGE_TARGET ON EDGE(TARGET);
CREATE INDEX IF NOT EXISTS IDX_KEY_EDGE_PROPERTIES ON EDGE_PROPERTIES(KEY);
CREATE INDEX IF NOT EXISTS IDX_KEY_NODE_PROPERTIES ON NODE_PROPERTIES(KEY);
CREATE INDEX IF NOT EXISTS IDX_ID_EDGE_PROPERTIES ON EDGE_PROPERTIES(ID);
CREATE INDEX IF NOT EXISTS IDX_ID_NODE_PROPERTIES ON NODE_PROPERTIES(ID);

