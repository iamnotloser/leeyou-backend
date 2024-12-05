create table if not exists user_center.team
(
    id           bigint auto_increment comment '主键'
    primary key,
    name     varchar(256)                      not  null comment '队伍名',
    user_id      bigint                          not null comment '创建人',
    max_num      int      default 1                 not null comment '最大人数',
    description varchar(1024)                       null comment '队伍描述',
    password     varchar(256)                       not null comment '队伍密码',
    expire_time  datetime                           null comment '过期时间',
    status       int      default 0                 not null comment '是否有效 0公开 1 私有 2 加密 ',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除 1 已删除 0 未删除'

    )
    comment '队伍表';

create table if not exists user_center.user_team
(
    id           bigint auto_increment comment '主键'
    primary key,
    user_id      bigint                           comment '用户id',
    team_id     bigint                           comment '队伍id',
    join_time   datetime  null comment '加入时间',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除 1 已删除 0 未删除'

)
    comment '队伍表';
