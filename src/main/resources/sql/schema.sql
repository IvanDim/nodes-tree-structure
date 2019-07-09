-- MySQL Script generated by MySQL Workbench
--  5.07.2019 (пт) 11:33:04 EEST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema nodes_server
-- -----------------------------------------------------

CREATE SCHEMA IF NOT EXISTS `nodes_server` DEFAULT CHARACTER SET utf8;
USE `nodes_server`;

-- -----------------------------------------------------
-- Table `nodes_server`.`nodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nodes_server`.`nodes`;

CREATE TABLE IF NOT EXISTS `nodes_server`.`nodes`
(
  `id`        BIGINT       NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(255) NOT NULL,
  `parent_id` BIGINT       NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

START TRANSACTION;
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (1, 'node A', null);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (2, 'node B', 1);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (3, 'node C', 1);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (4, 'node D', 2);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (5, 'node E', 2);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (6, 'node F', 2);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (7, 'node G', 3);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (8, 'node H', 3);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (9, 'node I', 3);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (10, 'node J', 3);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (11, 'node K', 1);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (12, 'node L', 11);
INSERT INTO `nodes` (`id`, `name`, `parent_id`)
VALUES (13, 'node M', 11);

COMMIT;