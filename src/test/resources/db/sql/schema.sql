CREATE TABLE IF NOT EXISTS `acl_sid` (

  `id` bigint NOT NULL AUTO_INCREMENT,

  `principal` tinyint NOT NULL,

  `sid` varchar(100) NOT NULL,

  PRIMARY KEY (`id`)

);

CREATE TABLE IF NOT EXISTS `acl_class` (

  `id` bigint NOT NULL AUTO_INCREMENT,

  `class` varchar(255) NOT NULL,

  PRIMARY KEY (`id`)


) ;

CREATE TABLE IF NOT EXISTS `acl_entry` (

  `id` bigint NOT NULL AUTO_INCREMENT,

  `acl_object_identity` bigint NOT NULL,

  `ace_order` int(11) NOT NULL,

  `sid` bigint NOT NULL,

  `mask` int(11) NOT NULL,

  `granting` tinyint NOT NULL,

  `audit_success` tinyint NOT NULL,

  `audit_failure` tinyint NOT NULL,

  PRIMARY KEY (`id`)

) ;

CREATE TABLE IF NOT EXISTS `acl_object_identity` (

  `id` bigint NOT NULL AUTO_INCREMENT,

  `object_id_class` bigint NOT NULL,

  `object_id_identity` bigint NOT NULL,

  `parent_object` bigint DEFAULT NULL,

  `owner_sid` bigint DEFAULT NULL,

  `entries_inheriting` tinyint NOT NULL,

  PRIMARY KEY (`id`)

);