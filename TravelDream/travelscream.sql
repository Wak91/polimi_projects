SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `traveldream` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `traveldream` ;

-- -----------------------------------------------------
-- Table `traveldream`.`Utente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Utente` (
  `Username` VARCHAR(16) NOT NULL,
  `Password` VARCHAR(128) NOT NULL,
  `Nome` VARCHAR(20) NOT NULL,
  `Cognome` VARCHAR(20) NOT NULL,
  `Data di nascita` VARCHAR(45) NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Volo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Volo` (
  `ID` INT NOT NULL,
  `Luogo partenza` VARCHAR(45) NOT NULL,
  `Luogo arrivo` VARCHAR(45) NOT NULL,
  `Data` DATETIME NOT NULL,
  `Costo` INT NOT NULL,
  `Compagnia` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Hotel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Hotel` (
  `ID` INT NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Luogo` VARCHAR(45) NOT NULL,
  `Data inizio` DATE NOT NULL,
  `Data fine` DATE NOT NULL,
  `Costo giornaliero` INT NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Viaggio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Viaggio` (
  `ID` INT NOT NULL,
  `Data inizio` DATE NOT NULL,
  `Data fine` DATE NOT NULL,
  `Hotel` INT NOT NULL,
  `Volo` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `HotelViaggio_idx` (`Hotel` ASC),
  INDEX `VoloViaggio_idx` (`Volo` ASC),
  CONSTRAINT `VoloViaggio`
    FOREIGN KEY (`Volo`)
    REFERENCES `traveldream`.`Volo` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `HotelViaggio`
    FOREIGN KEY (`Hotel`)
    REFERENCES `traveldream`.`Hotel` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Escursione`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Escursione` (
  `ID` INT NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Data` DATE NOT NULL,
  `Costo` INT NOT NULL,
  `Luogo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Pacchetto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Pacchetto` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(40) NOT NULL,
  `Destinazione` VARCHAR(50) NOT NULL,
  `Data inizio` DATE NULL,
  `Data fine` DATE NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Gift List`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Gift List` (
  `Viaggio` INT NOT NULL,
  `Utente` VARCHAR(16) NOT NULL,
  `VoloAPag` TINYINT(1) NOT NULL,
  `VoloRPag` TINYINT(1) NOT NULL,
  `HotelPag` TINYINT(1) NOT NULL,
  `IDGiftList` VARCHAR(45) NULL,
  INDEX `Viaggio_idx` (`Viaggio` ASC),
  INDEX `Utente_idx` (`Utente` ASC),
  PRIMARY KEY (`Viaggio`, `Utente`),
  CONSTRAINT `UtenteGift`
    FOREIGN KEY (`Utente`)
    REFERENCES `traveldream`.`Utente` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ViaggioGift`
    FOREIGN KEY (`Viaggio`)
    REFERENCES `traveldream`.`Viaggio` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Invito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Invito` (
  `ID` INT NOT NULL,
  `Status` TINYINT(1) NOT NULL,
  `Amico` VARCHAR(45) NOT NULL,
  `Utente` VARCHAR(16) NOT NULL,
  `Viaggio` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `Utente_idx` (`Utente` ASC),
  INDEX `Viaggio_idx` (`Viaggio` ASC),
  CONSTRAINT `UtenteInvito`
    FOREIGN KEY (`Utente`)
    REFERENCES `traveldream`.`Utente` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ViaggioInvito`
    FOREIGN KEY (`Viaggio`)
    REFERENCES `traveldream`.`Viaggio` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Prenotazione`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Prenotazione` (
  `ID` INT NOT NULL,
  `Numero persone` INT NOT NULL,
  `Costo` INT NOT NULL,
  `Utente` VARCHAR(16) NOT NULL,
  `Viaggio` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `Utente_idx` (`Utente` ASC),
  INDEX `Viaggio_idx` (`Viaggio` ASC),
  CONSTRAINT `UtentePrenotazione`
    FOREIGN KEY (`Utente`)
    REFERENCES `traveldream`.`Utente` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ViaggioPrenotazione`
    FOREIGN KEY (`Viaggio`)
    REFERENCES `traveldream`.`Viaggio` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Amico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Amico` (
  `ID` INT NOT NULL,
  `Amico` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`HotelPacchetto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`HotelPacchetto` (
  `Hotel_ID` INT NOT NULL,
  `Pacchetto_ID` INT NOT NULL,
  PRIMARY KEY (`Hotel_ID`, `Pacchetto_ID`),
  INDEX `fk_Hotel_has_Pacchetto_Pacchetto1_idx` (`Pacchetto_ID` ASC),
  INDEX `fk_Hotel_has_Pacchetto_Hotel1_idx` (`Hotel_ID` ASC),
  CONSTRAINT `fk_Hotel_has_Pacchetto_Hotel1`
    FOREIGN KEY (`Hotel_ID`)
    REFERENCES `traveldream`.`Hotel` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Hotel_has_Pacchetto_Pacchetto1`
    FOREIGN KEY (`Pacchetto_ID`)
    REFERENCES `traveldream`.`Pacchetto` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`EscursioneViaggio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`EscursioneViaggio` (
  `Viaggio_ID` INT NOT NULL,
  `Escursioni_ID` INT NOT NULL,
  PRIMARY KEY (`Viaggio_ID`, `Escursioni_ID`),
  INDEX `fk_Viaggio_has_Escursioni_Escursioni1_idx` (`Escursioni_ID` ASC),
  INDEX `fk_Viaggio_has_Escursioni_Viaggio1_idx` (`Viaggio_ID` ASC),
  CONSTRAINT `fk_Viaggio_has_Escursioni_Viaggio1`
    FOREIGN KEY (`Viaggio_ID`)
    REFERENCES `traveldream`.`Viaggio` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Viaggio_has_Escursioni_Escursioni1`
    FOREIGN KEY (`Escursioni_ID`)
    REFERENCES `traveldream`.`Escursione` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`VoloPacchetto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`VoloPacchetto` (
  `Voli_ID` INT NOT NULL,
  `Pacchetto_ID` INT NOT NULL,
  PRIMARY KEY (`Voli_ID`, `Pacchetto_ID`),
  INDEX `fk_Voli_has_Pacchetto_Pacchetto1_idx` (`Pacchetto_ID` ASC),
  INDEX `fk_Voli_has_Pacchetto_Voli1_idx` (`Voli_ID` ASC),
  CONSTRAINT `fk_Voli_has_Pacchetto_Voli1`
    FOREIGN KEY (`Voli_ID`)
    REFERENCES `traveldream`.`Volo` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Voli_has_Pacchetto_Pacchetto1`
    FOREIGN KEY (`Pacchetto_ID`)
    REFERENCES `traveldream`.`Pacchetto` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`EscursionePacchetto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`EscursionePacchetto` (
  `Pacchetto_ID` INT NOT NULL,
  `Escursioni_ID` INT NOT NULL,
  PRIMARY KEY (`Pacchetto_ID`, `Escursioni_ID`),
  INDEX `fk_Pacchetto_has_Escursioni_Escursioni1_idx` (`Escursioni_ID` ASC),
  INDEX `fk_Pacchetto_has_Escursioni_Pacchetto1_idx` (`Pacchetto_ID` ASC),
  CONSTRAINT `fk_Pacchetto_has_Escursioni_Pacchetto1`
    FOREIGN KEY (`Pacchetto_ID`)
    REFERENCES `traveldream`.`Pacchetto` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pacchetto_has_Escursioni_Escursioni1`
    FOREIGN KEY (`Escursioni_ID`)
    REFERENCES `traveldream`.`Escursione` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`AmicoGiftList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`AmicoGiftList` (
  `Gift List_Viaggio` INT NOT NULL,
  `Gift List_Utente` VARCHAR(16) NOT NULL,
  `Amico_ID` INT NOT NULL,
  PRIMARY KEY (`Gift List_Viaggio`, `Gift List_Utente`, `Amico_ID`),
  INDEX `fk_Gift List_has_Amico_Amico1_idx` (`Amico_ID` ASC),
  INDEX `fk_Gift List_has_Amico_Gift List1_idx` (`Gift List_Viaggio` ASC, `Gift List_Utente` ASC),
  CONSTRAINT `fk_Gift List_has_Amico_Gift List1`
    FOREIGN KEY (`Gift List_Viaggio` , `Gift List_Utente`)
    REFERENCES `traveldream`.`Gift List` (`Viaggio` , `Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gift List_has_Amico_Amico1`
    FOREIGN KEY (`Amico_ID`)
    REFERENCES `traveldream`.`Amico` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`EscursioneGiftList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`EscursioneGiftList` (
  `Gift List_Viaggio` INT NOT NULL,
  `Gift List_Utente` VARCHAR(16) NOT NULL,
  `Escursione_ID` INT NOT NULL,
  `EscursionePag` TINYINT(1) NOT NULL,
  PRIMARY KEY (`Gift List_Viaggio`, `Gift List_Utente`, `Escursione_ID`),
  INDEX `fk_Gift List_has_Escursione_Escursione1_idx` (`Escursione_ID` ASC),
  INDEX `fk_Gift List_has_Escursione_Gift List1_idx` (`Gift List_Viaggio` ASC, `Gift List_Utente` ASC),
  CONSTRAINT `fk_Gift List_has_Escursione_Gift List1`
    FOREIGN KEY (`Gift List_Viaggio` , `Gift List_Utente`)
    REFERENCES `traveldream`.`Gift List` (`Viaggio` , `Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gift List_has_Escursione_Escursione1`
    FOREIGN KEY (`Escursione_ID`)
    REFERENCES `traveldream`.`Escursione` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`Gruppo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`Gruppo` (
  `NomeGruppo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`NomeGruppo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `traveldream`.`UtenteGruppo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `traveldream`.`UtenteGruppo` (
  `Utente_Username` VARCHAR(16) NOT NULL,
  `Gruppo_NomeGruppo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Utente_Username`, `Gruppo_NomeGruppo`),
  INDEX `fk_Utente_has_Gruppo_Gruppo1_idx` (`Gruppo_NomeGruppo` ASC),
  INDEX `fk_Utente_has_Gruppo_Utente1_idx` (`Utente_Username` ASC),
  CONSTRAINT `fk_Utente_has_Gruppo_Utente1`
    FOREIGN KEY (`Utente_Username`)
    REFERENCES `traveldream`.`Utente` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Utente_has_Gruppo_Gruppo1`
    FOREIGN KEY (`Gruppo_NomeGruppo`)
    REFERENCES `traveldream`.`Gruppo` (`NomeGruppo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
