delimiter $$

CREATE TABLE `CARD` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entityVersion` bigint(20) NOT NULL,
  `iName` varchar(1000) DEFAULT NULL,
  `multiverseId` varchar(255) DEFAULT NULL,
  `name` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `iname_index` (`iName`(255)) USING HASH,
  KEY `name_index` (`name`(255)) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=7935 DEFAULT CHARSET=utf8$$

CREATE TABLE `CARD_SET` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entityVersion` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `frDisplayName` varchar(255) DEFAULT NULL,
  `releaseDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `setNameIndex` (`name`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8$$

CREATE TABLE `CARD_CARD_SET` (
  `cards_id` bigint(20) NOT NULL,
  `sets_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cards_id`,`sets_id`),
  KEY `FK28CA0AE2FE40B8CA` (`cards_id`),
  KEY `FK28CA0AE2F9EDC5D8` (`sets_id`),
  CONSTRAINT `FK28CA0AE2F9EDC5D8` FOREIGN KEY (`sets_id`) REFERENCES `CARD_SET` (`id`),
  CONSTRAINT `FK28CA0AE2FE40B8CA` FOREIGN KEY (`cards_id`) REFERENCES `CARD` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$


CREATE TABLE `CARD_NAME` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entityVersion` bigint(20) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `multiverseId` varchar(255) DEFAULT NULL,
  `name` varchar(1000) DEFAULT NULL,
  `translatedLang` varchar(255) DEFAULT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK993D225A1FB4E9FD` (`card_id`),
  KEY `name_lg_index` (`name`(255)) USING HASH,
  CONSTRAINT `FK993D225A1FB4E9FD` FOREIGN KEY (`card_id`) REFERENCES `CARD` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22779 DEFAULT CHARSET=utf8$$


CREATE TABLE `CARD_PRICE_SOURCE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entityVersion` bigint(20) NOT NULL,
  `lastUpdate` datetime DEFAULT NULL,
  `lastPrice` float NOT NULL,
  `sourceName` varchar(255) DEFAULT NULL,
  `sourceType` varchar(255) DEFAULT NULL,
  `url` varchar(300) DEFAULT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  `lastStock` int(11) DEFAULT '-1',
  `currency` varchar(10) NOT NULL DEFAULT 'EUR',
  PRIMARY KEY (`id`),
  KEY `FK6F3320C01FB4E9FD` (`card_id`),
  KEY `lUpdate` (`lastUpdate`) USING BTREE,
  CONSTRAINT `FK6F3320C01FB4E9FD` FOREIGN KEY (`card_id`) REFERENCES `CARD` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=94833 DEFAULT CHARSET=utf8$$


CREATE TABLE `CARD_PRICE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entityVersion` bigint(20) NOT NULL,
  `fetchDate` datetime DEFAULT NULL,
  `price` float NOT NULL,
  `source_id` bigint(20) DEFAULT NULL,
  `stock` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`),
  KEY `FK8E8B033AA3DF1330` (`source_id`),
  CONSTRAINT `FK8E8B033AA3DF1330` FOREIGN KEY (`source_id`) REFERENCES `CARD_PRICE_SOURCE` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2193615 DEFAULT CHARSET=utf8$$


CREATE TABLE `UPDATE_PRICES` (
  `updatedon` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_id` bigint(20) DEFAULT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  `priceDiff` float DEFAULT NULL,
  `lastPrice` float DEFAULT NULL,
  KEY `UPDATE_PRICES_CRD_ID` (`card_id`),
  KEY `UPDATE_PRICES_SRC` (`source_id`),
  CONSTRAINT `UPDATE_PRICES_SRC` FOREIGN KEY (`source_id`) REFERENCES `CARD_PRICE_SOURCE` (`id`) ON DELETE CASCADE,
  CONSTRAINT `UPDATE_PRICES_CRD_ID` FOREIGN KEY (`card_id`) REFERENCES `CARD` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$


CREATE
DEFINER=`rdbmuser`@`%`
TRIGGER `mtga`.`price_update_trigger`
BEFORE UPDATE ON `mtga`.`CARD_PRICE_SOURCE`
FOR EACH ROW
BEGIN
		IF (OLD.lastPrice != NEW.lastPrice) THEN
			INSERT INTO UPDATE_PRICES (`updatedon`,`source_id`,`card_id`,`lastPrice`,`priceDiff`) 
				VALUES	(now(),NEW.id,NEW.card_id,NEW.lastPrice,NEW.lastPrice-OLD.lastPrice);
		END IF;
  END
$$


CREATE TABLE `FEEDBACK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  `message` text,
  `senton` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8$$


CREATE TABLE `MSTORES` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `store` varchar(400) DEFAULT NULL,
  `senton` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8$$


CREATE TABLE `PRICE_SUBSCRIPTION` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subscribedon` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `email` varchar(100) DEFAULT NULL,
  `uname` varchar(200) DEFAULT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  `lang` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PRICE_SUBSCRIPTION_CRD_ID_IDX` (`card_id`),
  CONSTRAINT `PRICE_SUBSCRIPTION_CRD_ID` FOREIGN KEY (`card_id`) REFERENCES `CARD` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8$$















