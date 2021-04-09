package de.trs.javafx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Member Class Stellt die Entität zum speichern der Mitglieder bereit.
 * 
 * Anotation von Lombok Bibliothek:
 * 
 * @Data übernimmt das setzen aller getter/setter, sowie das erstellen der hash
 *       und equal Funktionen
 * @AllArgsConstruktor erstellt einen Konstruktor mit allen Parametern der
 *                     Klasse
 * @NoArgsConstruktor erstellt einen leeren Konstruktor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private String vorname;
    private String nachname;
    private String sitzplatz;
}