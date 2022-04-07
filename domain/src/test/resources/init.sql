CREATE DATABASE `drools-example` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `USER`
(
    `ID`        bigint NOT NULL,
    `CREATE_AT` datetime    DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_AT` datetime    DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_BY` varchar(45) DEFAULT '',
    `VERSION`   bigint      DEFAULT '1',
    `DELETED`   int         DEFAULT '0',
    `AGE`       int         DEFAULT '0',
    `GENDER`    varchar(5)  DEFAULT '',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `COMPANY`
(
    `ID`        bigint NOT NULL,
    `CREATE_AT` datetime     DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_AT` datetime     DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_BY` varchar(45)  DEFAULT '',
    `VERSION`   bigint       DEFAULT '1',
    `DELETED`   int          DEFAULT '0',
    `NAME`      varchar(100) DEFAULT '',
    `ADDRESS`   varchar(200) DEFAULT '',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `RULE_REALM`
(
    `ID`        bigint NOT NULL,
    `CREATE_AT` datetime      DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_AT` datetime      DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_BY` varchar(45)   DEFAULT '',
    `VERSION`   bigint        DEFAULT '1',
    `DELETED`   int           DEFAULT '0',
    `TYPE`      varchar(100)  DEFAULT '',
    `NAME`      varchar(100)  DEFAULT '',
    `GROUP`     varchar(100)  DEFAULT '',
    `HEADER`    varchar(1024) DEFAULT '',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `RULE_ITEM`
(
    `ID`            bigint NOT NULL,
    `CREATE_AT`     datetime     DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_AT`     datetime     DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_BY`     varchar(45)  DEFAULT '',
    `VERSION`       bigint       DEFAULT '1',
    `DELETED`       int          DEFAULT '0',
    `RULE_REALM_ID` bigint NOT NULL,
    `NAME`          varchar(100) DEFAULT '',
    `LHS`           varchar(255) DEFAULT '',
    `RHS`           varchar(255) DEFAULT '',
    `ROW_NO`        int          DEFAULT '0',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `KIE_HIS`
(
    `ID`                  bigint NOT NULL,
    `CREATE_AT`           datetime     DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_AT`           datetime     DEFAULT CURRENT_TIMESTAMP,
    `UPDATE_BY`           varchar(45)  DEFAULT '',
    `VERSION`             bigint       DEFAULT '1',
    `DELETED`             int          DEFAULT '0',
    `KIE_BASE_MODEL_NAME` varchar(100) DEFAULT '',
    `PACKAGE_NAME`        varchar(50)  DEFAULT '',
    `CONTENT`             text COMMENT 'full rule content',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `RULE_REALM`
VALUES (1508807688107012097, '2022-03-29 22:06:00', '2022-03-29 22:06:00', 'system', 1, 0, 'user', '用户规则集', 'user',
        'import com.domain.entity.User;\nimport com.domain.dto.UserDTO;\nimport com.qualitycheck.QualityCheckContext;\nimport org.slf4j.Logger;\ndialect  \"mvel\"\nglobal Logger logger;');

INSERT INTO `RULE_ITEM`
VALUES (1508824146966949890, '2022-03-29 23:11:24', '2022-04-04 21:15:12', 'system', 3, 0, 1508807688107012097,
        'user age < 11',
        '$ctx : QualityCheckContext(checkFlags contains \"age\")\n  $u : User(age < 11) from $ctx.data',
        'logger.info(\"user age < 11! user : {}\", $u);    $ctx.msg.add(\"user age < 11\");', 1),
       (1510583432986464258, '2022-04-03 19:42:10', '2022-04-03 19:57:57', 'system', 2, 0, 1508807688107012097,
        'user gender is unknown',
        '$ctx : QualityCheckContext(checkFlags contains \"gender\") \n $u : User(gender == null || gender != \"M\" && gender != \"F\") from $ctx.data',
        'logger.info(\"user gender is unknown! user : {}\", $u); \n $ctx.msg.add(\"user gender is unknown\");', 2);
