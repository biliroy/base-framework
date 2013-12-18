--删除所有表
drop table TB_DATA_DICTIONARY if exists;
drop table TB_DICTIONARY_CATEGORY if exists;
drop table TB_GROUP if exists;
drop table TB_GROUP_RESOURCE if exists;
drop table TB_GROUP_USER if exists;
drop table TB_RESOURCE if exists;
drop table TB_USER if exists;
drop table TB_OPERATING_RECORD if exists;

--创建系统字典表
create table TB_DATA_DICTIONARY (id varchar(32) not null, name varchar(256) not null, remark varchar(512), type varchar(1) not null, value varchar(32) not null, fk_category_id varchar(32) not null, primary key (id));
create table TB_DICTIONARY_CATEGORY (id varchar(32) not null, code varchar(128) not null, name varchar(256) not null, remark varchar(512), fk_parent_id varchar(32), primary key (id));

--创建权限表
create table TB_GROUP (id varchar(32) not null, name varchar(32) not null, remark varchar(512), state integer not null, type varchar(2) not null, fk_parent_id varchar(32), role varchar(64), value varchar(256), primary key (id));
create table TB_GROUP_RESOURCE (fk_resource_id varchar(32) not null, fk_group_id varchar(32) not null);
create table TB_GROUP_USER (fk_group_id varchar(32) not null, fk_user_id varchar(32) not null);
create table TB_RESOURCE (id varchar(32) not null, permission varchar(64), remark varchar(512), sort integer not null, name varchar(32) not null, type varchar(2) not null, value varchar(256), fk_parent_id varchar(32), icon varchar(32), leaf boolean, primary key (id));
create table TB_USER (id varchar(32) not null, email varchar(128), password varchar(32) not null, portrait varchar(256), realname varchar(64) not null, state integer not null, username varchar(32) not null, primary key (id));

--创建审计表
create table TB_OPERATING_RECORD (id varchar(32) not null, end_date timestamp not null, fk_user_id varchar(32), operating_target varchar(512) not null, start_date timestamp not null, username varchar(32), function varchar(128), ip varchar(64) not null, method varchar(256) not null, module varchar(128), remark clob, state integer not null, primary key (id));

--创建所有表关联
alter table TB_DICTIONARY_CATEGORY add constraint UK_9qkei4dxobl1lm4oa0ys8c3nr unique (code);
alter table TB_GROUP add constraint UK_byw2jrrrxrueqimkmgj3o842j unique (name);
alter table TB_RESOURCE add constraint UK_aunvlvm32xb4e6590jc9oooq unique (name);
alter table TB_USER add constraint UK_4wv83hfajry5tdoamn8wsqa6x unique (username);
alter table TB_DATA_DICTIONARY add constraint FK_layhfd1butuigsscgucmp2okd foreign key (fk_category_id) references TB_DICTIONARY_CATEGORY;
alter table TB_DICTIONARY_CATEGORY add constraint FK_bernf41kympxy2kjl4vbq5q44 foreign key (fk_parent_id) references TB_DICTIONARY_CATEGORY;
alter table TB_GROUP add constraint FK_idve4hc50mytxm181wl1knw28 foreign key (fk_parent_id) references tb_group;
alter table TB_GROUP_RESOURCE add constraint FK_q82fpmfh128qxoeyymrkg71e2 foreign key (fk_group_id) references TB_GROUP;
alter table TB_GROUP_RESOURCE add constraint FK_3tjs4wt3vvoibo1fvcvog5srd foreign key (fk_resource_id) references TB_RESOURCE;
alter table TB_GROUP_USER add constraint FK_7k068ltfepa1q75qtmvxuawk foreign key (fk_user_id) references TB_USER;
alter table TB_GROUP_USER add constraint FK_rgmkki7dggfag6ow6eivljmwv foreign key (fk_group_id) references TB_GROUP;
alter table TB_RESOURCE add constraint FK_k2heqvi9muk4cjyyd53r9y37x foreign key (fk_parent_id) references TB_RESOURCE;