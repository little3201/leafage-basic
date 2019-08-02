drop table if exists account_info;

/*==============================================================*/
/* Table: account_info                                          */
/*==============================================================*/
create table account_info
(
   id                   bigint(11) not null auto_increment comment '主键',
   account_id           varchar(32) comment '账户ID',
   account_code         bigint(20) comment '账户号码',
   account_type         char comment '账户类型',
   account_balance      decimal(11,3) comment '账户余额',
   user_id              bigint(11) not null comment '用户ID',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier_id          bigint(11) not null comment '修改人ID',
   modify_time          timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);

alter table account_info comment '账户信息表';
